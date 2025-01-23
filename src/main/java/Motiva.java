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

        formatReply(logo + " Hello! I'm Motiva.\n What can I do for you?");
    }

    private static void sayGoodBye() {
        formatReply(" Bye. Hope to see you again soon!");
    }

    public static void main(String[] args) {
        sayGreeting();

        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        while (true) {
            userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                sayGoodBye();
                break;
            } else {
                formatReply(userInput);
            }
        }
    }
}
