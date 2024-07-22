package mygame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Timberman");
        GamePanel panel = new GamePanel();
        try (InputStream is = Game.class.getResourceAsStream("/logo.png")) {
            Image icon = ImageIO.read(is);
            frame.setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
