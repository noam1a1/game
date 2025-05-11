package org.example;

import javax.swing.*;
import java.awt.*;

public class WinPanel extends JPanel {
    private final Image BG;

    public WinPanel(GameManager manager, String winnerName) {
        this.setLayout(null);


        String imagePath = "/images/WIN_" + winnerName.toLowerCase() + ".png";
        BG = new ImageIcon(getClass().getResource(imagePath)).getImage();

        ImageIcon backIcon = new ImageIcon(getClass().getResource("/images/Back.png"));
        ImageIcon backHoverIcon = new ImageIcon(getClass().getResource("/images/Back hover.png"));

        JButton backButton = new JButton(backIcon);
        backButton.setBounds(750, 540, backIcon.getIconWidth(), backIcon.getIconHeight());
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setRolloverIcon(backHoverIcon);
        backButton.addActionListener(e -> {
            SoundPlayer.playSound("click.wav");
            manager.showScreen("launcher");
        });
        this.add(backButton);
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
