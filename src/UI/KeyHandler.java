package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyHandler implements MouseListener {

    private JPanel panel;

    public KeyHandler(JPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse Clicked at (" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        panel.setBackground(Color.RED);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        panel.setBackground(Color.GREEN);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        panel.setBackground(Color.BLUE);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        panel.setBackground(Color.YELLOW);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MouseListener Example");
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 400));

        KeyHandler keyHandler = new KeyHandler(panel);
        panel.addMouseListener(keyHandler);

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
