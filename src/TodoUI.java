import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;



public class TodoUI extends JFrame {
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;
    private static final int INPUT_FIELD_SIZE = 20;
    private static final String WINDOW_TITLE = "Modular To-Do List";
    private static final String TASK_LABEL = "Task:";
    private static final String AddButtonText = "Add";
    private static final String RemoveButtonText = "Remove";



    private JPanel mainPanel;
    private JTextField taskInputField;
    private JButton addTaskButton;
    private JButton removeTaskButton;
    private JList<CompletedBox> taskList;
    private DefaultListModel<CompletedBox> listModel;
    private TaskManager taskManager;

    public TodoUI() {
        setTitle(WINDOW_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        initComponents();
        setupListener();
        setupLayout();

        setVisible(true);
    }

    private void initComponents() {

        //UI styling
        Font appFont = new Font("Segoe UI", Font.PLAIN, 14);

        mainPanel = new JPanel(new BorderLayout());
        taskManager = new TaskManager();
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setCellRenderer(new TaskCellRenderer());
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = taskList.locationToIndex(e.getPoint());
                if (index != -1) {
                    CompletedBox task = listModel.get(index);
                    task.toggleCompleted();
                    listModel.set(index, task);
                    taskList.repaint();
                }
            }
        });

        taskInputField = new JTextField(INPUT_FIELD_SIZE);

        addTaskButton = new roundedButton(AddButtonText,20);
        addTaskButton.setBackground(new Color(100,149,237));

        removeTaskButton = new roundedButton(RemoveButtonText,20);
        removeTaskButton.setBackground(new Color(232,66,69));

        taskList.setFont(appFont);
        taskInputField.setBackground(new Color(245,245,245));

        addTaskButton.setFont(appFont);
        addTaskButton.setForeground(Color.WHITE);
        addTaskButton.setFocusPainted(false);

        removeTaskButton.setFont(appFont);
        removeTaskButton.setForeground(Color.WHITE);
        removeTaskButton.setFocusPainted(false);

        taskList.setBackground(Color.WHITE);
        taskList.setFont(appFont);
    }


//    completely unnecessary for some reason after i fixed strikethrough
//    private void handleTaskCompletion(MouseEvent e) {
//        int index = taskList.locationToIndex(e.getPoint());
//        if (index != -1) {
//            CompletedBox task = taskManager.getTask(index);
//            if (task != null) {
//                task.setCompleted();
//                taskList.repaint();
//            }
//        }
//    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel(TASK_LABEL));
        inputPanel.add(taskInputField);
        inputPanel.add(addTaskButton);
        inputPanel.add(removeTaskButton);
        return inputPanel;
    }

    private void setupLayout() {
        JPanel inputPanel = createInputPanel();
        JScrollPane scrollPane = new JScrollPane(taskList);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void setupListener() {
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
        ArrayList<CompletedBox> tasks = taskManager.getTasks();
        for (CompletedBox task : tasks) {
            listModel.addElement(task);
        }
    }

    private static class TaskCellRenderer extends JCheckBox implements ListCellRenderer<CompletedBox> {

        @Override
        public Component getListCellRendererComponent(JList<? extends CompletedBox> list,
                                                      CompletedBox task, int index, boolean isSelected, boolean cellHasFocus) {
            //use HTML for strikethrough
            setText(formatTaskText(task));
            setSelected(task.isCompleted());
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));

            //color
            configureAppearance(list, isSelected);
            return this;
        }

        private String formatTaskText(CompletedBox task) {
            //small HTML escape safety measure
            String text = escapeHTML(task.getDescription());

            return task.isCompleted()
                    ? "<html><strike>" + text + "</strike></html>"
                    : text;
        }

        private void configureAppearance(JList<?> list, boolean isSelected) {
            setBackground(isSelected ? list.getSelectionBackground() : Color.WHITE);
            setForeground(isSelected ? list.getSelectionForeground() : Color.BLACK);
        }

        private String escapeHTML(String text) { //safety escape
            return text.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");
        }
    }
}