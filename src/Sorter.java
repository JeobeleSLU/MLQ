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
        ArrayList<Process>sortedProcesses= (ArrayList<Process>) processes.
                stream()
                .sorted(Comparator.comparing(e-> e.getBurstTime()))
                .collect(Collectors.toList());
        return sortedProcesses;
    }
    // todo: get priority based on the needed priority
    static public ArrayList<Process> sortByPriority(ArrayList<Process> processes, int priorityToGet){
        ArrayList<Process>sortedProcesses= (ArrayList<Process>) processes
                .stream()
                .filter(e -> e.getPrioritySchedule() == priorityToGet )
                .collect(Collectors.toList());
        return sortedProcesses;

    }
}
