package BackEndStuff;

import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        ArrayList<Process> processes = new ArrayList<>();

        // Adding some dummy processes
//        processes.add(new Process(1, 0, 5, 1));
//        processes.add(new Process(2, 2, 3, 2));
//        processes.add(new Process(3, 4, 1, 3));
//        processes.add(new Process(4, 6, 7, 1));
//        processes.add(new Process(5, 8, 4, 2));
//        processes.add(new Process(6, 3, 4, 1));
//        processes.add(new Process(7, 2, 4, 1));
//        processes.add(new Process(8, 2, 4, 1));
//        processes.add(new Process(9, 2, 4, 1));
//        processes.add(new Process(10, 2, 4, 1));
//        processes.add(new Process(11, 2, 4, 1));
//        processes.add(new Process(12, 2, 4, 1));



//        processes.add(new Process(9, 0, 3, 4,3));
//        processes.add(new Process(10, 0, 3, 4,1));
//        processes.add(new Process(11, 0, 3, 4,1));
//        processes.add(new Process(12, 0, 3, 4,4));
//
//        processes.add(new Process(13, 0, 3, 3));
//        processes.add(new Process(14, 0, 3, 3));
//        processes.add(new Process(15, 0, 3, 3));
//        processes.add(new Process(16, 0, 3, 3));
//        processes.add(new Process(21, 0, 2, 3));
//        processes.add(new Process(17, 0, 500, 3));
//        processes.add(new Process(18, 0, 2, 3));
//        processes.add(new Process(19, 0, 2, 3));
//        processes.add(new Process(20, 0, 2, 3));
//        processes.add(new Process(21, 1, 3, 3));
//        processes.add(new Process(22, 1, 3, 3));
//        processes.add(new Process(23, 1, 3, 3));
//        processes.add(new Process(24, 1, 3, 3));
//        processes.add(new Process(25, 1, 2, 3));
//        processes.add(new Process(26, 2, 2, 3));
//        processes.add(new Process(27, 2, 2, 3));
//        processes.add(new Process(28, 2, 2, 3));
//        processes1.add(new Process(29, 3, 2, 3));
//        processes.add(new Process(30, 3, 2, 3));
//        processes.add(new Process(31, 3, 2, 3));
//        processes.add(new Process(32, 3, 2, 3));
//
//        processes.add(new Process(33, 3, 2, 2));
//        processes.add(new Process(34, 50, 2, 2));
//
//        processes.add(new Process(1,2,10,4,1));
//        processes.add(new Process(2,2,10,4,1));
//        processes.add(new Process(3,2,10,4,1));
//        processes.add(new Process(4,1,3,4));
//        processes.add(new Process(10,1,5,4));
//        processes.add(new Process(5,2,3,2));
//        processes.add(new Process(6,1,10,1));
        processes.add(new Process(1,10,9,3,4));
        processes.add(new Process(2,85,14,3,3));
        processes.add(new Process(3,0,12,3,2));
        processes.add(new Process(4,89,10,3,2));
        processes.add(new Process(5,101,7,3,2));
        processes.add(new Process(6,15,15,3,1));
        processes.add(new Process(7,95,3,3,1));
        processes.add(new Process(8,5,4,3,3));
//        processes.add(new Process(8,1,10,2,3));
//
//        processes.add(new Process(9,10,10,1));


        processes.sort(Comparator.comparingInt(Process::getPid));

        SchedulingAlgo schedulingAlgo = new SchedulingAlgo(processes,1);
        schedulingAlgo.run();
    }
}
