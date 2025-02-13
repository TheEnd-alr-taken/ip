package motiva;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import motiva.parser.Parser;
import motiva.storage.Storage;
import motiva.task.TaskList;
import motiva.ui.Ui;

/**
 * The main class for the Motiva application, responsible for initializing and running the program.
 */
public class Motiva {
    private static final String DATA_FILE_PATH = "./data/motiva.txt";

    private final Storage storage;
    private TaskList taskList;

    /**
     * Constructs a new instance of Motiva with the specified file path for data storage.
     * Tries to load all the tasks into taskList. If exception occur, create a new taskList.
     *
     * @param filePath The file path where task data is stored.
     */
    public Motiva(String filePath) {
        storage = new Storage(filePath);

        try {
            taskList = storage.loadFromStorage();
        } catch (IOException | MotivaException e) {
            Ui.formatReply("An I/O error occur while trying to read from " + DATA_FILE_PATH
                    + " :\n" + e.getMessage());
            taskList = new TaskList();
        }
    }

    public Motiva() {
        this(DATA_FILE_PATH);
    }

    public String getResponse(String input) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            if (input.isEmpty()) {
                Ui.formatReply(Parser.listCommands());
            } else if (input.equals("bye")) {
                Ui.sayGoodBye();
            } else {
                Parser.parseCommand(input, taskList, storage);
            }
        } finally {
            System.setOut(originalOut);
        }

        return outputStream.toString().trim();
    }

    /**
     * Starts and runs the main execution loop of the application, processing user input.
     */
    public void run() {
        Ui.sayGreeting();
        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        while (true) {
            userInput = scanner.nextLine();

            if (userInput.isEmpty()) {
                Ui.formatReply(Parser.listCommands());
                continue;
            }

            if (userInput.equals("bye")) {
                Ui.sayGoodBye();
                break;
            }

            Parser.parseCommand(userInput, taskList, storage);

        }
        scanner.close();
    }

    /**
     * The main entry point of the Motiva application.
     *
     * @param args Command-line arguments (not used in the current implementation).
     */
    public static void main(String[] args) {
        new Motiva(DATA_FILE_PATH).run();
    }
}
