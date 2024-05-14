import java.util.ArrayList;
import java.util.Comparator;

public class SJF implements  Sorter,Scheduler {
    private ArrayList<Process> readyQueue = new ArrayList<>();
    private ArrayList<Process> processToQueue = new ArrayList<>();
    private GanttChart ganttChart;
    private int timer = 0;

    public SJF(ArrayList<Process> processes) {
        this.processToQueue.addAll(processes);
        ganttChart = new GanttChart();

    }

    public int getCurrentTime() {
        return this.timer;
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
            return readyQueue.get(0); // Return the process with the shortest burst time
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
                .mapToInt(Process::getPrioritySchedule)
                .max()
                .orElse(Integer.MIN_VALUE); // Return Integer.MIN_VALUE if the queue is empty
    }



    @Override
    public void run() {

        while (!processToQueue.isEmpty() || !readyQueue.isEmpty()) {
            // Add arriving processes to ready queue
            int finalTimer = timer;
            readyQueue.addAll(processToQueue.stream()
                    .filter(process -> process.getArrivalTime() == finalTimer)
                    .toList());
            processToQueue.removeAll(readyQueue);

            if (!readyQueue.isEmpty()) {
                readyQueue.sort(Comparator.comparingInt(Process::getBurstTime)); // Sort by burst time
                Process currentProcess = readyQueue.get(0);

                System.out.println("SJF"+"Executing process " + currentProcess.getPid() + " at time " + timer);
                currentProcess.setTimeStarted(timer);
                currentProcess.setTimeNow(timer);

                while (currentProcess.getBurstTime() > 0) {
                    currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                }
                System.out.println("Process " + currentProcess.getPid() + " completed at time " + timer);
                currentProcess.setTimeEnd(timer);
                ganttChart.addProcess(currentProcess);
                readyQueue.remove(currentProcess);
            } else {
                System.out.println("Idling at time " + timer);
            }
            timer++; // Increment the timer outside the if-else block
        }
    }

    public ArrayList<Process> getGanttChartArray() {
        return this.ganttChart.getProcesses();
    }
    public ArrayList<Process> processessToQueue() {
        return processToQueue;
    }


}
