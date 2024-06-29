package UI;


import BackEndStuff.GanttChart;
import BackEndStuff.Process;
import BackEndStuff.SchedulingAlgo;
import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Random;



import static UI.MyFrame.*;
import static javax.imageio.ImageIO.read;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class MyPanel extends JPanel implements ActionListener, Runnable{

    final int fps = 60;
    Thread animationThread;
    int drawCount = 0;
    ArrayList<Process> processOnQueue;
    private JTable[] ganttChartTables = new JTable[4];


    final int WIDTH = 2500;
    final int HEIGHT = 800;
    Image backgroundImage;
    Timer ballTimer;
    Timer labelTimer;
    boolean labelsAdded = false;
    JTable computationTable;

    JButton startButton;
    JButton stopButton;
    JButton aboutButton;
    DefaultTableModel ganttModel;

    public static final int BALL_SIZE = 25;
    public static final int velocityX = 20;
    public static final int velocityY = 20;
    JLabel timerLabel;
    int elapsedTime = 0;
    DefaultTableModel model ; // time in seconds

    int xVelocity = 2;
    int yVelocity = 2;
    int ballX = 150; // Ball's initial x position
    int[] ballY = {123,293,353,613}; // Ball's initial y position
    int[] ballYOrig = {123,293,353,613}; // Ball's initial y position



    String procId;
    private final int coreX = 730;
    private final int[] coresY = {123, 293, 453, 613};

    // Keep track of arrived processes
    private final Map<String, Image> ballImages = new HashMap<>();
    private final Map<String, Point> arrivedProcesses = new HashMap<>();
    private final Set<Point> occupiedPositions = new HashSet<>();

    // Added part: Track ball velocities
    private final Map<String, Integer> ballVelocitiesX = new HashMap<>();
    private final Map<String, Integer> ballVelocitiesY = new HashMap<>();
    private int currentActivePriority = 1;
    BufferedImage bg;
    KeyHandler keyH;

    Thread updater;


    GanttChart allGantts = new GanttChart();
    //----------------------------------------------------------------------------------------------------------------------
    public MyPanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(null);
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);
        keyH = new KeyHandler(this);
        this.addMouseListener(keyH);
        getResources();
        System.out.println("gant sa panel");
        System.out.println("Size: " + SchedulingAlgo.gantts.size());
        int i = 1;
        for (GanttChart gantt : SchedulingAlgo.gantts) {
            System.out.println("Gant: " + i);
            gantt.displayChart();
            i++;
        }
        processOnQueue = new ArrayList<>();

