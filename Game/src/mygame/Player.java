package mygame;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Player {
    private BufferedImage spriteSheet;
    private BufferedImage[] sprites;
    private BufferedImage ripImage; 
    private int currentFrame;
    private int defaultFrame;
    private float x, y;
    private boolean isLeft;
    private boolean chopping;
    private boolean gameOver;
    private final int width = 100; 
    private final int height = 100;
    private SoundPlayer soundPlayer; 

    public Player(float x, float y) {
        this.x = x+40;
        this.y = y;
        this.isLeft = true;
        this.chopping = false;
        this.currentFrame = 2; 
        this.defaultFrame = 3; 
        this.gameOver = false; 

        spriteSheet = ImageLoader.loadImage("/man.png");
        ripImage = ImageLoader.loadImage("/rip.png");
        sprites = new BufferedImage[4];

        for (int i = 0; i < 4; i++) {
            sprites[i] = spriteSheet.getSubimage(i * 527, 0, 527, 412);
        }
        soundPlayer = new SoundPlayer();
    }

    public void draw(Graphics g) {
        if (gameOver) {
            Image scaledRipImage = ripImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            g.drawImage(scaledRipImage, (int) x - width / 2, (int) y, null);
        } else {
            BufferedImage imageToDraw = sprites[currentFrame];
            if (!isLeft) {
                imageToDraw = flipImage(sprites[currentFrame]);
            }
            Image scaledImage = imageToDraw.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            g.drawImage(scaledImage, (int) x - width / 2, (int) y, null);
        }
    }

    private BufferedImage flipImage(BufferedImage img) {
        BufferedImage flippedImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0, rx = img.getWidth() - 1; x < img.getWidth(); x++, rx--) {
                flippedImage.setRGB(rx, y, img.getRGB(x, y));
            }
        }
        return flippedImage;
    }

    public void chop() {
        chopping = true;
        if (isLeft) {
            currentFrame = 1;
        } else {
            currentFrame = 1;
        }
        soundPlayer.playSound("/cut.wav"); 
    }

    public void resetFrame() {
        chopping = false;
        currentFrame = defaultFrame;
    }

    public void update() {
        if (!chopping) {
            resetFrame();
        }
    }

    public boolean isLeft() {
        return isLeft;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void switchSide() {
        isLeft = !isLeft;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
