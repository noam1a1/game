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
    private final SpecialAttackBar specialAttackBar;
    private JLabel playerNameLabel;
    private String name;
    private boolean attacking = false;
    private long lastAttackTime = 0;
    private final int attackCooldown = 500;
    private final int attackDamage = 10;
    private Direction moveDirection = null;
    private boolean isPlayer1;



    public Player(int startX, int startY, int type, boolean isPlayer1) {
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
        this.specialAttackBar = new SpecialAttackBar();
        this.isPlayer1 = isPlayer1;




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

        if (this.isPlayer1){
            healthBar.draw(g, 10, 20, Direction.LEFT);
            specialAttackBar.draw(g, 233, 20, Direction.LEFT);
        }else {
            healthBar.draw(g, 820, 20, Direction.RIGHT);
            specialAttackBar.draw(g, 807, 20, Direction.RIGHT);

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
            specialAttackBar.addSpecial(0.2);
            System.out.println(name + " פגע ב-" + target.getName() + "!");
        } else {
            System.out.println(name + " פספס!");
        }

        new Timer(attackCooldown, e -> attacking = false).start();
    }

    public void specialAttack(Player target) {
        long currentTime = System.currentTimeMillis();

        if (attacking) return;
        if (currentTime - lastAttackTime < attackCooldown) return;
        if (specialAttackBar.canSAttack()) return;

        attacking = true;
        lastAttackTime = currentTime;

        if (getAttackBox().intersects(target.getHitbox())) {
            int specialDamage = attackDamage * 3;
            target.takeDamage(specialDamage);
            this.specialAttackBar.decreaseSpecial();
            System.out.println(name + " עשה מתקפה מיוחדת על " + target.getName() + "!");
        } else {
            System.out.println(name + " פספס במתקפה מיוחדת!");
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
    public int getCenterX() {
        return x + currentImage.getWidth(null) / 2;
    }

    public void setMoveDirection(Direction direction) {
        this.moveDirection = direction;
    }

    public Direction getMoveDirection() {
        return moveDirection;
    }

    public void moveSmooth() {
        if (moveDirection == Direction.LEFT) {
            x -= speed;
            direction = Direction.LEFT;
            if (x < 0) x = 0;
        } else if (moveDirection == Direction.RIGHT) {
            x += speed;
            direction = Direction.RIGHT;
            if (x > 1080 - currentImage.getWidth(null)) x = 1080 - currentImage.getWidth(null);
        }
    }



}

