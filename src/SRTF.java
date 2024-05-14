import java.util.ArrayList;

public class SRTF implements Sorter, Scheduler {
    private ArrayList<Process> readyQueue;
    private ArrayList<Process> processToQueue;
    private GanttChart ganttChart;
    private int timer = 0;
    private ArrayList<Integer> startTime;
    private ArrayList<Integer> endTime;


    public SRTF(ArrayList<Process> processes) {
        this.processToQueue = new ArrayList<>(processes);
        this.readyQueue = new ArrayList<>();
        this.ganttChart = new GanttChart();
        startTime = new ArrayList<>();
        endTime = new ArrayList<>();

    }



    @Override
    public void run() {
        while (!processToQueue.isEmpty() || !readyQueue.isEmpty()) {
            // Add arriving processes to ready queue
            for (Process process : processToQueue) {
                if (process.getArrivalTime() == timer) {//checks if the process have arrived
                        readyQueue.add(process);//added it to the queue
                    System.out.println("Ready Queue size");
                    System.out.println(readyQueue.size());
                }
            }
            processToQueue.removeAll(readyQueue);// remove all the process that are == to the processToQueue from ready queue
            System.out.println("PROCESS2Q" + processToQueue);
//            System.out.println("READYQ" + readyQueue);


            // Sort ready queue by burst time
            readyQueue = Sorter.sortByBurstTime(readyQueue);// sort all the burst time of the processes in the ready queue
            System.out.println("READYQFize" + readyQueue.size());

            if (!readyQueue.isEmpty()) {// if ready queue have an element inside it
                Process currentProcess = readyQueue.get(0); //gets the first element of the ready queue
                // Log start time if it's the first execution
                if (currentProcess.isFirstExecution()) {//checks if the process have already executed
//                    currentProcess.addTimeStarted(timer);
                    startTime.add(timer);// add start time
                    currentProcess.setTimeNow(timer);
                    currentProcess.setTimeStarted(timer);
                    currentProcess.setFirstExecution(false);
                }

                // Execute the current process
                System.out.println("SRTF: Executing process " + currentProcess.getPid() + " at time " + timer);
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
//                startTime.add(timer);


                // Check for preemption
                System.out.println("ReadyQ" + readyQueue.size());
                System.out.println("Process to Queue"+ processToQueue.size());
                if (!processToQueue.isEmpty()) {
                    for (Process process : processToQueue) {
                        System.out.println("Checkign preemption");

                        if (process.getArrivalTime() == timer && process.getBurstTime() < currentProcess.getBurstTime()) {
                            // Preemption occurred
                            System.out.println("A process was Preempted");
                            startTime.add(timer - currentProcess.getArrivalTime());
                            currentProcess.addTimeEnded(timer);
                            readyQueue.set(0, process);
                            ganttChart.addProcess(currentProcess);
                            readyQueue.remove(currentProcess);
                            readyQueue.add(process);
                            break; // No need to continue checking
                        }
                    }
                }

                // Check if the process has completed execution
                if (currentProcess.getBurstTime() == 0) {
//                    endTime.add(timer);
                    System.out.println("Process " + currentProcess.getPid() + " completed at time " + timer);
                    ganttChart.addProcess(currentProcess);
                    currentProcess.setTimeEnd(timer);

                    readyQueue.remove(currentProcess);
                    System.out.println(currentProcess.getTimesStarted() + " " +currentProcess.getTimesEnded());
                }
                currentProcess.setTimesStarted(startTime);
                currentProcess.setTimesEnded(endTime);
                System.out.println("TESTTTT" + currentProcess.getTimesStarted());
                System.out.println("TEST END!!!" + currentProcess.getTimesEnded());
            } else {
                // Idling if no processes are ready
                System.out.println("Idling at time " + timer);
            }
            timer++;

        }
    }

//    @Override
//    public void run() {
//        while (!processToQueue.isEmpty() || !readyQueue.isEmpty()) {
//            for (Process process : processToQueue) {
//                if (process.getArrivalTime() == timer) {
//                    readyQueue.add(process);
//                    System.out.println(timer);
//                }
//            }
//            processToQueue.removeAll(readyQueue);
//
//            if (!readyQueue.isEmpty()) {
//                Process currentProcess = readyQueue.get(0);
//
//                if (currentProcess.isFirstExecution()) {
//                    currentProcess.addTimeStarted(timer);
//                    currentProcess.setFirstExecution(false);
//                }
//
//                System.out.println("SRTF: Executing process " + currentProcess.getPid() + " at time " + timer);
//                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
//
//
//                // Check for preemption
//                    if (!processToQueue.isEmpty()) {
//                        System.out.println("OUTER IF");
//                        for (Process process : processToQueue) {
//                        System.out.println("TIMER" + timer);
//                        System.out.println("PROCESS ARRIVAL " + process.getArrivalTime());
//                        if (process.getArrivalTime() == ++timer) {
//                                System.out.println("INNER IF");
//                                if (process.getBurstTime() < currentProcess.getRemainingBurstTime()) {
//                                    // Preemption occurred
//                                    System.out.println("INNER INNER IF");
//
//                                    currentProcess.setRemainingBurstTime(currentProcess.getBurstTime());
//                                    currentProcess.setFirstExecution(true);
//                                    readyQueue.set(0, currentProcess);
//                                    ganttChart.addProcess(currentProcess);
//                                    readyQueue.remove(currentProcess);
//                                    readyQueue.add(process);
//                                    break; // No need to continue checking
//                                }
//                        }
//                    }
//                }
//                if (currentProcess.getBurstTime() == 0) {
//                    currentProcess.addTimeEnded(timer);
////                    endTime.add(timer);
//                    System.out.println("Process " + currentProcess.getPid() + " completed at time " + timer);
//                    ganttChart.addProcess(currentProcess);
//                    readyQueue.remove(currentProcess);
//                    System.out.println(currentProcess.getTimesStarted() + " " +currentProcess.getTimesEnded());
//                }
//
//                System.out.println("SRTF Time Started" + currentProcess.getTimesStarted());
//                System.out.println("SRTF time Ended" + currentProcess.getTimesEnded());
//            } else {
//                // ...
//                System.out.println("SRTF Idling at time " + timer);
//            }
//            timer++;
//        }
//    }




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
