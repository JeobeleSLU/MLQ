import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SchedulingAlgo {
    private int timer;
    private ArrayList<Process> processToQueueList;
    private ArrayList<Process> processOnQueueList;
    private ArrayList<Process> processDoneList;

    /*
    This class should do all the running of the algorithms based on the mlq process
     Processes to queue
        -All process are added on to this list
      ReadyQueue
        -If timer == arrivalTime add it to the ready Queue
        -Move the process form processes to queue to the ready queue
      ProcesDoneQueue
        -If remaining BurstTime == 0 add it to this queue
        -Move From readyQueue to ProcessDoneQueue

    For 2 cores:
        -If no process is running it should add idle to the current TimeLine
        -For real time process it should go to core 2
        - For the rest it should go to core 1
        -If a higher priority process  arrives it should preeempt the running lower priority process
            *Use the addFirst() Function of the list
     For 4 Cores:
             -If no process is running it should add idle to the current TimeLine
        -For real time process it should go to core 2
        -Check all the ready queue of all the cores
        - For the rest it should go to core 2,3,4
        -If a higher priority process  arrives it should preeempt the running lower priority process
            *Use the addFirst() Function of the list

    */

    public SchedulingAlgo(ArrayList<Process> processes) {
        processToQueueList = processes;
        processDoneList = new ArrayList<>();
        processToQueueList = new ArrayList<>();
        processOnQueueList = new ArrayList<>();
        timer = 0;


        while (!processOnQueueList.isEmpty() || !processToQueueList.isEmpty()) {


        }


    }
}
