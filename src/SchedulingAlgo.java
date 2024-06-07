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
                arrayListOfCores.add(new Core("core"+i));
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
            public void run () {
            boolean areAllCoreEmpty = false;

                while (!processOnQueueList.isEmpty() || !processToQueueList.isEmpty()|| areAllCoreEmpty) {
                    areAllCoreEmpty = checkCoresWithProcess();

                    //get the arriving process
                    processOnQueueList.addAll(Sorter.getArrivedProcess(processToQueueList, timer));
                    assignProcess();
                    // removes the process that are already on the process on queue
                    //sort cores based on the number of least higher priority and add it to the
                    for (Process process : processOnQueueList) {
                        // add the process in to the core at index 0
                        sortCoreToLeastProcess();
                    }
                    processToQueueList.removeAll(processOnQueueList);
                    timer++;

                }

            }

        private boolean checkCoresWithProcess() {
            // Initialize processCounter to count the number of cores with processes
            int processCounter = 0;
            // Loop through each core and check if it has any processes
            for (Core core : arrayListOfCores) {
                if (core.getNumberOfProcesses() > 0) {
                    processCounter++;
                }
            }

            // If processCounter is 0, it means all cores are empty, hence return false
            // Otherwise, return true indicating that there are cores with processes
            return processCounter > 0;
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
        void assignProcess() {
            // Get the arrived processes
            ArrayList<Process> arrived = Sorter.getArrivedProcess(this.processToQueueList, this.timer);

            // Filter processes according to their priority
            ArrayList<Process> rr = Sorter.filterPriority(arrived, 1);
            ArrayList<Process> srtf = Sorter.filterPriority(arrived, 2);
            ArrayList<Process> sjf = Sorter.filterPriority(arrived, 3);
            ArrayList<Process> npps = Sorter.filterPriority(arrived, 4);

            // Add processes to the cores using load balancing
            for (Process process : rr) {
                sortCoreToLeastProcess();
                arrayListOfCores.get(0).addToRoundRobinScheduler(process);
            }
            for (Process process : srtf) {
                sortCoreToLeastProcess();
                arrayListOfCores.get(0).addToSRTFScheduler(process);
            }
            for (Process process : sjf) {
                sortCoreToLeastProcess();
                arrayListOfCores.get(0).addToSJFScheduler(process);
            }
            for (Process process : npps) {
                sortCoreToLeastProcess();
                arrayListOfCores.get(0).addToNPPSScheduler(process);
            }
        }
    }
