package motiva.task;

import java.time.LocalDateTime;

/**
 * Represents a deadline task with a specific due date and time.
 */
public class Deadline extends Task {

    protected LocalDateTime by;

    /**
     * Constructs a Deadline task with the specified description and due date.
     *
     * @param description The description of the task.
     * @param by The due date and time of the task.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = super.parseDateTime(by);
    }

    /**
     * Converts the deadline task into a formatted string suitable for file storage.
     *
     * @return The formatted string representation of the deadline task.
     */
    public String toFileString() {
        return String.format("D | %s | %s",
                super.toFileString(),
                this.by.format(Task.DATE_TIME_FORMAT));
    }

    /**
     * Returns a string representation of the deadline task
     *
     * @return The string representation of the deadline task.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + this.by.format(Task.DISPLAY_FORMAT) + ")";
    }
}
