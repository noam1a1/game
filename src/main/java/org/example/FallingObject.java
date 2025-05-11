package org.example;

import javax.swing.*;
import java.awt.*;

public class FallingObject {
    private int x, y;
    private final int targetX;
    private final Image image;
    private boolean active = true;
    private final int fallSpeed = 7;

    public FallingObject(int targetX, int startY) {
        this.x = targetX;
        this.y = startY;
        this.targetX = targetX;
        this.image = new ImageIcon(getClass().getResource("/images/effects/Suitecase.png")).getImage();
    }

    public void update() {
        if (!active) return;
        y += fallSpeed;
        if (y > 600) {
            active = false;
        }
    }

    public void draw(Graphics g) {
        if (active) {
            g.drawImage(image, x, y, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }
}
