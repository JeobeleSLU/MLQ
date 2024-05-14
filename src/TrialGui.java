
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

    public class TrialGui extends JFrame{
        private static final int WIDTH = 500;
        private static final int HEIGHT = 300;
        public void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame mainFrame = new JFrame();
                    mainFrame.setSize(WIDTH, HEIGHT);
                    mainFrame.setLocationRelativeTo(null);
                    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    mainFrame.setResizable(false);
                    mainFrame.getContentPane().setBackground(new Color(240, 233, 230));

                    // User input fields
                    JTextField priority = new JTextField(10);
                    JTextField burstTimeField = new JTextField(10);
                    JTextField arrivalTimeField = new JTextField(10);
                    JTextField timeQuantumField = new JTextField(10);
                    timeQuantumField.setVisible(false);

                    // Use GridBagLayout for the panel containing the box and dropdown
                    JPanel panel = new JPanel(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.anchor = GridBagConstraints.WEST;

                    panel.add(new JLabel("Priority Level [1. Real Time 2. System 3. Interactive 4. Batch "), gbc);
                    gbc.gridy++;
                    panel.add(priority, gbc);

                    gbc.gridy++;
                    panel.add(new JLabel("Burst Time:"), gbc);
                    gbc.gridy++;
                    panel.add(burstTimeField, gbc);

                    gbc.gridy++;
                    panel.add(new JLabel("Arrival Time:"), gbc);
                    gbc.gridy++;
                    panel.add(arrivalTimeField, gbc);

                    JButton continueButton = new JButton("Continue");
                    gbc.gridy++;
                    panel.add(continueButton, gbc);

                    continueButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Create and display an instance of the GUI class in a new window
                            new GUI();

                        }
                    });

                    mainFrame.add(panel);
                    mainFrame.setVisible(true);
                    addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent we) {
                            dispose();
                        }
                    });
                }
            });

        }}


