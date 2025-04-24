package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private final Image ARENA_BG;
    private final Player player1;
    private final Player player2;
    private JLabel playerNameLabel1;
    private JLabel playerNameLabel2;



    public GamePanel(GameManager manager) {
        this.setLayout(null);
        setFocusable(true);
        ARENA_BG = new ImageIcon(getClass().getResource("/images/ArenaBG.png")).getImage();
        player1 = new Player(100, 300, 1);
        player2 = new Player(900, 300, 2);

        playerNameLabel1 = new JLabel(player1.getName());
        playerNameLabel1.setFont(new Font("Impact", Font.BOLD, 24));
        playerNameLabel1.setForeground(Color.WHITE);
        playerNameLabel1.setBounds(100, 30, 200, 30);
        add(playerNameLabel1);

        playerNameLabel2 = new JLabel(player2.getName());
        playerNameLabel2.setFont(new Font("Impact", Font.BOLD, 24));
        playerNameLabel2.setForeground(Color.WHITE);
        playerNameLabel2.setBounds(900, 30, 200, 30);
        add(playerNameLabel2);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> player1.moveLeft();
                    case KeyEvent.VK_RIGHT -> player1.moveRight();
                    case KeyEvent.VK_X -> player1.takeDamage(5);
                    case KeyEvent.VK_C -> player1.buff(5);
                    case KeyEvent.VK_Q -> player1.attack(player2);



                }
                repaint();
            }
        });
        SwingUtilities.invokeLater(this::requestFocusInWindow);


    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(ARENA_BG, 0, 0, getWidth(), getHeight(), this);
        player1.draw(g);
        player2.draw(g);

    }
    @Override
    public void addNotify() {
        super.addNotify();

        setFocusable(true);
        requestFocusInWindow();

        SwingUtilities.invokeLater(() -> {
            if (!hasFocus()) {
                requestFocusInWindow();
            }
        });
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1080, 720);
    }
}
