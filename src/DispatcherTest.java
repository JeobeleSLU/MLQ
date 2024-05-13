import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DispatcherTest {

    @org.junit.Test
    @Test
    public void testDispatcher() {
        // Create some processes with different priorities
        Process process1 = new Process(1, 0, 6, 2);  // Priority 2
        Process process2 = new Process(2, 1, 5, 1);  // Priority 1
        Process process3 = new Process(3, 2, 4, 3);  // Priority 3

        // Create a list of processes
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(process1);
        processes.add(process2);
        processes.add(process3);

        // Create the dispatcher
        Dispatcher dispatcher = new Dispatcher(processes);

        // Test if processes are correctly assigned to schedulers
        assertEquals(1, dispatcher.getRoundRobinScheduler().getProcesses().size());
        assertEquals(1, dispatcher.getSRTFScheduler().getProcesses().size());
        assertEquals(1, dispatcher.getSJFScheduler().getProcesses().size());

        // Execute process1 five times
        for (int i = 0; i < 5; i++) {
            executeProcess(process1);
        }

        // Change the priority of process1
        process1.setSchedulerPriority(1);  // Change the priority of process1 to 1

        // Test if process1 is transferred from SJF to Round Robin scheduler
        assertEquals(2, dispatcher.getRoundRobinScheduler().getProcesses().size());
        assertTrue(dispatcher.getRoundRobinScheduler().getProcesses().contains(process1));
        assertEquals(0, dispatcher.getSJFScheduler().getProcesses().size());
    }

    // Simulate executing a process
    private void executeProcess(Process process) {
        // Execute process logic here
        // For simplicity, let's just decrement the burst time by 1
        process.setBurstTime(process.getBurstTime() - 1);
    }
}
