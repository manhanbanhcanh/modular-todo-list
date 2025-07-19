import java.util.ArrayList;

public class TaskManager {
    private final ArrayList<CompletedBox> tasks;

    public TaskManager(){
        tasks = new ArrayList<>();
    }

    public void addTask(String taskText){
        tasks.add(new CompletedBox(taskText));
    }

    public void removeTask(int index){
        if (index >= 0 && index < tasks.size()){
            tasks.remove(index);
        }
    }

    public CompletedBox getTask(int index){
        return (index >= 0 && index < tasks.size()) ? tasks.get(index) : null;
    }

    public ArrayList<CompletedBox> getTasks() {
            return tasks;
    }
}

