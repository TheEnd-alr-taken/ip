package motiva.task;

public class Todo extends Task {
    
    public Todo(String description) {
        super(description);
    }

    public String toFileString() {
        return String.format("T | %s", 
                super.toFileString());
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
