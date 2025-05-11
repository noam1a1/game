package org.example;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.List;

import org.example.FallingObject;

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
    private boolean defending = false;
    private String imageName;
    private boolean stunned = false;
    private Image baseImage;
    private final Map<String, Integer> imageOffsets = new HashMap<>();
    private String currentImageName;
    Random random = new Random();





    public Player(int startX, int startY, int type, boolean isPlayer1) {
        this.x = startX;
        this.y = startY;
        this.currentHealth = maxHealth;
        this.type = type;
        this.speed = 5;

        if (type==1){
            name = "BORIS";
            imageName = "Boris";
        } else {
            name = "DVORA";
            imageName = "Dvora";
        }
        this.direction = Direction.RIGHT;
        this.healthBar = new HealthBar(() -> this.currentHealth, () -> this.maxHealth);
        this.specialAttackBar = new SpecialAttackBar();
        this.isPlayer1 = isPlayer1;
        baseImage = loadImage(imageName + "_1.png");
        this.currentImage = baseImage;

        imageOffsets.put("Boris_1.png", 0);
        imageOffsets.put("Boris_2.png", -91);
        imageOffsets.put("Boris_3.png", 0);

        imageOffsets.put("Dvora_1.png", 0);
        imageOffsets.put("Dvora_2.png", -49);
        imageOffsets.put("Dvora_3.png", -15);

    }

    public void moveLeft() {
        x -= speed;
        direction = Direction.LEFT;
        currentImage = loadImage(this.imageName+"_1.png");
        if (x < 0) x = 0;

    }

    public void moveRight() {
        x += speed;
        direction = Direction.RIGHT;
        currentImage = loadImage(this.imageName+"_1.png");
        if (x > 1080-164) x = 1080-164;

    }

    public void draw(Graphics g) {
        String currentImageName = getCurrentImageName();
        int offsetX = imageOffsets.getOrDefault(currentImageName, 0);


        if (direction == Direction.LEFT) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(
                    currentImage,
                    x + offsetX + currentImage.getWidth(null), y,
                    -currentImage.getWidth(null), currentImage.getHeight(null),
                    null
            );
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
        if (defending) {
            System.out.println(name + " חסם את המתקפה!");
            return;
        }

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

        currentImage = loadImage(this.imageName+"_2.png");



        if (getAttackBox().intersects(target.getHitbox())) {
            int num = random.nextInt(2) + 1;
            target.takeDamage(attackDamage);
            specialAttackBar.addSpecial(0.2);
            SoundPlayer.playSound("punch/" + num + ".wav");
            System.out.println(name + " פגע ב-" + target.getName() + "!");

        } else {
            System.out.println(name + " פספס!");
        }
        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentImage = loadImage(this.imageName+"_1.png");
        }).start();

        new Timer(attackCooldown, e -> attacking = false).start();
    }

 public void specialAttack(Player target) {
    System.out.println("ניסיתי מתקפה מיוחדת");

    long currentTime = System.currentTimeMillis();

    if (attacking) {
        System.out.println("כבר תוקף!");
        return;
    }
    if (currentTime - lastAttackTime < attackCooldown) {
        System.out.println("קירור לא נגמר!");
        return;
    }
    if (!specialAttackBar.canSAttack()) {
        System.out.println("הבר לא מלא!");
        return;
    }

    attacking = true;
    lastAttackTime = currentTime;

    if (getAttackBox().intersects(target.getHitbox())) {
        int specialDamage = attackDamage * 3;
        target.takeDamage(specialDamage);
        target.stun(1000);
        specialAttackBar.decreaseSpecial();
        if (this.type == 1 && isPlayer1) {
            this.currentImage =loadImage(this.imageName+"_3.png");
            int effectX = target.getX();
            int effectY = target.getY();
            GamePanel.addEffect(new AttackEffect(effectX, effectY));
        }
        SoundPlayer.playSound("B SA.wav");
        System.out.println(name + " עשה מתקפה מיוחדת על " + target.getName() + "!");
    } else {
        System.out.println(name + " פספס במתקפה מיוחדת!");
    }

    new Timer(attackCooldown, e -> attacking = false).start();
}
    public void specialAttack(Player target, List<FallingObject> worldObjects) {
        if (!specialAttackBar.canSAttack()) return;
        specialAttackBar.decreaseSpecial();

        if (this.type==2) {
            this.currentImage =loadImage(this.imageName+"_3.png");
            SoundPlayer.playSound("D SA.wav");
            int centerX = target.getCenterX() - 20;
            FallingObject obj = new FallingObject(centerX, 0);
            worldObjects.add(obj);
            System.out.println(name + " זימנה חפץ שנופל!");
        }
    }





    private Image loadImage(String filename) {
        this.currentImageName = filename;
        String path = "/images/" + imageName + "/" + filename;
        URL resource = getClass().getResource(path);
        if (resource == null) {
            System.err.println("בעיה: תמונה לא נמצאה בנתיב: " + path);
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        return new ImageIcon(resource).getImage();
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
    public void specialAttackBuff(double amount){
        specialAttackBar.addSpecial(amount);
    }

    public Direction getMoveDirection() {
        return moveDirection;
    }

    public void moveSmooth() {
        if (stunned) return;
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

    public int getCurrentHealth() {
        return currentHealth;
    }
    public void reset() {
        this.currentHealth = maxHealth;
        this.specialAttackBar.resetSpecial();

        if (isPlayer1) {
            this.x = 100;
            this.direction = Direction.RIGHT;
        } else {
            this.x = 900;
            this.direction = Direction.LEFT;
        }

        this.attacking = false;
        this.moveDirection = null;
        this.currentImage = loadImage(type == 1 ? "boris_1.png" : "dvora_1.png");

    }
    public void startDefense() {
        defending = true;

    }

    public void stopDefense() {
        defending = false;

    }
    public boolean canUseSpecial() {
        return specialAttackBar.canSAttack();
    }
    public boolean isDefending() {
        return defending;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isStunned() {
        return stunned;
    }

    public void stun(int durationMs) {
        stunned = true;
        new Thread(() -> {
            try {
                Thread.sleep(durationMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stunned = false;
        }).start();
    }

    public int getType() {
        return type;
    }
    public String getCurrentImageName() {
        return currentImageName;
    }
}

