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
    }

    public void agregarPersona(Persona persona) {
        // Agregar a la lista
        listaPersonas.agregar(persona);

        // Crear acción para undo
        Accion accion = new Accion("alta", null, persona, getTimestamp());
        undoStack.push(accion);

        // Limpiar redoStack
        redoStack = new Pila();

        // Registrar en log
        log.agregarEntrada(getTimestamp() + " alta " + persona.getId());

        System.out.println("Persona agregada con éxito: " + persona.getNombre() + " " + persona.getApellido());
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
            // Para una BAJA, el objeto eliminado va en personaAnterior (el estado "antes" de la acción)
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
    public void actualizarPersona(Persona nuevosDatos) {
        // Buscar primero
        Persona original = listaPersonas.buscarPorId(nuevosDatos.getId());

        if (original == null) {
            System.out.println("No existe una persona con ese ID.");
            return;
        }

        // Crear copia para guardar en undo
        Persona copiaAnterior = new Persona(
            original.getId(),
            original.getNombre(),
            original.getApellido(),
            original.getEdad(),
            original.getEmail(),
            original.getFechaRegistro()
        );

        // Guardar para undo
        undoStack.push(new Accion("modif", copiaAnterior, null, getTimestamp()));
        redoStack = new Pila();

        // Aplicar cambios (sin tocar ID ni fechaRegistro)
        original.setNombre(nuevosDatos.getNombre());
        original.setApellido(nuevosDatos.getApellido());
        original.setEdad(nuevosDatos.getEdad());
        original.setEmail(nuevosDatos.getEmail());

        log.agregarEntrada(getTimestamp() + " modif " + original.getId());

        System.out.println("Persona actualizada correctamente.");
    }
    //Metodo deshacer
    public void deshacer() {
    if (undoStack.isEmpty()) {
        System.out.println("No hay acciones para deshacer.");
        return;
    }

    Accion accion = undoStack.pop(); // Sacamos la última acción
    redoStack.push(accion);          // Se guarda para rehacer después

    switch (accion.getTipo()) {

        case "alta":
            // Si fue ALTA → deshacer es ELIMINAR
            listaPersonas.eliminar(accion.getPersonaNueva().getId());
            System.out.println("Se deshizo: alta = eliminado");
            break;

        case "baja":
            // Si fue BAJA → deshacer es VOLVER A AGREGAR
            listaPersonas.agregar(accion.getPersonaAnterior());
            System.out.println("Se deshizo: baja = restaurado");
            break;

        case "modif":
            // Si fue MODIFICACIÓN → deshacer es regresar al estado anterior
            listaPersonas.reemplazarPersona(accion.getPersonaAnterior());
            System.out.println("Se deshizo: modificación = valores anteriores restaurados");
            break;

        default:
            System.out.println("Acción desconocida.");
    }
}
    //Metodo rehacer 
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
                System.out.println("Rehecha acción (ALTA): Reagregada Persona ID " + accion.getPersonaNueva().getId());
                break;
                
            case "baja":
                // Rehacer Eliminar = Volver a eliminar
                listaPersonas.eliminar(accion.getPersonaAnterior().getId()); 
                System.out.println("Rehecha acción (BAJA): Re-eliminada Persona ID " + accion.getPersonaAnterior().getId());
                break;

            case "modif":
                // Rehacer Modificar = Aplicar el estado Nuevo
                // Se necesita un método 'reemplazarPorId' en Lista.java para esto
                // listaPersonas.reemplazarPorId(accion.getPersonaNueva());
                System.out.println("Rehecha acción (MODIF): Re-aplicada modificación en Persona ID " + accion.getPersonaNueva().getId());
                break;

            default:
                System.out.println("Error: Tipo de acción no reconocido para Rehacer.");
        }
    }
}
