package motiva.task;

/**
 * Represents a simple to-do task with just a description.
 */
public class Todo extends Task {

    /**
     * Constructs a ToDo task with the specified description.
     *
     * @param description The description of the to-do task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Converts the to-do task into a formatted string suitable for file storage.
     *
     * @return The formatted string representation of the to-do task.
     */
    public String toFileString() {
        return String.format("T | %s",
                super.toFileString());
    }

    /**
     * Returns a string representation of the to-do task.
     *
     * @return The string representation of the to-do task.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
