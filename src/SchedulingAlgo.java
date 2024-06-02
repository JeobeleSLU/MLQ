import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class SchedulingAlgo implements Sorter {
    private int timer;
    private ArrayList<Process> processToQueueList;
    private ArrayList<Process> processOnQueueList;
    private ArrayList<Process> processDoneList;
    private ArrayList<Core> arrayListOfCores;

    //Create an Arraylist of Cores

    /*===============================================================================
                    todo: Create a Scalable scheduling algorithm
                     where this constructor would take number of Cores?
    ==================================================================================
     */
    SchedulingAlgo(int numberOfCores){
        this.processDoneList = new ArrayList<>();
        this.processOnQueueList = new ArrayList<>();
        this.processToQueueList = new ArrayList<>();
        this.arrayListOfCores = new ArrayList<>();

        //list this based on the number of cores
        for (int i = 0 ; i < numberOfCores ; i++){
            arrayListOfCores.add(new Core());
        }

        timer = 0;

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

    }
    public void run(){

        while (!processOnQueueList.isEmpty() || !processToQueueList.isEmpty()) {

            //get the arriving p[rocess
            processOnQueueList.addAll(Sorter.getArrivedProcess(processToQueueList,timer));
            assignProcess();
            // removes the process that are already on the process on queue
            processToQueueList.removeAll(processOnQueueList);
            /*
            add process according to their priorirty
            e.g prio 1 to roundrobin
             */
            arrayListOfCores.stream()
                    .map(e )
                    .collect(Collectors.toList());

                    //maybe add thread sleep for slower run time
                    timer += 1;

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
    public void getCoreWithLeastProcess(){
        ArrayList<Core> cores = new ArrayList<>();

        cores.stream()
                .sorted(Comparator.comparingInt(Core :: getNumberOfNPPSProcess)
                        .thenComparing(Core :: getNumberOfSJFProcess)
                        .thenComparing(Core :: getNumberOfSRTFProcess)
                        .thenComparing(Core :: getNumberOfRoundRobinProcess))
                .collect(Collectors.toList());
    }
    void assignProcess(){
        //get the arrived process
        ArrayList<Process> arrived = Sorter.getArrivedProcess(this.processToQueueList,this.timer);
        //this will assign process according to its priority
        ArrayList<Process> rr =Sorter.filterPriority(arrived, 1);
        ArrayList<Process> srtf =Sorter.filterPriority(arrived, 2);
        ArrayList<Process> sjf =Sorter.filterPriority(arrived, 3);
        ArrayList<Process> npps =Sorter.filterPriority(arrived, 4);
        /*
        * TODO: add load balancer here,
        *  Get the core with least process
        *  add the process that core
        * */



    }
}
