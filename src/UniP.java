import java.util.ArrayList;

public class UniP implements Scheduler,Sorter {
    private RoundRobinScheduler realTimeScheduler;
    private SRTF srtfScheduler;
    private SJF sjfScheduler;
    private NPPS batchScheduler;
    private int currentTime = 0;

    public UniP(ArrayList<Process> processes) {
        initializeSchedulers(processes);
    }

    private void initializeSchedulers(ArrayList<Process> processes) {

    }

    @Override
    public void run() {
        while (!allSchedulersEmpty()) {
            checkForArrivals(); // Check for process arrivals at current time
            runSchedulers();    // Run the schedulers
            currentTime++;      // Increment the time
        }
    }

    private void checkForArrivals() {
        for (Process process : allProcesses()) {
            if (process.getArrivalTime() == currentTime) {
                switch (process.getPrioritySchedule()) {
                    case 1:
                        realTimeScheduler.addToQueue(process);
                        break;
                    case 2:
                        srtfScheduler.addToQueue(process);
                        break;
                    case 3:
                        sjfScheduler.addToQueue(process);
                        break;
                    case 4:
                        batchScheduler.addToQueue(process);
                        break;
                }
            }
        }
    }

    private void runSchedulers() {
        // Check if a higher priority process has arrived
        Process newProcess = highestPriorityArrival();
        if (newProcess != null) {
            preemptIfNecessary(newProcess);
        }

        // Run real-time scheduler
        realTimeScheduler.run();

        // Run other schedulers
        srtfScheduler.run();
        sjfScheduler.run();
        batchScheduler.run();
    }

    private Process highestPriorityArrival() {
        Process newProcess = null;
        for (Process process : allProcesses()) {
            if (process.getArrivalTime() == currentTime) {
                if (newProcess == null || process.getPrioritySchedule() < newProcess.getPrioritySchedule()) {
                    newProcess = process;
                }
            }
        }
        return newProcess;
    }

    private void preemptIfNecessary(Process newProcess) {
        // Check if any of the schedulers have a process currently executing
        Process executingProcess = getExecutingProcess();

        // If there's an executing process and its priority is lower than the new process,
        // preempt it and put it back into the appropriate scheduler's ready queue
        if (executingProcess != null && executingProcess.getPrioritySchedule() > newProcess.getPrioritySchedule()) {
                switch (executingProcess.getPrioritySchedule()) {
                case 1:
                    realTimeScheduler.addToQueue(executingProcess);
                    break;
                case 2:
                    srtfScheduler.addToQueue(executingProcess);
                    break;
                case 3:
                    sjfScheduler.addToQueue(executingProcess);
                    break;
                case 4:
                    batchScheduler.addToQueue(executingProcess);
                    break;
            }

            // Now, replace the executing process with the new process
            switch (newProcess.getPrioritySchedule()) {
                case 1:
                    realTimeScheduler.removeFromQueue(newProcess);
                    break;
                case 2:
                    srtfScheduler.removeFromQueue(newProcess);
                    break;
                case 3:
                    sjfScheduler.removeFromQueue(newProcess);
                    break;
                case 4:
                    batchScheduler.removeFromQueue(newProcess);
                    break;
            }

            // Update the executing process to the new process
            executingProcess = newProcess;
        }
    }

    private Process getExecutingProcess() {
        // Check each scheduler to see if there's any process currently executing
        if (realTimeScheduler.getQueueSize() > 0) {
            return realTimeScheduler.getNextProcess();
        } else if (srtfScheduler.getQueueSize() > 0) {
            return srtfScheduler.getNextProcess();
        } else if (sjfScheduler.getQueueSize() > 0) {
            return sjfScheduler.getNextProcess();
        } else if (batchScheduler.getQueueSize() > 0) {
            return batchScheduler.getNextProcess();
        } else {
            return null; // No process is currently executing
        }
    }

    private boolean allSchedulersEmpty() {
        return realTimeScheduler.getQueueSize() == 0 &&
                srtfScheduler.getQueueSize() == 0 &&
                sjfScheduler.getQueueSize() == 0 &&
                batchScheduler.getQueueSize() == 0;
    }

    @Override
    public void addToQueue(Process process) {

    }

    @Override
    public void removeFromQueue(Process process) {

    }

    @Override
    public int getQueueSize() {
        return realTimeScheduler.getQueueSize() + srtfScheduler.getQueueSize() +
                sjfScheduler.getQueueSize() + batchScheduler.getQueueSize();
    }

    @Override
    public Process getNextProcess() {
        return null;
    }

    @Override
    public ArrayList<Process> getProcesses() {
        return null;
    }

    @Override
    public int getHighestPriority() {
        return 0;
    }

    @Override
    public ArrayList<Process> processessToQueue() {
        return null;
    }

    private ArrayList<Process> allProcesses() {
        ArrayList<Process> allProcesses = new ArrayList<>();
        allProcesses.addAll(realTimeScheduler.processessToQueue());
        allProcesses.addAll(srtfScheduler.processessToQueue());
        allProcesses.addAll(sjfScheduler.processessToQueue());
        allProcesses.addAll(batchScheduler.processessToQueue());
        return allProcesses;
    }
    public static void main(String[] args) {
        // Create processes
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 5, 1));  // PID: 1, Arrival Time: 0, Burst Time: 5, Priority: 1 (Real-Time)
        processes.add(new Process(2, 1, 3, 2));  // PID: 2, Arrival Time: 1, Burst Time: 3, Priority: 2 (System)
        processes.add(new Process(3, 2, 4, 3));  // PID: 3, Arrival Time: 2, Burst Time: 4, Priority: 3 (Interactive)
        processes.add(new Process(4, 3, 6, 4));  // PID: 4, Arrival Time: 3, Burst Time: 6, Priority: 4 (Batch)

        // Create UniP scheduler
        UniP uniP = new UniP(processes);

        // Run the scheduler
        uniP.run();
    }
}

