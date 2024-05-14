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
    private int responseTime;
    private int turnAroundTime;
    private int waitingTime;
    private int timeNow;
    private int lastExecutedTime = 0;
    private int remainingBurstTime;
    private int originalBurst;


    public Process(int pid, int arrivalTime, int burstTime, int schedulerPriority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.schedulerPriority = schedulerPriority;
        this.timesExecuted = 0;
        this.isRunning = false;
        this.hasExecuted = false;
        this.timesStarted = new ArrayList<>();
        this.timesEnded = new ArrayList<>();
        this.isFirstExecution = true;
        this.timeNow = 0;
        this.turnAroundTime = 0;
        this.waitingTime =0;
        this.remainingBurstTime = burstTime;
        this.originalBurst = burstTime;
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

    public void setRemainingBurstTime(int remainingBurstTime) {
        this.remainingBurstTime = remainingBurstTime;
    }

    public int getRemainingBurstTime() {
        return remainingBurstTime;
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
        this.isFirstExecution = firstExecution;
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

    public void setTimesStarted(ArrayList<Integer> timesStarted) {
        this.timesStarted = timesStarted;
    }
    public ArrayList<Integer> setTimesEnded(ArrayList<Integer> timesEnded) {
        return timesEnded;
    }
    public void setLastExecutedTime(int time) {
        lastExecutedTime = time;
    }

    public int getLastExecutedTime() {
        return lastExecutedTime;
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

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public void setTimeNow(int timeNow) {
        this.timeNow = timeNow;
        this.responseTime =timeNow-this.arrivalTime;
        System.out.println(responseTime);
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTimeNow() {
        return timeNow;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public int getResponseTime() {
        return responseTime;
    }
    public void calculateTurnAroundTime() {
        this.turnAroundTime = this.timeEnd - this.timeStarted;
    }
    public void updateTimes() {
        calculateTurnAroundTime();
        calculateWaitingTime();
    }

    public void calculateWaitingTime() {
        this.waitingTime = this.turnAroundTime - this.originalBurst;
    }
}
