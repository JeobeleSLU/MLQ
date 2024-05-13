import java.util.ArrayList;

public class Dispatcher implements Sorter {
    private static final int SJF_THRESHOLD_PRIORITY = 1 ;
    private static final int SRTF_THRESHOLD_PRIORITY = 2 ;
    private static final int RR_THRESHOLD_PRIORITY = 3;
    private RoundRobinScheduler roundRobinScheduler;
    private SRTF srtfScheduler;
    private SJF sjfScheduler;


    public SJF getSJFScheduler() {
        return sjfScheduler;
    }

    public SRTF getSRTFScheduler() {
        return srtfScheduler;
    }

    public RoundRobinScheduler getRoundRobinScheduler() {
        return roundRobinScheduler;
    }

    public Dispatcher(ArrayList<Process> processes) {
        ArrayList<Process> rrArray = Sorter.sortByPriority(processes, 1);
        ArrayList<Process> srtfArray =Sorter.sortByPriority(processes, 2);
        ArrayList<Process> sjfArray = Sorter.sortByPriority(processes, 3);
        roundRobinScheduler = new RoundRobinScheduler(rrArray);
        srtfScheduler = new SRTF(srtfArray);
        sjfScheduler = new SJF(sjfArray);
    }

    // Method to transfer a process from SJF to SRTF if its priority increases
    public void transferProcessFromSJFToSRTF(Process process) {
        if (process.getPrioritySchedule() > SJF_THRESHOLD_PRIORITY) {
            sjfScheduler.removeFromQueue(process);
            srtfScheduler.addToQueue(process);
        }
    }

    // Method to transfer a process from SRTF to Round Robin if its priority decreases
    public void transferProcessFromSRTFToRoundRobin(Process process) {
        if (process.getPrioritySchedule() <= SRTF_THRESHOLD_PRIORITY) {
            // Remove process from SRTF queue
            srtfScheduler.removeFromQueue(process);
            // add process into Round Robin queue
            roundRobinScheduler.addToQueue(process);
        }
    }

    // Method to transfer a process from Round Robin to SRTF if its priority increases
    public void transferProcessFromRoundRobinToSRTF(Process process) {
        if (process.getPrioritySchedule() > RR_THRESHOLD_PRIORITY) {
            // Remove process from Round Robin queue
            roundRobinScheduler.removeFromQueue(process);
            // add process into SRTF queue
            srtfScheduler.addToQueue(process);
        }
    }


}

     /*todo: Create a scheduler that checks if a thread is empty if it is empty then
        it should put a process in that, if there is a process that have higher priority then
        it should stop process that is running that have a lower priority
         */