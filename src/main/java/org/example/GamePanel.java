package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel {
    private final Image ARENA_BG;
    private final PlayerSelection selection;
    private final Player player1;
    private final Player player2;
    private JLabel playerNameLabel1;
    private JLabel playerNameLabel2;
    private JLabel score;
    private Thread aiThread;
    private boolean aiRunning = true;
    private final Random random = new Random();
    private List<Potion> potions = new ArrayList<>();
    private Thread potionSpawnerThread;
    private boolean spawnerRunning = true;
    private int player1Score = 0;
    private int player2Score = 0;
    private final int WIN_SCORE = 3;
    private boolean roundActive = true;
    private JLabel roundLabel;
    private boolean roundStarting = false;
    private static final Color PLAYER1_WIN_COLOR = new Color(107, 177, 124);
    private static final Color PLAYER2_WIN_COLOR = new Color(206, 84, 51);
    private PausePanel pausePanel;
    private boolean gamePaused = false;
    private GameManager manager;
    private List<AttackEffect> effects = new ArrayList<>();
    private final List<FallingObject> fallingObjects = new ArrayList<>();
    private static GamePanel instance;







    public GamePanel(GameManager manager, PlayerSelection selection) {
        instance = this;
        this.selection = selection;
        this.manager = manager;
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

        score = new JLabel(player1Score + " : " + player2Score);
        score.setFont(new Font("Impact", Font.BOLD, 30));
        score.setForeground(Color.WHITE);
        score.setBounds(500, 30, 200, 30);
        add(score);

        roundLabel = new JLabel("", SwingConstants.CENTER);
        roundLabel.setFont(new Font("Impact", Font.BOLD, 60));
        roundLabel.setForeground(Color.WHITE);
        roundLabel.setBounds(0, 200, 1080, 100);
        roundLabel.setVisible(true);
        add(roundLabel);

        startPotionSpawner();
        resetRound(Color.WHITE);


        aiThread = new Thread(() -> {
            long lastDecisionTime = System.currentTimeMillis();
            while (aiRunning) {
                long now = System.currentTimeMillis();

                fallingObjects.removeIf(obj -> !obj.isActive());
                for (FallingObject obj : fallingObjects) {
                    obj.update();
                    if (obj.isActive() && obj.getBounds().intersects(player2.getHitbox())) {
                        player2.takeDamage(30);
                        obj.deactivate();
                    }
                }
                if (!roundStarting && !gamePaused && now - lastDecisionTime > 300) {
                    decideDirection();
                    lastDecisionTime = now;
                }

                if (!roundStarting && !gamePaused) {
                    player2.moveSmooth();
                }

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
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                    return;
                }
                if (roundStarting || gamePaused) return;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        player1.moveLeft();
                        checkPotionCollision();

                    }
                    case KeyEvent.VK_RIGHT -> {
                        player1.moveRight();
                        checkPotionCollision();
                    }
                    case KeyEvent.VK_X -> player1.takeDamage(5);
                    case KeyEvent.VK_C -> player1.buff(5);
                    case KeyEvent.VK_Q -> player1.attack(player2);
                    case KeyEvent.VK_R -> {
                        if (player1.getType()==2) {
                            player1.specialAttack(player2, fallingObjects);
                        } else {
                            player1.specialAttack(player2);
                        }
                    }                    case KeyEvent.VK_V -> player1.specialAttackBuff(0.2);
                    case KeyEvent.VK_E -> player1.startDefense();




                }
                repaint();
                checkRoundEnd();
            }
            public void keyReleased(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_E){
                    player1.stopDefense();
                }


            }
        });

        SwingUtilities.invokeLater(this::requestFocusInWindow);


    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(ARENA_BG, 0, 0, getWidth(), getHeight(), this);
        player1.draw(g);
        player2.draw(g);
        for (Potion potion : potions) {
            potion.draw(g);
        }
        for (AttackEffect effect : effects) {
            effect.draw(g);
        }
        effects.removeIf(e -> !e.isActive());
        for (FallingObject obj : new ArrayList<>(fallingObjects)) {
            obj.draw(g);
        }
    }
    private void checkPotionCollision() {
        Rectangle playerBounds = player1.getHitbox();

        Iterator<Potion> iterator = potions.iterator();
        while (iterator.hasNext()) {
            Potion potion = iterator.next();
            if (playerBounds.intersects(potion.getBounds())) {
                SoundPlayer.playSound("POTIONS.wav");
                if (potion.getType() == PotionType.HEALTH) {
                    player1.buff(20);
                    System.out.println("אספת שיקוי חיים!");
                } else if (potion.getType() == PotionType.SPECIAL) {
                    player1.specialAttackBuff(1.0);
                    System.out.println("אספת שיקוי מיוחד!");
                }
                iterator.remove();
            }
        }
    }
    private void spawnRandomPotion() {
        PotionType type = random.nextBoolean() ? PotionType.HEALTH : PotionType.SPECIAL;

        String imagePath = (type == PotionType.HEALTH)
                ? "/images/health_potion.png"
                : "/images/special_potion.png";

        int x = random.nextInt(1080 - 40);
        int y = random.nextInt(580 - 480 + 1) + 480;

        potions.add(new Potion(imagePath, x, y, type));
    }
    private void startPotionSpawner() {
        potionSpawnerThread = new Thread(() -> {
            while (spawnerRunning) {
                int time = random.nextInt(9000 - 5000 + 1) + 5000;
                if (!gamePaused && potions.size() < 4) {
                    spawnRandomPotion();
                    repaint();
                }
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        potionSpawnerThread.start();
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


    private void decideDirection() {
        int distance = player2.getCenterX() - player1.getCenterX();
        boolean makeMistake = random.nextInt(100) < 15;
        boolean chooseIdle = random.nextInt(100) < 10;
        boolean shouldDefend = random.nextInt(100) < 8;
        boolean useSpecial = random.nextInt(100) < 12;

        if (gamePaused || roundStarting) return;

        if (shouldDefend && !player2.isDefending()) {
            player2.startDefense();
            new Timer(800, e -> player2.stopDefense()).start();
            return;
        }
        if (useSpecial && player2.canUseSpecial()) {
            player2.specialAttack(player1);
            return;
        }
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
    private void checkRoundEnd() {
        if (!roundActive) return;

        if (player1.getCurrentHealth() <= 0) {
            player2Score++;
            roundActive = false;
            updateScoreLabel(PLAYER2_WIN_COLOR);
            System.out.println("שחקן 2 ניצח ראונד!");
            checkGameEnd();
        } else if (player2.getCurrentHealth() <= 0) {
            player1Score++;
            roundActive = false;
            updateScoreLabel(PLAYER1_WIN_COLOR);
            System.out.println("שחקן 1 ניצח ראונד!");
            checkGameEnd();
        }
    }
    private void checkGameEnd() {
        if (player1Score >= WIN_SCORE) {
            manager.showWinScreen(player1.getType());
            stopGame();
        } else if (player2Score >= WIN_SCORE) {
            manager.showWinScreen(player2.getType());
            stopGame();
        } else {
            Color roundWinnerColor = (player1.getCurrentHealth() <= 0) ? PLAYER2_WIN_COLOR : PLAYER1_WIN_COLOR;
            resetRound(roundWinnerColor);
        }
    }
    private void resetRound(Color color) {
        player1.reset();
        player2.reset();
        updateScoreLabel(color);
        roundActive = true;
        showRoundLabel();
    }
    private void resetGame() {
        player1Score = 0;
        player2Score = 0;
        roundStarting = true;
        showRoundLabel();
        score.setText("0 : 0");
        score.setForeground(Color.WHITE);
        score.repaint();

        resetRound(Color.WHITE);
    }
    private void updateScoreLabel(Color color) {
        score.setText(player1Score + " : " + player2Score);
        score.setForeground(color);
        score.repaint();

        Timer resetColorTimer = new Timer(500, e -> {
            score.setForeground(Color.WHITE);
            score.repaint();
        });
        resetColorTimer.setRepeats(false);
        resetColorTimer.start();
    }
    private void updateScoreLabel() {
        updateScoreLabel(Color.WHITE);
    }

    private void showRoundLabel() {
        roundStarting = true;

        int roundNumber = player1Score + player2Score + 1;
        String text;

        if (player1Score == 0 && player2Score == 0) {
            text = "ROUND 1";
        } else if (roundNumber == 5 && player1Score < WIN_SCORE && player2Score < WIN_SCORE) {
            text = "FINAL ROUND!";
        } else {
            text = "ROUND " + roundNumber;
        }

        roundLabel.setText(text);
        roundLabel.setVisible(true);

        new Timer(2000, e -> {
            roundLabel.setVisible(false);
            roundStarting = false;
        }) {{
            setRepeats(false);
            start();
        }};
    }


    @Override
    public void removeNotify() {
        super.removeNotify();
        aiRunning = false;
        spawnerRunning = false;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1080, 720);
    }
    private void togglePause() {
        if (!gamePaused) {
            gamePaused = true;
            pausePanel = new PausePanel(manager, this);
            add(pausePanel);
            setComponentZOrder(pausePanel, 0);
            pausePanel.repaint();
            pausePanel.revalidate();
        } else {
            resumeGame();
        }
    }

    public void resumeGame() {
        if (pausePanel != null) {
            remove(pausePanel);
            pausePanel = null;
        }
        gamePaused = false;
        requestFocusInWindow();
        repaint();
    }
    public static void addEffect(AttackEffect effect) {
        if (instance != null) {
            instance.effects.add(effect);
        }
    }

    public void stopGame() {
        aiRunning = false;
        spawnerRunning = false;
    }
}
