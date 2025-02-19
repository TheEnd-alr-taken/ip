package motiva.parser;

import java.io.IOException;
import java.util.Arrays;

import motiva.MotivaException;
import motiva.storage.Storage;
import motiva.task.Deadline;
import motiva.task.Event;
import motiva.task.Task;
import motiva.task.TaskList;
import motiva.task.Todo;
import motiva.ui.Ui;

/**
 * Parses user input and executes corresponding commands.
 */
public class Parser {

    /**
     * Parses and executes a user command.
     *
     * @param userInput The user input command.
     * @param taskList The list of tasks to modify.
     * @param storage The storage system to persist changes.
     */
    public static void parseCommand(String userInput, TaskList taskList, Storage storage) {
        try {
            if (userInput.equals("list")) {
                listTasks(taskList);

            } else if (userInput.equals("sort")) {
                sortTasks(taskList);
                storage.writeToStorage(taskList);

            } else if (userInput.matches("^(mark|unmark) .*")) {
                toggleTask(userInput, taskList);
                storage.writeToStorage(taskList);

            } else if (userInput.matches("^(todo|deadline|event) .*")) {
                addTask(userInput, taskList);
                storage.writeToStorage(taskList);

            } else if (userInput.matches("^delete .*")) {
                deleteTask(userInput, taskList);
                storage.writeToStorage(taskList);

            } else if (userInput.matches("^find .*")) {
                findTasks(userInput, taskList);

            } else {
                Ui.formatReply("Invalid command. " + listCommands());
            }

        } catch (IOException e) {
            Ui.formatReply("An I/O error occur while trying to write to " + storage.getFilePath()
                    + ":\n" + e.getMessage());
        }

    }

    /**
     * Parses a task from a single line of text.
     *
     * @param line The line of text representing a task.
     * @return The corresponding Task object.
     * @throws MotivaException If the task format is invalid.
     */
    public static Task parseTaskFromLine(String line) throws MotivaException {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3 || !parts[0].matches("^[TDE]$") || !parts[1].matches("^[ X]$")) {
            throw new MotivaException("Invalid task format: " + line);
        }

        String taskType = parts[0];
        boolean isDone = parts[1].equals("X");
        String[] taskParts = Arrays.copyOfRange(parts, 2, parts.length);

        if (!Task.isValidTask(taskType, taskParts)) {
            throw new MotivaException("Invalid task format: " + line);
        }

        Task task = createTask(taskType, taskParts);
        if (isDone) {
            task.toggleDone();
        }

