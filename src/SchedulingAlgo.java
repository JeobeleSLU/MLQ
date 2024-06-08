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

            //list this based on the number of cores
            for (int i = 0 ; i < numberOfCores ; i++){
                arrayListOfCores.add(new Core("core"+i));
            }

            timer = 0;

        }
        public SchedulingAlgo(ArrayList<Process> processes,int numberOfCores) {
            processDoneList = new ArrayList<>();
            processOnQueueList = new ArrayList<>();
            processToQueueList = processes;
            this.arrayListOfCores = new ArrayList<>();

            timer = -1;
            for (int i = 0 ; i < numberOfCores ; i++){
                this.arrayListOfCores.add(new Core("core"+i));
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

            public void run () {
            boolean areAllCoreEmpty = false;

                while (!processOnQueueList.isEmpty() || !processToQueueList.isEmpty()|| !areAllCoreEmpty) {
                    areAllCoreEmpty = checkCoresWithProcess();
                    this.timer++;
                    System.out.println(timer);

                    //get the arriving process
                    processOnQueueList.addAll(Sorter.getArrivedProcess(processToQueueList, timer));
                    System.out.println(processOnQueueList.size());
                    assignProcess();
                    System.out.println(processToQueueList.size());
                    // removes the process that are already on the process on queue
                    //sort cores based on the number of least higher priority and add it to the
                    processToQueueList.removeAll(processOnQueueList);
                    for (Core core : arrayListOfCores) {
                        core.runProcesses(timer);
                    }
                    if (timer == 20){
                        break;
                    }

                }

            }

        private boolean checkCoresWithProcess() {
            return arrayListOfCores.stream().anyMatch(core -> core.getNumberOfProcesses() > 0);
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
        public void sortCoreToLeastProcess(){

         arrayListOfCores = (ArrayList<Core>) arrayListOfCores.stream()
                    .sorted(Comparator.comparingInt(Core :: getNumberOfNPPSProcess)
                            .thenComparing(Core :: getNumberOfSJFProcess)
                            .thenComparing(Core :: getNumberOfSRTFProcess)
                            .thenComparing(Core :: getNumberOfRoundRobinProcess))
                    .collect(Collectors.toList());
        }
        int counter = 0;

        void assignProcess() {
            System.out.println("Process Assign");
            // Get the arrived processes
            ArrayList<Process> arrived = Sorter.getArrivedProcess(processToQueueList, timer);
            System.out.println("Arrived size" + arrived.size());

            // Filter processes according to their priority
            ArrayList<Process> rr = Sorter.filterPriority(arrived, 1);
            ArrayList<Process> srtf = Sorter.filterPriority(arrived, 2);
            ArrayList<Process> sjf = Sorter.filterPriority(arrived, 3);
            ArrayList<Process> npps = Sorter.filterPriority(arrived, 4);
            // Add processes to the cores using load balancing
            //todo: Debug not assigning process properly?
            for (Process process : rr) {
                sortCoreToLeastProcess();//sort core per least higher process
                arrayListOfCores.get(0).addToRoundRobinScheduler(process);
                System.out.println("Assigning RR");
                System.out.println("Core Name:"+ " "+arrayListOfCores.getFirst()+" "+"RR:"+
                arrayListOfCores.getFirst().getNumberOfRoundRobinProcess());
            }
            for (Process process : srtf) {
                sortCoreToLeastProcess();
                arrayListOfCores.get(0).addToSRTFScheduler(process);
                System.out.println("assigning SRTF");
                System.out.println("Core Name:"+ " "+arrayListOfCores.getFirst()+" "+"SRTF:"+
                arrayListOfCores.getFirst().getNumberOfSRTFProcess());

            }
            for (Process process : sjf) {
                sortCoreToLeastProcess();
                arrayListOfCores.get(0).addToSJFScheduler(process);
                System.out.println("assigning SJF");
                System.out.println("Core Name:"+ " "+arrayListOfCores.getFirst()+" "+"SJF:"+
                arrayListOfCores.getFirst().getNumberOfSJFProcess());
            }
            for (Process process : npps) {
                sortCoreToLeastProcess();
                arrayListOfCores.get(0).addToNPPSScheduler(process);
                System.out.println("assigning NPPS");
                System.out.println("Core Name:"+ " "+arrayListOfCores.getFirst().getName()+" "+"NPPS:"+
                        arrayListOfCores.getFirst().getNumberOfNPPSProcess());

            }
        }
    }
