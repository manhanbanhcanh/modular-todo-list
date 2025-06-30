import javax.swing.*;
import java.awt.*;

import static java.awt.AWTEventMulticaster.add;

public class tasklistpanel extends JPanel {
    private DefaultListModel<task> listModel;
    private JList<task> taskList;

    public tasklistpanel() {
        setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addTask(task task) {
        listModel.addElement(task);
    }

    public void removeSelectedTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            listModel.remove(selectedIndex);
        }
    }

    public int getSelectedIndex() {
        return taskList.getSelectedIndex();
    }

    public void refresh(taskmanager manager) {
        listModel.clear();
        for (task task : manager.getTasks()) {
            listModel.addElement(task);
        }
    }
}
