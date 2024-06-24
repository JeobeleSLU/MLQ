package BackEndStuff;

import java.util.ArrayList;

public class Process {
    public final int BALL_SIZE = 25;
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
    private boolean isPreempted;
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
    final int speed = 4;
    private int readyQueueX;
    private int readyQueueY;
    private int coreIDAffinity;
    ArrayList <Integer> timeOnCore;


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
        this.remainingBurstTime = this.burstTime;
        this.originalBurst = burstTime;
        this.processPriority  = 3;
        readyQueueX = 0;
        readyQueueY = 0;
        timeOnCore = new ArrayList<>();
    }
    public Process() {
        this.pid = 0;
        this.arrivalTime = 0;
        this.burstTime = 0;
        this.schedulerPriority = 0;
        timesEnded = new ArrayList<>();
        timeOnCore = new ArrayList<>();
        timesStarted = new ArrayList<>();

    }


    public Process(int pid, int arrivalTime, int burstTime, int schedulerPriority, int processPriority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.schedulerPriority = schedulerPriority;
        this.processPriority = processPriority;
        timesExecuted = 0;
        this.remainingBurstTime = this.burstTime;
        isRunning = false;
        hasExecuted = false;
        timesStarted = new ArrayList<>();
        timesEnded = new ArrayList<>();
        isFirstExecution = true;
        this.isPreempted = false;
        readyQueueX = 0;
        readyQueueY = 0;
        timeOnCore = new ArrayList<>();
    }

    public void setPreempted(boolean preempted) {
        isPreempted = preempted;
    }

    public boolean isPreempted() {
        return isPreempted;
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


    public Process(int pid, ArrayList<Integer> timeStarted, int i) {
        this.pid = pid;
        this.timesStarted = getTimesStarted();
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
        timesStarted.add(this.timeStarted);
    }

    public int getTimesExecuted() {
        return timesExecuted;
    }

    public void setRunning(boolean running) {
        isRunning = running;
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
        this.responseTime = timeNow-this.arrivalTime;
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
//        this.turnAroundTime = this.timeEnd - this.timeStarted;
        if (this.schedulerPriority != 1){
            this.turnAroundTime = this.timeEnd - this.arrivalTime;
        }else
            this.turnAroundTime = timesEnded.getLast() - this.arrivalTime;
            System.out.println("Turn around time: "+this.turnAroundTime);
    }
    public void updateTimes() {
        calculateTurnAroundTime();
        calculateWaitingTime();
    }

    public void calculateWaitingTime() {
//        this.waitingTime = this.turnAroundTime - this.originalBurst;
        if (this.schedulerPriority == 2 || this.schedulerPriority == 3){
            this.waitingTime = this.timeStarted - this.arrivalTime;
        }else
            for (int i = 0; i < timesStarted.size(); i++) {
                if (i == 0) {
                    waitingTime += (timesStarted.get(i) - arrivalTime);
                } else {
                    waitingTime += (timesStarted.get(i) - timesEnded.get(i - 1));
                }
            }
        System.out.println("Waiting time: "+ this.waitingTime);;
    }
    void decrementBurst(){
        if (this.remainingBurstTime > 0){
            this.remainingBurstTime --;
        }else {
            updateTimes();
            System.out.println("Is zero");
        }
    }

    public ArrayList<Integer> getTimeOnCore() {
        return timeOnCore;
    }

    public void setCoreIDAffinity(int coreIDAffinity) {
        this.coreIDAffinity = coreIDAffinity;
    }
    void addTimeOnCore(int time){
        timeOnCore.add(time);
    }

    boolean isProcessDone(){
        return this.remainingBurstTime <= 0;
    }

    boolean isExecuting(int time) {
        for (Integer i : timesStarted) {
            if (time == i) {
                return true; // Return true if a match is found
            }
        }
        return false; // Return false if no match is found
    }
    //add Draw readyQueue here
}
