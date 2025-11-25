package gestionPersonas.app;

import gestionPersonas.model.Persona;
import gestionPersonas.service.CrudService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CrudService service = new CrudService();
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\nMenú Principal ---- PERSONAS ----");
            System.out.println("[1] Agregar");
            System.out.println("[2] Eliminar");
            System.out.println("[3] Actualizar");
            System.out.println("[4] Deshacer");
            System.out.println("[5] Rehacer");
            System.out.println("[6] Log");
            System.out.println("[7] Imprimir");
            System.out.println("[8] Salir");
            System.out.print("Seleccione una opción: ");

            if (sc.hasNextInt()) {
                opcion = sc.nextInt();
                sc.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1:
                        System.out.println("Introduce los datos de la persona:");

                        try {
                            System.out.print("ID: ");
                            if (!sc.hasNextInt()) {
                                System.out.println("Error: El ID debe ser un número entero.");
                                sc.nextLine(); // limpiar entrada inválida
                                break;
                            }
                            int id = sc.nextInt();
                            sc.nextLine(); // limpiar buffer

                            System.out.print("Nombre: ");
                            String nombre = sc.nextLine().trim();

                            System.out.print("Apellido: ");
                            String apellido = sc.nextLine().trim();

                            System.out.print("Edad: ");
                            if (!sc.hasNextInt()) {
                                System.out.println("Error: La edad debe ser un número entero.");
                                sc.nextLine(); // limpiar entrada inválida
                                break;
                            }
                            int edad = sc.nextInt();
                            sc.nextLine(); // limpiar buffer

                            System.out.print("Email: ");
                            String email = sc.nextLine().trim();

                            String fechaRegistro = java.time.LocalDateTime.now().format(
                                    java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss"));

                            Persona persona = new Persona(id, nombre, apellido, edad, email, fechaRegistro);
                            service.agregarPersona(persona); // El método ya maneja las validaciones
                        } catch (Exception e) {
                            System.out.println("Error al procesar los datos. Intente nuevamente.");
                            sc.nextLine(); // limpiar buffer en caso de error
                        }
                        break;
                    case 2:
                        service.imprimirPersonas();
                        System.out.print("ID de la persona a eliminar: ");
                        if (sc.hasNextInt()) {
                            int idEliminar = sc.nextInt();
                            sc.nextLine(); // limpiar buffer
                            service.eliminarPersona(idEliminar);
                        } else {
                            System.out.println("Entrada inválida. Introduzca el ID (número entero).");
                            sc.nextLine();
                        }
                        break;
                    case 3:
                        service.imprimirPersonas();
                        System.out.println("Actualizar los datos de la persona con el ID:");

                        try {
                            System.out.print("ID: ");
                            if (!sc.hasNextInt()) {
                                System.out.println("Error: El ID debe ser un número entero.");
                                sc.nextLine(); // limpiar entrada inválida
                                break;
                            }
                            int idAct = sc.nextInt();
                            sc.nextLine();

                            // Validar que el ID existe
                            Persona existente = service.buscarPersonaPorId(idAct);

                            if (existente == null) {
                                System.out.println("No existe una persona con ese ID.");
                                break;
                            }

                            System.out.println("\nPersona encontrada:");
                            System.out.println(existente);

                            System.out.print("Nuevo nombre: ");
                            String nomAct = sc.nextLine().trim();

                            System.out.print("Nuevo apellido: ");
                            String apeAct = sc.nextLine().trim();

                            System.out.print("Nueva edad: ");
                            if (!sc.hasNextInt()) {
                                System.out.println("Error: La edad debe ser un número entero.");
                                sc.nextLine(); // limpiar entrada inválida
                                break;
                            }
                            int edadAct = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Nuevo email: ");
                            String emailAct = sc.nextLine().trim();

                            // Crear SOLO un objeto con los datos modificados
                            Persona datos = new Persona();
                            datos.setId(idAct);
                            datos.setNombre(nomAct);
                            datos.setApellido(apeAct);
                            datos.setEdad(edadAct);
                            datos.setEmail(emailAct);

                            service.actualizarPersona(datos); // El método ya maneja las validaciones
                        } catch (Exception e) {
                            System.out.println("Error al procesar los datos. Intente nuevamente.");
                            sc.nextLine(); // limpiar buffer en caso de error
                        }
                        break;
                    case 4:
                        service.deshacer();
                        break;
                    case 5:
                        service.rehacer();
                        break;
                    case 6:
                        service.mostrarLog();
                        break;
                    case 7:
                        service.imprimirPersonas();
                        break;
                    case 8:
                        System.out.println("Gracias por usar el sistema CRUD+ Personas");
                        break;
                    default:
                        System.out.println("Opción inválida. Intente de nuevo.");
                }
            } else {
                System.out.println("Entrada inválida. Introduzca un número del 1 al 8.");
                sc.nextLine(); // limpiar entrada inválida
                opcion = 0; // para continuar el ciclo
            }

        } while (opcion != 8);

        sc.close();
    }
}
