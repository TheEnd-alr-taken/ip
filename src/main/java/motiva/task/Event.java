package motiva.task;

import java.time.LocalDateTime;

/**
 * Represents an event task with a specific start and end date/time.
 */
public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructs an Event task with the specified description, start date/time, and end date/time.
     *
     * @param description The description of the event.
     * @param from The start date and time of the event.
     * @param to The end date and time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = super.parseDateTime(from);
        this.to = super.parseDateTime(to);
    }

    /**
     * Converts the event task into a formatted string suitable for file storage.
     *
     * @return The formatted string representation of the event task.
     */
    public String toFileString() {
        return String.format("E | %s | %s | %s",
                super.toFileString(),
                this.from.format(Task.DATE_TIME_FORMAT),
                this.to.format(Task.DATE_TIME_FORMAT));
    }

    /**
     * Returns a string representation of the event task.
     *
     * @return The string representation of the event task.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from.format(Task.DISPLAY_FORMAT)
                + " to: " + this.to.format(Task.DISPLAY_FORMAT) + ")";
    }
}
