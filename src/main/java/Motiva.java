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
            "|_|  |_|\\___/ \\__|_| \\_/ \\__,_|\n";

        formatReply(logo + "Hello! I'm Motiva.\nWhat can I do for you?");
    }

    private static void sayGoodBye() {
        formatReply("Bye. Hope to see you again soon!");
    }

    private static void listTasks(ArrayList<Task> taskList) {

        if (taskList.isEmpty()) {
            formatReply("No tasks found.");
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

        String[] parts = userInput.split(" ", 2);
        String taskType = parts[0];
        String taskDescription = parts.length > 1 ? parts[1] : "";

        try {
            switch (taskType) {
                case "todo":
                    createTodo(taskDescription, taskList);
                    break;
        
                case "deadline":
                    createDeadline(taskDescription, taskList);
                    break;
        
                case "event":
                    createEvent(taskDescription, taskList);
                    break;

                default:
                    throw new MotivaException("Invalid task type: " + taskType + "\nPlease use: todo, deadline or event");
            }
        } catch (MotivaException e) {
            formatReply(e.getMessage());
        }
    }

    private static void createTodo(String taskDescription, ArrayList<Task> taskList) throws MotivaException {
        if (taskDescription.trim().isEmpty()) {
            throw new MotivaException("Invalid todo format. Please use:\ntodo <task description>");
        }
        Task task = new Todo(taskDescription.trim());
        taskList.add(task);
        formatReply("Got it. I've added this task:\n  " + task 
                + "\nNow you have " + taskList.size() + " tasks in the list.");
    }

    private static void createDeadline(String taskDescription, ArrayList<Task> taskList) throws MotivaException {
        String[] parts = taskDescription.split(" /by ", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new MotivaException("Invalid deadline format. Please use:\ndeadline <task description> /by <due date>");
        }

        Task task = new Deadline(parts[0].trim(), parts[1].trim());
        taskList.add(task);
        formatReply("Got it. I've added this task:\n  " + task 
                + "\nNow you have " + taskList.size() + " tasks in the list.");
    }

    private static void createEvent(String taskDescription, ArrayList<Task> taskList) throws MotivaException{
        String[] parts = taskDescription.split(" /from | /to ");

        if (parts.length < 3 || parts[0].trim().isEmpty()
                || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            throw new MotivaException("Invalid event format. "
                    + "Please use:\nevent <task description> /from <fromDate> /to <toDate>");
        }

        Task task = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        taskList.add(task);
        formatReply("Got it. I've added this task:\n  " + task 
                + "\nNow you have " + taskList.size() + " tasks in the list.");
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
            } else if (userInput.matches("^(mark|unmark).*")) {
                toggleTask(userInput, taskList);
            } else if (userInput.matches("^(todo|deadline|event).*")) {
                addTask(userInput, taskList);
            } else {
                String commands = "\tlist\n"
                                + "\tbye\n"
                                + "\tmark <index>\n"
                                + "\tunmark <index>\n"
                                + "\ttodo <task description>\n"
                                + "\tdeadline <task description> /by <due date>\n"
                                + "\tevent <task description> /from <fromDate> /to <toDate>\n";
                formatReply("Invalid command. Please try one of the following commands:\n" + commands);
            }
        }
    }
}
