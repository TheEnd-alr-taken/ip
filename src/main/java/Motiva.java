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

    private static void listTasks(ArrayList<String> taskList) {

        if (taskList.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            String text = "";
            int count = 1;

            for (String task : taskList) {
                text += count + ". " + task + "\n";
                count++;
            }

            formatReply(text);
        }
    }

    private static void addTask(String userInput, ArrayList<String> taskList) {
        taskList.add(userInput);
        formatReply("added: " + userInput);
    }

    public static void main(String[] args) {
        sayGreeting();

        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        ArrayList<String> taskList = new ArrayList<>();

        while (true) {
            userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                sayGoodBye();
                break;
            } else if (userInput.equals("list")) {
                listTasks(taskList);
            } else {
                addTask(userInput, taskList);
            }
        }
    }
}
