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
}
