import java.util.ArrayList;

public class SRTF implements Sorter, ProcessInterface, Runnable {
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

    public SRTF() {
        this.readyQueue = new ArrayList<>();
        this.ganttChart = new GanttChart();
        startTime = new ArrayList<>();
        endTime = new ArrayList<>();
    }

        /*
    Todo: Refactor this code so that you can call the run method to execute the
    process and minus the remaining burst time based on this algorithm
    */



    @Override
    public void run() {
        while (!processToQueue.isEmpty() || !readyQueue.isEmpty()) {
            // Add arriving processes to ready queue
            readyQueue = Sorter.getArrivedProcess(processToQueue, timer );

//            for (Process process : processToQueue) {
//                if (process.getArrivalTime() == timer) {//checks if the process have arrived
//                              readyQueue.add(process);//added it to the queue
//                    System.out.println("Ready Queue size");
//                    System.out.println(readyQueue.size());
//                }
//            }
            processToQueue.removeAll(readyQueue);// remove all the process that are == to the processToQueue from ready queue
            System.out.println("PROCESS2Q" + processToQueue);
//            System.out.println("READYQ" + readyQueue);


            // Sort ready queue by burst time
            ;// sort all the burst time of the processes in the ready queue
            System.out.println("READYQFize" + readyQueue.size());

            if (!readyQueue.isEmpty()) {
                // if ready queue have an element inside it
                readyQueue = Sorter.sortByBurstTime(readyQueue);
                Process currentProcess = readyQueue.get(0);
                //gets the first element of the ready queue
                // Log start time if it's the first execution
                if (currentProcess.isFirstExecution()) {
                    //checks if the process have already executed
//                    startTime.add(timer);// add start time
                    currentProcess.setTimeNow(timer);
                    currentProcess.addTimeStarted(timer);
                    currentProcess.setFirstExecution(false);
                    
                    ganttChart.addProcess(currentProcess);
                }
                if (currentProcess.isPreempted()){
                    currentProcess.addTimeStarted(timer);
                }

                // Execute the current process
                System.out.println("SRTF: Executing process " + currentProcess.getPid() + " at time " + timer);
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                startTime.add(timer);
                System.out.println(startTime);

                // Check for preemption
                System.out.println("ReadyQ" + readyQueue.size());
                System.out.println("Process to Queue"+ processToQueue.size());
//                if (!processToQueue.isEmpty()) {
                    for (Process process : processToQueue) {
                        System.out.println("Checking preemption");
                        System.out.println("Process "+process.getPid()+ "Arrival:" + process.getArrivalTime());
                        System.out.println("Time"+ timer);

                        if ( ( process.getArrivalTime() == timer + 1 ) ){
                            if (process.getBurstTime() < currentProcess.getBurstTime()){
                                System.out.println("A process was Preempted");
//                                startTime.add(timer);
                                currentProcess.addTimeEnded(timer);
                                currentProcess.setFirstExecution(false);
                                currentProcess.setPreempted(true);
                                ganttChart.addProcess(currentProcess);
//                                readyQueue.add(0, process);
//                                readyQueue.remove(currentProcess);
                                break;
                            }
                        }
                            // Preemption occurred
//                            startTime.add(timer - currentProcess.getArrivalTime());
//                            endTime.add(timer);

//                            readyQueue.add(process);
//                            readyQueue.add(currentProcess);
//                            ganttChart.addProcess(currentProcess);
                             // No need to continue checking
                        }
//                }

                // Check if the process has completed execution
                if (currentProcess.getBurstTime() == 0) {
//                    endTime.add(timer);
                        System.out.println("SRTF Process " + currentProcess.getPid() + " completed at time " + timer);
//                    endTime.add(timer);
                    currentProcess.addTimeEnded(timer);
                    readyQueue.remove(currentProcess);
                    System.out.println(currentProcess.getTimesStarted() + " " +currentProcess.getTimesEnded());
                    ganttChart.addProcess(currentProcess);

                }
//                currentProcess.setTimeStarted(startTime);
//                currentProcess.setTimesEnded(endTime);
                System.out.println("TESTTTT" + currentProcess.getTimesStarted());
                System.out.println("TEST END!!!" + currentProcess.getTimesEnded());
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

    @Override
    public boolean isEmpty() {
        return this.readyQueue.isEmpty();
    }

    @Override
    public int getNumberOfProcesses() {
            return this.readyQueue.size();
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

//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.LinkedList;
//
//public class SRTF {
//    GanttChart gant;
//    private GanttChart gChart;
//    private ArrayList <Process> processToQueue;
//
//
//
//    public  ArrayList<Process> SRTF(ArrayList<Process> processes) {
//        int currentTime = 0;
//        int totalWaitingTime = 0;
//        this.processToQueue = processes;
//        this.gant = new GanttChart();
//
//
//        // Sort processes by arrival time
//        processes.sort(Comparator.comparingInt(p -> p.getArrivalTime()));
//
//        LinkedList<Process> queue = new LinkedList<>();
//        int completedProcesses = 0;
//        int[] remainingTime = new int[processes.size()];
////        ganttChart = new ArrayList<>();
//
//        while (completedProcesses < processes.size()) {
//            // Add arrived processes to the queue
//            for (int i = 0; i < processes.size(); i++) {
//                if (processes.get(i).getArrivalTime() <= currentTime && remainingTime[i] == 0) {
//                    processes.get(i).getPrioritySchedule();
//                    queue.add(processes.get(i));
//                    remainingTime[i] = processes.get(i).getBurstTime();
//                }
//            }
//            // If the queue is empty, move to the next arrival time
//            if (queue.isEmpty()) {
//                currentTime = processes.get(completedProcesses).getArrivalTime();
//                continue;
//            }
//
//            // Find process with shortest remaining time
//            Process shortestProcess = queue.stream().min(Comparator.comparingInt(p -> p.getRemainingBurstTime())).get();
//
//            // Execute the shortest process for one unit
//            shortestProcess.setRemainingBurstTime(shortestProcess.getRemainingBurstTime()-1);
//            currentTime++;
//
//            // Update waiting time for other processes in the queue
////            for (Process process : queue) {
////                if (process != shortestProcess) {
////                    process.setwaitingTime++;
////                }
////            }
//
//            // Process completed
////            if (shortestProcess.getRemainingBurstTime() == 0) {
////                totalWaitingTime += shortestProcess.getWaitingTime();
//////                shortestProcess.setTurnAroundTime(currentTime - shortestProcess.getArrivalTime((((); // Calculate turnaround time after completion
////                queue.remove(shortestProcess);
////                completedProcesses++;
////                ganttChart.add("P"+shortestProcess.getPid()+" ["+(currentTime-shortestProcess.getBurstTime())+"]");
////                ganttChart.add(shortestProcess);
////            }
////
////        }
////        System.out.println("Shortest Remaining Time Results:");
//////        printProcessDetails(processes); // Use the modified printProcessDetails method
////        System.out.println("Total Waiting Time: " + totalWaitingTime);
////        return processes;
////    }
////
////    private static int getCpuBurst(LinkedList<Process> queue) {
////        int minRemainingTime = Integer.MAX_VALUE;
////        for (Process process : queue) {
////            minRemainingTime = Math.min(minRemainingTime, process.getRemainingBurstTime());
////        }
////        return minRemainingTime;
////    }
//
//    // Modified printProcessDetails method to include classification
////    public static void printProcessDetails(ArrayList<Process> processes) {
////        System.out.println("| PID | Arrival Time | Burst Time | Waiting Time | Turnaround Time | Classification |");
////        System.out.println("|---|---|---|---|---|---|");
////        for (Process process : processes) {
////            System.out.println(String.format("| %d | %d | %d | %d | %d | %s |",
////                    process.pid, process.arrivalTime, process.burstTime, process.waitingTime, process.turnAroundTime, process.classification));
////        }
////        System.out.println(ganttChart);
////    }
//}
//
