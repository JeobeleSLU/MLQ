//import java.util.ArrayList;
//
//public class trial {
//    private Thread realTimeThread;
//    private Thread core2Thread;
//    private RoundRobinScheduler realTimeQueue;
//    private SJF interactiveQueue;
//    private NPPS batchQueue;
//    private SRTF systemQueue;
//
//    public trial(ArrayList<Process> processes) {
//        ArrayList<Process> sortedProcessesByPriority1 = Sorter.sortByPriority(processes, 1);
//        ArrayList<Process> sortedProcessesByPriority2 = Sorter.sortByPriority(processes, 2);
//        ArrayList<Process> sortedProcessesByPriority3 = Sorter.sortByPriority(processes, 3);
//        ArrayList<Process> sortedProcessesByPriority4 = Sorter.sortByPriority(processes, 4);
//
//        // Initialize queues with respective scheduling algorithms
//        this.realTimeQueue = new RoundRobinScheduler(sortedProcessesByPriority1);
//        this.systemQueue = new SRTF(sortedProcessesByPriority2);
//        this.interactiveQueue = new SJF(sortedProcessesByPriority3);
//        this.batchQueue = new NPPS(sortedProcessesByPriority4);
//    }
//
//    public void run(int numCores) {
//        System.out.println("HI");
//        // Execute real-time queue on core 1 if there are processes
//        if (!realTimeQueue.getProcesses().isEmpty()) {
//            realTimeThread = new Thread(() -> realTimeQueue.run());
//            realTimeThread.start();
//        }
//
//        // Execute other queues on core 2 if there are processes
//        if (!systemQueue.getProcesses().isEmpty() || !interactiveQueue.getProcesses().isEmpty() || !batchQueue.getProcesses().isEmpty()) {
//            core2Thread = new Thread(() -> {
//                runIfArrived(systemQueue);
//                runIfArrived(interactiveQueue);
//                runIfArrived(batchQueue);
//            });
//            core2Thread.start();
//        }
//
//        // Wait for both threads to finish
//        try {
//            if (realTimeThread != null)
//                realTimeThread.join();
//            if (core2Thread != null)
//                core2Thread.join();
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt(); // Reset interrupt flag
//            // Handle interruption appropriately, e.g., log or re-throw
//        }
//    }
//
//    private void runIfArrived(Scheduler scheduler) {
//        ArrayList<Process> processes = scheduler.getProcesses();
//        for (Process process : processes) {
//            if (process.getArrivalTime() <= Scheduler.getCurrentTime()) { // Assuming Scheduler has a static method to get current time
//                scheduler.run();
//                break; // only run one process
//            }
//        }
//    }
//}
