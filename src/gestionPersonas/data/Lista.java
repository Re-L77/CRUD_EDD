package gestionPersonas.data;

import gestionPersonas.model.Persona;

public class Lista {

    // Nodo interno
    private class Nodo {
        Persona dato;
        Nodo siguiente;

        Nodo(Persona dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo cabeza; // inicio de la lista

    public Lista() {
        this.cabeza = null;
    }

    // Método para agregar una persona al final de la lista
    public void agregar(Persona persona) {
        Nodo nuevo = new Nodo(persona);

        if (cabeza == null) {
            cabeza = nuevo; // si la lista está vacía
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo; // agregar al final
        }
    }

    // Método para imprimir la lista
    public void imprimir() {
        Nodo actual = cabeza;
        while (actual != null) {
            System.out.println(actual.dato);
            actual = actual.siguiente;
        }
    }
//_________________________________________________________________________________________________________________________
    public Persona eliminar(int id) {
        if (cabeza == null) {
            return null; 
        }

        // Caso 1: La cabeza es el nodo a eliminar
        if (cabeza.dato.getId() == id) {
            Persona personaEliminada = cabeza.dato;
            cabeza = cabeza.siguiente; // Mover la cabeza al siguiente nodo
            return personaEliminada;
        }

        // Caso 2: El nodo a eliminar está en medio o al final
        Nodo actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.getId() == id) {
                Persona personaEliminada = actual.siguiente.dato;
                // Saltar el nodo: el actual apunta al siguiente del nodo a eliminar
                actual.siguiente = actual.siguiente.siguiente; 
                return personaEliminada;
            }
            actual = actual.siguiente;
        }

        return null; // No se encontró el ID
    }
    
    public Persona buscarPorId(int id){
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.dato.getId() == id) {
                return actual.dato;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public Persona reemplazarPersona(Persona persona) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.dato.getId() == persona.getId()) {
                Persona personaAnterior = actual.dato; // Guardar la persona anterior
                actual.dato = persona; // Reemplazar con la nueva persona
                return personaAnterior;
            }
            actual = actual.siguiente;
        }
        return null; // No se encontró la persona
    }

}
