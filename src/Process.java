import java.util.ArrayList;

public class Process {
    private int pid;
    private int arrivalTime;
    private int burstTime;
    private int schedulerPriority;
    private volatile boolean isRunning;
    private int timesExecuted;
    private int timeStarted ;
    private int timeEnd;
    private boolean hasExecuted;
    private boolean isFirstExecution;
    private String idle;
    private ArrayList<Integer> timesStarted;
    private ArrayList<Integer> timesEnded;
    private int processPriority;


    public Process(int pid, int arrivalTime, int burstTime, int schedulerPriority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.schedulerPriority = schedulerPriority;
        timesExecuted = 0;
        isRunning = false;
        hasExecuted = false;
        timesStarted = new ArrayList<>();
        timesEnded = new ArrayList<>();
        isFirstExecution = true;
    }

    public Process(int pid, int arrivalTime, int burstTime, int schedulerPriority, int processPriority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.schedulerPriority = schedulerPriority;
        this.processPriority = processPriority;
        timesExecuted = 0;
        isRunning = false;
        hasExecuted = false;
        timesStarted = new ArrayList<>();
        timesEnded = new ArrayList<>();
        isFirstExecution = true;
    }
    public Process(String idle, int arrivalTime, int burstTime, int schedulerPriority) {
        this.idle = idle;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.schedulerPriority = schedulerPriority;
        timesExecuted = 0;
        isRunning = false;
        hasExecuted = false;
        timesStarted = new ArrayList<>();
        timesEnded = new ArrayList<>();
    }

    public void setTimeEnd(int timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setProcessPriority(int processPriority) {
        this.processPriority = processPriority;
    }

    public int getProcessPriority() {
        return processPriority;
    }

    public void setTimeStarted(int timeStarted) {
        this.timeStarted = timeStarted;
    }

    public int getTimesExecuted() {
        return timesExecuted;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }


    public Process() {
        this.pid = 0;
        this.arrivalTime = 0;
        this.burstTime = 0;
        this.schedulerPriority = 0;
    }

    public int getPid() {
        return pid;
    }

    public boolean isFirstExecution() {
        return isFirstExecution;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
    public void addTimeStarted(int val){
        timesStarted.add(val);
    }

    public void setFirstExecution(boolean firstExecution) {
        isFirstExecution = firstExecution;
    }


    public void addTimeEnded(int val){
        timesEnded.add(val);
    }

    public ArrayList<Integer> getTimesStarted() {
        return timesStarted;
    }


    public ArrayList<Integer> getTimesEnded() {
        return timesEnded;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getPrioritySchedule() {
        return schedulerPriority;
    }

    public void setSchedulerPriority(int schedulerPriority) {
        this.schedulerPriority = schedulerPriority;
    }
    void updatePriority(){
        this.hasExecuted = true;
        timesExecuted++;
        if (timesExecuted == 5){
            schedulerPriority--;
            timesExecuted = 0;
        }
        if (schedulerPriority < 0 ){
            this.schedulerPriority = 1;

        }else if (schedulerPriority > 3){
            this.schedulerPriority =3;
        }
    }

    public int getTimeStarted() {
        return timeStarted;
    }

    public int getTimeEnd() {
        return timeEnd;
    }


    void lowerPriority(){
        this.hasExecuted = true;
        timesExecuted++;
        if (timesExecuted == 5){
            schedulerPriority++;
            timesExecuted = 0;
        }
        if (schedulerPriority < 0 ){
            this.schedulerPriority = 1;

        }else if (schedulerPriority > 3){
            this.schedulerPriority =3;
        }
    }
    boolean getHasExecuted() {
        return hasExecuted;
    }
}
