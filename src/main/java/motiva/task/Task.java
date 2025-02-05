package motiva.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Task {

    protected String description;
    protected boolean isDone;

    protected static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    protected static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    protected static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void toggleDone() {
        this.isDone = !this.isDone;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public String getStatusIcon() {
        return (isDone) ? "X" : " ";
    }

    public String toFileString() {
        return String.format("%s | %s", 
                getStatusIcon(),
                this.description);
    }

    public static boolean isValidDate(String dateTime) {
        try {
            DATE_TIME_FORMAT.parse(dateTime);
            return true;
        } catch (DateTimeParseException e1) {
            try {
                DATE_FORMAT.parse(dateTime);
                return true;
            } catch (DateTimeParseException e2) {
                return false;
            }
        }
    }

    public static boolean isValidTask(String taskType, String[] parts) {
        switch (taskType) {
            case "T":
                return parts.length == 1 && !parts[0].trim().isEmpty();
            case "D":
                return parts.length == 2 && !parts[0].trim().isEmpty() && isValidDate(parts[1].trim());
            case "E":
                return parts.length == 3 && !parts[0].trim().isEmpty()
                        && isValidDate(parts[1].trim()) && isValidDate(parts[2].trim());
            default:
                return false;
        }
    }

    public LocalDateTime parseDateTime(String dateTime) {
            if (dateTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                return LocalDateTime.parse(dateTime, DATE_TIME_FORMAT);
            } else {
                LocalDate date = LocalDate.parse(dateTime, DATE_FORMAT);
                return date.atTime(23, 59);
            }  
    }

    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }
}
