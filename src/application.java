import javax.swing.*;
import java.awt.*;

public class application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("To-Do List");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(450, 400);
            frame.setLayout(new BorderLayout());
            frame.setLocationRelativeTo(null);

            taskmanager taskManager = new taskmanager();
            tasklistpanel taskListPanel = new tasklistpanel();

            //input pannel
            JPanel inputPanel = new JPanel();
            JTextField taskInput = new JTextField(20);
            JButton addButton = new JButton("Add Task");
            JButton removeButton = new JButton("Remove Task");

            inputPanel.add(taskInput);
            inputPanel.add(addButton);
            inputPanel.add(removeButton);

            //events listener
            addButton.addActionListener(e -> {
                String text = taskInput.getText().trim();
                if (!text.isEmpty()) {
                    task newTask = new task(text);
                    taskManager.addTask(newTask);
                    taskListPanel.refresh(taskManager);
                    taskInput.setText("");
                }
            });

            removeButton.addActionListener(e -> {
                int selectedIndex = taskListPanel.getSelectedIndex();
                if (selectedIndex != -1) {
                    taskManager.removeTask(selectedIndex);
                    taskListPanel.refresh(taskManager);
                }
            });

            frame.add(inputPanel, BorderLayout.NORTH);
            frame.add(taskListPanel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}