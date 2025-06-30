import java.util.ArrayList;

public class taskmanager {
    private ArrayList<task> tasks;

    public taskmanager() {
        tasks = new ArrayList<>();
    }

    public void addTask(task task) {
        tasks.add(task);
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        }
    }

    public ArrayList<task> getTasks() {
        return tasks;
    }
}
