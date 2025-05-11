package org.example;

import javax.swing.*;

public class Main {
    public static final int WINDOW_WIDTH = 1080;
    public static final int WINDOW_HEIGHT = 720;
    public static void main(String[] args) {
        JFrame window = new JFrame("Mortal Madmach");
        GameManager manager = new GameManager();
        PlayerSelection selection = new PlayerSelection();
        window.setContentPane(manager);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        SoundPlayer.playSound("ST.wav", -20.0f);

        window.setVisible(true);

    }
}