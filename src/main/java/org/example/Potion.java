package org.example;

import javax.swing.*;
import java.awt.*;

public class Potion {
    private Image image;
    private int x, y;
    private int width, height;
    private PotionType type;

    public Potion(String imagePath, int x, int y, PotionType type) {
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage();
        this.x = x;
        this.y = y;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        this.type = type;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public PotionType getType() {
        return type;
    }
}
