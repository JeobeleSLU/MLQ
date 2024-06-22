package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

public class Welcome extends JFrame {
    BufferedImage bg;

    public Welcome() {
        // Set up the frame
        setTitle("CPU Simulation");
        setSize(900, 700); // Increased size for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getResources();

        // Create a custom panel with background image

        BackgroundPanel panel = new BackgroundPanel(bg);
        panel.setLayout(new BorderLayout());

        // Create a sub-panel for welcome text and start button
        JPanel textPanel = new JPanel(new GridBagLayout());
        textPanel.setOpaque(false); // Make the panel transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Create welcome label
        JLabel welcomeLabel = new JLabel("Welcome to CPU Simulation");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        textPanel.add(welcomeLabel, gbc);

        // Create a button to show the meaning of CPU simulation
        JButton meaningButton = new JButton("What is CPU Simulation?");
        meaningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display meaning text in a dialog box
                JTextArea meaningTextArea = new JTextArea("Meaning: \n" +
                        "A CPU simulation is a software or educational tool \n" +
                        "that emulates the behavior and functionality of a Central \n" +
                        "Processing Unit (CPU). The purpose of such simulations can vary, but generally, they are used for understanding, analyzing, and teaching how a CPU works.\n" +
                        "\n" +
                        "To learn more about the CPU algorithms, check for an 'about' button later.." );
                meaningTextArea.setFont(new Font("Times New Roman", Font.PLAIN, 16));
                meaningTextArea.setWrapStyleWord(true);
                meaningTextArea.setLineWrap(true);
                meaningTextArea.setOpaque(false);
                meaningTextArea.setEditable(false);

                JScrollPane scrollPane = new JScrollPane(meaningTextArea);
                scrollPane.setPreferredSize(new Dimension(400, 150));

                JOptionPane.showMessageDialog(null, scrollPane, "Meaning of CPU Simulation", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        textPanel.add(meaningButton, gbc);

        // Create start button
        JButton startButton = new JButton("Start Simulation");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open MyFrame window
                new MyFrame().setVisible(true);
                dispose(); // Close the welcome window
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        textPanel.add(startButton, gbc);

        // Add the text panel to the center of the main panel
        panel.add(textPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(panel);

        // Set frame visibility
        setVisible(true);
    }

    private void getResources() {
        try {
            bg = read((getClass().getResourceAsStream("/resources/cpu.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Create and display the welcome page
        SwingUtilities.invokeLater(() ->{
                new Welcome();
        });
    }
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
    }
}
