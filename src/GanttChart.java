import java.util.ArrayList;

public class GanttChart {
    private ArrayList<Process> processes;


    public GanttChart() {
        this.processes = new ArrayList<>();

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

    public ArrayList<Process> getProcesses() {
        return this.processes;
    }

    public void displayChart() {
        System.out.println("Gantt Chart:");
        System.out.println(processes.size());
    for (Process process : processes) {
            process.getTimeStarted();
            process.getTimesEnded();
            System.out.println("pid"+process.getPid()+"\t"+process.getTimesStarted().size());
        }
    }
}
