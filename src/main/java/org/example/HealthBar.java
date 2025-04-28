package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HealthBar {
    private final Supplier<Integer> currentHealth;
    private final Supplier<Integer> maxHealth;
    private final Map<Integer, Image> healthImages;

    public HealthBar(Supplier<Integer> currentHealth, Supplier<Integer> maxHealth) {
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.healthImages = new HashMap<>();

        for (int i = 0; i <= 100; i += 5) {
            String path = String.format("/images/health bar/" + i + ".png");
            healthImages.put(i, new ImageIcon(getClass().getResource(path)).getImage());
        }
    }

    public void draw(Graphics g, int x, int y, Direction direction) {
        int health = currentHealth.get();
        int max = maxHealth.get();

        int percent = (int) Math.round((health * 100.0) / max);
        int rounded = Math.min(100, Math.max(0, (int)(Math.round(percent / 5.0) * 5)));

        Image barImage = healthImages.getOrDefault(rounded, healthImages.get(0));
        int width = barImage.getWidth(null);
        int height = barImage.getHeight(null);

        if (direction == Direction.LEFT) {
            g.drawImage(barImage, x, y, null);
        } else {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(barImage, x + width, y, -width, height, null);
        }
    }
}
