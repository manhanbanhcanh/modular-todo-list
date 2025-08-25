public class CompletedBox {

    private final String title;
    private final String description;
    private boolean completed;
    private TaskPriority priority;

    public CompletedBox(String tilte, String description) {
        this.title = tilte;
        this.description = description;
        this.completed = false;
        this.priority = TaskPriority.MEDIUM;
        System.out.println("Complete box created");
    }

    //something about overload constructor
    public CompletedBox(String title, String description, TaskPriority priority) {
        this.title = title;
        this.description = description;
        this.completed = false;
        this.priority = priority != null ? priority : TaskPriority.MEDIUM;
    }

    //getters
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public boolean isCompleted() {return completed;}
    public TaskPriority getPriority() {return priority;}

    //setters
    public void toggleCompleted(){this.completed = !this.completed;}
    public void setPriority(TaskPriority priority) {this.priority = priority;}

    //tf is this?
    @Override
    public String toString() {
        return description;
    }
}