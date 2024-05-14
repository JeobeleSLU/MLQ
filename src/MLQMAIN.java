import java.util.ArrayList;

public class MLQMAIN {
    public static void main(String[] args) {
        // Create a Multi-Level Scheduler with 2 cores and 4 priority levels
         // Create some sample processes
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 5,2)); // Process with priority 1, arrival time 0, and burst time 5
        processes.add(new Process(2, 1, 3,1)); // Process with priority 2, arrival time 1, and burst time 3
        processes.add(new Process(3, 2, 4,3)); // Process with priority 3, arrival time 2, and burst time 4
        processes.add(new Process(6, 2, 4,4)); // Process with priority 3, arrival time 2, and burst time 4
        processes.add(new Process(5, 2, 4,1)); // Process with priority 3, arrival time 2, and burst time 4
        processes.add(new Process(4, 2, 4,1)); // Process with priority 3, arrival time 2, and burst time 4

        // Create a multi-level queue processor
        MultiLevelQueueProcessor processor = new MultiLevelQueueProcessor(processes);

        // Run the processor with 2 cores
        processor.run();
    }
}