package mygame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
public class Tree {
    private BufferedImage stumpImage;
    private BufferedImage trunkImage;
    private float x, y;
    private final int stumpWidth = 40;
    private final int stumpHeight = 50;
    private final int trunkWidth = 100;
    private final int trunkHeight = 100;
    private final int yOffset = 50; 

    public Tree(float x, float y) {
        this.x = x;
        this.y = y;
        stumpImage = ImageLoader.loadImage("/stump.png");
        trunkImage = ImageLoader.loadImage("/trunk1.png");
    }
    public void draw(Graphics g) {
        Image scaledStumpImage = stumpImage.getScaledInstance(stumpWidth, stumpHeight, Image.SCALE_SMOOTH);
        Image scaledTrunkImage = trunkImage.getScaledInstance(trunkWidth, trunkHeight, Image.SCALE_SMOOTH);
        int stumpX = (int) x - stumpWidth / 2;
        int stumpY = (int) y + 450 + yOffset; 
        g.drawImage(scaledStumpImage, stumpX, stumpY, null);
        int trunkX = (int) x - trunkWidth / 2;
        int trunkY = stumpY - trunkHeight;	
        for (int i = 0; i < 5; i++) {
            g.drawImage(scaledTrunkImage, trunkX, trunkY, null);
            trunkY -= trunkHeight;
        }
    }
}