        return task;
    }

    private static Task createTask(String taskType, String[] taskParts) throws MotivaException {
        return switch (taskType) {
            case "T" -> new Todo(taskParts[0].trim());
            case "D" -> new Deadline(taskParts[0].trim(), taskParts[1].trim());
            case "E" -> new Event(taskParts[0].trim(), taskParts[1].trim(), taskParts[2].trim());
            default -> throw new MotivaException("Unexpected task type: " + taskType);
        };
    }

    /**
     * Returns a string listing all available commands.
     *
     * @return A formatted list of commands.
     */
    public static String listCommands() {
        String commands = "\tlist\n"
                + "\tsort\n"
                + "\tbye\n"
                + "\tmark <index>\n"
                + "\tunmark <index>\n"
                + "\tdelete <index>\n"
                + "\tfind <keyword>\n"
                + "\ttodo <task description>\n"
                + "\tdeadline <task description> /by <due date>\n"
                + "\tevent <task description> /from <fromDate> /to <toDate>\n";
        return "Please try one of the following commands:\n" + commands;
    }

    private static void listTasks(TaskList taskList) {

        if (taskList.isEmpty()) {
            Ui.formatReply("No tasks found.");
        } else {
            String text = "Here are the tasks in your list:\n";
            int count = 1;

            for (Task task : taskList.getTasks()) {
                text += count + "." + task + "\n";
                count++;
            }

            Ui.formatReply(text);
        }
    }

    private static void sortTasks(TaskList taskList) {
        taskList.sort();
        listTasks(taskList);
    }

    private static void findTasks(String userInput, TaskList taskList) {
        try {
            String[] parts = userInput.split(" ", 2);
            String keyword = parts.length > 1 ? parts[1].trim() : "";

            if (parts.length != 2 || keyword.isEmpty()) {
                throw new MotivaException("Invalid find format. Please use:\nfind <keyword_can_have_spaces>");
            }

            String result = "";
            int count = 1;

            for (Task task : taskList.getTasks()) {
                if (task.toString().toLowerCase().contains(keyword.toLowerCase())) {
                    result += count + "." + task + "\n";
                    count++;
                }
            }

            if (result.isEmpty()) {
                result = "No tasks found with that keyword.";
            } else {
                result = "Here are the matching tasks in your list:\n" + result;
            }

            Ui.formatReply(result);

        } catch (MotivaException e) {
            Ui.formatReply(e.getMessage());
        }
    }

    private static void toggleTask(String userInput, TaskList taskList) {
        try {
            String[] parts = userInput.split(" ");

            if (parts.length != 2) {
                throw new MotivaException("Invalid " + parts[0]
                        + " format. Please use:\n" + parts[0] + " <index>");
            }

            int index = Integer.parseInt(parts[1]) - 1;
            Task task = taskList.get(index);

            if (userInput.startsWith("mark") && !task.isDone()) {
                task.toggleDone();
                Ui.formatReply("Nice! I've marked this task as done:\n  " + task);

            } else if (userInput.startsWith("unmark") && task.isDone()) {
                task.toggleDone();
                Ui.formatReply("OK, I've marked this task as not done yet:\n  " + task);

            } else {
                Ui.formatReply("\"" + task + "\" is already " + parts[0] + "ed");
            }

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            Ui.formatReply("Invalid index: no tasks found with that index");
        } catch (MotivaException e) {
            Ui.formatReply(e.getMessage());
        }
    }

    private static void deleteTask(String userInput, TaskList taskList) {
        try {
            String[] parts = userInput.split(" ");

            if (parts.length != 2) {
                throw new MotivaException("Invalid delete format. Please use:\ndelete <index>");
            }

            int index = Integer.parseInt(parts[1]) - 1;
            Task task = taskList.get(index);
            taskList.remove(index);
            Ui.formatReply("Noted. I've removed this task:\n  " + task
                    + "\nNow you have " + taskList.size() + " tasks in the list.");

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            Ui.formatReply("Invalid index: no tasks found with that index");
        } catch (MotivaException e) {
            Ui.formatReply(e.getMessage());
        }
    }

    private static void addTask(String userInput, TaskList taskList) {

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
            }
        } catch (MotivaException e) {
            Ui.formatReply(e.getMessage());
        }
    }

    private static void createTodo(String taskDescription, TaskList taskList)
            throws MotivaException {
        if (taskDescription.trim().isEmpty()) {
            throw new MotivaException("Invalid todo format. Please use:\ntodo <task description>\n"
                    + "Where:\n"
                    + "  - <task description> is a description of the task (cannot be empty).\n");
        }
        Task task = new Todo(taskDescription.trim());
        taskList.add(task);
        Ui.formatReply("Got it. I've added this task:\n  " + task
                + "\nNow you have " + taskList.size() + " tasks in the list.");
    }

    private static void createDeadline(String taskDescription, TaskList taskList)
            throws MotivaException {
        String[] parts = taskDescription.split(" /by ", 2);

        if (!Task.isValidTask("D", parts)) {
            throw new MotivaException("Invalid deadline format. "
                    + "Please use:\ndeadline <task description> /by <due date>\n"
                    + "Where:\n"
                    + "  - <task description> is a description of the task (cannot be empty).\n"
                    + "  - <due date> must be in one of the following formats:\n"
                    + "    - yyyy-MM-dd (e.g., 2025-12-31)\n"
                    + "    - yyyy-MM-dd HHmm (e.g., 2025-12-31 2359)\n");
        }

        Task task = new Deadline(parts[0].trim(), parts[1].trim());
        taskList.add(task);
        Ui.formatReply("Got it. I've added this task:\n  " + task
                + "\nNow you have " + taskList.size() + " tasks in the list.");
    }

    private static void createEvent(String taskDescription, TaskList taskList)
            throws MotivaException {
        String[] parts = taskDescription.split(" /from | /to ");

        if (!Task.isValidTask("E", parts)) {
            throw new MotivaException("Invalid event format. "
                    + "Please use:\nevent <task description> /from <fromDate> /to <toDate>\n"
                    + "Where:\n"
                    + "  - <task description> is a description of the task (cannot be empty).\n"
                    + "  - <fromDate> & <toDate> must be in one of the following formats:\n"
                    + "    - yyyy-MM-dd (e.g., 2025-12-31)\n"
                    + "    - yyyy-MM-dd HHmm (e.g., 2025-12-31 2359)\n");
        }

        Task task = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        taskList.add(task);
        Ui.formatReply("Got it. I've added this task:\n  " + task
                + "\nNow you have " + taskList.size() + " tasks in the list.");
    }

}
