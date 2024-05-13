import java.util.ArrayList;

public class SRTF implements Sorter, Scheduler {
    private ArrayList<Process> readyQueue;
    private ArrayList<Process> processToQueue;
    private GanttChart ganttChart;
    private int timer = 0;


    public SRTF(ArrayList<Process> processes) {
        this.processToQueue = new ArrayList<>(processes);
        this.readyQueue = new ArrayList<>();
        this.ganttChart = new GanttChart();
    }

//    @Override
//    public int getCurrentTime() {
//        return this.timer;
//    }

    @Override
    public void run() {

        while (!processToQueue.isEmpty() || !readyQueue.isEmpty()) {
            // Add arriving processes to ready queue
            for (Process process : processToQueue) {
                if (process.getArrivalTime() == timer) {
                    readyQueue.add(process);
                }
            }
            processToQueue.removeAll(readyQueue);

            // Sort ready queue by burst time
            readyQueue = Sorter.sortByBurstTime(readyQueue);

            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.get(0);

                // Log start time if it's the first execution
                if (currentProcess.isFirstExecution()) {
                    currentProcess.addTimeStarted(timer);
                    currentProcess.setFirstExecution(false);
                }

                // Execute the current process
                System.out.println("SRTF: Executing process " + currentProcess.getPid() + " at time " + timer);
                currentProcess.updatePriority();
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);

                // Check for preemption
                if (!processToQueue.isEmpty()) {
                    for (Process process : processToQueue) {
                        if (process.getArrivalTime() == timer && process.getBurstTime() < currentProcess.getBurstTime()) {
                            // Preemption occurred
                            currentProcess.setFirstExecution(true);
                            readyQueue.set(0, currentProcess);
                            ganttChart.addProcess(currentProcess);
                            readyQueue.remove(currentProcess);
                            readyQueue.add(process);
                            break; // No need to continue checking
                        }
                    }
                }

                // Check if the process has completed execution
                if (currentProcess.getBurstTime() == 0) {
                    currentProcess.addTimeEnded(timer);
                    System.out.println("Process " + currentProcess.getPid() + " completed at time " + timer);
                    ganttChart.addProcess(currentProcess);
                    readyQueue.remove(currentProcess);
                    System.out.println(currentProcess.getTimesStarted() + " " +currentProcess.getTimesEnded());
                }
            } else {
                // Idling if no processes are ready
                System.out.println("Idling at time " + timer);
            }
            timer++;
        }
    }

    @Override
    public ArrayList<Process> getProcesses() {
        return readyQueue;
    }

    @Override
    public void addToQueue(Process process) {

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
        return processToQueue;
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
}
