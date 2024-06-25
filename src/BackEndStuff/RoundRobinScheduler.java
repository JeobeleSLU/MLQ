package BackEndStuff;

import java.util.ArrayList;

public class RoundRobinScheduler implements ProcessInterface {
    private  int quantumTime;
    ArrayList<Process> readyQueue ;
    ArrayList<Process> processOnQueue;
    private int quantumTimer;
    public int time = 0;
    private ArrayList<Process> processDone;

    public RoundRobinScheduler(ArrayList<Process> processes) {
        this.readyQueue = new ArrayList<>();
        this.processOnQueue = new ArrayList<>();
        this.processOnQueue.addAll(processes);
        ganttChart = new GanttChart();
        this.quantumTime = 3;
        this.processDone = new ArrayList<>();

        /*
    Todo: Refactor this code so that you can call the run method to execute the
        process and minus the remaining burst time based on this algorithm
        */

    }

    public RoundRobinScheduler() {
        this.readyQueue = new ArrayList<Process>();
        this.processOnQueue = new ArrayList<Process>();
        this.quantumTime = 3;
        this.quantumTimer = this .quantumTime;
        this.processDone = new ArrayList<>();
    }
    public RoundRobinScheduler(int time) {
        this.readyQueue = new ArrayList<>();
        this.processOnQueue = new ArrayList<>();
        this.quantumTime = time;
        this.quantumTimer = this.quantumTime;
    }

    public void addToQueue(Process process) {
        this.readyQueue.add(process);
    }

    public void setQuantumTime(int quantumTime) {
        this.quantumTime = quantumTime;
        this.quantumTimer = quantumTime;
    }

    public void removeFromQueue(Process process) {
        readyQueue.remove(process);
    }

    @Override
    public int getQueueSize() {
        return readyQueue.size();

    }

    @Override
    public Process getNextProcess() {
        if (!readyQueue.isEmpty()) {
            return readyQueue.get(0); // Return the process with the shortest burst time
        } else {
            return null; // Queue is empty
        }
    }

    @Override
    public ArrayList<Process> getProcesses() {
        return readyQueue;
    }
    public ArrayList<Process> processessToQueue() {
        return processOnQueue;
    }

    @Override
    public boolean isEmpty() {
        return this.readyQueue.isEmpty() && this.processOnQueue.isEmpty();
    }

    @Override
    public int getNumberOfProcesses() {
        return this.readyQueue.size();
    }

    @Override
    public int getHighestPriority() {
        return readyQueue.stream()
                .mapToInt(Process::getPrioritySchedule)
                .max()
                .orElse(Integer.MIN_VALUE); // Return Integer.MIN_VALUE if the queue is empty
    }
    private GanttChart ganttChart;


    public void run(int timer) {
        this.time = timer;
        int quantumCounter = 0;
        //loop through the arrival then add it to the process on queue
        System.out.println("\n#1 ready Queue: " + readyQueue.size());
        if (processOnQueue.isEmpty()) {
            processOnQueue.add(readyQueue.getFirst());
            readyQueue.remove(processOnQueue.get(0));
            processOnQueue.getFirst().addTimeStarted(timer);
        }

        //if quantum counter is still on going then execute task
        if (quantumTimer > 0) {
            //check for burst Time
//            if (processOnQueue.getFirst().getBurstTime() == processOnQueue.getFirst().getRemainingBurstTime()) {
//                processOnQueue.getFirst().setTimeStarted(timer);
//            }
            if (processOnQueue.getFirst().getBurstTime() != 0) {
                processOnQueue.getFirst().decrementBurst();
                processOnQueue.getFirst().addTimeOnCore(timer);
                System.out.println("\n#1 RR Decremented Process:  " + this.processOnQueue.getFirst().getPid());
                System.out.println("#1 Remaining burst time: " + this.processOnQueue.get(0).getRemainingBurstTime());

                quantumTimer--;
                System.out.println("Quantum Time: " + quantumTimer);
            }
            if (processOnQueue.getFirst().getRemainingBurstTime() == 0) {
                System.out.println("#1 Process: " + processOnQueue.getFirst().getPid() + " is Done");
                processDone.add(processOnQueue.getFirst());
                processOnQueue.getFirst().setTimeEnd(timer + 1);
                processOnQueue.getFirst().addTimeEnded(timer + 1);
                System.out.println("TT: "+ processOnQueue.getFirst().getTurnAroundTime());
                processOnQueue.remove(processOnQueue.getFirst());
                quantumTimer = quantumTime;
            }

        } else {
            System.out.println("Q time stopped");
            readyQueue.addLast(processOnQueue.getFirst());// add it to the tail of the ready Queue
            processOnQueue.getFirst().addTimeEnded(timer);
            processDone.add(processOnQueue.getFirst());
            processOnQueue.clear();
            quantumTimer = quantumTime;
            run(timer); // call the method again since the run, ran because it checks if the qT is > 0 and will skip process if not done?
        }
    }
    public boolean processDoneIsEmpty(){
        return this.processDone.isEmpty();
    }

    public ArrayList<Process> getProcessDone() {
        return processDone;
    }

    public void addToqueue(ArrayList <Process> processes){
        this.readyQueue = processes;
    }
    public ArrayList<Process> getGanttChartArray() {
       return ganttChart.getProcesses();
    }
    public void clearProcessDone(){
        processDone.clear();
    }
}
