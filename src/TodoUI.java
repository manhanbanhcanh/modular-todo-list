import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;



public class TodoUI extends JFrame {
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;
    private static final String WINDOW_TITLE = "Modular To-Do List";
    private static final String TASK_LABEL = "Task:";
    private static final String AddButtonText = "Add";
    private static final String RemoveButtonText = "Remove";



    private JPanel mainPanel;
    private JButton addTaskButton;
    private JButton removeTaskButton;
    private JList<CompletedBox> taskList;
    private JLabel detailTitleLabel;
    private JTextArea detailDescriptionArea;
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

        //left side table
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
                    Rectangle bounds = taskList.getCellBounds(index, index);

                    int checkBoxX = bounds.x +5;
                    int checkBoxY = bounds.y + 5;
                    int checkBoxWidth = 20;
                    int checkBoxHeight = 20;

                    Rectangle checkBoxArea = new Rectangle(checkBoxX, checkBoxY, checkBoxWidth, checkBoxHeight);

                    if (checkBoxArea.contains(e.getPoint())) {
                        CompletedBox task = listModel.getElementAt(index);
                        task.toggleCompleted();
                        taskList.repaint(checkBoxArea);
                    }

                }
            }
        });
        // hover effect
        taskList.addMouseMotionListener(new MouseMotionAdapter() {
           @Override
           public void mouseMoved(MouseEvent e) {
               int index = taskList.locationToIndex(e.getPoint());
               if (index != -1) {
                   Rectangle bounds = taskList.getCellBounds(index, index);
                   int checkBoxX = bounds.x + 5;
                   int checkBoxY = bounds.y + 5;
                   int checkBoxWidth = 20;
                   int checkBoxHeight = 20;

                   Rectangle checkboxArea = new Rectangle(checkBoxX, checkBoxY, checkBoxWidth, checkBoxHeight);

                   if (checkboxArea.contains(e.getPoint())) {
                       taskList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                   } else {
                       taskList.setCursor(Cursor.getDefaultCursor());
                   }

               }
            }
        });

        addTaskButton = new roundedButton(AddButtonText,20);
        addTaskButton.setBackground(new Color(100,149,237));

        removeTaskButton = new roundedButton(RemoveButtonText,20);
        removeTaskButton.setBackground(new Color(232,66,69));

        addTaskButton.setFont(appFont);
        addTaskButton.setForeground(Color.WHITE);
        addTaskButton.setFocusPainted(false);

        removeTaskButton.setFont(appFont);
        removeTaskButton.setForeground(Color.WHITE);
        removeTaskButton.setFocusPainted(false);

        taskList.setBackground(Color.WHITE);
        taskList.setFont(appFont);

        //right side table
        detailTitleLabel = new JLabel("Select a task to view in details");
        detailTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        detailDescriptionArea = new JTextArea();
        detailDescriptionArea.setEditable(false);
        detailDescriptionArea.setLineWrap(true);
        detailDescriptionArea.setWrapStyleWord(true);
        detailDescriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

//        JPanel detailPanel = new JPanel(new BorderLayout(10,10));
//        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        detailPanel.add(detailTitleLabel, BorderLayout.NORTH);
//        detailPanel.add(new JScrollPane(detailDescriptionArea), BorderLayout.CENTER);
    }

    //construct detail panel
    private JPanel createDetailPanel() {
        JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailPanel.add(detailTitleLabel, BorderLayout.NORTH);
        detailPanel.add(new JScrollPane(detailDescriptionArea), BorderLayout.CENTER);
        return detailPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel(TASK_LABEL));
        inputPanel.add(addTaskButton);
        inputPanel.add(removeTaskButton);
        return inputPanel;
    }

    private void setupLayout() {
        JPanel inputPanel = createInputPanel();
        JScrollPane scrollPane = new JScrollPane(taskList);

        //left side wrap
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        //split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, createDetailPanel());
        splitPane.setResizeWeight(0.4);
        splitPane.setDividerSize(5);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void setupListener() {
        addTaskButton.addActionListener(e -> openTaskDialog());

        removeTaskButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                taskManager.removeTask(selectedIndex);
                refreshTaskList();
            }
        });

        taskList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                CompletedBox selectedTask = taskList.getSelectedValue();
                if (selectedTask != null) {
                    detailTitleLabel.setText(selectedTask.getTitle());
                    detailDescriptionArea.setText(selectedTask.getDescription());
                } else {
                    detailTitleLabel.setText("Select a task");
                    detailDescriptionArea.setText("");
                }
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

    private void openTaskDialog() {
        JDialog dialog = new JDialog(this, "Add new task", true);
        dialog.setSize(300, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        //title and description field
        JTextField titleField = new JTextField();
        JTextArea descriptionField = new JTextArea();
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);

        JPanel inputPanel = new JPanel(new GridLayout(4, 1,5,5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);

        System.out.println("Dialog opened and loaded");

        //submit button
        JButton submitButton = new roundedButton("Submit", 20);
        submitButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();
            if (!title.isEmpty()) {
                taskManager.addTask(title, description);
                refreshTaskList();
                dialog.dispose();
            }
        });


        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(submitButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
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
            String text = escapeHTML(task.getTitle());

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