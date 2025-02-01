public class Task {

    protected String description;
    protected boolean isDone;

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

    public static boolean isValidTask(String taskType, String[] parts) {
        switch (taskType) {
            case "T":
                return parts.length == 1 && !parts[0].trim().isEmpty();
            case "D":
                return parts.length == 2 && !parts[0].trim().isEmpty() && !parts[1].trim().isEmpty();
            case "E":
                return parts.length == 3 && !parts[0].trim().isEmpty()
                        && !parts[1].trim().isEmpty() && !parts[2].trim().isEmpty();
            default:
                return false;
        }
    }

    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }
}
