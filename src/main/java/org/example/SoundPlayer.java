package org.example;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
    public static void playSound(String soundFileName) {
        try {
            URL soundURL = SoundPlayer.class.getResource("/sounds/" + soundFileName);
            if (soundURL == null) {
                System.err.println("קובץ הסאונד לא נמצא: " + soundFileName);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
