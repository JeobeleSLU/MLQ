public class Idle {
    private int startTime;
    private int endTime;
    private boolean hasExecuted;

    public Idle(int startTime, int endTime){
        this.startTime = startTime;
        this.endTime = endTime;
        hasExecuted = false;
    }
    public Idle(){
        this.startTime = 0;
        this.endTime = 0;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
