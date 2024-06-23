package UI;

import BackEndStuff.Process;
import BackEndStuff.SchedulingAlgo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class MyFrame extends JFrame {

    private int maxInputs = 20; // Make maxInputs dynamic
    private DefaultTableModel model;
    static JTextField priorityField;
    static JTextField burstTimeField;
    static JTextField arrivalTimeField;
    static JTextField timeQuantumField;
    static JTextField levelProcess;
    private JTextField maxInputsField; // Field to take max inputs from user
    private JButton continueButton = new JButton("Continue");
    private int inputCount = 0;
    private boolean isFirstTimeQuantumSet = false;
    private String firstTimeQuantum = "";
    public String priority;
    public String burstTime;
    public String arrivalTime;
    public String timeQuantum;
    public String processPriority;
    public String processId;
    static public Object[] rowData;
    static public ArrayList<String> processIDArray = new ArrayList<>();
    static public ArrayList<String> processPriorityArray = new ArrayList<>();
    static public ArrayList<String> priorityArray = new ArrayList<>();
    static public ArrayList<String> burstTimeArray = new ArrayList<>();
    static public ArrayList<String> arrivalTimeArray = new ArrayList<>();
    static public ArrayList<String> timeQuantumArray = new ArrayList<>();
     static public ArrayList<Process> processes;
    public int firstQuantum;
    public static SchedulingAlgo sched;
    //--------------------------------------------------------------------------------------------------------------
    public MyFrame() {
        setTitle("Process Scheduler");
        String[] columnNames = {"Process ID", "Process Level", "Priority", "Burst Time", "Arrival Time", "Time Quantum"};
        model = new DefaultTableModel(new Object[0][6], columnNames);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel userInputPanel = createUserInputPanel();
        mainPanel.add(userInputPanel, BorderLayout.WEST);

        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        this.processes  = new ArrayList<>();
    }

    //--------------------------------------------------------------------------------------------------------------
    private JPanel createUserInputPanel() {
        JPanel userInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        userInput.add(new JLabel("Max Inputs:"), gbc);
        gbc.gridy++;
        maxInputsField = new JTextField(10);
        userInput.add(maxInputsField, gbc);

        gbc.gridy++;
        userInput.add(new JLabel("Level of Process [1. Real Time 2. System 3. Interactive 4. Batch]"), gbc);
        gbc.gridy++;
        levelProcess = new JTextField(10);
        userInput.add(levelProcess, gbc);

        gbc.gridy++;
        userInput.add(new JLabel("Burst Time:"), gbc);
        gbc.gridy++;
        burstTimeField = new JTextField(10);
        userInput.add(burstTimeField, gbc);

        gbc.gridy++;
        userInput.add(new JLabel("Arrival Time:"), gbc);
        gbc.gridy++;
        arrivalTimeField = new JTextField(10);
        userInput.add(arrivalTimeField, gbc);

        gbc.gridy++;
        userInput.add(new JLabel("Time Quantum:"), gbc);
        gbc.gridy++;
        timeQuantumField = new JTextField(10);
        userInput.add(timeQuantumField, gbc);

        gbc.gridy++;
        userInput.add(new JLabel("Priority Level:"), gbc);
        gbc.gridy++;
        priorityField = new JTextField(10);
        userInput.add(priorityField, gbc);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addToTable());
        gbc.gridy++;
        userInput.add(addButton, gbc);

        JButton generateButton = new JButton("Generate Random Process");
        gbc.gridy++;
        userInput.add(generateButton, gbc);

        generateButton.addActionListener(e -> generateRandomProcess());

        continueButton.setEnabled(false);
        continueButton.addActionListener(e -> continueProcess());
        gbc.gridy++;
        userInput.add(continueButton, gbc);

        return userInput;
    }

    //--------------------------------------------------------------------------------------------------------------
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(50, 50, 50));

        JTable table = new JTable(model);
        table.setGridColor(Color.WHITE);
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(70, 70, 70));

        JTableHeader header = table.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 30));

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    //--------------------------------------------------------------------------------------------------------------
    private void generateRandomProcess() {
        try {
            maxInputs = Integer.parseInt(maxInputsField.getText()); // Get dynamic max inputs from user
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for max inputs.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < maxInputs; i++) {

            int randomPriority = (int) (Math.random() * 4) + 1;
            levelProcess.setText(String.valueOf(randomPriority));

            int randomBurstTime = (int) (Math.random() * 20) + 1;
            int randomArrivalTime = (int) (Math.random() * 10);
            burstTimeField.setText(String.valueOf(randomBurstTime));
            arrivalTimeField.setText(String.valueOf(randomArrivalTime));

            if (randomPriority == 1) {
                if (!isFirstTimeQuantumSet) {
                    int randomTimeQuantum = (int) (Math.random() * 10) + 1;
                    timeQuantumField.setText(String.valueOf(randomTimeQuantum));
                    firstTimeQuantum = timeQuantumField.getText();
                    firstQuantum = Integer.parseInt(timeQuantumField.getText());
                    isFirstTimeQuantumSet = true;
                } else {
                    timeQuantumField.setText(firstTimeQuantum);
                }
            } else {
                timeQuantumField.setText("");
            }

            if (randomPriority == 3) {
                int randomProcessPriority = (int) (Math.random() * 10) + 1;
                priorityField.setText(String.valueOf(randomProcessPriority)); ///////
            }
            System.out.println(priorityField.getText());
//
            addToTable();
        }

        if (inputCount <= maxInputs) {
            continueButton.setEnabled(true);
        }
    }

    //--------------------------------------------------------------------------------------------------------------
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setBurstTime(String burstTime) {
        this.burstTime = burstTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setTimeQuantum(String timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    public void setProcessPriority(String processPriority) {
        this.processPriority = processPriority;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    // ------------------------------------------------------------------------
    public void addToTable() {
        try {
            maxInputs = Integer.parseInt(maxInputsField.getText()); // Get dynamic max inputs from user
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for max inputs.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (inputCount < maxInputs) {

            this.priority = (String.valueOf(priorityField.getText()));
            priorityArray.add(this.priority);
            System.out.println("Priority: "+ priorityField.getText());

            this.processPriority = String.valueOf(levelProcess.getText());
            processPriorityArray.add(this.processPriority);

            System.out.println("BT: "+ this.burstTime);
            this.burstTime = String.valueOf(burstTimeField.getText());
            burstTimeArray.add(this.burstTime);

            this.arrivalTime = String.valueOf(arrivalTimeField.getText());
            arrivalTimeArray.add(this.arrivalTime);

            this.timeQuantum = String.valueOf(timeQuantumField.getText());
            timeQuantumArray.add(this.timeQuantum);


            int burst = Integer.parseInt(String.valueOf(burstTimeField.getText()));
            int arrival = Integer.parseInt(String.valueOf(arrivalTimeField.getText()));
            int levelPrio = Integer.parseInt(String.valueOf(processPriority));
            if (levelPrio == 3){
                int priority =  Integer.parseInt(String.valueOf(priorityField.getText()));
                Process process = new Process(inputCount+1,arrival,burst,levelPrio,priority);
                processes.add(process);
            }else {
                Process process = new Process(inputCount+1,arrival,burst,levelPrio);
                processes.add(process);
            }

            this.processId = "P" + (inputCount + 1);
            processIDArray.add(this.processId);

            if (processPriority.equals("1")) {
                if (!isFirstTimeQuantumSet) {
                    firstTimeQuantum = timeQuantum;
                    isFirstTimeQuantumSet = true;
                } else {
                    timeQuantum = firstTimeQuantum;
                }
            }
            rowData = new Object[]{
                    processId, processPriority, priority, burstTime, arrivalTime, timeQuantum
            };

            model.addRow(rowData);
            inputCount++;

            setPriority(priority);
            setBurstTime(burstTime);
            setArrivalTime(arrivalTime);
            setTimeQuantum(timeQuantum);
            setProcessPriority(processPriority);
            setProcessId(processId);



            priorityField.setText("");
            burstTimeField.setText("");
            arrivalTimeField.setText("");
            timeQuantumField.setText("");
            levelProcess.setText("");

            if (inputCount <= maxInputs) {
                continueButton.setEnabled(true);
            }

        }
    }

    //--------------------------------------------------------------------------------------------------------------
    private void continueProcess() {
        sched = new SchedulingAlgo(processes,4);
        sched.run();

        System.out.println("Frame :");
        processes.forEach(e-> {
            System.out.println("Process ID:" + e.getPid());
            System.out.println("BT:" + e.getBurstTime());
            System.out.println("level: :" + e.getPrioritySchedule());
            System.out.println("Arrival:" + e.getArrivalTime());
        });
        SwingUtilities.invokeLater(() -> {
            MyPanel panel = new MyPanel();
            JFrame newFrame = new JFrame("Process Scheduler - Visualization");
            newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            newFrame.getContentPane().add(panel);
            newFrame.pack();
            newFrame.setLocationRelativeTo(null);
            newFrame.setResizable(false);
            newFrame.setVisible(true);
            dispose();
        });
    }

    //--------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyFrame::new);
    }
    /** -------------------------------------------------------------------------------------------------------- */
}
