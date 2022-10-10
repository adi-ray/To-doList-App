package com.example.mytodolist;

public class Model {
    private String task,description,id,date,dueDate;
    public Model(){

    }
    public Model(String task, String description, String id, String date,String dueDate) {
        this.task = task;
        this.description = description;
        this.id = id;
        this.date = date;
        this.dueDate = dueDate;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDueDate() {
        return dueDate;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
