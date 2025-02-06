package motiva.ui;

/**
 * Handles user interface interactions for the Motiva application.
 */
public class Ui {

    /**
     * Formats and prints a reply message with indentation and separators.
     *
     * @param text The message to be formatted and displayed.
     */
    public static void formatReply(String text) {
        String indent = " ".repeat(4);
        String[] lines = text.split("\n");

        System.out.println(indent + "============================================================");

        for (String line : lines) {
            System.out.println(" " + indent + line);
        }

        System.out.println(indent + "============================================================\n");
    }

    /**
     * Displays a greeting message when the application starts.
     */
    public static void sayGreeting() {
        String logo = " __  __       _   _\n"
                + "|  \\/  | ___ | |_(_)_   ____ _\n"
                + "| |\\/| |/ _ \\| __| \\ \\ / / _` |\n"
                + "| |  | | (_) | |_| |\\ V / (_| |\n"
                + "|_|  |_|\\___/ \\__|_| \\_/ \\__,_|\n";

        formatReply(logo + "Hello! I'm Motiva.\nWhat can I do for you?");
    }

    /**
     * Displays a goodbye message when the application exits.
     */
    public static void sayGoodBye() {
        formatReply("Bye. Hope to see you again soon!");
    }

}
