import java.io.IOException;
import java.util.Scanner;

public class Motiva {
    private static final String DATA_FILE_PATH = "./data/motiva.txt";
    
    private final Storage storage;
    private TaskList taskList;

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

    public void run() {
        Ui.sayGreeting();
        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        while (true) {
            userInput = scanner.nextLine();

            if (userInput.isEmpty()) {
                Ui.formatReply("No task captured.\nDo key in the task for me to keep track.");
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

    public static void main(String[] args) {
        new Motiva(DATA_FILE_PATH).run();
    }
}
