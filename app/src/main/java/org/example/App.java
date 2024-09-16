package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static TaskService taskService;
    private static Scanner teclado;

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            taskService = new TaskService(conn);
            teclado = new Scanner(System.in);

            System.out.println("------------Bienvenido a su Gestor de Tareas----------------");

            mostrarMenu();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para mostrar todas las tareas pendientes
    private static void mostrarTareasPendientes() throws SQLException {
        System.out.println("Sus Tareas son:");
        List<Task> tasks = taskService.getAllTasks();
        tasks.forEach(System.out::println);
    }

    // Método para mostrar el menú
    private static void mostrarMenu() throws SQLException {
        System.out.println("\n---------------Menú de opciones:-------------\n" + 
                           "1 - Crear Tareas\n" + 
                           "2 - Crear Etiquetas\n" + 
                           "3 - Marcar como completado\n" + 
                           "4 - Eliminar Tareas\n" + 
                           "5 - Actualizar Tarea\n" + 
                           "6 - Mostrar Tareas Completadas\n" +
                           "7 - Mostrar Todas las Tareas");
        int user_input = teclado.nextInt();
        teclado.nextLine(); // Consumir la nueva línea

        switch (user_input) {
            case 1:
                crearTarea();
                break;
            case 2:
                crearEtiqueta();
                break;
            case 3:
                marcarTareaComoCompletada();
                break;
            case 4:
                eliminarTarea();
                break;
            case 5:
                actualizarTarea();
                break;
            case 6:
                mostrarTareasCompletadas();
                break;
            case 7:
                mostrarTodasLasTareas();
            default:
                System.out.println("Opción no válida.");
                mostrarMenu(); // Volver a mostrar el menú si la opción no es válida
                break;
        }
    }

    // Método para crear una nueva tarea
    private static void crearTarea() throws SQLException {
        System.out.println("--Ingrese el título de la tarea:");
        String title = teclado.nextLine();

        System.out.println("Ingrese la descripción de la tarea:");
        String description = teclado.nextLine();

        System.out.println("Seleccione una etiqueta de la siguiente lista:");
        List<Label> labels = taskService.getAllLabels();
        labels.forEach(System.out::println);

        int labelId = teclado.nextInt();
        teclado.nextLine(); // Consumir el salto de línea

        // Obtener el nombre de la etiqueta usando el ID
        String labelName = taskService.getLabelNameById(labelId);
        // Crear una nueva tarea
        Task task = new Task(title, description, false, labelName);
        taskService.addTask(task);

        System.out.println("La tarea ha sido creada exitosamente.");
        mostrarMenu(); // Volver al menú después de crear la tarea
    }

    //Método para crear una etiqueta
    private static void crearEtiqueta() throws SQLException{
        System.out.println("Ingresa el nombre de la etiqueta que quieres crear:\n");
        String labelname = teclado.nextLine();
        taskService.addLabel(labelname);

        System.out.println("La etiqueta ha sido creada exitosamente");
        mostrarMenu();
    }

    // Método para marcar una tarea como completada
    private static void marcarTareaComoCompletada() throws SQLException {
        System.out.println("¿Qué tarea deseas marcar como completada?");
        mostrarTareasPendientes();
        
        int completed = teclado.nextInt();
        taskService.markTaskAsCompleted(completed);

        System.out.println("La tarea ha sido marcada como completada.");
        mostrarMenu(); // Volver al menú
    }

    // Método para eliminar una tarea
    private static void eliminarTarea() throws SQLException {
        System.out.println("¿Qué tarea deseas eliminar?");
        mostrarTareasPendientes();

        int deleted = teclado.nextInt();
        taskService.deleteTask(deleted);

        System.out.println("La tarea ha sido eliminada.");
        mostrarMenu(); // Volver al menú
    }

    // Método para actualizar una tarea
    private static void actualizarTarea() throws SQLException {
        System.out.println("¿Qué tarea deseas actualizar?");
        mostrarTareasPendientes();

        int idupdated = teclado.nextInt();
        teclado.nextLine(); // Consumir la nueva línea

        System.out.println("--Ingrese el título de la tarea:");
        String title = teclado.nextLine();

        System.out.println("Ingrese la descripción de la tarea:");
        String description = teclado.nextLine();

        // Crear una nueva tarea
        Task task = new Task(idupdated, title, description, false, "Education");
        taskService.updateTask(task);

        System.out.println("La tarea ha sido actualizada.");
        mostrarMenu(); // Volver al menú
    }

    // Método para mostrar las tareas completadas
    private static void mostrarTareasCompletadas() throws SQLException {
        List<Task> completedTasks = taskService.getIncompletedTasks();

        if (completedTasks.isEmpty()) {
            System.out.println("No hay tareas incompletas.");
        } else {
            System.out.println("Tareas incompletas:");
            completedTasks.forEach(System.out::println);
        }

        mostrarMenu(); // Volver al menú
    }

    //Método para mostrar todas las tareas
    private static void mostrarTodasLasTareas() throws SQLException{
        mostrarTareasPendientes();
        mostrarMenu();
    }
}
