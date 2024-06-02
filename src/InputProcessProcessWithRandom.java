//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.JTableHeader;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class InputProcessProcessWithRandom extends JFrame implements Sorter {
//    private static final int WIDTH = 1000;
//    private static final int HEIGHT = 900;
//    private static final int MAX_INPUTS = 15;
//
//    private DefaultTableModel model;
//    private JTextField priorityField;
//    private JTextField burstTimeField;
//    private JTextField arrivalTimeField;
//    private JTextField timeQuantumField;
//    private JTextField priorityField1;
//    private int inputCount = 0;
//    private boolean timeQuantumLocked = false;
//    private String firstTimeQuantum = "";
//    ArrayList<Process> processes;
//
//
//    private Map<String, String> priorityToProcessorMap;
//
//    public InputProcessProcessWithRandom() {
//        initializeProcessorMapping();
//        processes = new ArrayList<>();
//        JButton continueButton = new JButton("Continue");
//
//
//        setSize(WIDTH, HEIGHT);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setResizable(false);
//        getContentPane().setBackground(new Color(248, 233, 227));
//
//        // Initialize table model with the new column "Process ID" and "Priority"
//        String[] columnNames = {"Process ID", "Priority", "Priority Level", "Burst Time", "Arrival Time", "Time Quantum"};
//        model = new DefaultTableModel(new Object[0][6], columnNames);
//
//        // Main panel
//        JPanel mainPanel = new JPanel(new BorderLayout());
//
//        // User input panel
//        JPanel userInput = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.anchor = GridBagConstraints.WEST;
//        gbc.weightx = 1.0;
//
//        // Add input fields
//        userInput.add(new JLabel("Priority Level [1. Real Time 2. System 3. Interactive 4. Batch]"), gbc);
//        gbc.gridy++;
//        priorityField = new JTextField(10);
//        userInput.add(priorityField, gbc);
//
//        // Add a button to generate a random process
//        JButton generateButton = new JButton("Generate Random Process");
//        gbc.gridy++;
//        userInput.add(generateButton, gbc);
//
//        final boolean[] generateQuantum = {true};
//
//        // Action listener for the generate button
//        generateButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Generate a random priority level between 1 and 4
//                for (int i = 0; i <= 15; i++) {
//                    int randomPriority = (int) (Math.random() * 4) + 1;
//                    priorityField.setText(String.valueOf(randomPriority));
//
//                    // Generate random values for burst time and arrival time
//                    int randomBurstTime = (int) (Math.random() * 20) + 1; // Example range: 1 to 20
//                    int randomArrivalTime = (int) (Math.random() * 10); // Example range: 0 to 9
//                    burstTimeField.setText(String.valueOf(randomBurstTime));
//                    arrivalTimeField.setText(String.valueOf(randomArrivalTime));
//
//                    // Generate random time quantum for priority level 1
//                    if (randomPriority == 1 && generateQuantum[0]) {
//                        generateQuantum[0] = false;
//                        int randomTimeQuantum = (int) (Math.random() * 10) + 1; // Example range: 1 to 10
//                        timeQuantumField.setText(String.valueOf(randomTimeQuantum));
//                    }
//
//                    // Generate random priority for priority level 4
//                    if (randomPriority == 4) {
//                        int randomProcessPriority = (int) (Math.random() * 10) + 1; // Example range: 1 to 10
//                        priorityField1.setText(String.valueOf(randomProcessPriority));
//                    }
//                    addToTable();
//                    continueButton.setEnabled(true);
//                }
//            }
//        });
//
//
//        gbc.gridy++;
//        userInput.add(new JLabel("Burst Time:"), gbc);
//        gbc.gridy++;
//        burstTimeField = new JTextField(10);
//        userInput.add(burstTimeField, gbc);
//
//        gbc.gridy++;
//        userInput.add(new JLabel("Arrival Time:"), gbc);
//        gbc.gridy++;
//        arrivalTimeField = new JTextField(10);
//        userInput.add(arrivalTimeField, gbc);
//
//        gbc.gridy++;
//        userInput.add(new JLabel("Time Quantum:"), gbc);
//        gbc.gridy++;
//        timeQuantumField = new JTextField(10);
//        userInput.add(timeQuantumField, gbc);
//
//        gbc.gridy++;
//        userInput.add(new JLabel("Priority:"), gbc);
//        gbc.gridy++;
//        priorityField1 = new JTextField(10);
//        userInput.add(priorityField1, gbc);
//
//        JButton addButton = new JButton("Add");
//        gbc.gridy++;
//        userInput.add(addButton, gbc);
//
//        continueButton.setEnabled(false); // Initially disabled
//        gbc.gridy++;
//        userInput.add(continueButton, gbc);
//
//
//        mainPanel.add(userInput, BorderLayout.WEST);
//
//        // Table panel
//        JPanel tablePanel = new JPanel(new BorderLayout());
//        tablePanel.setBackground(new Color(50, 50, 50));
//
//        JTable table = new JTable(model);
//        table.setGridColor(Color.WHITE);
//        table.setForeground(Color.WHITE);
//        table.setBackground(new Color(70, 70, 70));
//
//        JTableHeader header = table.getTableHeader();
//        header.setForeground(Color.WHITE);
//        header.setBackground(new Color(30, 30, 30));
//
//        JScrollPane scrollPane = new JScrollPane(table);
//        tablePanel.add(scrollPane, BorderLayout.CENTER);
//
//        mainPanel.add(tablePanel, BorderLayout.CENTER);
//
//        // Right panel for additional content
//        JPanel additionalContentPanel = new JPanel();
//        additionalContentPanel.setLayout(new BoxLayout(additionalContentPanel, BoxLayout.Y_AXIS));
//        additionalContentPanel.setBackground(new Color(240, 233, 230));
//
//        // Title
//        JLabel titleLabel = new JLabel("Welcome to the CPU Simulator");
//        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        additionalContentPanel.add(titleLabel);
//
//        // Learning Objectives
//        JTextArea objectivesArea = new JTextArea(5, 30);
//        objectivesArea.setText("Learning Objectives:\n"
//                + "- Understand different CPU scheduling algorithms.\n"
//                + "- Visualize the execution of processes based on different scheduling rules.\n"
//                + "- Analyze the performance of each scheduling algorithm.\n"
//                + "- Gain hands-on experience in configuring and running CPU simulations.");
//        objectivesArea.setFont(new Font("Serif", Font.PLAIN, 16));
//        objectivesArea.setEditable(false);
//        objectivesArea.setBackground(new Color(240, 233, 230));
//        additionalContentPanel.add(objectivesArea);
//
//// CPU Scheduling Rules
//        JTextArea rulesArea = new JTextArea(5, 30);
//        rulesArea.setText("CPU Scheduling Rules:\n"
//                + "- Shortest Remaining Time First: SRTF scheduling selects the process with the shortest\n remaining execution time for execution.\n"
//                + "- Shortest Job Next (SJN): Processes with the shortest burst time are scheduled first.\n"
//                + "- Priority Scheduling: Processes are scheduled based on priority levels.\n"
//                + "- Round Robin (RR): Each process is assigned a fixed time slice (Time Quantum).");
//        rulesArea.setFont(new Font("Serif", Font.PLAIN, 16));
//        rulesArea.setEditable(false);
//        rulesArea.setBackground(new Color(240, 233, 230));
//        additionalContentPanel.add(rulesArea);
//
//// Simulation Components
//        JTextArea componentsArea = new JTextArea(5, 30);
//        componentsArea.setText("Simulation Components:\n"
//                + "- Process Table: Displays details of all processes.\n"
//                + "- Gantt Chart: Visual representation of process execution.\n"
//                + "- Controls: Buttons to start, pause, and reset the simulation.");
//        componentsArea.setFont(new Font("Serif", Font.PLAIN, 16));
//        componentsArea.setEditable(false);
//        componentsArea.setBackground(new Color(240, 233, 230));
//        additionalContentPanel.add(componentsArea);
//
//// Add the additional content panel to the right side of the main panel
//        mainPanel.add(additionalContentPanel, BorderLayout.EAST);
//
//// Add the main panel to the JFrame
//        add(mainPanel);
//        addButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (inputCount < MAX_INPUTS) {
//                    // Get input values from text fields
//                    String priority = priorityField.getText();
//                    String burstTime = burstTimeField.getText();
//                    String arrivalTime = arrivalTimeField.getText();
//                    String timeQuantum = timeQuantumField.getText();
//                    String processPriority = priorityField1.getText();
//                    String processId = "P" + (inputCount + 1);
//
//                    // Validate priority level
//                    int priorityValue;
//                    try {
//                        priorityValue = Integer.parseInt(priority);
//                        if (priorityValue < 1 || priorityValue > 4) {
//                            throw new NumberFormatException();
//                        }
//                    } catch (NumberFormatException ex) {
//                        JOptionPane.showMessageDialog(null, "Priority level must be an integer between 1 and 4.");
//                        return;
//                    }
//
//                    String priorityName = getPriorityName(priorityValue);
//
//                    // Lock the time quantum field after the first input
//                    if (!timeQuantumLocked) {
//                        timeQuantumLocked = true;
//
//                        timeQuantumField.setEditable(false);
//                        firstTimeQuantum = timeQuantum;
//                    } else {
//                        timeQuantum = firstTimeQuantum; // Use the first time quantum for subsequent inputs
//                    }
//
//                    // Add input values to the table model
//                    Object[] rowData = {processId, processPriority, priority, burstTime, arrivalTime, timeQuantum};
//                    model.addRow(rowData);
//                    inputCount++;
//
//                    // Clear input fields
//                    priorityField.setText("");
//                    burstTimeField.setText("");
//                    arrivalTimeField.setText("");
//                    timeQuantumField.setText("");
//                    priorityField1.setText("");
//
//                    if (inputCount >= MAX_INPUTS) {
//                        addButton.setEnabled(false); // Disable Add button
//                        disableInputFields();
//                        continueButton.setEnabled(true); // Enable Continue button
//                    }
//                }
//            }
//        });
//
//
//        // Add action listener to the Continue button
//        continueButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Create and show the table in a new window
//                JFrame tableFrame = new JFrame("Input Data Table");
//                tableFrame.setSize(WIDTH, HEIGHT);
//                tableFrame.setLocationRelativeTo(null);
//                tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//                // CPU panel
//                JPanel cpuPanel = new JPanel();
//                cpuPanel.setLayout(new BoxLayout(cpuPanel, BoxLayout.Y_AXIS));
//                cpuPanel.setBackground(new Color(50, 50, 50));
//
//                JLabel cpuLabel = new JLabel("CPU Processor:");
//                cpuLabel.setForeground(Color.WHITE);
//                cpuPanel.add(cpuLabel);
//
//                JPanel cpuDetailsPanel = new JPanel(new GridLayout(4, 1));
//                cpuDetailsPanel.setBackground(new Color(50, 50, 50));
//                cpuDetailsPanel.setPreferredSize(new Dimension(120, HEIGHT));
//
//                // Add CPU processor details
//                for (int i = 1; i <= 4; i++) {
//                    String priorityName = getPriorityName(i);
//                    String processor = priorityToProcessorMap.get(priorityName);
//                    JLabel processorLabel = new JLabel(processor + " for " + priorityName);
//                    processorLabel.setForeground(Color.WHITE);
//                    cpuDetailsPanel.add(processorLabel);
//                }
//
//                cpuPanel.add(cpuDetailsPanel);
//
//                // Progress bar panel
//                JPanel progressPanel = new JPanel();
//                progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
//
//                JProgressBar[] progressBars = new JProgressBar[MAX_INPUTS];
//                for (int i = 0; i < MAX_INPUTS; i++) {
//                    progressBars[i] = new JProgressBar();
//                    progressBars[i].setMinimum(0);
//                    progressBars[i].setMaximum(100);
//                    progressBars[i].setValue(0);
//                    progressBars[i].setStringPainted(true);
//                    progressPanel.add(progressBars[i]);
//                }
//
//
//                // Simulate some progress (based on burst time)
//                Timer timer = new Timer(100, new ActionListener() {
//                    private int[] remainingTime = new int[MAX_INPUTS]; // Store remaining burst time for each process
//
//                    // Initialize remaining burst time for each process
//                    {
//                        for (int i = 0; i < MAX_INPUTS; i++) {
//                            String burstTimeString = (String) model.getValueAt(i, 3); // Get burst time from the table (index adjusted)
//                            remainingTime[i] = burstTimeString.isEmpty() ? 0 : Integer.parseInt(burstTimeString);
//                        }
//                    }
//
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        boolean allFinished = true; // Flag to check if all processes have finished
//
//                        // Update remaining time for each process
//                        for (int i = 0; i < MAX_INPUTS; i++) {
//                            if (remainingTime[i] > 0) {
//                                allFinished = false; // At least one process is still running
//                                remainingTime[i]--;
//                                int progress = (int) (((double) (Integer.parseInt((String) model.getValueAt(i, 3)) - remainingTime[i])
//                                        / Integer.parseInt((String) model.getValueAt(i, 3))) * 100); // Calculate progress
//                                progressBars[i].setValue(progress); // Update progress bar
//                                if (progress == 100) {
//                                    progressBars[i].setForeground(Color.RED);
//                                }
//                            }
//                        }
//
//                        if (allFinished) {
//                            ((Timer) e.getSource()).stop(); // Stop the timer if all processes have finished
//                        }
//                        try {
//                            Thread.sleep(900); // Delay for 1 second (1000 milliseconds)
//                        } catch (InterruptedException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                });
//                timer.start();
//
//
//                progressPanel.setPreferredSize(new Dimension(100, HEIGHT));
//
//                JPanel tablePanel = new JPanel(new BorderLayout());
//                tablePanel.setBackground(new Color(50, 50, 50));
////                tablePanel.setPreferredSize(new Dimension(300, 80));
//
//                JTable table = new JTable(model);
//                table.setGridColor(Color.WHITE);
//                table.setForeground(Color.WHITE);
//                table.setBackground(new Color(70, 70, 70));
//
//                JTableHeader header = table.getTableHeader();
//                header.setForeground(Color.WHITE);
//                header.setBackground(new Color(30, 30, 30));
//
//
//                JScrollPane scrollPane = new JScrollPane(table);
//                tablePanel.add(scrollPane, BorderLayout.CENTER);
//
//                // Results panel in the second frame
//                JPanel resultsPanel = new JPanel(new GridLayout(3, 2));
//                resultsPanel.setBackground(new Color(50, 50, 50));
//
//                JLabel waitingTimeLabel = new JLabel("Waiting Time:");
//                waitingTimeLabel.setForeground(Color.WHITE);
//                resultsPanel.add(waitingTimeLabel);
//
//                JTextField waitingTimeField = new JTextField(10);
//                waitingTimeField.setEditable(false);
//                resultsPanel.add(waitingTimeField);
//
//                JLabel turnaroundTimeLabel = new JLabel("Turnaround Time:");
//                turnaroundTimeLabel.setForeground(Color.WHITE);
//                resultsPanel.add(turnaroundTimeLabel);
//
//                JTextField turnaroundTimeField = new JTextField(10);
//                turnaroundTimeField.setEditable(false);
//                resultsPanel.add(turnaroundTimeField);
//
//                JLabel responseTimeLabel = new JLabel("Response Time:");
//                responseTimeLabel.setForeground(Color.WHITE);
//                resultsPanel.add(responseTimeLabel);
//
//                JTextField responseTimeField = new JTextField(10);
//                responseTimeField.setEditable(false);
//                resultsPanel.add(responseTimeField);
//
//
//                // Button panel for stop, play, reset
//                JPanel buttonPanel = new JPanel();
//                JButton stopButton = new JButton("Stop");
//                JButton playButton = new JButton("Play");
//                JButton resetButton = new JButton("Reset");
//
//                playButton.setEnabled(false);
//
//                stopButton.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        new Thread(() -> {
//                            timer.stop();
//                            stopButton.setEnabled(false);
//                            playButton.setEnabled(true);
//                        }).start();
//                    }
//                });
//
//                playButton.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        // Add action for play button
//                        new Thread(() -> {
//                            timer.start();
//                            playButton.setEnabled(false);
//                            stopButton.setEnabled(true);
//                        }).start();
//                    }
//                });
//
//                resetButton.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        // Add action for reset button
//                        new Thread(() -> {
//                            for (int i = 0; i < MAX_INPUTS; i++) {
//                                progressBars[i].setValue(0);
//                            }
//                            playButton.setEnabled(true);
//
//                        }).start();
//                    }
//                });
//
//                buttonPanel.add(stopButton);
//                buttonPanel.add(playButton);
//                buttonPanel.add(resetButton);
//
//                JPanel containerPanel = new JPanel(new BorderLayout());
//                containerPanel.add(buttonPanel, BorderLayout.NORTH);
//                containerPanel.add(cpuPanel, BorderLayout.WEST);
//                containerPanel.add(progressPanel, BorderLayout.CENTER);
//                containerPanel.add(tablePanel, BorderLayout.EAST);
//                containerPanel.add(resultsPanel, BorderLayout.SOUTH);
//
//
//                tableFrame.add(containerPanel);
//                tableFrame.setVisible(true);
//
//                dispose(); // Dispose of the current window
//            }
//        });
//        mainPanel.add(tablePanel, BorderLayout.SOUTH);
//        add(mainPanel);
//        setVisible(true);
//    }
//
//    private void initializeProcessorMapping() {
//        priorityToProcessorMap = new HashMap<>();
//        priorityToProcessorMap.put("Real Time", "Core 1");
//        priorityToProcessorMap.put("System", "Core 2");
//        priorityToProcessorMap.put("Interactive", "Core 3");
//        priorityToProcessorMap.put("Batch", "Core 4");
//    }
//
//    private String getPriorityName(int priorityValue) {
//        switch (priorityValue) {
//            case 1:
//                return "Real Time";
//            case 2:
//                return "System";
//            case 3:
//                return "Interactive";
//            case 4:
//                return "Batch";
//            default:
//                return "";
//        }
//    }
//    void addToTable(){
//        if (inputCount < MAX_INPUTS) {
//            // Get input values from text fields
//            String priority = priorityField.getText();
//            String burstTime = burstTimeField.getText();
//            String arrivalTime = arrivalTimeField.getText();
//            String timeQuantum = timeQuantumField.getText();
//            String processPriority = priorityField1.getText();
//            String processId = "P" + (inputCount + 1);
//
//            // Validate priority level
//            int priorityValue;
//            try {
//                priorityValue = Integer.parseInt(priority);
//                if (priorityValue < 1 || priorityValue > 4) {
//                    throw new NumberFormatException();
//                }
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(null, "Priority level must be an integer between 1 and 4.");
//                return;
//            }
//
//            String priorityName = getPriorityName(priorityValue);
//
//            // Lock the time quantum field after the first input
//            if (!timeQuantumLocked) {
//                timeQuantumLocked = true;
//
//                timeQuantumField.setEditable(false);
//                firstTimeQuantum = timeQuantum;
//            } else {
//                timeQuantum = firstTimeQuantum; // Use the first time quantum for subsequent inputs
//            }
//
//            // Add input values to the table model
//            Object[] rowData = {processId, processPriority, priority, burstTime, arrivalTime, timeQuantum};
//            model.addRow(rowData);
//            inputCount++;
//
//            // Clear input fields
//            priorityField.setText("");
//            burstTimeField.setText("");
//            arrivalTimeField.setText("");
//            timeQuantumField.setText("");
//            priorityField1.setText("");
//
//            if (inputCount >= MAX_INPUTS) {
//                disableInputFields();
//            }
//        }
//    }
//
//    private void disableInputFields() {
//        priorityField.setEnabled(false);
//        burstTimeField.setEnabled(false);
//        arrivalTimeField.setEnabled(false);
//        timeQuantumField.setEnabled(false);
//    }
//    private void startProcessess(ArrayList<Process> process){
//        ArrayList<Process> array1;
//        ArrayList<Process> array2;
//        ArrayList<Process> array3;
//        ArrayList<Process> array4;
//
//        array1 = Sorter.sortByPriority(processes, 1);
//        array2 = Sorter.sortByPriority(processes, 2);
//        array3 = Sorter.sortByPriority(processes, 3);
//        array4 = Sorter.sortByPriority(processes, 4);
//
//
//        RoundRobinScheduler rr = new RoundRobinScheduler(array1);
//        SRTF srtf = new SRTF(array2);
//        SJF sjf = new SJF(array3);
//        NPPS npps = new NPPS(array4);
//
//        Thread thread1 = new Thread(rr);
//        Thread thread2 = new Thread(srtf);
//        Thread thread3 = new Thread(sjf);
//        Thread thread4 = new Thread(npps);
//
//
//        thread1.start();
//        thread2.start();
//        thread3.start();
//        thread4.start();
//        try{
//            thread1.join();
//            thread2.join();
//            thread3.join();
//            thread4.join();
//
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
//
//    }
//    void continueButton(){
//
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(InputProcessProcessWithRandom::new);
//    }
//}
