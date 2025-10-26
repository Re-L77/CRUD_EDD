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

    // Aquí luego puedes agregar eliminar, buscar, actualizar...
}
