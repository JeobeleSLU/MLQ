import java.util.ArrayList;
import java.util.Comparator;

public class SJF implements  Sorter, ProcessInterface {
    private ArrayList<Process> readyQueue;
    private ArrayList<Process> processOnQueue;
    private GanttChart ganttChart;
    private int timer = 0;
    private ArrayList<Process> doneList;

    public SJF(ArrayList<Process> processes) {
        this.readyQueue  = new ArrayList<>();
        this.processOnQueue = new ArrayList<>();
        this.processOnQueue.addAll(processes);
        ganttChart = new GanttChart();

    }

    public SJF() {
        this.readyQueue  = new ArrayList<>();
        this.processOnQueue = new ArrayList<>();
        this.doneList = new ArrayList<>();
        ganttChart = new GanttChart();
    }
    /*

    Todo: Refactor this code so that you can call the run method to execute the
    process and minus the remaining burst time based on this algorithm

      */

    public int getCurrentTime() {
        return this.timer;
    }

    public void addToQueue(Process process) {
        readyQueue.add(process);
    }

    public void removeFromQueue(Process process) {
        readyQueue.remove(process);
    }

    @Override
    public int getQueueSize() {
       return readyQueue.size();
    }

    @Override
    public Process getNextProcess() {
        if (!readyQueue.isEmpty()) {
            return readyQueue.get(0); // Return the process with the shortest burst time
        } else {
            return null; // Queue is empty
        }
    }

    @Override
    public ArrayList<Process> getProcesses() {
        return readyQueue;
    }

    @Override
    public int getHighestPriority() {
        return readyQueue.stream()
                .mapToInt(Process::getPrioritySchedule)
                .max()
                .orElse(Integer.MIN_VALUE); // Return Integer.MIN_VALUE if the queue is empty
    }







            // Add arriving processes to ready queue
//            int finalTimer = this.timer;
//            readyQueue.addAll(processToQueue.stream()
//                    .filter(process -> process.getArrivalTime() == finalTimer)
//                    .toList());


//            processToQueue.removeAll(readyQueue);
            /*
            Get the process with least burst time
            execcute it
             */
    public void run(int timer) {
        this.timer = timer;
            readyQueue.sort(Comparator.comparingInt(Process::getBurstTime)); // Sort by burst time
                if (processOnQueue.isEmpty()){
                    processOnQueue.add(readyQueue.getFirst());
                    readyQueue.removeFirst();
                    processOnQueue.getFirst().setTimeStarted(this.timer); //this is a bug? because it would differ
                }

                if (processOnQueue.getFirst().getRemainingBurstTime() != 0 ){
                    System.out.println("SJF99"+"Executing process " + processOnQueue.getFirst().getPid() + " at time " + this.timer);
                    processOnQueue.getFirst().decrementBurst();
                    System.out.println("Remaining BurstTime: "+ processOnQueue.getFirst().getRemainingBurstTime());
                }
                if (processOnQueue.getFirst().getRemainingBurstTime() == 0){
                    System.out.println("Process " + processOnQueue.getFirst().getPid() + " completed at time " + this.timer);
                    processOnQueue.getFirst().setTimeEnd(this.timer);
                    ganttChart.addProcess(processOnQueue.getFirst());
                    System.out.println("Done, Removing , SJF Process ...." + processOnQueue.getFirst().getPid());
                    System.out.println(processOnQueue.getFirst().getRemainingBurstTime());
                    processOnQueue.clear();
                }
     }

    public ArrayList<Process> getGanttChartArray() {
        return this.ganttChart.getProcesses();
    }
    public ArrayList<Process> processessToQueue() {
        return processOnQueue;
    }

    @Override
    public boolean isEmpty() {
        return this.readyQueue.isEmpty() && this.processOnQueue.isEmpty();
    }

    @Override
    public int getNumberOfProcesses() {
        if (!this.readyQueue.isEmpty()){
            System.out.println("Ready Queue Not empty");
            this.readyQueue.forEach(e-> System.out.println("PID: " +e.getPid() ));
        }
        if (!this.processOnQueue.isEmpty()){
            System.out.println("Process Queue Not empty");
            this.processOnQueue.forEach(e-> System.out.println("PID: "+ e.getPid()+"Remaining BurstTime:"+e.getRemainingBurstTime()));
        }
        return
                this.readyQueue.size()+this.processOnQueue.size();
    }

    @Override
    public void addToqueue(ArrayList<Process> processes) {
        this.readyQueue.addAll(processes);
    }


    public void addToqueue(Process process) {
        this.readyQueue.add(process);
    }

    public ArrayList<Process> getProcessOnQueue() {
        return processOnQueue;
    }

    public ArrayList<Process> getDoneList() {
        return doneList;
    }
}
