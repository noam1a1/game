package org.example;

import javax.swing.*;
import java.awt.*;

public class GameManager extends JPanel {
    private final CardLayout cardLayout;
    private final PlayerSelection selection;
    private LauncherPanel launcher;
    private CharacterSelectPanel characterSelectPanel;
    private HowToPlayPanel howToPlayPanel;
    private GamePanel gamePanel;

    public GameManager() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setPreferredSize(new Dimension(1080, 720));

        selection = new PlayerSelection();

        launcher = new LauncherPanel(this);
        characterSelectPanel = new CharacterSelectPanel(this, selection);
        howToPlayPanel = new HowToPlayPanel(this);

        add(launcher, "launcher");
        add(howToPlayPanel, "howToPlay");
        add(characterSelectPanel, "select");

        showScreen("launcher");
    }

    public void showScreen(String name) {
        if (name.equals("game")) {
            if (gamePanel != null) {
                remove(gamePanel);
            }
            gamePanel = new GamePanel(this, selection);
            add(gamePanel, "game");
        }

        cardLayout.show(this, name);

        Component current = switch (name) {
            case "launcher" -> launcher;
            case "game" -> gamePanel;
            case "howToPlay" -> howToPlayPanel;
            case "select" -> characterSelectPanel;
            default -> null;
        };

        if (current != null) {
            SwingUtilities.invokeLater(current::requestFocusInWindow);
        }
    }
}
