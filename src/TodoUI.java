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
    private JLabel detailPriorityLabel;
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
        System.out.println("UI created");
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
                if (index != -1 && getCheckBoxArea(index).contains(e.getPoint())) {
                        CompletedBox task = listModel.getElementAt(index);
                        task.toggleCompleted();
                        taskList.repaint(getCheckBoxArea(index));
                }
            }
        });
        // hover effect
        taskList.addMouseMotionListener(new MouseMotionAdapter() {
           @Override
           public void mouseMoved(MouseEvent e) {
               int index = taskList.locationToIndex(e.getPoint());
               if (index != -1) {
                   Cursor cursor = getCheckBoxArea(index).contains(e.getPoint()) ?
                           Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) :
                           Cursor.getDefaultCursor();
                   taskList.setCursor(cursor);
               } else {
                    taskList.setCursor(Cursor.getDefaultCursor());
               }
            }
        });

        addTaskButton = new JButton(AddButtonText);
        addTaskButton.setBackground(new Color(0, 196, 255));

        removeTaskButton = new JButton(RemoveButtonText);
        removeTaskButton.setBackground(new Color(232,66,69));

        taskList.setBackground(Color.WHITE);
        taskList.setFont(appFont);

        //right side table
        detailTitleLabel = new JLabel("");
        detailTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        detailPriorityLabel = new JLabel("Priority: -");
        detailPriorityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        detailDescriptionArea = new JTextArea();
        detailDescriptionArea.setEditable(false);
        detailDescriptionArea.setLineWrap(true);
        detailDescriptionArea.setWrapStyleWord(true);
        detailDescriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        System.out.println("Components initialized");
    }

    //getting check box area
    private Rectangle getCheckBoxArea(int index) {
        Rectangle bounds = taskList.getCellBounds(index, index);
        int padding = 5;
        int size = 20;
        return new Rectangle(bounds.x + padding, bounds.y + padding, size, size);
    }

    //construct detail panel
    private JPanel createDetailPanel() {
        JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(detailTitleLabel);
        header.add(Box.createVerticalStrut(6));
        header.add(detailPriorityLabel);

        detailPanel.add(header, BorderLayout.NORTH);
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

        System.out.println("Layout setup");
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
                    detailTitleLabel.setText("<html>" + markdownToHtml(escapeHTML(selectedTask.getTitle())) + "</html>");
                    detailDescriptionArea.setText(markdownToPlainText(selectedTask.getDescription()));

                    detailPriorityLabel.setText("Priority: " + selectedTask.getPriority());
                    detailPriorityLabel.setForeground(
                            selectedTask.isCompleted() ? Color.GRAY : selectedTask.getPriority().getColor()
                    );

                } else {
                    detailTitleLabel.setText("");
                    detailDescriptionArea.setText("Select a task to view in details");
                    detailPriorityLabel.setText("Priority: -");
                    detailPriorityLabel.setForeground(Color.BLACK);
                }
            }
        });

        System.out.println("Listeners setup");
    }

    private String markdownToHtml(String text) {
        text = text.replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>"); // bold
        text = text.replaceAll("\\*(.*?)\\*", "<i>$1</i>");       // italic
        return text;
    }

    private String markdownToPlainText(String text) {
        return text.replaceAll("\\*\\*(.*?)\\*\\*", "$1")
                .replaceAll("\\*(.*?)\\*", "$1"); // remove markdown for text area
    }

    private String escapeHTML(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private void refreshTaskList() {
        listModel.clear();
        ArrayList<CompletedBox> tasks = taskManager.getTasks();
        for (CompletedBox task : tasks) {
            listModel.addElement(task);
        }

        System.out.println("Task list refreshed");
    }

    private void openTaskDialog() {
            JDialog dialog = new JDialog(this, "Add new task", true);
            dialog.setSize(350, 300);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTHWEST;

            // title part
            JLabel titleLabel = new JLabel("Title:");
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0;
            dialog.add(titleLabel, gbc);

            JTextField titleField = new JTextField();
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1;
            dialog.add(titleField, gbc);

            // description part
            JLabel descLabel = new JLabel("Description:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0;
            dialog.add(descLabel, gbc);

            JTextArea descriptionArea = new JTextArea(5, 20);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(descriptionArea);
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.fill = GridBagConstraints.BOTH;
            dialog.add(scrollPane, gbc);

            //prio part
            JLabel prioLabel = new JLabel("Priority:");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            dialog.add(prioLabel, gbc);

            JComboBox<TaskPriority> priorityComboBox = new JComboBox<>(TaskPriority.values());
            priorityComboBox.setSelectedItem(TaskPriority.MEDIUM);
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.weightx = 1;
            dialog.add(priorityComboBox, gbc);;

            // submit button
            JButton submitButton = new JButton("Submit");
            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            dialog.add(submitButton, gbc);

            // action listener
            submitButton.addActionListener(e -> {
                String title = titleField.getText().trim();
                String description = descriptionArea.getText().trim();
                TaskPriority priority = (TaskPriority) priorityComboBox.getSelectedItem();

                if (!title.isEmpty()) {
                    CompletedBox newTask = new CompletedBox(title, description, priority);
                    listModel.addElement(newTask);
                    dialog.dispose();
                }
            });

            submitButton.setBackground(new Color(100, 237, 107)); // Cornflower Blue
            submitButton.setForeground(Color.BLACK);

            dialog.setVisible(true);

            System.out.println("Task dialog opened");
        }


    private static class TaskCellRenderer extends JCheckBox implements ListCellRenderer<CompletedBox> {

        @Override
        public Component getListCellRendererComponent(JList<? extends CompletedBox> list,
                                                      CompletedBox task, int index, boolean isSelected, boolean cellHasFocus) {

            //markdown text to html
            String htmlText = markdownToHtml(escapeHTML(task.getTitle()));
            setText("<html>" + htmlText + "</html>");

            //appearance
            setSelected(task.isCompleted());
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));

            configureAppearance(task, isSelected);
            return this;
        }

        private void configureAppearance(CompletedBox task, boolean isSelected) {
            if (isSelected) {
                setBackground(new Color(0xCCE5FF)); // light blue background for selected
            } else {
                setBackground(Color.WHITE);
            }

            if(task.isCompleted()){
                setForeground(Color.GRAY);
            } else {
                setForeground(task.getPriority().getColor());
            }
        }


        private String markdownToHtml(String text) {
            text = text.replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>"); // bold
            text = text.replaceAll("\\*(.*?)\\*", "<i>$1</i>");       // italic
            return text;
        }

        private String escapeHTML(String text) {
            return text.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");
        }
    }
}