import java.io.IOException;

public class Parser {
    
    public static void parseCommand(String userInput, TaskList taskList, Storage storage) {
        try {
            if (userInput.equals("list")) {
                listTasks(taskList);

            } else if (userInput.matches("^(mark|unmark).*")) {
                toggleTask(userInput, taskList);
                storage.writeToStorage(taskList);

            } else if (userInput.matches("^(todo|deadline|event).*")) {
                addTask(userInput, taskList);
                storage.writeToStorage(taskList);

            } else if (userInput.matches("^delete.*")) {
                deleteTask(userInput, taskList);
                storage.writeToStorage(taskList);

            } else {
                String commands = "\tlist\n"
                                + "\tbye\n"
                                + "\tmark <index>\n"
                                + "\tunmark <index>\n"
                                + "\tdelete <index>\n"
                                + "\ttodo <task description>\n"
                                + "\tdeadline <task description> /by <due date>\n"
                                + "\tevent <task description> /from <fromDate> /to <toDate>\n";
                Ui.formatReply("Invalid command. Please try one of the following commands:\n" + commands);
            }

        } catch (IOException e) {
            Ui.formatReply("An I/O error occur while trying to write to " + storage.getFilePath()
                        + ":\n" + e.getMessage());
        }
        
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

    private static void toggleTask(String userInput, TaskList taskList ) {
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
                Ui.formatReply("\"" + task + "\" is already " + parts[0] +"ed");
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
            throws MotivaException{
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
