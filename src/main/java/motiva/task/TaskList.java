package motiva.task;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents a list of tasks in the Motiva application.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Sorts tasks by deadlines chronologically (earliest first).
     * - Deadline tasks are sorted by their `by` field.
     * - Event tasks are sorted by their `from` field.
     * - Todo tasks are placed at the end.
     */
    public void sort() {
        tasks.sort(Comparator.comparing((Task task) -> {
            if (task instanceof Deadline) {
                return ((Deadline) task).by;
            } else if (task instanceof Event) {
                return ((Event) task).from;
            }
            return null;
        }, Comparator.nullsLast(Comparator.naturalOrder())));
    }

    /**
     * Returns the list of tasks.
     *
     * @return The list of tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task from the list by index.
     *
     * @param index The index of the task to remove.
     */
    public void remove(int index) {
        tasks.remove(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Retrieves a task by index.
     *
     * @param index The index of the task to retrieve.
     * @return The task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Checks if the task list is empty.
     *
     * @return True if the task list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

}
