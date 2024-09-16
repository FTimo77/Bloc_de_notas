package org.example;

import org.example.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskService {

    private Connection connection;

    // Constructor que inicializa la conexión a la base de datos
    public TaskService(Connection connection) {
        this.connection = connection;
    }

    // Método para agregar una nueva tarea a la base de datos
    public void addTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, description, completed, label) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setBoolean(3, task.isCompleted());
            statement.setString(4, task.getLabel());
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        task.setId(generatedId); // Asignar el ID generado a la tarea
                    }
                }
            }
        }
    }
    

    // Método para obtener todas las tareas desde la base de datos
    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                boolean completed = resultSet.getBoolean("completed");
                String label = resultSet.getString("label");
                Task task = new Task(id, title, description, completed, label);
                tasks.add(task);
            }
        }
        return tasks;
    }

    // Método para actualizar una tarea existente en la base de datos basado en su ID
    public void updateTask(Task task) throws SQLException {
        String sql = "UPDATE tasks SET title = ?, description = ?, completed = ?, label = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setBoolean(3, task.isCompleted());
            statement.setString(4, task.getLabel());
            statement.setInt(5, task.getId());
            statement.executeUpdate();
        }
    }
    

    // Método para eliminar una tarea de la base de datos
    public void deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, taskId);
            statement.executeUpdate();
        }
    }
    // Método para marcar una tarea como completada
public void markTaskAsCompleted(int taskId) throws SQLException {
    String sql = "UPDATE tasks SET completed = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setBoolean(1, true);  // Establecer "completed" en true (completada)
        statement.setInt(2, taskId);    // Establecer el id de la tarea
        statement.executeUpdate();
    }
}
//Método para obtener las tareas incompletas
public List<Task> getIncompletedTasks() throws SQLException {
    List<Task> completedTasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE completed = 0";
    
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sql)) {
        
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            boolean completed = resultSet.getBoolean("completed");
            String label = resultSet.getString("label");
            
            // Crea un nuevo objeto Task con los datos obtenidos de la base de datos
            Task task = new Task(id, title, description, completed, label);
            completedTasks.add(task);  // Agrega la tarea completada a la lista
        }
    }
    
    return completedTasks;  // Devuelve la lista de tareas completadas
}

//Crear Etiquetas por el usuario
public void addLabel(String labelName) throws SQLException {
    String sql = "INSERT INTO labels (name) VALUES (?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, labelName);
        statement.executeUpdate();
    }
}
//Obtener las etiquetas
public List<Label> getAllLabels() throws SQLException {
    List<Label> labels = new ArrayList<>();
    String sql = "SELECT * FROM labels";
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sql)) {

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Label label = new Label(id, name);
            labels.add(label);
        }
    }
    return labels;
}

public String getLabelNameById(int labelId) throws SQLException {
    String sql = "SELECT name FROM labels WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, labelId);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getString("name");
            } else {
                throw new SQLException("No se encontró una etiqueta con el ID: " + labelId);
            }
        }
    }
}


}

