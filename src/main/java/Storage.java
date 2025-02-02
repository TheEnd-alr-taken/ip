import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> loadFromStorage() throws IOException, MotivaException {
        ArrayList<Task> taskList = new ArrayList<>();
        File file = new File(filePath);
        
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return taskList;
        }

        Scanner scanner = new Scanner(file); 
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" \\| ");

            if (parts.length < 3 || !parts[0].matches("^[TDE]$") || !parts[1].matches("^[ X]$")) {
                throw new MotivaException("Invalid task format in " + this.filePath + " :\n " + line);
            }

            String taskType = parts[0];
            boolean isDone = parts[1].equals("X");
            String[] taskParts = Arrays.copyOfRange(parts, 2, parts.length);

            if (!Task.isValidTask(taskType, taskParts)) {
                throw new MotivaException("Invalid task format in " + this.filePath + " :\n " + line);
            }

            Task task = switch (taskType) {
                case "T" -> new Todo(taskParts[0].trim());
                case "D" -> new Deadline(taskParts[0].trim(), taskParts[1].trim());
                case "E" -> new Event(taskParts[0].trim(), taskParts[1].trim(), taskParts[2].trim());
                default -> new Task("Should Never Reach Here");
            };

            if (isDone) {
                task.toggleDone();
            }

            taskList.add(task);
        }

        return taskList;
    }

    public void writeToStorage(ArrayList<Task> taskList) throws IOException {
        FileWriter fw = new FileWriter(this.filePath);
        for (Task task : taskList) {
            fw.write(task.toFileString() + "\n");
        }
        fw.close();
    }
}
