package org.example;

import javax.swing.*;
import java.awt.*;

public class GameManager extends JPanel {
    private CardLayout cardLayout;

    public GameManager() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setPreferredSize(new Dimension(1080, 720));

        LauncherPanel launcher = new LauncherPanel(this);
        GamePanel game = new GamePanel(this);
        HowToPlayPanel howToPlayPanel = new HowToPlayPanel(this);

        add(launcher, "launcher");
        add(game, "game");
        add(howToPlayPanel ,"howToPlay");
        showScreen("launcher");
    }
    public void showScreen(String name) {
        cardLayout.show(this, name);
        Component current = switch (name) {
            case "launcher" -> getComponent(0);
            case "game" -> getComponent(1);
            case "howToPlay" -> getComponent(2);

            default -> null;
        };

        if (current != null) {
            SwingUtilities.invokeLater(current::requestFocusInWindow);
        }
    }

}
