package gestionPersonas.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import gestionPersonas.data.Lista;
import gestionPersonas.data.Log;
import gestionPersonas.data.Pila;
import gestionPersonas.model.Persona;
import gestionPersonas.model.Accion;

public class CrudService {
    private Lista listaPersonas;
    private Pila undoStack;
    private Pila redoStack;
    private Log log;

    public CrudService() {
        listaPersonas = new Lista();
        undoStack = new Pila();
        redoStack = new Pila();
        log = new Log();

        // Cargar datos de muestra
        cargarDatosPrecargados();
    }

    private void cargarDatosPrecargados() {
        System.out.println("Cargando datos de muestra...");

        String fechaActual = getTimestamp();

        // Persona 1
        Persona persona1 = new Persona(1, "Juan", "Pérez", 25, "juan.perez@email.com", fechaActual);
        listaPersonas.agregar(persona1);
        log.agregarEntrada(fechaActual + " alta 1");

        // Persona 2
        Persona persona2 = new Persona(2, "María", "García", 30, "maria.garcia@email.com", fechaActual);
        listaPersonas.agregar(persona2);
        log.agregarEntrada(fechaActual + " alta 2");

        // Persona 3
        Persona persona3 = new Persona(3, "Carlos", "López", 28, "carlos.lopez@email.com", fechaActual);
        listaPersonas.agregar(persona3);
        log.agregarEntrada(fechaActual + " alta 3");

        // Persona 4
        Persona persona4 = new Persona(4, "Ana", "Martín", 35, "ana.martin@email.com", fechaActual);
        listaPersonas.agregar(persona4);
        log.agregarEntrada(fechaActual + " alta 4");

        // Persona 5
        Persona persona5 = new Persona(5, "Luis", "Rodríguez", 42, "luis.rodriguez@email.com", fechaActual);
        listaPersonas.agregar(persona5);
        log.agregarEntrada(fechaActual + " alta 5");
    }

