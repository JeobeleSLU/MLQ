import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main implements Sorter{
    public static void main(String[] args) {
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 990, 53,2));
        processes.add(new Process(2, 1000, 20, 1));
        processes.add(new Process(3, 1000, 33, 3));
        processes.add(new Process(4, 993, 99, 3));
        processes.add(new Process(5, 1000, 41, 2));
        processes.add(new Process(6, 999, 3, 2));
        processes.add(new Process(7, 1000, 20, 1));
        processes.add(new Process(8, 1000, 33, 1));
        processes.add(new Process(9, 993, 99, 1));
        processes.add(new Process(10, 1000, 41, 4,1));
        processes.add(new Process(13, 1000, 41, 4,3));
        processes.add(new Process(12, 1000, 41, 4,2));

            ArrayList<Process> rrArray = Sorter.sortByPriority(processes, 1);
        ArrayList<Process> srtfArray = processes.stream()
                .filter(p -> p.getPrioritySchedule() == 2)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Process> sjfArray = processes.stream()
                .filter(p -> p.getPrioritySchedule() == 3)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Process> nppsArray = Sorter.sortByPriority(processes, 4);



        /*
        *Todo: sort the array list based on the priority then it should spit out the priority
        * if it isnt == to that priority based on the scheduling it should remove that process
        * and revalidate the priority and sort it based on the priority
        *
        */
        RoundRobinScheduler rr = new RoundRobinScheduler(rrArray);
        SJF shortest = new SJF(sjfArray);
        SRTF shortesremain = new SRTF(srtfArray);
        NPPS npps = new NPPS(nppsArray);

        Thread thread1 = new Thread(rr);
        Thread thread2 = new Thread(shortesremain);
        Thread thread3 = new Thread(shortest);
        Thread thread4 = new Thread(npps);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GanttChart gantt = new GanttChart();
        shortesremain.getGanttChartArray();
        gantt.addProcess(npps.getGanttChartArray());
        gantt.addProcess(shortesremain.getGanttChartArray());
        gantt.addProcess(shortest.getGanttChartArray());

        gantt.displayChart();




//        SJF sjf = new SJF(processes);
//        sjf.run();
//        SRTF srtf = new SRTF(processes);
//        srtf.run();

//        rrs.run();


    }
}