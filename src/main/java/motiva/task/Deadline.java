package motiva.task;

import java.time.LocalDateTime;

public class Deadline extends Task {

    protected LocalDateTime by;

    public Deadline(String description, String by) {
        super(description);
        this.by = super.parseDateTime(by);
    }

    public String toFileString() {
        return String.format("D | %s | %s",
                super.toFileString(),
                this.by.format(Task.DATE_TIME_FORMAT));
    }

    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + this.by.format(Task.DISPLAY_FORMAT) + ")";
    }
}
