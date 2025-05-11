package org.example;

import javax.swing.*;
import java.awt.*;

public class AttackEffect {
    private final Image[] frames;
    private int currentFrame = 0;
    private int x, y;
    private boolean active = true;

    public AttackEffect(int x, int y) {
        this.x = x;
        this.y = y;
        frames = new Image[]{
                new ImageIcon(getClass().getResource("/images/effects/Lim_1.png")).getImage(),
                new ImageIcon(getClass().getResource("/images/effects/Lim_2.png")).getImage()
        };

        new Thread(() -> {
            try {
                while (active) {
                    Thread.sleep(100);
                    currentFrame = (currentFrame + 1) % frames.length;
                }
            } catch (InterruptedException ignored) {}
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
            active = false;
        }).start();
    }

    public void draw(Graphics g) {
        if (active) {
            g.drawImage(frames[currentFrame], x, y, null);
        }
    }

    public boolean isActive() {
        return active;
    }
}
