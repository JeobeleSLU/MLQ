import java.util.ArrayList;

public class RoundRobinScheduler implements ProcessInterface {
    private  int quantumTime;
    ArrayList<Process> readyQueue ;
    ArrayList<Process> processToQueue ;

    public RoundRobinScheduler(ArrayList<Process> processes) {
        this.readyQueue = new ArrayList<>();
        this.processToQueue = new ArrayList<>();
        this.processToQueue.addAll(processes);
        ganttChart = new GanttChart();
        this.quantumTime = 3;

        /*
    Todo: Refactor this code so that you can call the run method to execute the
    process and minus the remaining burst time based on this algorithm
        */


    }

    public RoundRobinScheduler() {
        this.readyQueue = new ArrayList<>();
        this.processToQueue = new ArrayList<>();
        this.quantumTime = 3;
    }

    public void addToQueue(Process process) {
        readyQueue.add(process);
    }

    public void setQuantumTime(int quantumTime) {
        this.quantumTime = quantumTime;
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
    public boolean isEmpty() {
        return this.readyQueue.isEmpty();
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
                Process currentProcess = readyQueue.get(0); // Start process

                // Add the time started the first time the process is executed
                if (currentProcess.isFirstExecution()) {
                    currentProcess.setFirstExecution(false);
                    currentProcess.setTimeNow(timer);
                    currentProcess.setTimeStarted(timer);
                    ganttChart.addProcess(currentProcess);

                }


                System.out.println("Executing process " + currentProcess.getPid() + " at time " + timer);
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                System.out.println(currentProcess.getBurstTime());
                quantumTimer++;
                if (quantumTime == quantumTimer){
                    currentProcess.addTimeStarted(timer);
                    currentProcess.addTimeStarted(timer);
                }

                if (quantumTimer == quantumTime || currentProcess.getBurstTime() == 0) {
                    quantumTimer = 0;
                    quantumCounter++;
                    currentProcess.addTimeEnded(timer);
                    if (quantumCounter == 5) {
                        quantumCounter = 0;
                    }

                    if (currentProcess.getBurstTime() == 0) {
                        System.out.println("Process " + currentProcess.getPid() + " completed at time " + timer);
                        currentProcess.setTimeEnd(timer);
                        currentProcess.addTimeEnded(timer);
                        readyQueue.remove(currentProcess);
                    } else {
                        readyQueue.remove(currentProcess);
                        readyQueue.add(currentProcess);
                    }
                }
            } else {
                System.out.println("Idling at time " + timer);
            }
            timer++;
        }
    }
    public void addToqueue(ArrayList <Process> processes){
        this.readyQueue = processes;
    }
    public ArrayList<Process> getGanttChartArray() {
       return ganttChart.getProcesses();
    }
}