    // Métodos de validación
    private boolean validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            System.out.println("Error: El " + campo + " no puede estar vacío.");
            return false;
        }

        // No debe contener números
        if (texto.matches(".*\\d.*")) {
            System.out.println("Error: El " + campo + " no puede contener números.");
            return false;
        }

        // Solo letras, espacios y algunos caracteres especiales
        if (!texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s'-]+$")) {
            System.out.println("Error: El " + campo + " contiene caracteres no válidos.");
            return false;
        }

        return true;
    }

    private boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Error: El email no puede estar vacío.");
            return false;
        }

        // Validación básica de email
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailRegex)) {
            System.out.println("Error: El formato del email no es válido.");
            return false;
        }

        return true;
    }

    private boolean validarEdad(int edad) {
        if (edad < 0 || edad > 120) {
            System.out.println("Error: La edad debe estar entre 0 y 120 años.");
            return false;
        }
        return true;
    }

    private boolean existeId(int id) {
        return listaPersonas.buscarPorId(id) != null;
    }

    public boolean agregarPersona(Persona persona) {
        // Validar que no exista el ID
        if (existeId(persona.getId())) {
            System.out.println("Error: Ya existe una persona con el ID " + persona.getId() + ".");
            return false;
        }

        // Validar nombre
        if (!validarTexto(persona.getNombre(), "nombre")) {
            return false;
        }

        // Validar apellido
        if (!validarTexto(persona.getApellido(), "apellido")) {
            return false;
        }

        // Validar email
        if (!validarEmail(persona.getEmail())) {
            return false;
        }

        // Validar edad
        if (!validarEdad(persona.getEdad())) {
            return false;
        }

        // Si todas las validaciones pasan, agregar a la lista
        listaPersonas.agregar(persona);

        // Crear acción para undo
        Accion accion = new Accion("alta", null, persona, getTimestamp());
        undoStack.push(accion);

        // Limpiar redoStack
        redoStack = new Pila();

        // Registrar en log
        log.agregarEntrada(getTimestamp() + " alta " + persona.getId());

        System.out.println("Persona agregada con éxito: " + persona.getNombre() + " " + persona.getApellido());
        return true;
    }

    public void mostrarLog() {
        System.out.println("\n--- LOG DE TRANSACCIONES ---");
        log.imprimir();
        System.out.println("--- FIN DEL LOG ---\n");
    }

    public void imprimirPersonas() {
        System.out.println("\n--- LISTA DE PERSONAS ---");
        listaPersonas.imprimir();
        System.out.println("--- FIN DE LA LISTA ---\n");
    }

    private String getTimestamp() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }

    public void eliminarPersona(int id) {
        // 1. Ejecutar la eliminación en la lista (esto devuelve el objeto eliminado)
        Persona personaEliminada = listaPersonas.eliminar(id);

        if (personaEliminada != null) {

            // 2. Crear y registrar la acción "baja"
            // Para una BAJA, el objeto eliminado va en personaAnterior (el estado "antes"
            // de la acción)
            Accion accion = new Accion("baja", personaEliminada, null, getTimestamp());

            // 3. Gestionar las pilas (como en agregarPersona)
            undoStack.push(accion);
            redoStack = new Pila(); // Limpiar redoStack al hacer una nueva acción

            // 4. Registrar en log
            log.agregarEntrada(getTimestamp() + " baja " + personaEliminada.getId());

            System.out.println("Persona con ID " + id + " eliminada.");
        } else {
            System.out.println("Error: No se encontró una persona con el ID " + id + " para eliminar.");
        }
    }

    public Persona buscarPersonaPorId(int id) {
        return listaPersonas.buscarPorId(id);
    }

    // metodo actualizar
    public boolean actualizarPersona(Persona nuevosDatos) {
        // Buscar primero
        Persona original = listaPersonas.buscarPorId(nuevosDatos.getId());

        if (original == null) {
            System.out.println("No existe una persona con ese ID.");
            return false;
        }

        // Validar nuevo nombre
        if (!validarTexto(nuevosDatos.getNombre(), "nombre")) {
            return false;
        }

        // Validar nuevo apellido
        if (!validarTexto(nuevosDatos.getApellido(), "apellido")) {
            return false;
        }

        // Validar nuevo email
        if (!validarEmail(nuevosDatos.getEmail())) {
            return false;
        }

        // Validar nueva edad
        if (!validarEdad(nuevosDatos.getEdad())) {
            return false;
        }

        // Crear copia para guardar en undo
        Persona copiaAnterior = new Persona(
                original.getId(),
                original.getNombre(),
                original.getApellido(),
                original.getEdad(),
                original.getEmail(),
                original.getFechaRegistro());

        // Crear copia de los nuevos datos
        Persona copiaNueva = new Persona(
                original.getId(), // Mantener el mismo ID
                nuevosDatos.getNombre(),
                nuevosDatos.getApellido(),
                nuevosDatos.getEdad(),
                nuevosDatos.getEmail(),
                original.getFechaRegistro() // Mantener la fecha de registro original
        );

        undoStack.push(new Accion("modif", copiaAnterior, copiaNueva, getTimestamp()));
        redoStack = new Pila();

        // Aplicar cambios (sin tocar ID ni fechaRegistro)
        original.setNombre(nuevosDatos.getNombre());
        original.setApellido(nuevosDatos.getApellido());
        original.setEdad(nuevosDatos.getEdad());
        original.setEmail(nuevosDatos.getEmail());

        log.agregarEntrada(getTimestamp() + " modif " + original.getId());

        System.out.println("Persona actualizada correctamente.");
        return true;
    }

    // Metodo deshacer
    public void deshacer() {
        if (undoStack.isEmpty()) {
            System.out.println("No hay acciones para deshacer.");
            return;
        }

        Accion accion = undoStack.pop(); // Sacamos la última acción
        redoStack.push(accion); // Se guarda para rehacer después

        switch (accion.getTipo()) {

            case "alta":
                // Si fue ALTA → deshacer es ELIMINAR
                listaPersonas.eliminar(accion.getPersonaNueva().getId());
                log.agregarEntrada(getTimestamp() + " deshacer_alta " + accion.getPersonaNueva().getId());
                System.out.println("Se deshizo: alta = eliminado");
                break;

            case "baja":
                // Si fue BAJA → deshacer es VOLVER A AGREGAR
                listaPersonas.agregar(accion.getPersonaAnterior());
                log.agregarEntrada(getTimestamp() + " deshacer_baja " + accion.getPersonaAnterior().getId());
                System.out.println("Se deshizo: baja = restaurado");
                break;

            case "modif":
                // Si fue MODIFICACIÓN → deshacer es regresar al estado anterior
                listaPersonas.reemplazarPersona(accion.getPersonaAnterior());
                log.agregarEntrada(getTimestamp() + " deshacer_modif " + accion.getPersonaAnterior().getId());
                System.out.println("Se deshizo: modificación = valores anteriores restaurados");
                break;

            default:
                System.out.println("Acción desconocida.");
        }
    }

    // Metodo rehacer
    public void rehacer() {
        if (redoStack.isEmpty()) {
            System.out.println("No hay acciones para Rehacer.");
            return;
        }

        // 1. Obtener y mover el comando de REDO a UNDO
        Accion accion = redoStack.pop();
        undoStack.push(accion);

        // 2. Re-aplicar la acción (Redo)
        switch (accion.getTipo().toLowerCase()) {
            case "alta":
                // Rehacer Agregar = Volver a agregar
                listaPersonas.agregar(accion.getPersonaNueva());
                log.agregarEntrada(getTimestamp() + " rehacer_alta " + accion.getPersonaNueva().getId());
                System.out.println("Rehecha acción (ALTA): Reagregada Persona ID " + accion.getPersonaNueva().getId());
                break;

            case "baja":
                // Rehacer Eliminar = Volver a eliminar
                listaPersonas.eliminar(accion.getPersonaAnterior().getId());
                log.agregarEntrada(getTimestamp() + " rehacer_baja " + accion.getPersonaAnterior().getId());
                System.out.println(
                        "Rehecha acción (BAJA): Re-eliminada Persona ID " + accion.getPersonaAnterior().getId());
                break;

            case "modif":
                // Rehacer Modificar = Aplicar el estado Nuevo
                listaPersonas.reemplazarPersona(accion.getPersonaNueva());
                log.agregarEntrada(getTimestamp() + " rehacer_modif " + accion.getPersonaNueva().getId());
                System.out.println("Rehecha acción (MODIF): Re-aplicada modificación en Persona ID "
                        + accion.getPersonaNueva().getId());
                break;

            default:
                System.out.println("Error: Tipo de acción no reconocido para Rehacer.");
        }
    }
}
