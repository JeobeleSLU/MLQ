    import java.util.ArrayList;

    public class Core implements Sorter {
        RoundRobinScheduler roundRobinScheduler1;
        NPPS nonPreemptivePriorityScheduling4;
        SRTF shortestRemaininggTimeFirst2;
        SJF shortestJobFirst3;
        static int numberOfCores;
        ArrayList<Process> processes;
        GanttChart ganttChart ;
        int timer;
        int coreID;
        public Core(int i){
            this.coreID = i;
            this.roundRobinScheduler1 = new RoundRobinScheduler();
            this.shortestJobFirst3 = new SJF();
            this.shortestRemaininggTimeFirst2 = new SRTF();
            this.nonPreemptivePriorityScheduling4 = new NPPS();
            this.processes = new ArrayList<>();
            this.ganttChart = new GanttChart();
            this.timer = 0;

            numberOfCores +=1;
            /*

            get the processes
            run the highest priority process
            based on the algorithm
            example if a process have arrive
            it should run the highest one first
            rounrobin.run something
            then check the lower priority

             */

        }
  /*
           Executes the task based on the priority
           this will first execute if the roundRobin if the round robin is not empty
           then it will only execute the srtf the round robin its done
           then ....
            */

        public void runProcesses(int time){
            this.timer = time;

            if (!roundRobinScheduler1.isEmpty()){
                roundRobinScheduler1.run(timer);
                System.out.println("RR is executing");
            } else if (!shortestRemaininggTimeFirst2.isEmpty()) {
                shortestRemaininggTimeFirst2.run(timer);
                System.out.println("SRTF is Executing");

            } else if (!shortestJobFirst3.isEmpty()) {
                shortestJobFirst3.run(timer);

                System.out.println("SJF is executing");
            } else if (!nonPreemptivePriorityScheduling4.isEmpty()) {
                nonPreemptivePriorityScheduling4.run(timer);
                System.out.println("NPPS is executing");
            }else {
                //todo:create an idle timer logic here
                System.out.println("Core:"+this.coreID+" is idling");
                System.out.println("No process");
            }

            /*
            Todo: Add Gantt Chart Logic to add process here
             may be it can use the ganttChart from each algorithm?
             */
        }
        void addProcess (ArrayList<Process> process){
            process.addAll(this.processes);
        }
        void addProcess(Process process){
            this.processes.add(process);
        }
        int getNumberOfRoundRobinProcess(){
            return this.roundRobinScheduler1.getNumberOfProcesses();

        }
        int getNumberOfNPPSProcess(){
            return this.nonPreemptivePriorityScheduling4.getNumberOfProcesses();

        }
        int getNumberOfSRTFProcess(){
            return this.shortestRemaininggTimeFirst2.getNumberOfProcesses();

        }
        int getNumberOfSJFProcess(){
            return this.shortestJobFirst3.getNumberOfProcesses();
        }
        //this may be used for multi something
        int getNumberOfCores(){
            return numberOfCores;
        }
        int getNumberOfProcesses(){
            System.out.println("\nRoundRobin Process:"+ getNumberOfRoundRobinProcess());
            System.out.println("SRTF Process:" + getNumberOfSRTFProcess());
            System.out.println("SJF Process:"+ getNumberOfSJFProcess());
            System.out.println("NPPS Process:"+ getNumberOfNPPSProcess()+"\n");
            return getNumberOfRoundRobinProcess()+
                    getNumberOfSRTFProcess()+
                    getNumberOfSJFProcess()+
                    getNumberOfNPPSProcess();
        }
        void addToRoundRobinScheduler (Process processToAdd){
           this.roundRobinScheduler1.addToQueue(processToAdd);
        }
        void addToSRTFScheduler (Process processToAdd){
            this.shortestRemaininggTimeFirst2.addToQueue(processToAdd);
        }
        void addToSJFScheduler (Process processToAdd){
            this.shortestJobFirst3.addToQueue(processToAdd);
        }
        void addToNPPSScheduler (Process processToAdd){
            this.nonPreemptivePriorityScheduling4.addToQueue(processToAdd);
        }

        public int getCoreID() {
            return coreID;
        }

        public void addLastToRoundRobinScheduler(Process process) {
            this.roundRobinScheduler1.readyQueue.addFirst(process);
        }

        public void setTimer(int timer) {
            this.timer = timer;
        }

        public ArrayList getSJFDoneList(){
            return this.shortestJobFirst3.getDoneList();
        }
    }
