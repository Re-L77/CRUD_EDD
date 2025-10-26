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

                        System.out.print("ID: ");
                        int id = sc.nextInt();
                        sc.nextLine(); // limpiar buffer

                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();

                        System.out.print("Apellido: ");
                        String apellido = sc.nextLine();

                        System.out.print("Edad: ");
                        int edad = sc.nextInt();
                        sc.nextLine(); // limpiar buffer

                        System.out.print("Email: ");
                        String email = sc.nextLine();

                        String fechaRegistro = java.time.LocalDateTime.now().format(
                                java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss"));

                        Persona persona = new Persona(id, nombre, apellido, edad, email, fechaRegistro);
                        service.agregarPersona(persona);
                        break;
                    case 2:
                        System.out.println("Función Eliminar aún no implementada.");
                        break;
                    case 3:
                        System.out.println("Función Actualizar aún no implementada.");
                        break;
                    case 4:
                        System.out.println("Función Deshacer aún no implementada.");
                        break;
                    case 5:
                        System.out.println("Función Rehacer aún no implementada.");
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
