package org.example;

import javax.swing.*;
import java.awt.*;

public class LauncherPanel extends JPanel {
    private final Image BG;

    public LauncherPanel(GameManager manager) {
        this.setLayout(null);
        BG = new ImageIcon(getClass().getResource("/images/LauncherBG.png")).getImage();
        ImageIcon startIcon = new ImageIcon(getClass().getResource("/images/Start.png"));
        ImageIcon howToPlayIcon = new ImageIcon(getClass().getResource("/images/HowToPlay.png"));
        ImageIcon startHoverIcon = new ImageIcon(getClass().getResource("/images/Start hover.png"));
        ImageIcon howToPlayHoverIcon = new ImageIcon(getClass().getResource("/images/HowToPlay Hover.png"));

        JButton startButton = new JButton(startIcon);
        startButton.setBounds(600, 475, startIcon.getIconWidth(), startIcon.getIconHeight());
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);
        startButton.setRolloverIcon(startHoverIcon);
        startButton.addActionListener(e -> manager.showScreen("select"));
        this.add(startButton);

        JButton howToPlayButton = new JButton(howToPlayIcon);
        howToPlayButton.setBounds(820, 475, howToPlayIcon.getIconWidth(), howToPlayIcon.getIconHeight());
        howToPlayButton.setBorderPainted(false);
        howToPlayButton.setContentAreaFilled(false);
        howToPlayButton.setFocusPainted(false);
        howToPlayButton.setOpaque(false);
        howToPlayButton.setRolloverIcon(howToPlayHoverIcon);
        howToPlayButton.addActionListener(e -> manager.showScreen("howToPlay"));
        this.add(howToPlayButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BG, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1080, 720);
    }
}
