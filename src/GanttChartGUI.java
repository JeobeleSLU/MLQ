import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GanttChartGUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel avgTurnAroundTimeLabel;
    private JLabel avgWaitingTimeLabel;

    public GanttChartGUI() {
        frame = new JFrame("Gantt Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel();
        tableModel.addColumn("PID");
        tableModel.addColumn("Start Times");
        tableModel.addColumn("End Times");
        tableModel.addColumn("Response Time");
        tableModel.addColumn("Arrival");
        tableModel.addColumn("Priority");
        tableModel.addColumn("Turn Around Time");
        tableModel.addColumn("Waiting time");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel setup for averages
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 2));

        avgTurnAroundTimeLabel = new JLabel("Average Turnaround Time: ");
        avgWaitingTimeLabel = new JLabel("Average Waiting Time: ");

        bottomPanel.add(avgTurnAroundTimeLabel);
        bottomPanel.add(avgWaitingTimeLabel);

        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void addProcess(Process process) {
        String startTimes = String.valueOf(process.getTimeStarted());
        String endTimes = String.valueOf(process.getTimeEnd());
        String responseTime = String.valueOf(process.getResponseTime());
        String arrival = String.valueOf(process.getArrivalTime());
        String priority = String.valueOf(process.getPrioritySchedule());
        String turn = String.valueOf(process.getTurnAroundTime());
        String waiting = String.valueOf(process.getWaitingTime());

        tableModel.addRow(new Object[]{process.getPid(), startTimes, endTimes, responseTime, arrival,priority,turn,waiting});
    }

    public void display() {
        frame.setVisible(true);
    }

    public void updateAverages(ArrayList<Process> processes) {
        double totalTurnAroundTime = 0;
        double totalWaitingTime = 0;
        int count = processes.size();



        avgTurnAroundTimeLabel.setText("Average Response Time ");
//        avgWaitingTimeLabel.setText("Average Waiting Time: " + avgWaitingTime);
    }

    public static void main(String[] args) {
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 5,2)); // Process with priority 1, arrival time 0, and burst time 5
        processes.add(new Process(2, 1, 3,1)); // Process with priority 2, arrival time 1, and burst time 3
        processes.add(new Process(3, 2, 4,3)); // Process with priority 3, arrival time 2, and burst time 4
        processes.add(new Process(6, 2, 4,4,3));
        processes.add(new Process(9, 2, 4,4,1)); // Process with priority 3, arrival time 2, and burst time 4
        // Process with priority 3, arrival time 2, and burst time 4
        processes.add(new Process(5, 2, 4,1)); // Process with priority 3, arrival time 2, and burst time 4
        processes.add(new Process(4, 2, 4,1)); //

        ArrayList<Process> rrArray = Sorter.sortByPriority(processes, 1);
        ArrayList<Process> srtfArray = processes.stream()
                .filter(p -> p.getPrioritySchedule() == 2)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Process> sjfArray = processes.stream()
                .filter(p -> p.getPrioritySchedule() == 3)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Process> nppsArray = Sorter.sortByPriority(processes, 4);
        RoundRobinScheduler rr = new RoundRobinScheduler(rrArray);
        SJF shortest = new SJF(sjfArray);
        SRTF shortesremain = new SRTF(srtfArray);
        NPPS npps = new NPPS(nppsArray);

        Thread thread1 = new Thread(rr);
        Thread thread2 = new Thread(shortesremain);
        Thread thread3 = new Thread(shortest);
        Thread thread4 = new Thread(npps);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GanttChart gantt = new GanttChart();
        gantt.addProcess(rr.getGanttChartArray());
        gantt.addProcess(shortesremain.getGanttChartArray());
        gantt.addProcess(shortest.getGanttChartArray());
        gantt.addProcess(npps.getGanttChartArray());
        gantt.updateAllValues();


        ArrayList<Process> nppsGantt = npps.getGanttChartArray();
        System.out.println(gantt.getAverage());

        GanttChartGUI gui = new GanttChartGUI();
        gantt.getProcesses().forEach(gui::addProcess);
        gui.display();

    }
}
