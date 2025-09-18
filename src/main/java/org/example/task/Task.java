package org.example.task;

import java.util.Objects;

public class Task {
    private final long id;
    private final String message;
    private final String date;

    public Task(long id, String message, String date) {
        this.id = id;
        this.message = message;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
