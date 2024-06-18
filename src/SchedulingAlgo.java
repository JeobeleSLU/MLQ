import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class SchedulingAlgo implements Sorter {
    private int timer;
    private ArrayList<Process> processToQueueList;
    private ArrayList<Process> processOnQueueList;
    private ArrayList<Process> processDoneList;
    private ArrayList<Core> arrayListOfCores;
    private ArrayList<Process> processDone ;

    // Constructor to initialize the number of cores
    SchedulingAlgo(int numberOfCores){
        this.processDoneList = new ArrayList<>();
        this.processOnQueueList = new ArrayList<>();
        this.processToQueueList = new ArrayList<>();
        this.arrayListOfCores = new ArrayList<>();

        for (int i = 0; i < numberOfCores; i++){
            arrayListOfCores.add(new Core(i));
        }

        this.timer = 0;
    }

    public SchedulingAlgo(ArrayList<Process> processes, int numberOfCores) {
        this.processDoneList = new ArrayList<>();
        this.processOnQueueList = new ArrayList<>();
        this.processToQueueList = processes;
        this.arrayListOfCores = new ArrayList<>();
        this.processDoneList = new ArrayList<>();

        for (int i = 0; i < numberOfCores; i++){
            arrayListOfCores.add(new Core(i));
        }

        this.timer = 0;
    }

    // Main run method for scheduling processes
    public void run() {
        boolean areAllCoreEmpty = true;

        while ( !processToQueueList.isEmpty() && areAllCoreEmpty){
            areAllCoreEmpty = checkCoresWithProcess();
            System.out.println(areAllCoreEmpty);
            processOnQueueList.addAll(Sorter.getArrivedProcess(processToQueueList, timer));
            assignProcess();
            processToQueueList.removeAll(processOnQueueList);
            System.out.println("Number of Process On Queue: "+ this.processOnQueueList.size());
            System.out.println("Number of Process To Queue: "+ this.processToQueueList.size());

            for (Core core : arrayListOfCores) {
                core.setTimer(this.timer);
                core.runProcesses(timer);
            }
            for (Core core : arrayListOfCores) {
                for (Object o : core.getSJFDoneList()) {
                    processDone.add((Process) o);
                    System.out.println(((Process) o).getPid());

                }
                this.processOnQueueList.removeAll(core.getSJFDoneList());
            }
            System.out.println("\nEnd of Cycle: "+timer);

//            if (timer == 40){
//                break;
//            }
            this.timer++;


        }
    }

    private boolean checkCoresWithProcess() {
        arrayListOfCores.forEach(e-> System.out.println("Process at Core "+e.getCoreID() + "\nNumber of Process"+e.getNumberOfProcesses()));
        for (Core core : arrayListOfCores) {
            if (core.isEmpty()){
                return true;
            }
        }
        return false;
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

    private void sortCoreToLeastProcess() {
        arrayListOfCores = (ArrayList<Core>) arrayListOfCores.stream()
                .sorted(Comparator.comparingInt(Core::getNumberOfNPPSProcess)
                        .thenComparing(Core::getNumberOfSJFProcess)
                        .thenComparing(Core::getNumberOfSRTFProcess)
                        .thenComparing(Core::getNumberOfRoundRobinProcess))
                .collect(Collectors.toList());
    }

    void assignProcess() {
        ArrayList<Process> arrived = Sorter.getArrivedProcess(processToQueueList, timer);

        ArrayList<Process> rr = Sorter.filterPriority(arrived, 1);
        ArrayList<Process> srtf = Sorter.filterPriority(arrived, 2);
        ArrayList<Process> sjf = Sorter.filterPriority(arrived, 3);
        ArrayList<Process> npps = Sorter.filterPriority(arrived, 4);

        for (Process process : rr) {
            sortCoreToLeastProcess();
            System.out.println("Assigning RR");
            arrayListOfCores.get(0).addLastToRoundRobinScheduler(process);
        }
        for (Process process : srtf) {
            System.out.println("Assigning SRTF");

            sortCoreToLeastProcess();
            arrayListOfCores.get(0).addToSRTFScheduler(process);
        }
        for (Process process : sjf) {

            sortCoreToLeastProcess();
            System.out.println("Assigning SJF\n"+"Process ID: "+process.getPid()+
                    "\nAssigning  to core: " +arrayListOfCores.getFirst().getCoreID());
            arrayListOfCores.get(0).addToSJFScheduler(process);
        }
        for (Process process : npps) {
            System.out.println("Assigning NPPS");
            sortCoreToLeastProcess();
            arrayListOfCores.get(0).addToNPPSScheduler(process);
        }
        arrived.forEach(e-> this.processToQueueList.remove(e));
    }
}
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
                 //quite done
            ================================================================================
             */

