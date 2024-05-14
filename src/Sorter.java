import java.util.*;
import java.util.stream.Collectors;

public interface Sorter {
    /*
    * =============================================================================
    *                                 Sorter
    * =============================================================================
    * */

    static public ArrayList<Process> sortByArrival(ArrayList<Process> processes){
        ArrayList<Process>sortedProcesses= (ArrayList<Process>) processes.
                stream()
                .sorted(Comparator.comparing(e-> e.getArrivalTime()))
                .collect(Collectors.toList());
        return sortedProcesses;
    }
    static public ArrayList<Process> sortByBurstTime(ArrayList<Process> processes){
        return (ArrayList<Process>) processes
                .stream()
                .sorted(Comparator.comparingInt(e-> e.getBurstTime()))
                .collect(Collectors.toList());
    }
    // todo: get priority based on the needed priority
    static public ArrayList<Process> sortByPriority(ArrayList<Process> processes, int priorityToGet){
        ArrayList<Process> sortedProcesses= (ArrayList<Process>) processes
                .stream()
                .filter(e -> e.getPrioritySchedule() == priorityToGet )
                .collect(Collectors.toList());
        return sortedProcesses;
    }
    static public ArrayList<Process> getArrivingProcess(ArrayList<Process> processes, int timer){
        return processes = (ArrayList<Process>) processes.stream()
                .filter(e-> e.getArrivalTime() == timer)
                .collect(Collectors.toList());
    }

}
