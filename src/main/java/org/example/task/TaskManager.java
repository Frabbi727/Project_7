package org.example.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class TaskManager {
    private final Set<Task> tasks;
    private final Map<Long, Task> byId;
    private final Map<String, Set<Task>> byDate;
    private final AtomicLong seq = new AtomicLong(100); // start ids at 100

    private static final DateTimeFormatter DTF = DateTimeFormatter
            .ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public TaskManager() {
        // Keep tasks ordered by date (dd/MM/yyyy), then id
        Comparator<Task> cmp = Comparator
                .comparing((Task t) -> parseDate(t.getDate()))
                .thenComparingLong(Task::getId);
        this.tasks = new TreeSet<>(cmp);
        this.byId = new HashMap<>();
        this.byDate = new HashMap<>();
    }

    private static LocalDate parseDate(String d) {
        return LocalDate.parse(d, DTF);
    }

    public static boolean isValidDate(String d) {
        try { parseDate(d); return true; }
        catch (Exception e) { return false; }
    }

    public Task addTask(String message, String date) {
        long id = seq.getAndIncrement();
        Task t = new Task(id, message.trim(), date.trim());
        tasks.add(t);
        byId.put(id, t);
        byDate.computeIfAbsent(date, k -> new LinkedHashSet<>()).add(t);
        return t;
    }

    public boolean deleteById(long id) {
        Task t = byId.remove(id);
        if (t == null) return false;
        tasks.remove(t);
        Set<Task> set = byDate.get(t.getDate());
        if (set != null) {
            set.remove(t);
            if (set.isEmpty()) byDate.remove(t.getDate());
        }
        return true;
    }

    public Task getById(long id) { return byId.get(id); }

    public List<Task> getByDate(String date) {
        Set<Task> set = byDate.getOrDefault(date, Collections.emptySet());
        return new ArrayList<>(set);
    }

    public List<Task> getAll() { return new ArrayList<>(tasks); }
}
