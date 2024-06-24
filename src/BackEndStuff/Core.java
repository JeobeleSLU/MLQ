package BackEndStuff;

import javax.swing.*;
    import java.util.ArrayList;
import java.util.Formatter;

import UI.MyPanel;

    public class Core implements Sorter {

     RoundRobinScheduler roundRobinScheduler1;
    NPPS nonPreemptivePriorityScheduling4;
    SRTF shortestRemainingTimeFrist2;
    SJF shortestJobFirst3;
    static int numberOfCores;
    ArrayList<Process> processes;
    int timer;
    int coreID;
    int targetX;
    int targetY;
    MyPanel mp;
    GanttChart gantt;
    Process idle ;

        public Core(int i) {
        this.coreID = i;
        this.roundRobinScheduler1 = new RoundRobinScheduler();
        this.shortestJobFirst3 = new SJF();
        this.shortestRemainingTimeFrist2 = new SRTF();
        this.nonPreemptivePriorityScheduling4 = new NPPS();
        this.processes = new ArrayList<>();
        this.timer = 0;
        numberOfCores += 1;
        if (coreID == 0) {
            this.targetX = 739;
            this.targetY = 123;// set
        } else if (coreID == 1) {
            this.targetX = 740;
            this.targetY = 311;
        } else if (coreID == 2) {
            this.targetX = 740;
            this.targetY = 475;
        } else if (coreID == 3) {
            this.targetX = 740;
            this.targetY = 621;
        }
        gantt = new GanttChart();
       idle = new Process();
       idle.setPid(-1);
    }
    public Core(int i, MyPanel panel) {
     this.coreID = i;
     this.roundRobinScheduler1 = new RoundRobinScheduler();
     this.shortestJobFirst3 = new SJF();
     this.shortestRemainingTimeFrist2 = new SRTF();
     this.nonPreemptivePriorityScheduling4 = new NPPS();
     this.processes = new ArrayList<>();
     this.timer = 0;
     numberOfCores += 1;
     if (coreID == 0) {
         this.targetX = 739;
         this.targetY = 123;// set
     } else if (coreID == 1) {
         this.targetX = 740;
         this.targetY = 311;
     } else if (coreID == 2) {
         this.targetX = 740;
         this.targetY = 475;
     } else if (coreID == 3) {
         this.targetX = 740;
         this.targetY = 621;
     }

 }
public void runProcesses(int time){
    this.timer = time;
    System.out.println("RR boolean: "+ !roundRobinScheduler1.isEmpty());
    System.out.println("SRTF boolean: "+ !shortestRemainingTimeFrist2.isEmpty());
    System.out.println("SJF boolean: "+ !shortestJobFirst3.isEmpty());
    if (!roundRobinScheduler1.processDoneIsEmpty()){
        System.out.println("Adding proess to gantt RR PID:"+ roundRobinScheduler1.getProcessDone().getFirst().getPid());
        gantt.addProcess(roundRobinScheduler1.getProcessDone().getFirst());
        roundRobinScheduler1.clearProcessDone();
    }

    if (!nonPreemptivePriorityScheduling4.processDoneIsEmpty()){
        System.out.println("Adding proess to gantt NPPS PID: "+ nonPreemptivePriorityScheduling4.getProcessDone().getFirst().getPid());
        gantt.addProcess(nonPreemptivePriorityScheduling4.getProcessDone()); // get the process done when
        nonPreemptivePriorityScheduling4.clearProcessDone();
    }
    if (!shortestJobFirst3.processDoneIsEmpty()){
        System.out.println("Adding proess to gantt SJF PID:"+ shortestJobFirst3.getProcessDone().getFirst().getPid());
        gantt.addProcess(shortestJobFirst3.getProcessDone());
        shortestJobFirst3.clearProcessDone();
    }
    if (!shortestRemainingTimeFrist2.processDoneIsEmpty()){
        System.out.println("Adding proess to gantt SRTF PID:"+ shortestRemainingTimeFrist2.getProcessDone().getFirst().getPid());
        gantt.addProcess(shortestRemainingTimeFrist2.getProcessDone());// get the process done when
        shortestRemainingTimeFrist2.clearProcessDone();
    }
    if (!isEmpty()){
        idle.addTimeEnded(time);
    }
    if (!roundRobinScheduler1.isEmpty()){
        roundRobinScheduler1.run(timer);
        System.out.println("RR is executing");
    }
    else if (!shortestJobFirst3.isEmpty()) {
        shortestJobFirst3.run(timer);
        System.out.println("SJF is executing");

    }
    else if (!nonPreemptivePriorityScheduling4.isEmpty()) {
        nonPreemptivePriorityScheduling4.run(timer);
        System.out.println("NPPS is executing");

    }
    else if (!shortestRemainingTimeFrist2.isEmpty()) {
        System.out.println("SRTF process to Run: "+ shortestRemainingTimeFrist2.getNumberOfProcesses());
        shortestRemainingTimeFrist2.run(timer);
        System.out.println("SRTF is Executing");
    }
    else {
        //todo:create an idle timer logic here
        idle.addTimeStarted(timer);
        System.out.println("Core:"+this.coreID+" is idling");
        System.out.println("No process");
    }

        /*
        Todo: Add Gantt Chart Logic to add process here
         may be it can use the ganttChart from each algorithm?
         */
    }
        void addProcess (ArrayList<Process> process){
            process.addAll(this.processes);
        }
        void addProcess(Process process){
            this.processes.add(process);
        }
        int getNumberOfRoundRobinProcess(){
            return this.roundRobinScheduler1.getNumberOfProcesses();

        }
        int getNumberOfNPPSProcess(){
            return this.nonPreemptivePriorityScheduling4.getNumberOfProcesses();

        }
        int getNumberOfSRTFProcess(){
            return this.shortestRemainingTimeFrist2.getNumberOfProcesses();

        }
        int getNumberOfSJFProcess(){
            return this.shortestJobFirst3.getNumberOfProcesses();
        }
        //this may be used for multi something
        int getNumberOfCores(){
            return numberOfCores;
        }
        int getNumberOfProcesses(){
            System.out.println("\nRoundRobin Process:"+ getNumberOfRoundRobinProcess());
            System.out.println("SRTF Process:" + getNumberOfSRTFProcess());
            System.out.println("SJF Process:"+ getNumberOfSJFProcess());
            System.out.println("NPPS Process:"+ getNumberOfNPPSProcess()+"\n"+"at Core: "+this.coreID);
            return getNumberOfRoundRobinProcess()+
                    getNumberOfSRTFProcess()+
                    getNumberOfSJFProcess()+
                    getNumberOfNPPSProcess();
        }
        void addToRoundRobinScheduler (Process processToAdd){
           this.roundRobinScheduler1.addToQueue(processToAdd);

        }
        void addToSRTFScheduler (Process processToAdd){
            this.shortestRemainingTimeFrist2.addToQueue(processToAdd);
        }
        void addToSJFScheduler (Process processToAdd){
            this.shortestJobFirst3.addToQueue(processToAdd);
        }
        void addToNPPSScheduler (Process processToAdd){
            this.nonPreemptivePriorityScheduling4.addToQueue(processToAdd);
        }
        void addIdleToGantt(){
            processes.add(idle);
        }

        public int getCoreID() {
            return coreID;
        }

        public void addLastToRoundRobinScheduler(Process process) {
            this.roundRobinScheduler1.readyQueue.addFirst(process);
        }

        public void setTimer(int timer) {
            this.timer = timer;
        }

        public ArrayList getSJFDoneList(){
            return this.shortestJobFirst3.getProcessDone();
        }
        public boolean isEmpty(){
            return
                    this.roundRobinScheduler1.isEmpty() &&
                            this.shortestRemainingTimeFrist2.isEmpty() &&
                            this.shortestJobFirst3.isEmpty() &&
                            this.nonPreemptivePriorityScheduling4.isEmpty();


        }


        public void setPanel(JPanel panel) {
        }

        public ArrayList<Process> getRoundRobinQueue() {
            return this.roundRobinScheduler1.getProcesses();
        }
        public ArrayList<Process> getSRTFQueue() {
            return this.shortestRemainingTimeFrist2.getProcesses();
        }
        public ArrayList<Process> getSJFPRocess() {
            return this.shortestJobFirst3.getProcesses();
        }
        public ArrayList<Process> getNPPS() {
            return this.nonPreemptivePriorityScheduling4.getProcesses();
        }

        public GanttChart getGantt() {
        return gantt;
        }

        public void getIdle() {
            this.gantt.addProcess(idle);
        }
    }
