import java.util.*;
import java.util.stream.Collectors;

public interface Sorter {
    /*
    * =============================================================================
    *                                 Sorter
    * =============================================================================
    * */

    static ArrayList<Process> sortByArrival(ArrayList<Process> processes){
        ArrayList<Process>sortedProcesses= (ArrayList<Process>) processes.
                stream()
                .sorted(Comparator.comparing(e-> e.getArrivalTime()))
                .collect(Collectors.toList());
        return  sortedProcesses;
    }
    static  ArrayList<Process> sortByBurstTime(ArrayList<Process> processes){
        return (ArrayList<Process>) processes
                .stream()
                .sorted(Comparator.comparingInt(e-> e.getBurstTime()))
                .collect(Collectors.toList());
    }
    // todo: get priority based on the needed priority
    static  ArrayList<Process> filterPriority(ArrayList<Process> processes, int priorityToGet){
        ArrayList<Process> sortedProcesses= (ArrayList<Process>) processes
                .stream()
                .filter(e -> e.getPrioritySchedule() == priorityToGet )
                .collect(Collectors.toList());
        return sortedProcesses;
    }

    static  ArrayList<Process> getArrivedProcess(ArrayList<Process> processes, int timer){
        return (ArrayList<Process>) processes.stream()
                .filter(e-> e.getArrivalTime() == timer) // get the process that have already arrived
                .sorted(Comparator.comparingInt(value -> value.getArrivalTime())) // if multiple process have arrived
                .collect(Collectors.toList());
    }
}
