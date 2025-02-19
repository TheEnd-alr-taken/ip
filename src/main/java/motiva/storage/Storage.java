package motiva.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import motiva.MotivaException;
import motiva.parser.Parser;
import motiva.task.Task;
import motiva.task.TaskList;


/**
 * Handles loading and saving tasks to a file.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a Storage object with the specified file path.
     *
     * @param filePath The file path where task data is stored.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the file path associated with this storage.
     *
     * @return The file path where task data is stored.
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return A TaskList containing all tasks loaded from storage.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws MotivaException If the file contains invalid task data.
     */
    public TaskList loadFromStorage() throws IOException, MotivaException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return new TaskList();
        }

        return readTasksFromFile(file);
    }

    private TaskList readTasksFromFile(File file) throws IOException, MotivaException {
        TaskList taskList = new TaskList();
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Task task = Parser.parseTaskFromLine(line);
            taskList.add(task);
        }
        scanner.close();
        return taskList;
    }

    /**
     * Writes the current list of tasks to the storage file.
     *
     * @param taskList The TaskList to be saved to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void writeToStorage(TaskList taskList) throws IOException {
        FileWriter fw = new FileWriter(this.filePath);
        for (Task task : taskList.getTasks()) {
            fw.write(task.toFileString() + "\n");
        }
        fw.close();
    }
}
