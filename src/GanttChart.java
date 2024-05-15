import java.util.ArrayList;
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
        boolean processExists = processes.stream()
                .anyMatch(existingProcess -> existingProcess.getPid() == process.getPid());

        if (processExists) {
            // If PID matches, merge the start and end times
            processes.stream()
                    .filter(existingProcess -> existingProcess.getPid() == process.getPid())
                    .findFirst()
                    .ifPresent(existingProcess -> {
                        existingProcess.getTimesStarted().addAll(process.getTimesStarted());
                        existingProcess.getTimesEnded().addAll(process.getTimesEnded());
                    });
        } else {
            // If no matching PID found, add the process to the chart
            this.processes.add(process);
        }
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
        System.out.printf("%-5s %-20s %-20s\n", "PID", "Start Times", "End Times");

        // Iterate through each process and print their details
        for (Process process : processes) {
            String startTimes = process.getTimesStarted().toString();
            String endTimes = process.getTimesEnded().toString();
            System.out.printf("%-5d %-20s\n", process.getPid(), startTimes);
        }
    }
}
