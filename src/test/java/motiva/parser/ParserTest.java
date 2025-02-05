package motiva.parser;

import motiva.storage.Storage;
import motiva.task.TaskList;
import motiva.task.Todo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ParserTest {

    private TaskList taskList;
    private Storage storage;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final String TEST_FILE_PATH = "./data/test.txt";

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        storage = new Storage(TEST_FILE_PATH);
        System.setOut(new PrintStream(outContent));
    }

    @AfterAll
    public static void tearDown() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void listTasks_emptyTaskList_printsNoTasks() {
        Parser.parseCommand("list", taskList, storage);
        assertTrue(outContent.toString().contains("No tasks found."));
    }

    @Test
    public void listTasks_withTasks_printsTaskList() {
        taskList.add(new Todo("Read book"));
        taskList.add(new Todo("Write report"));

        Parser.parseCommand("list", taskList, storage);
        String output = outContent.toString();
        assertTrue(output.contains("1.[T][ ] Read book"));
        assertTrue(output.contains("2.[T][ ] Write report"));
    }

    @Test
    public void toggleTask_validMarkCommand_marksTask() {
        taskList.add(new Todo("Complete homework"));
        Parser.parseCommand("mark 1", taskList, storage);

        assertTrue(taskList.get(0).isDone());
        assertTrue(outContent.toString().contains("I've marked this task as done"));

        Parser.parseCommand("mark 1", taskList, storage);

        assertTrue(outContent.toString().contains("is already marked"));
    }

    @Test
    public void toggleTask_validUnmarkCommand_unmarksTask() {
        Todo task = new Todo("Complete homework");
        task.toggleDone();
        taskList.add(task);

        Parser.parseCommand("unmark 1", taskList, storage);

        assertFalse(taskList.get(0).isDone());
        assertTrue(outContent.toString().contains("I've marked this task as not done yet"));

        Parser.parseCommand("unmark 1", taskList, storage);

        assertTrue(outContent.toString().contains("is already unmarked"));
    }

    @Test
    public void toggleTask_invalidIndex_showsErrorMessage() {
        Parser.parseCommand("mark 1", taskList, storage);
        assertTrue(outContent.toString().contains("Invalid index: no tasks found with that index"));
    }

    @Test
    public void toggleTask_invalidFormat_showsErrorMessage() {
        Parser.parseCommand("mark 1 2", taskList, storage);
        assertTrue(outContent.toString().contains("Invalid mark format. Please use"));

        Parser.parseCommand("unmark 1 2", taskList, storage);
        assertTrue(outContent.toString().contains("Invalid unmark format. Please use"));
    }

}
