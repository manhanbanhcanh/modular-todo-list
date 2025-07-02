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
    private static final String ADD_BUTTON_TEXT = "Add";
    private static final String REMOVE_BUTTON_TEXT = "Remove";

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
        mainPanel = new JPanel(new BorderLayout());
        taskManager = new TaskManager();
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setCellRenderer(new TaskCellRenderer());
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTaskCompletion(e);
            }
        });

        taskInputField = new JTextField(INPUT_FIELD_SIZE);
        addTaskButton = new JButton(ADD_BUTTON_TEXT);
        removeTaskButton = new JButton(REMOVE_BUTTON_TEXT);
    }

    private void handleTaskCompletion(MouseEvent e) {
        int index = taskList.locationToIndex(e.getPoint());
        if (index != -1) {
            CompletedBox task = taskManager.getTask(index);
            if (task != null) {
                task.setCompleted();
                taskList.repaint();
            }
        }
    }

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
            String displayText = formatTaskText(task);
            setText(displayText);
            setSelected(task.isCompleted());
            configureAppearance(list, isSelected);
            return this;
        }

        private String formatTaskText(CompletedBox task) {
            if (task.isCompleted()) {
                return String.format("<html><strike>" + task.getDescription() + "</strike></html>");
            } else {
                return task.getDescription();
            }
        }

        private void configureAppearance(JList<?> list, boolean isSelected) {
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
        }
    }
}