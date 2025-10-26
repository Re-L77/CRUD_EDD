package gestionPersonas.data;

import gestionPersonas.model.Accion;

public class Pila {

    // Nodo interno
    private class Nodo {
        Accion dato;
        Nodo siguiente;

        Nodo(Accion dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo cima; // cima de la pila

    public Pila() {
        this.cima = null;
    }

    // Push: agregar acción a la pila
    public void push(Accion accion) {
        Nodo nuevo = new Nodo(accion);
        nuevo.siguiente = cima;
        cima = nuevo;
    }

    // Pop: sacar acción de la pila
    public Accion pop() {
        if (cima == null)
            return null;
        Accion accion = cima.dato;
        cima = cima.siguiente;
        return accion;
    }

    // Peek: ver la acción en la cima sin sacarla
    public Accion peek() {
        if (cima == null)
            return null;
        return cima.dato;
    }

    // Verificar si la pila está vacía
    public boolean isEmpty() {
        return cima == null;
    }
}
