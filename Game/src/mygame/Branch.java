package mygame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Branch {
    private BufferedImage image;
    private float x, y;
    private boolean isLeft;
    private final int width = 100;
    private final int height = 50;

    public Branch(float x, float y, boolean isLeft) {
        this.x = x;
        this.y = y;
        this.isLeft = isLeft;

        String imagePath = isLeft ? "/branch1.png" : "/branch2.png";
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        int drawX = isLeft ? (int) x - width / 2 : (int) x - width / 2;
        g.drawImage(scaledImage, drawX, (int) y, null);
    }

    public float getX() {
        return x;
    }

    public void seXY(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isLeft() {
        return isLeft;
    }
}