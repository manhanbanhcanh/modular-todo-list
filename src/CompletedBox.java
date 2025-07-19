public class CompletedBox {
    private final String description;
    private boolean completed;

    public CompletedBox(String description) {
        this.description = description;
        this.completed = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted() {
        completed = !completed;
    }

    public void toggleCompleted(){
        this.completed = !this.completed;
    }

    @Override
    public String toString() {
        return description;
    }
}