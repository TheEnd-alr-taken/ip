public class Deadline extends Task {
    
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public String toFileString() {
        return String.format("D | %s | %s", 
                super.toFileString(), 
                this.by);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
