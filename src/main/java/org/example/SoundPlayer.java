package org.example;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
    public static void playSound(String soundFileName) {
        playSound(soundFileName, 0.0f);
    }

    public static void playSound(String soundFileName, float volumeDb) {
        try {
            URL soundURL = SoundPlayer.class.getResource("/sounds/" + soundFileName);
            if (soundURL == null) {
                System.err.println("קובץ הסאונד לא נמצא: " + soundFileName);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);


            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volumeDb);
            }

            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
