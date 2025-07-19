import java.util.ArrayList;

public class TaskManager {
    private final ArrayList<CompletedBox> tasks;

    public TaskManager(){
        tasks = new ArrayList<>();
        System.out.println("Task manager created");
    }

    public void addTask(String title, String description){
        tasks.add(new CompletedBox(title, description));
        System.out.println("Task added");
    }

    public void removeTask(int index){
        if (index >= 0 && index < tasks.size()){
            tasks.remove(index);
            System.out.println("Task removed");
        }
    }

    public ArrayList<CompletedBox> getTasks() {
            return tasks;
    }
}

