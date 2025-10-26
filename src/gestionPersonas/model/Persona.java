package gestionPersonas.model;

public class Persona {
    private int id;
    private String nombre;
    private String apellido;
    private int edad;
    private String email;
    private String fechaRegistro;

    // Constructor vacío
    public Persona() {
    }

    // Constructor completo
    public Persona(int id, String nombre, String apellido, int edad, String email, String fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.email = email;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // toString para imprimir
    @Override
    public String toString() {
        return "[" + id + ", " + nombre + ", " + apellido + ", " + edad + ", " + email + "]";
    }
}