//        updater = new Thread();
        //----------------------------------------------------------------------------------------------------------------------
        ballTimer = new Timer(0, this);
        labelTimer = new Timer(300, e -> {
            updateTimer();
            removeTableContents();

            updateGanttChart(elapsedTime);
        });
        timerLabel = new JLabel("Time: 0 seconds");
        timerLabel.setFont(new Font("Times New Roman", Font.BOLD, 15));
        timerLabel.setBounds(470, 5, 200, 30);
        this.add(timerLabel);
        //----------------------------------------------------------------------------------------------------------------------
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        aboutButton = new JButton("About");
        startButton.setBackground(Color.WHITE);
        stopButton.setBackground(Color.WHITE);
        aboutButton.setBackground(Color.white);

        startButton.setBounds(640, 10, 80, 30);
        stopButton.setBounds(730, 10, 80, 30);
        aboutButton.setBounds(830, 10, 80, 30);

        startButton.addActionListener(e -> {
            ballTimer.start();
            labelTimer.start();
        });
        stopButton.addActionListener(e -> {
            ballTimer.stop();
            labelTimer.stop();
        });
        aboutButton.addActionListener(e -> {
            JFrame aboutFrame = new JFrame("About");
            aboutFrame.setSize(700, 500);
            aboutFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            aboutFrame.setLocationRelativeTo(null);
            aboutFrame.setResizable(false);

            JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
            jTabbedPane.setBounds(23, 35, 613, 365);
            aboutFrame.add(jTabbedPane);


            JPanel rr = new JPanel();
            JPanel sjf = new JPanel();
            JPanel srtf = new JPanel();
            JPanel npps = new JPanel();

            jTabbedPane.addTab("RR", rr);
            jTabbedPane.addTab("SJF", sjf);
            jTabbedPane.addTab("SRTF", srtf);
            jTabbedPane.addTab("NPPS", npps);
            //----------------------------------------------------------------------------------------------------------------------
            JTextArea rrText = new JTextArea("""
                    Round Robin Scheduling:
                    Preemptive Scheduling
                    Every process gets a fixed amount of time quantum to execute the process.
                    Processes that have their burst time remaining after the expiration of the
                    time quantum are sent back to the ready state and wait for their next turn
                    to complete the execution until it terminates.

                    How Does It Work?
                    1. All the processes are added to the ready queue.
                    2. At first, The burst time of every process is compared to the time quantum of the CPU.
                    3. If the burst time of the process is less than or equal to the time quantum in the round-robin
                       scheduling algorithm, the process is executed to its burst time.
                    4. If the burst time of the process is greater than the time quantum,
                       the process is executed up to the time quantum (TQ).
                    5. When the time quantum expires, it checks if the process is executed completely or not.
                    6. On completion, the process terminates. Otherwise, it goes back again to the ready state.""");
            rr.add(rrText);
            rr.setBackground(Color.WHITE);
            rrText.setFont(new Font("Times New Roman", Font.PLAIN, 13));
            //----------------------------------------------------------------------------------------------------------------------
            JTextArea sjfText = new JTextArea("""
                    Shortest Job First Scheduling:
                    Non-Preemptive Scheduling
                    Processes with the shortest burst time are scheduled first.
                    This algorithm minimizes the average waiting time for processes.

                    How Does It Work?
                    1. All the processes are added to the ready queue.
                    2. The scheduler selects the process with the shortest burst time from the ready queue.
                    3. The selected process is executed until completion.
                    4. Once the process finishes execution, it terminates and is removed from the ready queue.
                    5. The scheduler then repeats the process, selecting the next shortest burst time from the remaining processes in the ready queue.
                    6. This continues until all processes have been executed and the ready queue is empty.""");
            sjf.add(sjfText);
            sjf.setBackground(Color.WHITE);
            sjfText.setFont(new Font("Times New Roman", Font.PLAIN, 13));
            //----------------------------------------------------------------------------------------------------------------------
            JTextArea srtfText = new JTextArea("""
                    Shortest Remaining Time First Scheduling:
                    Preemptive Scheduling
                    A variant of SJF, where the process with the shortest remaining burst time is selected for execution.
                    If a new process arrives with a shorter burst time than the remaining time of the currently executing process, the current process is
                    preempted.

                    How Does It Work?
                    1. All the processes are added to the ready queue.
                    2. The scheduler selects the process with the shortest burst time from the ready queue.
                    3. The selected process begins execution.
                    4. If a new process arrives with a shorter burst time than the remaining time of the currently executing process, the current process
                       is preempted and placed back in the ready queue.
                    5. The new process with the shorter burst time is executed.
                    6. The scheduler continues to select the process with the shortest remaining burst time, preempting current processes if
                       necessary, until all processes are completed and the ready queue is empty.""");
            srtf.add(srtfText);
            srtf.setBackground(Color.WHITE);
            srtfText.setFont(new Font("Times New Roman", Font.PLAIN, 13));
            //----------------------------------------------------------------------------------------------------------------------
            JTextArea nppsText = new JTextArea("""
                    Non-Preemptive Priority Scheduling:
                    Non-Preemptive Scheduling
                    Each process is assigned a priority, and processes are scheduled based on priority levels.
                    Higher priority processes are executed first. If two processes have the same priority, they are scheduled based on their arrival time.

                    How Does It Work?
                    1. All the processes are added to the ready queue.
                    2. Each process is assigned a priority level.
                    3.The scheduler selects the process with the highest priority from the ready queue.
                    4. If two or more processes have the same priority, the scheduler selects the process that arrived first.
                    5. The selected process is executed until completion.
                    6. Once the process finishes execution, it terminates and is removed from the ready queue.
                    7. The scheduler then repeats the process, selecting the next highest priority process from the remaining processes in the ready
                       queue.
                    8. This continues until all processes have been executed and the ready queue is empty.""");
            npps.add(nppsText);
            npps.setBackground(Color.WHITE);
            nppsText.setFont(new Font("Times New Roman", Font.PLAIN, 13));

            aboutFrame.setVisible(true);
        });

        this.add(startButton);
        this.add(stopButton);
        this.add(aboutButton);

        initializeTable();
    }

    private void getResources() {
        try {
            bg = read((getClass().getResourceAsStream("/resources/Frame 1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //----------------------------------------------------------------------------------------------------------------------
    private void initializeTable() {

        for (int i = 4; i < 8; i++){
            allGantts.addAllUniqueProcess(SchedulingAlgo.gantts.get(i).getUniqueProccess());
            System.out.println("Unique Process:" + allGantts.getUniqueProcessSize());
            allGantts.getUniqueProccess().forEach(e-> System.out.println("UNIQUE PID: "+ e.getPid()));
        }
        allGantts.sortByProcessID();
        computationTable = new JTable();
        Object[] columns = {"PID", "Level P", "Priority", "Burst", "Arrival", "Time Quantum", "Waiting","Turn","Affinity",  "Res", "Stat"};
       model= new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        computationTable.setModel(model);
        computationTable.setFocusable(false);
        computationTable.setEnabled(false);

        computationTable.setGridColor(Color.WHITE);
        computationTable.setForeground(Color.black);
        computationTable.setSelectionBackground(Color.black);
        computationTable.setGridColor(Color.black);
        computationTable.setRowHeight(30); // Adjust this value to change the row height
        computationTable.setAutoCreateRowSorter(true);

        // Set the intercell spacing to add space between rows
        computationTable.setIntercellSpacing(new Dimension(0, 10)); // 10 pixels vertical spacing


        JScrollPane scrollPane = new JScrollPane(computationTable);
        scrollPane.setBounds(955, 68, 570, 200);
        this.add(scrollPane);

        // Gantt Chart Tables
        int yPosition = 290;
        for (int core = 0; core < 4; core++) {
            DefaultTableModel ganttModel = new DefaultTableModel();
            ganttModel.addRow(new Object[]{});

            JTable ganttChartTable = new JTable(ganttModel);
            ganttChartTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            ganttChartTable.setFillsViewportHeight(true);
            ganttChartTable.setFont(new Font("Arial", Font.BOLD, 12));
            ganttChartTable.setRowHeight(40);

            JScrollPane ganttScrollPane = new JScrollPane(ganttChartTable);
            ganttScrollPane.setBounds(955, yPosition, 570, 70);
            this.add(ganttScrollPane);

            JLabel coreLabel = new JLabel("Core " + (core + 1));
            coreLabel.setFont(new Font("Arial", Font.BOLD, 12));
            coreLabel.setBounds(955, yPosition - 30, 570, 30);
            this.add(coreLabel);

            yPosition += 100; // dito adjust spacing

            ganttChartTables[core] = ganttChartTable;
        }

    }


    //----------------------------------------------------------------------------------------------------------------------
    public void updateTimer() {
//        repaint();

        elapsedTime++;
//        repaint();
        timerLabel.setText("Time: " + elapsedTime + " seconds");
        animationThread = new Thread(this);
        animationThread.start();
    }

    //----------------------------------------------------------------------------------------------------------------------
    private void addLabels() {
        JLabel rrLabel = new JLabel("Real Time Queueing (Round Robin)");
        rrLabel.setBounds(125, 49, 208, 12);
        this.add(rrLabel);
        JLabel systemLabel = new JLabel("System Queueing (SJF)");
        systemLabel.setBounds(125, 220, 208, 12);
        this.add(systemLabel);
        JLabel interactiveLabel = new JLabel("Interactive Queueing (NPPS)");
        interactiveLabel.setBounds(125, 384, 208, 12);
        this.add(interactiveLabel);
        JLabel batchLabel = new JLabel("Batch Queueing (SRTF)");
        batchLabel.setBounds(125, 548, 208, 12);
        this.add(batchLabel);
        JLabel core1Label = new JLabel("Core 1");
        core1Label.setBounds(700, 67, 66, 12);


        JLabel averageWaitingTime = new JLabel("Average Waiting Time:1 "+ getAverageWait(1) );
        averageWaitingTime.setBounds(1074, 270, 150, 12);
        this.add(averageWaitingTime);

        JLabel averageTurn = new JLabel("Average TurnAround:1 "+ getAverageTurn(1));
        averageTurn.setBounds(1380, 270, 150, 12);
        this.add(averageTurn);
//-----------------------------//

        JLabel averageWaitingTime2 = new JLabel("Average Waiting Time:2 "+ getAverageWait(2) );
        averageWaitingTime2.setBounds(1074, 375, 150, 12);
        this.add(averageWaitingTime2);

        JLabel averageTurn2 = new JLabel("Average TurnAround:3 "+ getAverageTurn(2));
        averageTurn2.setBounds(1380, 375, 150, 12);
        this.add(averageTurn2);
//-----------------------------//
        JLabel averageWaitingTime3 = new JLabel("Average Waiting Time:3 "+ getAverageWait(3) );
        averageWaitingTime3.setBounds(1074, 470, 150, 12);
        this.add(averageWaitingTime3);

        JLabel averageTurn3 = new JLabel("Average TurnAround:3 "+ getAverageTurn(3));
        averageTurn3.setBounds(1380, 470, 150, 12);
        this.add(averageTurn3);
//-----------------------------//
        JLabel averageWaitingTime4 = new JLabel("Average Waiting Time: 4 "+ getAverageWait(4) );
        averageWaitingTime4.setBounds(1074, 563, 150, 12);
        this.add(averageWaitingTime4);

        JLabel averageTurn4 = new JLabel("Average TurnAround: 4"+ getAverageTurn(4));
        averageTurn4.setBounds(1380, 563, 150, 12);
        this.add(averageTurn4);



        this.add(core1Label);
        JLabel core2Label = new JLabel("Core 2");
        core2Label.setBounds(700, 230, 66, 12);
        this.add(core2Label);
        JLabel core3Label = new JLabel("Core 3");
        core3Label.setBounds(700, 389, 66, 12);
        this.add(core3Label);
        JLabel core4Label = new JLabel("Core 4");
        core4Label.setBounds(700, 548, 66, 12);
        this.add(core4Label);
        System.out.println();
    }

    private int getAverageTurn(int i) {
        return SchedulingAlgo.gantts.get(i+3).getAverageTurn();
    }

    private int getAverageWait(int i) {
        return SchedulingAlgo.gantts.get(i+3).getAverageWait();
    }

    //----------------------------------------------------------------------------------------------------------------------
    //components inside the draw method
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;


        g2D.drawImage(bg, 0, 0, null);
        computationTable.repaint();
        // Draw the labels
        if (!labelsAdded) {
            addLabels();
            labelsAdded = true;
        }
        drawBallsOnCores(g2D);
        drawBall1(g2D);
        ganttChart(g2D);

    }

    private void drawBallsOnCores(Graphics2D g2D) {

        for (int i = 4; i <= 7; i++) {
            // Check each core's Gantt chart and draw ball if process is running
            GanttChart gantt = SchedulingAlgo.gantts.get(i);

            if (gantt.isRunning(elapsedTime)) {
//                System.out.println(i);
                Process curr = gantt.getProcessOnCore(elapsedTime);
//                System.out.println("Core: " + (i - 4) + "\nCurrent process: " + curr.getPid()+ "Affinity: "+ curr.getCoreIDAffinity());
                int coreIndex = i - 4; // Calculate core index based on loop index
                drawBallOnCore(g2D, curr, curr.getCoreIDAffinity());
            }
        }

    }

    private void drawBallOnCore(Graphics2D g2D, Process curr, int coreIndex) {
        if (curr != null) {
            String id = "P" + curr.getPid();
            g2D.setColor(Color.BLACK); // Set color for core balls

            animateBall(g2D, curr,coreIndex);

            // Draw the ball on the specified core
            g2D.fillOval(coreX, coresY[coreIndex], BALL_SIZE, BALL_SIZE);

            // Set font and color for text
            g2D.setFont(new Font("Arial", Font.BOLD, 12));
            g2D.setColor(Color.WHITE);

            // Center the text within the ball
            FontMetrics fm = g2D.getFontMetrics();
            int textWidth = fm.stringWidth(id);
            int textX = coreX + (BALL_SIZE - textWidth) / 2;
            int textY = coresY[coreIndex] + (BALL_SIZE + fm.getAscent()) / 2 - fm.getDescent();

            // Draw process ID inside the ball
            g2D.drawString(id, textX, textY);
        }
    }

    private void animateBall(Graphics2D g2D, Process curr, int coreIndex) {

    }


    //---------------------------------------------------------------------------------------------------------------------

    public void drawBall1(Graphics2D g2D) {
        drawBalls(g2D, "1", 78);
        drawBalls(g2D, "2", 250);
        drawBalls(g2D, "3", 420);
        drawBalls(g2D, "4", 570);
    }
    //---------------------------------------------------------------------------------------------------------------------

    private void drawBalls(Graphics2D g2D, String priority, int startY) {
        List<String> sortedArrivalTime = new ArrayList<>();
        List<String> processId = new ArrayList<>();
        List<Integer> sortedIndices = new ArrayList<>();

        for (int index = 0; index < processPriorityArray.size(); index++) {
            String element = processPriorityArray.get(index);
            if (priority.equals(element)) {
                String arrival = arrivalTimeArray.get(index);
                sortedArrivalTime.add(arrival);
                processId.add(processIDArray.get(index));
                sortedIndices.add(index);
            }
        }
        //---------------------------------------------------------------------------------------------------------------------
        Collections.sort(sortedIndices, (i1, i2) -> arrivalTimeArray.get(i1).compareTo(arrivalTimeArray.get(i2)));
        int x = 150;
        int y = startY;

        for (Integer index : sortedIndices) {
            String procId = processIDArray.get(index);
            if (elapsedTime >= Integer.parseInt(arrivalTimeArray.get(index)) && !arrivedProcesses.containsKey(procId)) {
                Point position = new Point(x, y);
                while (occupiedPositions.contains(position)) {
                    x += BALL_SIZE;
                    position = new Point(x, y);

                    if (x >= 350) {
                        x -= 210;
                        y += 25;
                        position = new Point(x, y); // Update the position after changing x and y
                    }
                }
                arrivedProcesses.put(procId, position);
                occupiedPositions.add(position);
                // Added part: Initialize ball velocity
                ballVelocitiesX.put(procId, xVelocity);
                ballVelocitiesY.put(procId, yVelocity);

                int currentPriority = Integer.parseInt(processPriorityArray.get(index));
                if (currentPriority < currentActivePriority) {
                    currentActivePriority = currentPriority;
                }
            }
        }
        for (Map.Entry<String, Point> entry : arrivedProcesses.entrySet()) {
            drawBall(g2D, entry.getKey(), entry.getValue().x, entry.getValue().y);
        }
    }

    //---------------------------------------------------------------------------------------------------------------------

    // Add this field to the MyPanel class to store the colors
    private final Map<String, Color[]> ballColors = new HashMap<>();

    private void drawBall(Graphics2D g2D, String id, int x, int y) {
        // If colors are not already generated for this ball, generate and store them
        if (!ballColors.containsKey(id)) {
            Random rand = new Random();
            Color color1 = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            Color color2 = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            ballColors.put(id, new Color[]{color1, color2});
        }

        // Retrieve the stored colors
        Color[] colors = ballColors.get(id);
        Color color1 = colors[0];
        Color color2 = colors[1];

        // Create a gradient color with the stored colors
        GradientPaint gradient = new GradientPaint(x, y, color1, x + BALL_SIZE, y + BALL_SIZE, color2);
        g2D.setPaint(gradient);

        // Draw the ball with the gradient
        g2D.fillOval(x, y, BALL_SIZE, BALL_SIZE);

        // Set the font and color for the text
        g2D.setFont(new Font("Arial", Font.BOLD, 12));
        g2D.setColor(Color.white);

        // Center the text within the ball
        FontMetrics fm = g2D.getFontMetrics();
        int textWidth = fm.stringWidth(id);
        int textX = x + (BALL_SIZE - textWidth) / 2;
        int textY = y + (BALL_SIZE + fm.getAscent()) / 2 - fm.getDescent();

        // Draw the process ID inside the ball
        g2D.drawString(id, textX, textY);
    }

    //---------------------------------------------------------------------------------------------------------------------
    @Override
    public void run() {
        double drawInterval = 1000000000 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        while (animationThread != null) {
            repaint();
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/ drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1){
                //
                update();

                repaint();
                delta --;
                drawCount++;
                //increment
            }
            if (timer >= 1000000000){
                drawCount = 0;
                timer = 0;
                timer ++;
            }
        }

    }

    public boolean isDoneAnimating = false;

    //---------------------------------------------------------------------------------------------------------------------
    public void actionPerformed(ActionEvent e) {
        // Update ball positions and repaint
        //    updateBallPositions();
        repaint();
    }

    //---------------------------------------------------------------------------------------------------------------------
    private void ganttChart(Graphics2D g2D) {
        // Implement Gantt chart drawing
    }


    private void update() {
        //update here based on where the process is

        /*
        Based on gantt chart that will be provided by the Scheduling algo
        for process in gantt chart
        loop through all the process
        if process.hasArrived()
        based on scheduler process segregate each process
        draw
        //find the core that is timeOnCore == timer
        once found get the coreID coords
         */
    }
    ArrayList<Process> executing = new ArrayList<>();
        int column = 0;
    public void updateGanttChart(int currentTime) {
        // Get the processes currently being executed at the given time
        executing = allGantts.getExecutedProcess(elapsedTime);

        for (int core = 0; core < ganttChartTables.length; core++) {
            boolean isCoreIdle = true; // Assume the core is idle initially

            // Iterate over the executing processes
            for (Process process1 : executing) {
                if (process1 != null && process1.getCoreIDAffinity() == core) {
                    DefaultTableModel ganttModel = (DefaultTableModel) ganttChartTables[core].getModel();
                    String columnName = "Time " + (elapsedTime);

                    int columnIndex = ganttModel.findColumn(columnName);
                    if (columnIndex == -1) {
                        // Add a new column for the current time
                        ganttModel.addColumn(columnName);
                        columnIndex = ganttModel.getColumnCount() - 1;
                    }

                    // Set the process ID (PID) in the appropriate row and column
                    if (process1.getPid() != -1) {
                        ganttModel.setValueAt("PID " + process1.getPid(), 0, columnIndex);
                    } else {
                        ganttModel.setValueAt("Idle", 0, columnIndex);
                    }

                    isCoreIdle = false; // The core is not idle since a process is assigned
                    break; // Break out of the inner loop as we've found a process for this core
                }
            }

            // If no process was assigned to the core, mark it as idle
            if (isCoreIdle) {
                DefaultTableModel ganttModel = (DefaultTableModel) ganttChartTables[core].getModel();
                String columnName = "Time " + elapsedTime;

                // Check if the column for this time already exists
                int columnIndex = ganttModel.findColumn(columnName);
                if (columnIndex == -1) {
                    // Add a new column for the current time
                    ganttModel.addColumn(columnName);
                    columnIndex = ganttModel.getColumnCount() - 1;
                }

                ganttModel.setValueAt("Idle", 0, columnIndex);
            }
        }
    }

    void removeTableContents(){
        model.setRowCount(0);
        populateTable();
    }
    void populateTable(){
        allGantts.sortByProcessID();
        allGantts.updateStatus(elapsedTime);
        allGantts.getExecutedProcess(elapsedTime).forEach(e-> e.setBurstTime(e.getBurstTime()-1));

        for (int i = 0; i < allGantts.getProcessessSize(); i++) {
            Object[] row = {
                    allGantts.getProcesses().get(i).getPid(),
                    allGantts.getProcesses().get(i).getPrioritySchedule(),
                    allGantts.getProcesses().get(i).getProcessPriority(),
                    allGantts.getProcesses().get(i).getBurstTime(),
                    allGantts.getProcesses().get(i).getArrivalTime(),
                    timeQuantumArray.get(i),
                    allGantts.getProcesses().get(i).getWaitingTime(),
                    allGantts.getProcesses().get(i).getTurnAroundTime(),
                    allGantts.getProcesses().get(i).getCoreIDAffinity(),
                    allGantts.getProcesses().get(i).getResponseTime(),
                    allGantts.getProcesses().get(i).getStatus(),
            };
            TableColumn statusColumn = computationTable.getColumnModel().getColumn(10);
            statusColumn.setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if ("Terminated".equals(value)) {
                        cell.setBackground(Color.red);
                        cell.setForeground(Color.WHITE);
                        cell.setFocusable(true);

                    } else if("Waiting".equals(value)) {
                        cell.setBackground(new Color(227, 218, 68));
                        cell.setForeground(Color.WHITE);

                    }else if("Executing".equals(value)) {
                        cell.setBackground(new Color(24, 252, 0));
                        cell.setForeground(new Color(255, 255, 255));
                        cell.setEnabled(true);

                    }else
                    {
                        cell.setBackground(new Color(101, 101, 101));
                        cell.setForeground(Color.WHITE);
                    }
                    return cell;
                }
            });
            model.addRow(row);

        }
        }
    }



//---------------------------------------------------------------------------------------------------------------------

/*
//----------------------------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------------------------*/