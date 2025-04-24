package org.example;

import javax.swing.*;
import java.awt.*;

public class Player {
    private int x, y;
    private int maxHealth = 100;
    private int currentHealth;
    private Image currentImage;
    private int speed;
    private Direction direction;
    private int type;
    private final HealthBar healthBar;
    private JLabel playerNameLabel;
    private String name;
    private boolean attacking = false;
    private long lastAttackTime = 0;
    private final int attackCooldown = 500; // במילישניות
    private final int attackDamage = 10;



    public Player(int startX, int startY, int type) {
        this.x = startX;
        this.y = startY;
        this.currentHealth = maxHealth;
        this.type = type;
        this.speed = 5;
        String imageName;
        if (type==1){
            name = "BORIS";
            imageName = "boris";
        } else {
            name = "DVORA";
            imageName = "dvora";
        }
        this.direction = Direction.RIGHT;
        this.currentImage = loadImage(imageName + "_1.png");
        this.healthBar = new HealthBar(() -> this.currentHealth, () -> this.maxHealth);




    }

    public void moveLeft() {
        x -= speed;
        direction = Direction.LEFT;
        currentImage = loadImage("boris_1.png");
        if (x < 0) x = 0;

    }

    public void moveRight() {
        x += speed;
        direction = Direction.RIGHT;
        currentImage = loadImage("boris_1.png");
        if (x > 1080-164) x = 1080-164;

    }

    public void draw(Graphics g) {
        if (direction == Direction.LEFT) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(currentImage, x + currentImage.getWidth(null), y, -currentImage.getWidth(null), currentImage.getHeight(null), null); // הפוך
        } else {
            g.drawImage(currentImage, x, y, null);

        }

        if (this.type==1){
            healthBar.draw(g, 10, 20, Direction.LEFT);
        }else {
            healthBar.draw(g, 820, 20, Direction.RIGHT);

        }

    }

    public void takeDamage(int amount) {
        currentHealth -= amount;
        if (currentHealth < 0) currentHealth = 0;
    }

    public void buff(int amount) {
        currentHealth += amount;
        if (currentHealth > 100) currentHealth = 100;
    }
    public void attack(Player target) {
        long currentTime = System.currentTimeMillis();

        if (attacking) return;
        if (currentTime - lastAttackTime < attackCooldown) return;

        attacking = true;
        lastAttackTime = currentTime;


        if (getAttackBox().intersects(target.getHitbox())) {
            target.takeDamage(attackDamage);
            System.out.println(name + " פגע ב-" + target.getName() + "!");
        } else {
            System.out.println(name + " פספס!");
        }

        new Timer(attackCooldown, e -> attacking = false).start();
    }


    private Image loadImage(String filename) {
        return new ImageIcon(getClass().getResource("/images/Boris/"+filename)).getImage();
    }

    public String getName() {
        return name;
    }
    public Rectangle getHitbox() {
        return new Rectangle(x, y, currentImage.getWidth(null), currentImage.getHeight(null));
    }

    public Rectangle getAttackBox() {
        int width = 40;
        int height = 30;
        int attackX = (direction == Direction.RIGHT) ? x + currentImage.getWidth(null) : x - width;
        return new Rectangle(attackX, y + 20, width, height);
    }


}

