import java.util.ArrayList;
import java.util.Comparator;

public class NPPS implements Runnable,Sorter, Scheduler {
    private ArrayList<Process> readyQueue = new ArrayList<>();
    private ArrayList<Process> processToQueue = new ArrayList<>();
    private GanttChart ganttChart;
    int timer = 1;

    public NPPS(ArrayList<Process> processes) {
        this.processToQueue.addAll(processes);
        ganttChart = new GanttChart();
    }

    public void addToQueue(Process process) {
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
        return processToQueue;
    }



    @Override
    public void run() {
        int timer = 1;

        while (!processToQueue.isEmpty() || !readyQueue.isEmpty()) {
            // Add arriving processes to ready queue
            int finalTimer = timer;
            readyQueue.addAll(processToQueue.stream()
                    .filter(process -> process.getArrivalTime() == finalTimer)
                    .toList());
            processToQueue.removeAll(readyQueue);

            if (!readyQueue.isEmpty()) {
                readyQueue.sort(Comparator.comparingInt(Process::getProcessPriority)); // Sort by priority
                Process currentProcess = readyQueue.get(0);
                if (!currentProcess.getHasExecuted()) {
                    currentProcess.setTimeStarted(timer);
                    ganttChart.addProcess(currentProcess);
                }
                System.out.println("Non-Preemptive Priority: Executing process " + currentProcess.getPid() + " at time " + timer);

                while (currentProcess.getBurstTime() > 0) {
                    currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                }
                System.out.println("Process " + currentProcess.getPid() + " completed at time " + timer);
                readyQueue.remove(currentProcess);
            } else {
                System.out.println("NPPS"+"Idling at time " + timer);
            }
            timer++; // Increment the timer outside the if-else block
        }
    }

    public ArrayList<Process> getGanttChartArray() {
        return this.ganttChart.getProcesses();
    }
    public void runProcessess(Process highestPriorityProcess) {


        while (!processToQueue.isEmpty() || !readyQueue.isEmpty()) {
            // Add arriving processes to ready queue
            int finalTimer = timer;
            readyQueue.addAll(processToQueue.stream()
                    .filter(process -> process.getArrivalTime() == finalTimer)
                    .toList());
            processToQueue.removeAll(readyQueue);

            if (!readyQueue.isEmpty()) {
                readyQueue.sort(Comparator.comparingInt(Process::getProcessPriority)); // Sort by priority
                Process currentProcess = readyQueue.get(0);
                if (!currentProcess.getHasExecuted()) {
                    currentProcess.setTimeStarted(timer);
                }
                System.out.println("Non-Preemptive Priority: Executing process " + currentProcess.getPid() + " at time " + timer);

                while (currentProcess.getBurstTime() > 0) {
                    currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                }
                System.out.println("Process " + currentProcess.getPid() + " completed at time " + timer);
                readyQueue.remove(currentProcess);
            } else {
                System.out.println("NPPS: Idling at time " + timer);
            }
            timer++; // Increment the timer outside the if-else block
        }
    }
    public int getCurrentTime(){
       return this.timer;
    }



}
