package BackEndStuff;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

public class GanttChart {
    private ArrayList<Process> processes;
    private HashSet<Process> uniqueProccess;
    private HashSet<Process> arrived;

    public GanttChart() {
        this.processes = new ArrayList<>();
        this.arrived = new HashSet<>();
        this.uniqueProccess = new HashSet<>();
    }

    void updateAllValues() {
        sortByProcessID();
        processes.forEach(Process::updateTimes);
    }

    public void getAllProcessess(ArrayList<Process> processes) {
        this.processes.addAll(processes);
    }

    public void addProcess(Process process) {

        // Check if a process with the same PID exists in the chart
        this.processes.add(process);
        this.uniqueProccess.add(process);
    }

    void addProcess(ArrayList<Process> gantt) {
        processes.addAll(gantt);
        this.uniqueProccess.addAll(processes);
    }

    public ArrayList<Process> getProcesses() {
        return this.processes;
    }

    public int getAverageResponse() {
        //ensure that all values are up to date
        int totalResponse = processes.stream().mapToInt(Process::getResponseTime).sum();
        System.out.println(totalResponse / processes.size());
        return totalResponse / processes.size();

    }

    public int getAverageWait() {
        //ensure that all values are up to date
        int totalWait = processes.stream().mapToInt(Process::getWaitingTime).sum();
        System.out.println(totalWait / processes.size());
        return totalWait / processes.size();

    }

    public int getAverageTurn() {
        //ensure that all values are up to date
        int totalTurn = processes.stream().mapToInt(Process::getTurnAroundTime).sum();
        System.out.println(totalTurn / processes.size());
        return totalTurn / processes.size();
    }

    public void displayChart() {
        System.out.println("Gantt Chart:");

        // Print header
        System.out.printf("%-5s %-20s %-20s %-20s %-20s\n", "PID", "Start Times", "End Times", "Waiting Time", "TurnAround");

        // Iterate through each process and print their details
        for (Process process : processes) {
            String startTimes = process.getTimesStarted().toString();
            String endTimes = process.getTimesEnded().toString();
            String timesOnCPU = process.timeOnCore.toString();
            String waitingTime = String.valueOf(process.getWaitingTime());
            String turnAround = String.valueOf(process.getTurnAroundTime());

            System.out.printf("%-5d %-20s %-20s %-40s %-40s \n", process.getPid(), startTimes, endTimes, waitingTime, turnAround);
        }
    }

    public void getSize() {
        System.out.println("Gantt Chart size is: " + processes.size());
    }

    public Process getProcessOnCore(int timer) {
        for (Process process : uniqueProccess) {
            if (process.isExecuting(timer)) {
                return process;
            }
        }
        return null;
    }

    public boolean isRunning(int timeElapsed) {
        for (Process process : uniqueProccess) {
            if (process.isExecuting(timeElapsed)) {
                return true;
            }
        }
        return false;
    }

    public HashSet<Process> getArrived(int timer) {
        //get all the process that have arrived
        return (HashSet<Process>) processes.stream().filter(e -> e.getArrivalTime() == timer);
    }

    public HashSet<Process> getUniqueProccess() {
        return (HashSet<Process>)
                this.uniqueProccess.stream()
                        .filter(e -> e.getPid() >= 1).
                        collect(Collectors.toSet());
    }

    public void addAllUniqueProcess(HashSet<Process> process) {
        this.uniqueProccess.addAll(process);
    }

    public int getUniqueProcessSize() {
        return this.uniqueProccess.size();
    }

    public void sortByProcessID() {
        this.processes = (ArrayList<Process>) uniqueProccess.stream().collect(Collectors.toList());
        processes.sort(Comparator.comparingInt(Process::getPid));
        for (Process process : processes) {
            System.out.println("Sorted: " + process.getPid());
        }
    }

    public int getProcessessSize() {
        return processes.size();
    }

    public void updateStatus(int elapsedTime) {
        for (Process process : processes) {
            if (process.getTimesEnded().getLast() <= elapsedTime) {
                process.setStatus("Terminated");
            } else if (process.isExecuting(elapsedTime)) {
                process.setStatus("Executing");
            } else if (process.getArrivalTime() > elapsedTime) {
                process.setStatus("Not Arrived");
            } else {
                process.setStatus("Waiting");
            }
        }
    }
}
