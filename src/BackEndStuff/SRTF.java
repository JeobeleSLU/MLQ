package BackEndStuff;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SRTF implements Sorter, ProcessInterface {
    private ArrayList<Process> readyQueue;
    private ArrayList<Process> processOnQueue;
    private GanttChart ganttChart;
    private int timer = 0;
    private ArrayList<Integer> startTime;
    private ArrayList<Integer> endTime;
    private Process lastProcess;
    private int runCounter = 0;
    private ArrayList<Process> processDone;


    public SRTF(ArrayList<Process> processes) {
        this.processOnQueue = new ArrayList<Process>(processes);
        this.readyQueue = new ArrayList<>();
        this.ganttChart = new GanttChart();
        startTime = new ArrayList<>();
        endTime = new ArrayList<>();
        this.processDone = new ArrayList<>();
    }

    @Override
    public ArrayList<Process> getProcesses() {
        return readyQueue;
    }

    @Override
    public void addToQueue(Process process) {
        this.readyQueue.add(process);
    }

    @Override
    public void removeFromQueue(Process process) {

    }

    @Override
    public int getQueueSize() {
        return readyQueue.size();
    }

    @Override
    public Process getNextProcess() {
        return readyQueue.isEmpty() ? null : readyQueue.get(0);
    }

    @Override
    public int getHighestPriority() {
        return readyQueue.stream()
                .mapToInt(Process::getPrioritySchedule)
                .max()
                .orElse(Integer.MIN_VALUE);
    }

    @Override
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
    public void addToqueue(ArrayList<Process> processes) {
        this.readyQueue.addAll(processes);
    }

    public ArrayList<Process> getGanttChartArray() {
        return ganttChart.getProcesses();
    }

    private boolean isPreempted(ArrayList<Process> processToQueue, int timer, int currentBurstTime) {
        for (Process process : processToQueue) {
            if (process.getArrivalTime() == timer && process.getBurstTime() < currentBurstTime) {
                return true;
            }
        }
        return false;
    }

    public GanttChart getGanttChart() {
        return this.ganttChart;
    }


    public SRTF() {
        this.readyQueue = new ArrayList<Process>();
        this.ganttChart = new GanttChart();
        this.processOnQueue = new ArrayList<Process>();
        this.processDone = new ArrayList<>();
        startTime = new ArrayList<>();
        endTime = new ArrayList<>();
    }

        /*
    Todo: Refactor this code so that you can call the run method to execute the
    process and minus the remaining burst time based on this algorithm
    */


    /*
    Psuedo Code:
    sort the ready queue on burst time
    add all the process on the Process on queue
    check if how many time did this class run
    if zero then getFirst on the process on queue
    set last id
    check if process remaining burst != 0
    if it is decrement remaining burst time
    check if remaining burst == 0
         add time ended on the process
    else
        this.lastProcessId = getFirst.getPID
   increment counter

    sort them by remaining burst time


     */
    public void run(int time) {

        this.timer = time;
        System.out.println("SRTF running");
        // sort all the burst time of the processes in the ready queue
        readyQueue = Sorter.sortByBurstTime(readyQueue); // i think this is not needed?
        processOnQueue.addAll(readyQueue); // add all the process inside the POQ
        readyQueue.removeAll(processOnQueue); // remove the process from the ready queue since
        System.out.println("SRTF readqueue remove");
        System.out.println("acckk");
        processOnQueue = Sorter.sortByRemainingBurstTime(processOnQueue);
        if (runCounter == 0) {// if its the first run set the last process id based on the first process on the process on queue
            System.out.println("First PID is :"+ processOnQueue.getFirst().getPid());
            this.lastProcess = processOnQueue.getFirst();
        }
        //check if the pid is same as the previous
        if (this.lastProcess != processOnQueue.getFirst()) {
            System.out.println("hii");
            setTimePreempted(timer); // add time ended to the last process since it was preempted
        }
        if (processOnQueue.getFirst().getBurstTime() != 0) {//ang gagi ayun nanaman problem
            processOnQueue.getFirst().decrementBurst();
            processOnQueue.getFirst().addTimeOnCore(timer);
            System.out.println("#2 Decremented Process: " + processOnQueue.getFirst().getPid() + "at time: " + time);
            System.out.println("Remaining time: " + processOnQueue.getFirst().getRemainingBurstTime());
        }
        if (processOnQueue.getFirst().getRemainingBurstTime() == 0) {
            processOnQueue.getFirst().setTimeEnd(time);
            processOnQueue.getFirst().addTimeEnded(time);
            processOnQueue.getFirst().updateTimes();
            System.out.println("Adding process on ");
            processDone.add(processOnQueue.getFirst());
            System.out.println("#2 Process Finished: " + processOnQueue.getFirst().getPid());
            processOnQueue.removeFirst(); //since it is finished, remove it on the queue
        }
        else {
            this.lastProcess = processOnQueue.getFirst();
        }
        runCounter ++;

    }
    private void setTimePreempted ( int timer){
        for (Process process : processOnQueue) { //search process on queue based on the last process id
            if (process == lastProcess) {
                System.out.println("SRTF Process :"+ process.getPid()+ "Is preempted");
                System.out.println("Process on Queue SRTF PID: "+ processOnQueue.getFirst());
                process.addTimeEnded(timer);

                processDone.add(process);
                break; // break since nahanap na
            }
        }
    }

    public boolean processDoneIsEmpty() {
        return processDone.isEmpty();
    }

    public ArrayList<Process> getProcessDone() {
        return processDone;
    }

    public void clearProcessDone() {
        processDone.clear();
    }
}



