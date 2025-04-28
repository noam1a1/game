package org.example;

import javax.swing.*;
import java.awt.*;

public class CharacterSelectPanel extends JPanel {
    private final Image BG;
    private final PlayerSelection selection;

    public CharacterSelectPanel(GameManager manager , PlayerSelection selection) {
        this.selection = selection;
        this.setLayout(null);
        BG = new ImageIcon(getClass().getResource("/images/CharacterSelectBG.png")).getImage();
        ImageIcon startIcon = new ImageIcon(getClass().getResource("/images/Start.png"));
        ImageIcon startHoverIcon = new ImageIcon(getClass().getResource("/images/Start hover.png"));
        ImageIcon player1SelectBorisIcon = new ImageIcon(getClass().getResource("/images/Select_Boris.png"));
        ImageIcon player1SelectBorisHoverIcon = new ImageIcon(getClass().getResource("/images/Select_Boris_Hover.png"));
        ImageIcon player1SelectDvoraIcon = new ImageIcon(getClass().getResource("/images/Select_Dvora.png"));
        ImageIcon player1SelectDvoraHoverIcon = new ImageIcon(getClass().getResource("/images/Select_Dvora_Hover.png"));
        ImageIcon player2SelectBorisIcon = new ImageIcon(getClass().getResource("/images/Select_Boris_Right.png"));
        ImageIcon player2SelectBorisHoverIcon = new ImageIcon(getClass().getResource("/images/Select_Boris_Right_Hover.png"));
        ImageIcon player2SelectDvoraIcon = new ImageIcon(getClass().getResource("/images/Select_Dvora_Right.png"));
        ImageIcon player2SelectDvoraHoverIcon = new ImageIcon(getClass().getResource("/images/Select_Dvora_Right_Hover.png"));

        JButton startButton = new JButton(startIcon);
        startButton.setBounds(440, 550, startIcon.getIconWidth(), startIcon.getIconHeight());
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);
        startButton.setRolloverIcon(startHoverIcon);
        startButton.addActionListener(e -> manager.showScreen("game"));
        this.add(startButton);

        JButton player1SelectBorisButton = new JButton(player1SelectBorisIcon);
        player1SelectBorisButton.setBounds(68, 300, player1SelectBorisIcon.getIconWidth(), player1SelectBorisIcon.getIconHeight());
        player1SelectBorisButton.setBorderPainted(false);
        player1SelectBorisButton.setContentAreaFilled(false);
        player1SelectBorisButton.setFocusPainted(false);
        player1SelectBorisButton.setOpaque(false);
        player1SelectBorisButton.setRolloverIcon(player1SelectBorisHoverIcon);
        player1SelectBorisButton.addActionListener(e -> {
            selection.setPlayer1Type(1);
            System.out.println("player 1 = boris");        });
        this.add(player1SelectBorisButton);

        JButton player1SelectDvoraButton = new JButton(player1SelectDvoraIcon);
        player1SelectDvoraButton.setBounds(277, 300, player1SelectDvoraIcon.getIconWidth(), player1SelectDvoraIcon.getIconHeight());
        player1SelectDvoraButton.setBorderPainted(false);
        player1SelectDvoraButton.setContentAreaFilled(false);
        player1SelectDvoraButton.setFocusPainted(false);
        player1SelectDvoraButton.setOpaque(false);
        player1SelectDvoraButton.setRolloverIcon(player1SelectDvoraHoverIcon);
        player1SelectDvoraButton.addActionListener(e -> {
            selection.setPlayer1Type(2);
            System.out.println("player 1 = Dvora");        });
        this.add(player1SelectDvoraButton);

        JButton player2SelectBorisButton = new JButton(player2SelectBorisIcon);
        player2SelectBorisButton.setBounds(837, 300, player2SelectBorisIcon.getIconWidth(), player2SelectBorisIcon.getIconHeight());
        player2SelectBorisButton.setBorderPainted(false);
        player2SelectBorisButton.setContentAreaFilled(false);
        player2SelectBorisButton.setFocusPainted(false);
        player2SelectBorisButton.setOpaque(false);
        player2SelectBorisButton.setRolloverIcon(player2SelectBorisHoverIcon);
        player2SelectBorisButton.addActionListener(e -> {
            selection.setPlayer2Type(1);
            System.out.println("player 2 = boris");        });
        this.add(player2SelectBorisButton);

        JButton player2SelectDvoraButton = new JButton(player2SelectDvoraIcon);
        player2SelectDvoraButton.setBounds(628, 300, player2SelectDvoraIcon.getIconWidth(), player2SelectDvoraIcon.getIconHeight());
        player2SelectDvoraButton.setBorderPainted(false);
        player2SelectDvoraButton.setContentAreaFilled(false);
        player2SelectDvoraButton.setFocusPainted(false);
        player2SelectDvoraButton.setOpaque(false);
        player2SelectDvoraButton.setRolloverIcon(player2SelectDvoraHoverIcon);
        player2SelectDvoraButton.addActionListener(e -> {
            selection.setPlayer2Type(2);
            System.out.println("player 2 = Dvora");        });
        this.add(player2SelectDvoraButton);
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
