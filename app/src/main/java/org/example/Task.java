package org.example;

public class Task {
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private String label;
    //constructor
     // Constructor con ID
     public Task(int id, String title, String description, boolean completed, String label) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.label = label;
    }
    // Constructor sin ID (para cuando aún no se ha generado)
    public Task(String title, String description, boolean completed, String label) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.label = label;
    }
    
    //getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getLabel() {
        return label;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    //método para ingresar al título el completo y la etiqueta
    @Override
    public String toString() {
        return id + " " + title + (completed ? " (Completed)" : "") + 
               (label != null && !label.isEmpty() ? "      [Etiqueta: " + label + "]" : "");
    }
}
