public class CompletedBox {

    private final String title;
    private final String description;

    private boolean completed;

    public CompletedBox(String tilte, String description) {
        this.title = tilte;
        this.description = description;
        this.completed = false;
        System.out.println("Complete box created");
    }

    public String getTitle() {return title;}
    public String getDescription() {return description;}

    public boolean isCompleted() {
        return completed;
    }

    public void toggleCompleted(){
        this.completed = !this.completed;
    }

    @Override
    public String toString() {
        return description;
    }
}