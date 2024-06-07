    import java.util.ArrayList;

    public class Core implements Sorter {
        RoundRobinScheduler roundRobinScheduler1;
        NPPS nonPreemptivePriorityScheduling4;
        SRTF shortestRemaininggTimeFirst2;
        SJF shortestJobFirst3;
        static int numberOfCores;
        ArrayList<Process> processes;
        GanttChart ganttChart ;
        String name;
        public Core(String s){
            this.name = s;
            this.roundRobinScheduler1 = new RoundRobinScheduler();
            this.shortestJobFirst3 = new SJF();
            this.shortestRemaininggTimeFirst2 = new SRTF();
            this.nonPreemptivePriorityScheduling4 = new NPPS();
            this.processes = new ArrayList<>();
            this.ganttChart = new GanttChart();

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

        public String getName() {
            return name;
        }

        public void runProcesses(){
            /*
           Executes the task based on the priority
           this will first execute if the roundRobin if the round robin is not empty
           then it will only execute the srtf the round robin its done
           then ....
            */
            if (!roundRobinScheduler1.isEmpty()){
                roundRobinScheduler1.run();
            } else if (!shortestRemaininggTimeFirst2.isEmpty()) {
                shortestRemaininggTimeFirst2.run();
            } else if (!shortestJobFirst3.isEmpty()) {
                shortestJobFirst3.run();
            } else if (!nonPreemptivePriorityScheduling4.isEmpty()) {
                nonPreemptivePriorityScheduling4.run(0);
            }else {
                //create an idle timer here
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
            return getNumberOfRoundRobinProcess()+
                    getNumberOfSRTFProcess()+
                    getNumberOfSJFProcess()+
                    getNumberOfNPPSProcess();
        }
        void addToRoundRobinScheduler (Process process){
           this.roundRobinScheduler1.addToqueue(processes);
        }
        void addToSRTFScheduler (Process process){
            this.shortestRemaininggTimeFirst2.addToqueue(processes);
        }
        void addToSJFScheduler (Process process){
            this.shortestJobFirst3.addToqueue(process);
        }
        void addToNPPSScheduler (Process process){
            this.nonPreemptivePriorityScheduling4.addToqueue(processes);
        }
    }
