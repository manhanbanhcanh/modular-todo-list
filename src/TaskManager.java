import java.util.ArrayList;

public class TaskManager {
    private ArrayList<String> tasks;

    public TaskManager(){
        tasks = new ArrayList<>();
    }

    public void addTask(String task){
        tasks.add(task);
    }

    public void removeTask(int index){
        if (index >= 0 && index < tasks.size()){
            tasks.remove(index);
        }
    }

    public ArrayList<String> getTasks(){
        return tasks;
    }
}
