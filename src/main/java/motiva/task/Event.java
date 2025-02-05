package motiva.task;

import java.time.LocalDateTime;

public class Event extends Task {
    
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = super.parseDateTime(from);
        this.to = super.parseDateTime(to);
    }

    public String toFileString() {
        return String.format("E | %s | %s | %s", 
                super.toFileString(), 
                this.from.format(Task.DATE_TIME_FORMAT),
                this.to.format(Task.DATE_TIME_FORMAT));
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from.format(Task.DISPLAY_FORMAT)
                + " to: " + this.to.format(Task.DISPLAY_FORMAT) + ")";
    }
}
