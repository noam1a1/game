package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SpecialAttackBar {
    private Image backgroundImage;
    private Image fillImage;
    private double specialAmount = 0.0;
    private final double maxSpecial = 3.0;

    public SpecialAttackBar() {
        backgroundImage = new ImageIcon(getClass().getResource("/images/health bar/SA bar/0.png")).getImage();
        fillImage = new ImageIcon(getClass().getResource("/images/health bar/SA bar/3.png")).getImage();



    }

    public void draw(Graphics g, int x, int y, Direction direction) {
        int width = backgroundImage.getWidth(null);
        int height = backgroundImage.getHeight(null);

        Graphics2D g2d = (Graphics2D) g;

        if (direction == Direction.LEFT) {
            g2d.drawImage(backgroundImage, x, y, null);
        } else {
            g2d.drawImage(backgroundImage, x + width, y, -width, height, null);
        }

        double percentFull = Math.min(specialAmount / maxSpecial, 1.0);
        int fillHeight = (int) (height * percentFull);

        if (fillHeight <= 0) return;

        if (direction == Direction.LEFT) {
            g2d.drawImage(
                    fillImage,
                    x, y + (height - fillHeight), x + width, y + height,
                    0, height - fillHeight, width, height,
                    null
            );
        } else {
            g2d.drawImage(
                    fillImage,
                    x + width, y + (height - fillHeight), x, y + height,
                    0, height - fillHeight, width, height,
                    null
            );
        }
    }




    public void addSpecial(double amount) {
        specialAmount = Math.min(specialAmount + amount, maxSpecial);
    }
    public void resetSpecial() {
        specialAmount = 0;
    }

    public void decreaseSpecial() {
        specialAmount = Math.max(0, specialAmount - 1.0);
    }

    public boolean canSAttack() {
        return specialAmount >= 1;
    }
}
