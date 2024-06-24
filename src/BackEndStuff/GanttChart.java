package BackEndStuff;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class GanttChart {
    private ArrayList<Process> processes;

    public GanttChart() {
        this.processes = new ArrayList<>();
    }

    void updateAllValues(){
        processes.forEach(Process::updateTimes);
    }

    public void addProcess(Process process) {

        // Check if a process with the same PID exists in the chart
        this.processes.add(process);
    }

    void addProcess(ArrayList<Process> gantt) {
        processes.addAll(gantt);
    }

    public ArrayList<Process> getProcesses() {
        return this.processes;
    }

    public int getAverage(){
        updateAllValues();//ensure that all values are up to date
        int totalResponse = processes.stream().mapToInt(Process::getResponseTime).sum();
        System.out.println(totalResponse/processes.size());
        return totalResponse / processes.size();

    }

    public void displayChart() {
        System.out.println("Gantt Chart:");

        // Print header
        System.out.printf("%-5s %-20s %-20s %-20s\n", "PID", "Start Times", "End Times", "Times on CPU");

        // Iterate through each process and print their details
        for (Process process : processes) {
            String startTimes = process.getTimesStarted().toString();
            String endTimes = process.getTimesEnded().toString();
            String timesOnCPU = process.timeOnCore.toString();
            System.out.printf("%-5d %-20s %-20s %-20s \n", process.getPid(), startTimes,endTimes, timesOnCPU);
        }
    }

    public void getSize() {
        System.out.println("Gantt Chart size is: "+ processes.size());
    }
    public Process getProcessOnCore(int timer) {
        for (Process process : processes) {
            if (process.isExecuting(timer)){
                return process;
            }
        }
        return null;
    }

    public boolean isRunning(int timeElapsed) {
        for (Process process : processes) {
            if (process.isExecuting(timeElapsed)) {
                return true;
            }
        }return false;
    }
}
