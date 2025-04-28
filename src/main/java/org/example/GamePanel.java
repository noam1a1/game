package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel {
    private final Image ARENA_BG;
    private final PlayerSelection selection;
    private final Player player1;
    private final Player player2;
    private JLabel playerNameLabel1;
    private JLabel playerNameLabel2;
    private Thread aiThread;
    private boolean aiRunning = true;
    private final Random random = new Random();



    public GamePanel(GameManager manager, PlayerSelection selection) {
        this.selection = selection;
        this.setLayout(null);
        setFocusable(true);
        ARENA_BG = new ImageIcon(getClass().getResource("/images/ArenaBG.png")).getImage();
        player1 = new Player(100, 300, selection.getPlayer1Type(),true);
        player2 = new Player(900, 300, selection.getPlayer2Type(),false);

        playerNameLabel1 = new JLabel(player1.getName());
        playerNameLabel1.setFont(new Font("Impact", Font.BOLD, 24));
        playerNameLabel1.setForeground(Color.WHITE);
        playerNameLabel1.setBounds(100, 30, 200, 30);
        add(playerNameLabel1);

        playerNameLabel2 = new JLabel(player2.getName());
        playerNameLabel2.setFont(new Font("Impact", Font.BOLD, 24));
        playerNameLabel2.setForeground(Color.WHITE);
        playerNameLabel2.setBounds(910, 30, 200, 30);
        add(playerNameLabel2);

        aiThread = new Thread(() -> {
            long lastDecisionTime = System.currentTimeMillis();
            while (aiRunning) {
                long now = System.currentTimeMillis();

                if (now - lastDecisionTime > 300) {
                    decideDirection();
                    lastDecisionTime = now;
                }

                player2.moveSmooth();
                repaint();

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        aiThread.start();


        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> player1.moveLeft();
                    case KeyEvent.VK_RIGHT -> player1.moveRight();
                    case KeyEvent.VK_X -> player1.takeDamage(5);
                    case KeyEvent.VK_C -> player1.buff(5);
                    case KeyEvent.VK_Q -> player1.attack(player2);
                    case KeyEvent.VK_R -> player1.specialAttack(player2);

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

    private void chasePlayer() {
        int distance = player2.getCenterX() - player1.getCenterX();
        boolean makeMistake = random.nextInt(100) < 15;

        if (Math.abs(distance) <= 100) {
            if (!makeMistake) {
                player2.attack(player1);
            } else {

                if (random.nextBoolean()) {
                    player2.moveLeft();
                } else {
                    player2.moveRight();
                }
            }
        } else if (distance > 0) {
            if (!makeMistake) {
                player2.moveLeft();
            } else {
                player2.moveRight();
            }
        } else {
            if (!makeMistake) {
                player2.moveRight();
            } else {
                player2.moveLeft();
            }
        }
    }

    private void decideDirection() {
        int distance = player2.getCenterX() - player1.getCenterX();
        boolean makeMistake = random.nextInt(100) < 15;
        boolean chooseIdle = random.nextInt(100) < 10;

        if (chooseIdle) {
            player2.setMoveDirection(null);
            return;
        }

        if (Math.abs(distance) <= 100) {
            if (!makeMistake) {
                player2.attack(player1);
                player2.setMoveDirection(null);
            } else {
                player2.setMoveDirection(random.nextBoolean() ? Direction.LEFT : Direction.RIGHT);
            }
        } else if (distance > 0) {
            if (!makeMistake) {
                player2.setMoveDirection(Direction.LEFT);
            } else {
                player2.setMoveDirection(Direction.RIGHT);
            }
        } else {
            if (!makeMistake) {
                player2.setMoveDirection(Direction.RIGHT);
            } else {
                player2.setMoveDirection(Direction.LEFT);
            }
        }
    }



    @Override
    public void removeNotify() {
        super.removeNotify();
        aiRunning = false;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1080, 720);
    }
}
