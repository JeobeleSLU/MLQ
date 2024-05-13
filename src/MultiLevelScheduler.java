//import java.util.ArrayList;
//
//public class MultiLevelScheduler implements Scheduler {
//    private ArrayList<Process> readyQueue = new ArrayList<>();
//    private ArrayList<Process> processes;
//    private SJFScheduler sjfScheduler;
//    private SRTFScheduler srtfScheduler;
//    private PPSScheduler ppsScheduler;
//
//    public MultiLevelScheduler(ArrayList<Process> sjfProcesses, ArrayList<Process> srtfProcesses, ArrayList<Process> ppsProcesses) {
//        this.processes = new ArrayList<>(sjfProcesses);
//        this.sjfScheduler = new SJFScheduler(sjfProcesses);
//        this.srtfScheduler = new SRTFScheduler(srtfProcesses);
//        this.ppsScheduler = new PPSScheduler(ppsProcesses);
//    }
//a
//    @Override
//    public void runProcess(Process process) {
//        if (!sjfScheduler.getProcesses().isEmpty()) {
//            sjfScheduler.runProcess(process);
//        } else if (!srtfScheduler.getProcesses().isEmpty()) {
//            srtfScheduler.runProcess(process);
//        } else {
//            ppsScheduler.runProcess(process);
//        }
//    }
//
//    @Override
//    public ArrayList<Process> getProcesses() {
//        return readyQueue;
//    }
//}
