package gestionPersonas.model;

public class Accion {

    private String tipo; // "alta", "baja", "modif"
    private Persona personaAnterior; // null si es alta
    private Persona personaNueva; // null si es baja
    private String timestamp; // fecha y hora de la acción

    // Constructor vacío
    public Accion() {
    }

    // Constructor completo
    public Accion(String tipo, Persona personaAnterior, Persona personaNueva, String timestamp) {
        this.tipo = tipo;
        this.personaAnterior = personaAnterior;
        this.personaNueva = personaNueva;
        this.timestamp = timestamp;
    }

    // Getters y setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Persona getPersonaAnterior() {
        return personaAnterior;
    }

    public void setPersonaAnterior(Persona personaAnterior) {
        this.personaAnterior = personaAnterior;
    }

    public Persona getPersonaNueva() {
        return personaNueva;
    }

    public void setPersonaNueva(Persona personaNueva) {
        this.personaNueva = personaNueva;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // toString para imprimir la acción
    @Override
    public String toString() {
        String info = "[" + timestamp + "] " + tipo.toUpperCase() + " - ";
        if (tipo.equalsIgnoreCase("alta")) {
            info += "Nueva: " + personaNueva;
        } else if (tipo.equalsIgnoreCase("baja")) {
            info += "Eliminada: " + personaAnterior;
        } else if (tipo.equalsIgnoreCase("modif")) {
            info += "Antes: " + personaAnterior + ", Después: " + personaNueva;
        }
        return info;
    }
}
