import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TodoUI extends JFrame{
    private JPanel mainPanel;
    private JTextField taskInputField;
    private JButton addTaskButton;
    private JButton removeTaskButton;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;

    private TaskManager taskManager;

    public TodoUI(){
        setTitle("modular to-do list");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280,720);
        setLocationRelativeTo(null); //centered window

        initComponents();
        setupListenter();
        setupLayout();

        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());

        taskManager = new TaskManager();
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);

        taskInputField = new JTextField(20);
        addTaskButton = new JButton("Add");
        removeTaskButton = new JButton("Remove");
    }

    private void setupLayout() {
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskInputField);
        inputPanel.add(addTaskButton);
        inputPanel.add(removeTaskButton);

        JScrollPane scrollPane = new JScrollPane(taskList);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void setupListenter() {
        addTaskButton.addActionListener(e -> {
            String taskText = taskInputField.getText().trim();
            if (!taskText.isEmpty()) {
                taskManager.addTask(taskText);
                refreshTaskList();
                taskInputField.setText("");
            }
        });

        removeTaskButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                taskManager.removeTask(selectedIndex);
                refreshTaskList();
            }
        });
    }

    private void refreshTaskList() {
        listModel.clear();
        ArrayList<String> tasks = taskManager.getTasks();
        for (String task : tasks) {
            listModel.addElement(task);
        }
    }
}

