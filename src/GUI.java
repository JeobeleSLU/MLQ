import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI extends JFrame {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 850;
    private LineDrawing line;
    private SRTF srtf;
    private JTable table;
    private DefaultTableModel model;

    public GUI() {
        // Initialize SRTF with some processes
        ArrayList<Process> processes = createProcesses(); // Create processes as per your requirements
        srtf = new SRTF(processes);

        // Initialize GUI components
        initUI();

        // Run SRTF scheduler in a separate thread
        new Thread(() -> srtf.run()).start();

        // Set up a timer to update the GUI periodically
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                line.moveBalls();
              //  updateTable();
                repaint();
            }
        });
        timer.start();
    }

    private ArrayList<Process> createProcesses() {
        // Create a list of processes (for example purposes)
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 8, 2));
        processes.add(new Process(2, 1, 4, 1));
        processes.add(new Process(3, 2, 9, 3));
        processes.add(new Process(4, 3, 5, 2));
        return processes;
    }

    private void initUI() {
        JLayeredPane jLayeredPane = new JLayeredPane();
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(240, 233, 230));
        getContentPane().add(jLayeredPane);

        JPanel topPanel = new JPanel();
        topPanel.setBounds(0, 0, 1600, 50);
        topPanel.setBackground(new Color(39, 46, 54));
        jLayeredPane.add(topPanel, Integer.valueOf(1));

        JPanel leftPanel = new JPanel();
        leftPanel.setBounds(0, 80, 100, 700);
        leftPanel.setBackground(new Color(39, 46, 48));
        jLayeredPane.add(leftPanel, Integer.valueOf(3));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBounds(100, 780, 1500, 90);
        bottomPanel.setBackground(new Color(39, 46, 54));
        jLayeredPane.add(bottomPanel, Integer.valueOf(3));

        JPanel rlPanel = new JPanel();
        rlPanel.setBounds(200, 90, 200, 100);
        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
        rlPanel.setBorder(blackBorder);
        jLayeredPane.add(rlPanel, Integer.valueOf(4));

        JPanel sysPanel = new JPanel();
        sysPanel.setBounds(200, 260, 200, 100);
        Border blackBorder1 = BorderFactory.createLineBorder(Color.BLACK);
        sysPanel.setBorder(blackBorder1);
        jLayeredPane.add(sysPanel, Integer.valueOf(4));

        JPanel batchPanel = new JPanel();
        batchPanel.setBounds(200, 460, 200, 100);
        Border blackBorder2 = BorderFactory.createLineBorder(Color.BLACK);
        batchPanel.setBorder(blackBorder2);
        jLayeredPane.add(batchPanel, Integer.valueOf(4));

        JPanel interactPanel = new JPanel();
        interactPanel.setBounds(200, 660, 200, 100);
        Border blackBorder3 = BorderFactory.createLineBorder(Color.BLACK);
        interactPanel.setBorder(blackBorder3);
        jLayeredPane.add(interactPanel, Integer.valueOf(4));

        JPanel core1Panel = new JPanel();
        core1Panel.setBounds(500, 100, 200, 200);
        core1Panel.setBackground(new Color(39, 46, 54));
        jLayeredPane.add(core1Panel, Integer.valueOf(8));

        JPanel core2Panel = new JPanel();
        core2Panel.setBounds(500, 450, 200, 200);
        core2Panel.setBackground(new Color(39, 46, 54));
        jLayeredPane.add(core2Panel, Integer.valueOf(9));

        JPanel exitPanel = new JPanel();
        exitPanel.setBounds(1400, 80, 100, 650);
        exitPanel.setBackground(new Color(39, 46, 48));
        jLayeredPane.add(exitPanel, Integer.valueOf(10));

        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(800, 70, 450, 210);
        tablePanel.setBackground(new Color(50, 50, 50));
        jLayeredPane.add(tablePanel, Integer.valueOf(1));

        line = new LineDrawing(WIDTH);
        line.setBounds(100, 30, WIDTH, HEIGHT);
        jLayeredPane.add(line, Integer.valueOf(2));

        String[] columnNames = {"Process ID", "Turn Around Time", "Waiting Time", "Response Time"};
        Object[][] data = new Object[15][4];
        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);
        table.setGridColor(Color.WHITE);
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(70, 70, 70));

        JTableHeader header = table.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(30, 30, 30));

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);

        setVisible(true);
    }

//    private void updateTable() {
//        ArrayList<Process> processes = srtf.getProcesses();
//        for (int i = 0; i < processes.size(); i++) {
//            Process process = processes.get(i);
//            model.setValueAt(process.getPid(), i, 0);
//            model.setValueAt(process.getTurnAroundTime(), i, 1);
//            model.setValueAt(process.getWaitingTime(), i, 2);
//            model.setValueAt(process.getResponseTime(), i, 3);
//        }
//    }

    public static class LineDrawing extends JPanel {
        private final int width;
        private int ball1X = 300;
        private int ball1Y = 100;
        private int ball2X = 300;
        private int ball2Y = 250;

        public LineDrawing(int width) {
            this.width = width;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBalls(g);
            drawThirdSetOflines(g);
            drawFourthSetOflines(g);
            drawFifthSetOflines(g);
        }

        private void drawBalls(Graphics g) {
            drawBall(g, ball1X, ball1Y);
            drawBall(g, ball2X, ball2Y);
        }

        private void drawBall(Graphics g, int x, int y) {
            g.setColor(Color.RED);
            g.fillOval(x, y, 20, 20);
        }

        public void moveBalls() {
            moveBall1();
            moveBall2();
        }

        private void moveBall1() {
            if (ball1Y < 650) {
                ball1Y += 1;
            }
        }

        private void moveBall2() {
            if (ball2X < 700) {
                ball2X += 1;
            }
        }

        private int calculateX(int y, int x1, int y1, int x2) {
            double slope = (double) (y1 - y) / (x1 - x2);
            return (int) ((y - y1) / slope + x1);
        }

        private void drawThirdSetOflines(Graphics g) {
            g.setColor(Color.black);
            g.drawLine(300, 700, 300, 100);
        }

        private void drawFourthSetOflines(Graphics g) {
            g.setColor(Color.black);
            g.drawLine(300, 600, 600, 600);
        }

        private void drawFifthSetOflines(Graphics g) {
            g.setColor(Color.black);
            g.drawLine(300, 300, 500, 300);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}