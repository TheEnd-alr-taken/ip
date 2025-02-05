package motiva.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TaskTest {

    @Test
    public void isValidDate_validDateFormats_returnTrue() {
        assertTrue(Task.isValidDate("2025-12-31"));
        assertTrue(Task.isValidDate("2025-12-31 2359"));
    }

    @Test
    public void isValidDate_invalidDateFormats_returnFalse() {
        assertFalse(Task.isValidDate("31-12-2025"));
        assertFalse(Task.isValidDate("2025-31-12"));
        assertFalse(Task.isValidDate("Dec 31 2025"));
        assertFalse(Task.isValidDate("invalid date"));
    }

    @Test
    public void isValidTask_validTodoFormat_returnTrue() {
        assertTrue(Task.isValidTask("T", new String[]{"Buy groceries"}));
    }

    @Test
    public void isValidTask_emptyTodoDescription_returnFalse() {
        assertFalse(Task.isValidTask("T", new String[]{""}));
    }

    @Test
    public void isValidTask_validDeadlineFormat_returnTrue() {
        assertTrue(Task.isValidTask("D", new String[]{"Submit report", "2025-12-31"}));
    }

    @Test
    public void isValidTask_invalidDeadlineFormat_returnFalse() {
        assertFalse(Task.isValidTask("D",
                new String[]{""})); // Missing description and date
        assertFalse(Task.isValidTask("D",
                new String[]{"Submit report"})); // Missing date
        assertFalse(Task.isValidTask("D",
                new String[]{"Submit report", "invalid date"})); // Invalid date
    }

    @Test
    public void isValidTask_validEventFormat_returnTrue() {
        assertTrue(Task.isValidTask("E",
                new String[]{"Conference", "2025-12-31", "2025-12-31 1800"}));
    }

    @Test
    public void isValidTask_invalidEventFormat_returnFalse() {
        assertFalse(Task.isValidTask("E",
                new String[]{""})); // Missing description and two dates
        assertFalse(Task.isValidTask("E",
                new String[]{"Conference", "2025-12-31"})); // Missing one date
        assertFalse(Task.isValidTask("E",
                new String[]{"Conference", "invalid date", "2025-12-31"})); // Invalid date
    }
}
