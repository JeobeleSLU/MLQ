package BackEndStuff;

import java.util.ArrayList;
import java.util.Comparator;

public class NPPS implements ProcessInterface, Sorter {
    private ArrayList<Process> readyQueue;
    private ArrayList<Process> processOnQueue;
    private GanttChart ganttChart;
    private int timer = 0;
    /*
    Todo: Refactor this code so that you can call the run method to execute the
    process and minus the remaining burst time based on this algorithm
    remove the loop
    */
    public NPPS(ArrayList<Process> processes) {
        this.readyQueue = new ArrayList<>();
        this.processOnQueue = new ArrayList<>();
        this.processOnQueue.addAll(processes);
        ganttChart = new GanttChart();
    }

    public NPPS() {
        this.readyQueue = new ArrayList<>();
        this.processOnQueue = new ArrayList<>();
        ganttChart = new GanttChart();
        this.timer = 0;

    }


    public void addToQueue(Process process) {
        System.out.println("NPPS added");
        readyQueue.add(process);
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
            return readyQueue.get(0); // Return the process with the highest priority
        } else {
            return null; // Queue is empty
        }
    }

    @Override
    public ArrayList<Process> getProcesses() {
        return readyQueue;
    }

    @Override
    public int getHighestPriority() {
        return readyQueue.stream()
                .mapToInt(Process::getProcessPriority)
                .max()
                .orElse(Integer.MIN_VALUE); // Return Integer.MIN_VALUE if the queue is empty
    }

    @Override
    public ArrayList<Process> processessToQueue() {
        return processOnQueue;
    }

    @Override
    public boolean isEmpty() {
        System.out.println("NPPS boolean:"+processOnQueue.isEmpty());
        System.out.println(processOnQueue.size());
        return this.readyQueue.isEmpty() && processOnQueue.isEmpty();
    }

    @Override
    public int getNumberOfProcesses() {
        return this.readyQueue.size();
    }

    @Override
    public void addToqueue(ArrayList<Process> processes) {
        this.readyQueue.addAll(processes);
    }

    public void run(int timer) {

        this.timer = timer;
                readyQueue.sort(Comparator.comparingInt(Process::getProcessPriority)); // Sort by priority
                if (processOnQueue.isEmpty()){
                    processOnQueue.add(readyQueue.getFirst());
                    System.out.println("NPPS added to On Queue");
                    readyQueue.removeFirst();
                    processOnQueue.getFirst().setTimeStarted(this.timer);
                }
                if (processOnQueue.getFirst().getBurstTime() !=0){
                    System.out.println("yawa");
                    System.out.println("#4 NPPS Executing process " + processOnQueue.getFirst().getPid() + " at time " + timer);
                    processOnQueue.getFirst().decrementBurst();
                    System.out.println("Remaining BurstTime: "+ processOnQueue.getFirst().getRemainingBurstTime());
                }
                if (processOnQueue.getFirst().getRemainingBurstTime() == 0){
                    System.out.println("Process " + processOnQueue.getFirst().getPid() + " completed at time " +timer);
                    System.out.println("Done, Removing , NPPS ...." + processOnQueue.getFirst().getPid());
                    processOnQueue.getFirst().setTimeEnd(timer);
                    processOnQueue.clear();
                }

                //todo: Maybe remove this idling? since the core itself will handle the idle

        }


    public ArrayList<Process> getGanttChartArray() {
        return this.ganttChart.getProcesses();
    }
}

//    Process currentProcess = readyQueue.get(0);
//                if (!currentProcess.getHasExecuted()) {
//        currentProcess.setTimeStarted(timer);
//        currentProcess.addTimeStarted(timer);
//        currentProcess.setTimeNow(timer);
//        ganttChart.addProcess(currentProcess);
//    }
//                System.out.println("Non-Preemptive Priority: Executing process " + currentProcess.getPid() + " at time " + timer);
//                currentProcess.addTimeStarted(timer);
//
//    // Simulate process execution by decrementing burst time
//                while (currentProcess.getBurstTime() > 0) {
//        currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
//        timer++; // Increment the timer for each time unit processed
//    }
//
//                System.out.println("Process " + currentProcess.getPid() + " completed at time " + timer);
//                currentProcess.addTimeEnded(timer);
//                currentProcess.setTimeEnd(timer);
//                readyQueue.remove(currentProcess);
//


