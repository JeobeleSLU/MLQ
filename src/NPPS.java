import java.util.ArrayList;
import java.util.Comparator;

public class NPPS implements ProcessInterface, Sorter {
    private ArrayList<Process> readyQueue;
    private ArrayList<Process> processToQueue;
    private GanttChart ganttChart;
    /*
    Todo: Refactor this code so that you can call the run method to execute the
    process and minus the remaining burst time based on this algorithm
    remove the loop
    */
    public NPPS(ArrayList<Process> processes) {
        this.readyQueue = new ArrayList<>();
        this.processToQueue = new ArrayList<>();
        this.processToQueue.addAll(processes);
        ganttChart = new GanttChart();
    }

    public NPPS() {
        this.readyQueue = new ArrayList<>();
        this.processToQueue = new ArrayList<>();
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
    public boolean isEmpty() {
        return this.readyQueue.isEmpty();
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

            // Add arriving processes to ready queue
            int finalTimer = timer;
            if (!readyQueue.isEmpty()) {
                readyQueue.sort(Comparator.comparingInt(Process::getProcessPriority)); // Sort by priority
                Process currentProcess = readyQueue.get(0);
                if (!currentProcess.getHasExecuted()) {
                    currentProcess.setTimeStarted(timer);
                    currentProcess.addTimeStarted(timer);
                    currentProcess.setTimeNow(timer);
                    ganttChart.addProcess(currentProcess);
                }
                System.out.println("Non-Preemptive Priority: Executing process " + currentProcess.getPid() + " at time " + timer);
                currentProcess.addTimeStarted(timer);

                // Simulate process execution by decrementing burst time
                while (currentProcess.getBurstTime() > 0) {
                    currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                    timer++; // Increment the timer for each time unit processed
                }

                System.out.println("Process " + currentProcess.getPid() + " completed at time " + timer);
                currentProcess.addTimeEnded(timer);
                currentProcess.setTimeEnd(timer);
                readyQueue.remove(currentProcess);
                //todo: Maybe remove this idling? since the core itself will handle the idle
            } else {//
                System.out.println("NPPS: Idling at time " + timer);
                timer++;   // Increment timer even if idling
            }
        }


    public ArrayList<Process> getGanttChartArray() {
        return this.ganttChart.getProcesses();
    }



}
