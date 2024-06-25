package BackEndStuff;

import UI.GanttTable;
import UI.MyPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class SchedulingAlgo implements Sorter {
    private int timer;
    private ArrayList<Process> processToQueueList;
    private ArrayList<Process> processOnQueueList;
    private ArrayList<Process> processDoneList;
    private ArrayList<Core> arrayListOfCores;
    private ArrayList<Process> processDone ;
    public ArrayList<Process> rr;
    public ArrayList<Process> srtf;
    public ArrayList<Process> sjf;
    public ArrayList<Process> npps;
    public static ArrayList<GanttChart> gantts;

    private Process processToDraw;

    public MyPanel panel;

    //input the coordinates of eahc process
    public int rrQueueX = 161;
    public int rrQueueEndX = 387;
    public int rrQueueY = 88;
    public int rrQueueEndY = 183;
    public int SRTFQueueX;
    public int SRTFQueueY;
    public int SJFQueueX;
    public int SJFQueueY;
    public int NPPSQueueX;
    public int NPPSueueY;


    // Constructor to initialize the number of cores
    SchedulingAlgo(int numberOfCores){
        this.processDoneList = new ArrayList<>();
        this.processOnQueueList = new ArrayList<>();
        this.processToQueueList = new ArrayList<>();
        this.arrayListOfCores = new ArrayList<>();

        for (int i = 0; i < numberOfCores; i++){
            arrayListOfCores.add(new Core(i));
        }
        this.timer = 0;
//       for (int i = 0; i < numberOfCores; i++){
//           gantts.add(new GanttChart());
//       }
    }

    public SchedulingAlgo(ArrayList<Process> processes, int numberOfCores) {
        this.processDoneList = new ArrayList<>();
        this.processOnQueueList = new ArrayList<>();
        this.processToQueueList = processes;
        this.arrayListOfCores = new ArrayList<>();
        this.processDoneList = new ArrayList<>();

        for (int i = 0; i < numberOfCores; i++){
            arrayListOfCores.add(new Core(i));
        }
        gantts = new ArrayList<>();
        for (int i = 0; i < numberOfCores; i++){
            gantts.add(new GanttChart());
        }

        this.timer = 0;
        rr = new ArrayList<>();
        srtf = new ArrayList<>();
        npps = new ArrayList<>();
        sjf = new ArrayList<>();

    }

    // Main run method for scheduling processes
    public void run() {

        while ( !processToQueueList.isEmpty() || checkCoresWithProcess()){
            System.out.println(checkCoresWithProcess());
            processOnQueueList.addAll(Sorter.getArrivedProcess(processToQueueList, timer));
            assignProcess();
            processToQueueList.removeAll(processOnQueueList);
            System.out.println("Number of Process On Queue: "+ this.processOnQueueList.size());
            System.out.println("Number of Process To Queue: "+ this.processToQueueList.size());

            for (Core core : arrayListOfCores) {
                core.setTimer(this.timer);
                core.runProcesses(timer);
            }

            System.out.println("\nEnd of Cycle: "+timer);
            this.timer++;


        }
        for (Core arrayListOfCore : arrayListOfCores) {
            System.out.println("adding Ganttchart of: "+ arrayListOfCore.getCoreID());
            arrayListOfCore.gatherProcessess();
            arrayListOfCore.getIdle();
            arrayListOfCore.gantt.updateAllValues();
            gantts.add(arrayListOfCore.getGantt());
            arrayListOfCore.getGantt().displayChart();
        }

    }

    private boolean checkCoresWithProcess() {
        arrayListOfCores.forEach(e-> System.out.println("Process at Core "+e.getCoreID() + "\nNumber of Process"+e.getNumberOfProcesses()));
        for (Core core : arrayListOfCores) {
            if (!core.isEmpty()){
                return true;
            }
        }
        return false;
    }

    public int getTimer() {
        return timer;
    }

    public ArrayList<Process> getProcessDoneList() {
        return processDoneList;
    }

    public ArrayList<Process> getProcessOnQueueList() {
        return processOnQueueList;
    }

    public ArrayList<Process> getProcessToQueueList() {
        return processToQueueList;
    }

    private void sortCoreToLeastProcess(int prio) {
       if (prio == 1){//change since mali ata order ko
           arrayListOfCores = (ArrayList<Core>) arrayListOfCores.stream()
                   .sorted(Comparator.comparingInt(Core::getNumberOfRoundRobinProcess))
             .collect(Collectors.toList());
       }else if (prio == 2) {
           arrayListOfCores = (ArrayList<Core>) arrayListOfCores.stream()
                   .sorted(Comparator.comparingInt(Core::getNumberOfSJFProcess))
                   .collect(Collectors.toList());
       } else if (prio == 3) {
           arrayListOfCores = (ArrayList<Core>) arrayListOfCores.stream()
                   .sorted(Comparator.comparing(Core::getNumberOfNPPSProcess))
                   .collect(Collectors.toList());
       } else if (prio == 4 ) {
           arrayListOfCores = (ArrayList<Core>) arrayListOfCores.stream()
                   .sorted(Comparator.comparingInt(Core::getNumberOfSRTFProcess))
                   .collect(Collectors.toList());
       }

    }

    void assignProcess() {
        ArrayList<Process> arrived = Sorter.getArrivedProcess(processToQueueList, timer);

        ArrayList<Process> rr = Sorter.filterPriority(arrived, 1);
        rr.forEach(e-> System.out.println("Burst time before assigning: "+ e.getBurstTime()));
        ArrayList<Process> srtf = Sorter.filterPriority(arrived, 4);
        ArrayList<Process> sjf = Sorter.filterPriority(arrived, 2);
        ArrayList<Process> npps = Sorter.filterPriority(arrived, 3);

        for (Process process : rr) {
            sortCoreToLeastProcess(1);
            System.out.println("Assigning RR: "+ arrayListOfCores.getFirst().getCoreID());
            System.out.println("Remaining Burst Time: "+ process.getBurstTime());
            arrayListOfCores.get(0).addLastToRoundRobinScheduler(process);
            process.setCoreIDAffinity(arrayListOfCores.getFirst().getCoreID());
//            drawRR(Graphics2D g2);

        }
        for (Process process : srtf) {
            setProcessToDraw(process);
            sortCoreToLeastProcess(4);
            System.out.println("Assigning SRTF: "+ arrayListOfCores.getFirst().getCoreID());
            arrayListOfCores.get(0).addToSRTFScheduler(process);
            process.setCoreIDAffinity(arrayListOfCores.getFirst().getCoreID());
        }
        for (Process process : sjf) {
            sortCoreToLeastProcess(2);
            System.out.println("Assigning SJF\n"+"Process ID: "+process.getPid()+
                    "\nAssigning  to core: " +arrayListOfCores.getFirst().getCoreID());
            arrayListOfCores.get(0).addToSJFScheduler(process);
            process.setCoreIDAffinity(arrayListOfCores.getFirst().getCoreID());

//            drawSJF();
        }
        for (Process process : npps) {
            sortCoreToLeastProcess(3);
            System.out.println("Assigning NPPS Core :"+ arrayListOfCores.getFirst().getCoreID());
            arrayListOfCores.get(0).addToNPPSScheduler(process);

            process.setCoreIDAffinity(arrayListOfCores.getFirst().getCoreID());
//            drawNPPS();
        }
        arrived.forEach(e-> this.processToQueueList.remove(e));
    }

    private void setProcessToDraw(Process process) {
        this.processToDraw = process;
    }


    public void setPanel(MyPanel panel) {
        this.panel = panel;
    }

    public void draw(Graphics2D g2D) {
        for (Core arrayListOfCore : arrayListOfCores) {
            rr.addAll(arrayListOfCore.getRoundRobinQueue());
            srtf.addAll(arrayListOfCore.getSRTFQueue());
            sjf.addAll(arrayListOfCore.getSJFPRocess());
            npps.addAll(arrayListOfCore.getNPPS());
        }
        for (Process process : rr) {
            //draw rr ball
            g2D.fillOval(rrQueueX, rrQueueY, MyPanel.BALL_SIZE, MyPanel.BALL_SIZE);
            String pid = "P "+ process.getPid();

            // Set the font and color for the text
            g2D.setFont(new Font("Arial", Font.BOLD, 12));
            g2D.setColor(Color.white);

            FontMetrics fm = g2D.getFontMetrics();
            int textWidth = fm.stringWidth(pid);
            int textX = rrQueueX + (MyPanel.BALL_SIZE - textWidth) / 2;
            int textY = rrQueueY + (MyPanel.BALL_SIZE + fm.getAscent()) / 2 - fm.getDescent();
            rrQueueX += MyPanel.BALL_SIZE;
            if (rrQueueX >= rrQueueEndX){
                rrQueueX = 161;
                rrQueueY += 10;
            }
            g2D.drawString(pid, textX, textY);
        }
    }
}


        /* everytime this is called it should adjust the ball positions
        that it has occupied to not stack the balls together
         */
