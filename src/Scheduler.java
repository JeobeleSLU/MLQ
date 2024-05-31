import java.util.ArrayList;

public interface Scheduler extends Runnable {


    void addToQueue(Process process);

    void removeFromQueue(Process process);
    int getQueueSize();
    Process getNextProcess();
    ArrayList<Process> getProcesses();
    int getHighestPriority();
    public ArrayList<Process> processessToQueue();

}