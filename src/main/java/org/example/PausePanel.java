package org.example;

import javax.swing.*;
import java.awt.*;

public class PausePanel extends JPanel {
    public PausePanel(GameManager manager, GamePanel gamePanel) {
        setLayout(null);
        setOpaque(false);
        ImageIcon resumeIcon = new ImageIcon(getClass().getResource("/images/resume.png"));
        ImageIcon homeIcon = new ImageIcon(getClass().getResource("/images/home.png"));
        ImageIcon resumeHoverIcon = new ImageIcon(getClass().getResource("/images/resume hover.png"));
        ImageIcon homeHoverIcon = new ImageIcon(getClass().getResource("/images/home Hover.png"));

        JButton resumeButton = new JButton(resumeIcon);
        resumeButton.setBounds(440, 250, resumeIcon.getIconWidth(), resumeIcon.getIconHeight());
        resumeButton.setBorderPainted(false);
        resumeButton.setContentAreaFilled(false);
        resumeButton.setFocusPainted(false);
        resumeButton.setOpaque(false);
        resumeButton.setRolloverIcon(resumeHoverIcon);
        resumeButton.addActionListener(e -> gamePanel.resumeGame());
        this.add(resumeButton);

        JButton homeButton = new JButton(homeIcon);
        homeButton.setBounds(440, 340, resumeIcon.getIconWidth(), resumeIcon.getIconHeight());
        homeButton.setBorderPainted(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setFocusPainted(false);
        homeButton.setOpaque(false);
        homeButton.setRolloverIcon(homeHoverIcon);
        homeButton.addActionListener(e -> manager.showScreen("launcher"));
        this.add(homeButton);

        setBounds(0, 0, 1080, 720);
        setVisible(true);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 128));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
