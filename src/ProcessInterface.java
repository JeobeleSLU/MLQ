import java.util.ArrayList;

public interface ProcessInterface {


    void addToQueue(Process process);

    void removeFromQueue(Process process);
    int getQueueSize();
    Process getNextProcess();
    ArrayList<Process> getProcesses();
    int getHighestPriority();
    public ArrayList<Process> processessToQueue();
    boolean isEmpty();
    int getNumberOfProcesses();

    void addToqueue(ArrayList<Process> processes);
}