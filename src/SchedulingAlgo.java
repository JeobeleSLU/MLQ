import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SchedulingAlgo implements Sorter {
    private int timer;
    private ArrayList<Process> processToQueueList;
    private ArrayList<Process> processOnQueueList;
    private ArrayList<Process> processDoneList;
    private Core core1;
    private Core core2;
    private Core core3;
    private Core core4;
    //Create an Arraylist of Cores

    /*===============================================================================
                    todo: Create a Scalable scheduling algorithm
                     where this constructor would take number of Cores?
    ==================================================================================
     */
    SchedulingAlgo(){
        processDoneList = new ArrayList<>();
        processOnQueueList = new ArrayList<>();
        processToQueueList = new ArrayList<>();
        timer = 0;
        core1 = new Core();
        core2 = new Core();
        core3 = new Core();
        core4 = new Core();
    }
    public SchedulingAlgo(ArrayList<Process> processes) {
        processDoneList = new ArrayList<>();
        processOnQueueList = new ArrayList<>();
        processToQueueList = processes;
        timer = 0;
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
            /*
            check if the core is empty
            get the highest priority process running
            get all the number of running highest priority
            get which core has the less running highest priority
            then add it to the core that has the less running process
            add

             */
  /*
            ================================================================================
            TODO: Add a load balancer that checks which cpu has the less number of process
             *Checks the core which one has the lowest number of high priority
                * add it to the core with the least number of higher priority
                *remove the process that have been queue from the
                 *process to queue to the process on queue
            ================================================================================
             */
        while (!processOnQueueList.isEmpty() || !processToQueueList.isEmpty()) {

            //get the arriving p[rocess
            processOnQueueList.addAll(Sorter.getArrivedProcess(processToQueueList,timer));
            // removes the process that are already on the process on queue
            processToQueueList.removeAll(processOnQueueList);

            //maybe add thread sleep for slower run time
            timer++;

        }


    }

    public int getTimer() {
        return timer;
    }

    public ArrayList<Process> getProcessDoneList() {
        return processDoneList;
    }

    public ArrayList<Process> getProcessOnQueueList() {
        return processOnQueueList;
    }

    public ArrayList<Process> getProcessToQueueList() {
        return processToQueueList;
    }
    public Core getCoreWithLeastProcess(){
        ArrayList<Core> cores = new ArrayList<>();
        cores.add(core1);
        cores.add(core2);
        cores.add(core3);
        cores.add(core4);
        cores.stream()
                .sorted(Comparator.comparingInt(Core :: getNumberOfNPPSProcess)
                        .thenComparing(Core :: getNumberOfSJFProcess)
                        .thenComparing(Core :: getNumberOfSRTFProcess)
                        .thenComparing(Core :: getNumberOfRoundRobinProcess))
                .collect(Collectors.toList());
        return cores.get(0);
    }
}
