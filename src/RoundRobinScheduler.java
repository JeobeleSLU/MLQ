import java.util.ArrayList;

public class RoundRobinScheduler implements Scheduler {
    private final int quantumTime = 3;
    ArrayList<Process> readyQueue = new ArrayList<>();
    ArrayList<Process> processToQueue = new ArrayList<>();

    public RoundRobinScheduler(ArrayList<Process> processes) {
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
        return processToQueue;
    }

    @Override
    public int getHighestPriority() {
        return readyQueue.stream()
                .mapToInt(Process::getPrioritySchedule)
                .max()
                .orElse(Integer.MIN_VALUE); // Return Integer.MIN_VALUE if the queue is empty
    }



    private GanttChart ganttChart;


    @Override
    public void run() {
        int timer = 0;
        int quantumTimer = 0;
        int quantumCounter = 0;


        while (!readyQueue.isEmpty() || !processToQueue.isEmpty()) {

            // Add arriving processes to ready queue
            int finalTimer = timer;
            readyQueue.addAll(processToQueue.stream()
                    .filter(process -> process.getArrivalTime() == finalTimer)
                    .toList());
            processToQueue.removeAll(readyQueue);

            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.get(0); /** start? */
                System.out.println("rr"+"Executing process " + currentProcess.getPid() + " at time " + timer);
                currentProcess.addTimeStarted(timer);
                System.out.println(currentProcess.getPrioritySchedule());
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);

                quantumTimer++;
                if (quantumTimer == quantumTime || currentProcess.getBurstTime() == 0) {
                    quantumTimer = 0;
                    quantumCounter++;
                    currentProcess.addTimeStarted(timer);
                    if (quantumCounter == 5) {
                        quantumCounter = 0;
                    }
                    if (currentProcess.getBurstTime() == 0) {
                        System.out.println("Process " + currentProcess.getPid() + " completed at time " + timer);
                        currentProcess.addTimeEnded(timer);
                        readyQueue.remove(currentProcess);  /** end? */
                    } else {
                        readyQueue.remove(currentProcess); /** end? */
                        readyQueue.add(currentProcess); /** end? */
                    }
                }
            } else {
                System.out.println("RR"+"Idling at time " + timer);
            }
            timer++;

        }
    }
    public ArrayList<Process> getGanttChartArray() {
        return this.ganttChart.getProcesses();
    }
}
