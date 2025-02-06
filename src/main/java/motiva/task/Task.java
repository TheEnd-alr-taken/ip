package motiva.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a general task with a description and completion status.
 */
public class Task {

    protected String description;
    protected boolean isDone;

    protected static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    protected static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    protected static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");

    /**
     * Constructs an incomplete Task with the specified description.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Toggles the completion status of the task.
     */
    public void toggleDone() {
        this.isDone = !this.isDone;
    }

    /**
     * Returns the completion status of the task.
     *
     * @return True if the task is completed, false otherwise.
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Returns the status icon for the task.
     *
     * @return "X" if done, empty space otherwise.
     */
    public String getStatusIcon() {
        return (isDone) ? "X" : " ";
    }

    /**
     * Converts the task into a formatted string suitable for file storage.
     *
     * @return The formatted string representation of the task.
     */
    public String toFileString() {
        return String.format("%s | %s",
                getStatusIcon(),
                this.description);
    }

    /**
     * Checks if a given date string is valid.
     * Date string given can either be in "yyyy-MM-dd HHmm" or "yyyy-MM-dd" format.
     *
     * @param dateTime The date string to validate.
     * @return True if the date string is valid, false otherwise.
     */
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

    /**
     * Validates if a task with a given type and parameters is correctly formatted.
     *
     * @param taskType The type of the task (T, D, or E).
     * @param parts The parameters of the task.
     * @return True if the task is valid, false otherwise.
     */
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

    /**
     * Parses a date-time string into a LocalDateTime object.
     * If only date is given, additionally set the time to 2359.
     *
     * @param dateTime The date-time string to parse.
     * @return A LocalDateTime object representing the given date-time.
     */
    public LocalDateTime parseDateTime(String dateTime) {
        if (dateTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
            return LocalDateTime.parse(dateTime, DATE_TIME_FORMAT);
        } else {
            LocalDate date = LocalDate.parse(dateTime, DATE_FORMAT);
            return date.atTime(23, 59);
        }
    }

    /**
     * Returns a string representation of the task.
     *
     * @return The string representation of the task.
     */
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }
}
