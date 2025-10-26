package gestionPersonas.data;

public class Log {

    // Nodo interno
    private class Nodo {
        String entrada; // Ejemplo: "14 mayo 2025, 09:55:40 alta HFZ24359"
        Nodo siguiente;

        Nodo(String entrada) {
            this.entrada = entrada;
            this.siguiente = null;
        }
    }

    private Nodo cabeza;

    public Log() {
        this.cabeza = null;
    }

    // Método para agregar una entrada al log
    public void agregarEntrada(String entrada) {
        Nodo nuevo = new Nodo(entrada);

        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
    }

    // Método para imprimir todas las entradas del log
    public void imprimir() {
        if (cabeza == null) {
            System.out.println("El log está vacío.");
            return;
        }

        Nodo actual = cabeza;
        while (actual != null) {
            System.out.println(actual.entrada);
            actual = actual.siguiente;
        }
    }
}
