package UI;


import BackEndStuff.Process;
import BackEndStuff.SchedulingAlgo;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
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

public class MyPanel extends JPanel implements ActionListener, Runnable {
    ArrayList<Process> processes ;

    final int fps = 60;
    Thread animationThread;
    int drawCount = 0;


    final int WIDTH = 2500;
    final int HEIGHT = 800;
    Image backgroundImage;
    Timer ballTimer;
    Timer labelTimer;
    boolean labelsAdded = false;

    JButton startButton;
    JButton stopButton;
    JButton aboutButton;

    public static final int BALL_SIZE = 25;
    JLabel timerLabel;
    int elapsedTime = 0; // time in seconds

    int xVelocity = 2;
    int yVelocity = 2;
    int ballX = 50; // Ball's initial x position
    int ballY = 50; // Ball's initial y position
    String procId;

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
    SchedulingAlgo schedulingAlgo;


    Process process;

    //----------------------------------------------------------------------------------------------------------------------
    public MyPanel() {

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(null);
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);
        keyH = new KeyHandler(this);
        this.addMouseListener(keyH);
        processes = new ArrayList<>();
        processes.add( new Process(1, 0, 10, 1));
        processes.add( new Process(3, 0, 10, 1));
        processes.add( new Process(4, 0, 10, 1));

        getResources();
        //----------------------------------------------------------------------------------------------------------------------
        ballTimer = new Timer(10, this);
        labelTimer = new Timer(1000, e -> updateTimer());
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
        schedulingAlgo = new SchedulingAlgo(processes,4 );// dummy try
        schedulingAlgo.setPanel(this);
        schedulingAlgo.run();

    }

    private void getResources() {
        try {
            bg= read((getClass().getResourceAsStream("/resources/Frame 1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //----------------------------------------------------------------------------------------------------------------------
    private void initializeTable() {
        JTable computationTable = new JTable();
        Object[] columns = {"PID", "Level P", "Priority", "Burst ", "Arrival ", "Time Quantum", "Waiting", "Turn", "Res", "Stat"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        computationTable.setModel(model);

        computationTable.setBackground(Color.lightGray);
        computationTable.setGridColor(Color.WHITE);
        computationTable.setForeground(Color.black);
        computationTable.setSelectionBackground(Color.black);
        computationTable.setGridColor(Color.black);
        computationTable.setRowHeight(30);
        computationTable.setAutoCreateRowSorter(true);

        for (int i = 0; i < processIDArray.size(); i++) {
            Object[] row = {
                    processIDArray.get(i),
                    processPriorityArray.get(i),
                    priorityArray.get(i),
                    burstTimeArray.get(i),
                    arrivalTimeArray.get(i),
                    timeQuantumArray.get(i),

            };
            model.addRow(row);
        }
        JScrollPane scrollPane = new JScrollPane(computationTable);
        scrollPane.setBounds(955, 68, 570, 200);
        this.add(scrollPane);
    }

    //----------------------------------------------------------------------------------------------------------------------
    public void updateTimer() {
        elapsedTime++;
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
        JLabel batchLabel = new JLabel("Batch Queueing (SJF)");
        batchLabel.setBounds(125, 548, 208, 12);
        this.add(batchLabel);
        JLabel core1Label = new JLabel("Core 1");
        core1Label.setBounds(700, 67, 66, 12);
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
    }

    //----------------------------------------------------------------------------------------------------------------------
    //components inside the draw method
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g;

        g2D.drawImage(bg, 0, 0, null);
        schedulingAlgo.draw(g2D);


        // Draw the labels
        if (!labelsAdded) {
            addLabels();
            labelsAdded = true;
        }

        drawBall1(g2D);
        ganttChart(g2D);
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
        while (animationThread!= null){

                System.out.println("Thread is Running");
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
                System.out.println(drawCount);
            }
        }
            timer ++;

    }
    public boolean isDoneAnimating = false;
    //---------------------------------------------------------------------------------------------------------------------
    @Override
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
}



//---------------------------------------------------------------------------------------------------------------------

/*
//----------------------------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------------------------*/