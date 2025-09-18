package org.example;

import org.example.task.Task;
import org.example.task.TaskManager;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TaskManager tm = new TaskManager();

        System.out.println("Console To-Do List");
        System.out.println("Commands: a  |  v<id>  |  v<dd/MM/yyyy>  |  d<id>");
        System.out.println("(Example: a, v101, v14/09/2025, d112)");
        System.out.println("--------------------------------------------------");

        while (true) {
            System.out.print("\nEnter command: ");
            String line = sc.nextLine().trim();

            if (line.equalsIgnoreCase("a")) {
                System.out.print("    Message: ");
                String msg = sc.nextLine();

                String date;
                while (true) {
                    System.out.print("    Date    : ");
                    date = sc.nextLine().trim();
                    if (TaskManager.isValidDate(date)) break;
                    System.out.println("    Invalid date. Use dd/MM/yyyy (e.g., 14/09/2025). Try again.");
                }

                Task added = tm.addTask(msg, date);
                System.out.println("\nAdded: " + added);
                printAll(tm);
                continue;
            }

            if (line.startsWith("v") || line.startsWith("V")) {
                String arg = line.substring(1).trim();
                if (arg.isEmpty()) {
                    System.out.println("Please provide an id or a date after 'v'.");
                    continue;
                }

                // Try as date first
                if (TaskManager.isValidDate(arg)) {
                    List<Task> list = tm.getByDate(arg);
                    if (list.isEmpty()) {
                        System.out.println("No tasks on date " + arg + ".");
                    } else {
                        System.out.println("Tasks on " + arg + ":");
                        list.forEach(t -> System.out.println("  " + t));
                    }
                } else {
                    // Try as id
                    try {
                        long id = Long.parseLong(arg);
                        Task t = tm.getById(id);
                        if (t == null) System.out.println("No task found with id " + id + ".");
                        else System.out.println(t);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid argument after 'v'. Use id (e.g., v101) or date (e.g., v14/09/2025).");
                    }
                }
                continue;
            }

            if (line.startsWith("d") || line.startsWith("D")) {
                String arg = line.substring(1).trim();
                if (arg.isEmpty()) {
                    System.out.println("Please provide an id after 'd'.");
                    continue;
                }
                try {
                    long id = Long.parseLong(arg);
                    boolean ok = tm.deleteById(id);
                    if (!ok) {
                        System.out.println("No task found with id " + id + ".");
                    } else {
                        System.out.println("Deleted task with id " + id +".");
                        printAll(tm);
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid id. Example: d112");
                }
                continue;
            }

            System.out.println("Unknown command. Use: a  |  v<id>  |  v<dd/MM/yyyy>  |  d<id>");
        }
    }

    private static void printAll(TaskManager tm) {
        List<Task> all = tm.getAll();
        if (all.isEmpty()) {
            System.out.println("(No tasks)");
            return;
        }
        System.out.println("All tasks:");
        for (Task t : all) {
            System.out.println("  " + t);
        }
    }
}
