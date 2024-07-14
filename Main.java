import java.io.*;
import java.util.Scanner;

class Task {
    private String title;
    private String description;
    private String time;
    private String label;
    private boolean isCompleted;

    public Task(String title, String description, String time, String label) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.label = label;
        this.isCompleted = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getLabel() {
        return label;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markAsCompleted() {
        isCompleted = true;
    }

    @Override
    public String toString() {
        return (isCompleted ? "Completed" : "Not Completed") + " | " + title + " | " + description + " | " + time
                + " | " + label;
    }
}

class ToDoList {
    private Task[] tasks;
    private int taskCount;
    private static final String FILE_NAME = "tasks.txt";
    private static final int MAX_TASKS = 100;

    public ToDoList() {
        tasks = new Task[MAX_TASKS];
        taskCount = 0;
        loadTasks();
    }

    public void addTask(String title, String description, String time, String label) {
        if (taskCount < MAX_TASKS) {
            tasks[taskCount++] = new Task(title, description, time, label);
            saveTasks();
        } else {
            System.out.println("Task list is full.");
        }
    }

    public void viewTasks() {
        if (taskCount == 0) {
            System.out.println("No tasks available.");
        } else {
            for (int i = 0; i < taskCount; i++) {
                System.out.println((i + 1) + ". " + tasks[i]);
            }
        }
    }

    public void markTaskAsCompleted(int index) {
        if (index >= 1 && index <= taskCount) {
            tasks[index - 1].markAsCompleted();
            saveTasks();
            System.out.println("Task " + index + " marked as completed.");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public void deleteTask(int index) {
        if (index >= 1 && index <= taskCount) {
            for (int i = index - 1; i < taskCount - 1; i++) {
                tasks[i] = tasks[i + 1];
            }
            tasks[--taskCount] = null;
            saveTasks();
            System.out.println("Task " + index + " deleted.");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    private void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null && taskCount < MAX_TASKS) {
                boolean isCompleted = line.startsWith("Completed");
                String[] parts = line.split(" \\| ");
                Task task = new Task(parts[1], parts[2], parts[3], parts[4]);
                if (isCompleted) {
                    task.markAsCompleted();
                }
                tasks[taskCount++] = task;
            }
        } catch (FileNotFoundException e) {
            // File not found, start with an empty task list
            tasks = new Task[MAX_TASKS];
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }

    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskCount; i++) {
                writer.write(tasks[i].toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ToDoList toDoList = new ToDoList();
        boolean running = true;

        while (running) {
            System.out.println("To-Do List Application");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Mark Task as Completed");
            System.out.println("4. Delete Task");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter task title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter task time: ");
                    String time = scanner.nextLine();
                    System.out.print("Enter task label: ");
                    String label = scanner.nextLine();
                    toDoList.addTask(title, description, time, label);
                    break;
                case 2:
                    toDoList.viewTasks();
                    break;
                case 3:
                    System.out.print("Enter task number to mark as completed: ");
                    int taskToComplete = scanner.nextInt();
                    toDoList.markTaskAsCompleted(taskToComplete);
                    break;
                case 4:
                    System.out.print("Enter task number to delete: ");
                    int taskToDelete = scanner.nextInt();
                    toDoList.deleteTask(taskToDelete);
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            System.out.println();
        }

        scanner.close();
    }
}
