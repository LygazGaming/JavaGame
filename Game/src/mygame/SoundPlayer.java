package mygame;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
    private Clip themeClip;

    public void playSound(String soundFile) {
        try {
            URL soundURL = getClass().getResource(soundFile);
            if (soundURL != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } else {
                System.err.println("File âm thanh không tồn tại: " + soundFile);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void loopSound(String soundFile) {
        stopTheme();
        try {
            URL soundURL = getClass().getResource(soundFile);
            if (soundURL != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
                themeClip = AudioSystem.getClip();
                themeClip.open(audioInputStream);
                themeClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.err.println("File âm thanh không tồn tại: " + soundFile);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopTheme() {
        if (themeClip != null && themeClip.isRunning()) {
            themeClip.stop();
            themeClip.close();
        }
    }
}
