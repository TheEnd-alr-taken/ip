import java.util.ArrayList;
import java.util.Scanner;

public class Motiva {

    private static void formatReply(String text) {
        String indent = " ".repeat(4);
        String[] lines = text.split("\n");

        System.out.println(indent + "============================================================");

        for (String line : lines) {
            System.out.println(" "+ indent + line);
        }

        System.out.println(indent + "============================================================\n");
    }

    private static void sayGreeting() {
        String logo =
            " __  __       _   _\n" +
            "|  \\/  | ___ | |_(_)_   ____ _\n" +
            "| |\\/| |/ _ \\| __| \\ \\ / / _` |\n" +
            "| |  | | (_) | |_| |\\ V / (_| |\n" +
            "|_|  |_|\\___/ \\__|_| \\_/ \\__,_|\n\n";

        formatReply(logo + "Hello! I'm Motiva.\nWhat can I do for you?");
    }

    private static void sayGoodBye() {
        formatReply("Bye. Hope to see you again soon!");
    }

    private static void listTasks(ArrayList<Task> taskList) {

        if (taskList.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            String text = "Here are the tasks in your list:\n";
            int count = 1;

            for (Task task : taskList) {
                text += count + "." + task + "\n";
                count++;
            }

            formatReply(text);
        }
    }

    private static void toggleTask(String userInput, ArrayList<Task> taskList ) {
        try {
            String[] parts = userInput.split(" ");
            int index = Integer.parseInt(parts[1]) - 1;
            Task task = taskList.get(index);

            if (userInput.startsWith("mark") && !task.isDone()) {
                task.toggleDone();
                formatReply("Nice! I've marked this task as done:\n  " + task);

            } else if (userInput.startsWith("unmark") && task.isDone()) {
                task.toggleDone();
                formatReply("OK, I've marked this task as not done yet:\n  " + task);

            } else {
                formatReply("\"" + task + "\" is already " + parts[0] +"ed");
            }

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            formatReply("Invalid index: no tasks found with that index");
        }
    }

    private static void addTask(String userInput, ArrayList<Task> taskList) {
        Task newTask = new Task(userInput);
        taskList.add(newTask);
        formatReply("added: " + userInput);
    }

    public static void main(String[] args) {
        sayGreeting();

        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        ArrayList<Task> taskList = new ArrayList<>();

        while (true) {
            userInput = scanner.nextLine();

            if (userInput.isEmpty()) {
                formatReply("No task captured.\nDo key in the task for me to keep track.");
                continue;
            }

            if (userInput.equals("bye")) {
                sayGoodBye();
                break;
            } else if (userInput.equals("list")) {
                listTasks(taskList);
            } else if (userInput.startsWith("mark") || userInput.startsWith("unmark")) {
                toggleTask(userInput, taskList);
            } else {
                addTask(userInput, taskList);
            }
        }
    }
}
