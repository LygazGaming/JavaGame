package mygame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = -1211772641204106907L;
	private Player player;
    private Tree tree;
    private List<Branch> branches;
    private Timer gameTimer;
    private Timer timeBarTimer;
    private boolean isGameOver;
    private int score;
    private int highScore;
    private float timeBarWidth;
    private final float maxTimeBarWidth = 200;
    private final float timeBarDecreaseRate = 5f;
    private BufferedImage backgroundImage;
    private BufferedImage gameOverImage;
    private BufferedImage playButtonImage;
    private BufferedImage barImage;
    private BufferedImage barBlackImage;
    private BufferedImage barContainerImage;
    private SoundPlayer soundPlayer;
    private Random random;
    private boolean chopping;

    private final String scoreFilePath = "scores.txt";

    public GamePanel() {
        setPreferredSize(new Dimension(800, 550));
        setFocusable(true);
        player = new Player(300, 450);
        branches = new ArrayList<>();
        tree = new Tree(400, 0);
        random = new Random();
        score = 0;
        highScore = 0;
        isGameOver = false;
        timeBarWidth = maxTimeBarWidth;
        soundPlayer = new SoundPlayer();
        loadScores();
        backgroundImage = ImageLoader.loadImage("/background.png");
        gameOverImage = ImageLoader.loadImage("/overScreen.png");
        playButtonImage = ImageLoader.loadImage("/playbtn.png");
        barImage = ImageLoader.loadImage("/bar.png");
        barBlackImage = ImageLoader.loadImage("/bar_black.png");
        barContainerImage = ImageLoader.loadImage("/bar_container.png");

        for (int i = 0; i < 5; i++) {
            addBranch();
        }
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });

        gameTimer = new Timer(1000 / 60, e -> {
            if (!isGameOver) {
                repaint();
            }
        });
        gameTimer.start();

        timeBarTimer = new Timer(100, e -> {
            if (!isGameOver) {
                decreaseTimeBar();
                if (timeBarWidth <= 0) {
                    endGame();
                }
                repaint();
            }
        });
        timeBarTimer.start();

        soundPlayer.loopSound("/theme.wav");
    }

    private void addBranch() {
        boolean isLeft = random.nextBoolean();
        float x = 400;
        float y = branches.isEmpty() ? 350 : branches.get(branches.size() - 1).getY() - 100;
        branches.add(new Branch(x, y, isLeft));
    }

    private void updateBranches() {
        for (Branch branch : branches) {
            branch.setY(branch.getY() + 100);
        }
        if (branches.get(0).getY() >= 600) {
            branches.remove(0);
            addBranch();
        }
    }

    private void checkCollision() {
        for (Branch branch : branches) {
            if (branch.getY() == player.getY() && branch.isLeft() == player.isLeft()) {
                endGame();
                break;
            }
        }
    }

    private void resetGame() {
        player = new Player(300, 450);
        tree = new Tree(400, 0);
        branches.clear();
        for (int i = 0; i < 5; i++) {
            addBranch();
        }
        score = 0;
        timeBarWidth = maxTimeBarWidth;
        isGameOver = false;
        soundPlayer.stopTheme();
        soundPlayer.loopSound("/theme.wav");
        saveScores();
        repaint();
    }

    private void endGame() {
        player.setGameOver(true);
        isGameOver = true;
        soundPlayer.playSound("/death.wav");
        checkHighScore();
        saveScores();
        repaint();
    }

    private void handleKeyPress(KeyEvent e) {
        if (!isGameOver) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                handleLeftKey();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                handleRightKey();
            }
        }
    }

    private void handleLeftKey() {
        player.setX(340);
        if (!player.isLeft()) {
            player.switchSide();
        }
        player.chop();
        score++;
        choppingAction();
    }

    private void handleRightKey() {
        player.setX(460);
        if (player.isLeft()) {
            player.switchSide();
        }
        player.chop();
        score++;
        choppingAction();
    }

    private void choppingAction() {
        chopping = true;
        updateBranches();
        checkCollision();
        increaseTimeBar();
        repaint();
    }

    private void handleKeyRelease(KeyEvent e) {
        if (chopping) {
            player.resetFrame();
            chopping = false;
            repaint();
        }
    }

    private void handleMouseClick(MouseEvent e) {
        if (isGameOver) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            if (mouseX >= 350 && mouseX <= 450 && mouseY >= 300 && mouseY <= 350) {
                soundPlayer.playSound("/menu.wav");
                resetGame();
            }
        }
    }

    private void increaseTimeBar() {
        timeBarWidth = Math.min(timeBarWidth + 20, maxTimeBarWidth);
    }

    private void decreaseTimeBar() {
        timeBarWidth -= timeBarDecreaseRate;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        tree.draw(g);
        player.draw(g);
        for (Branch branch : branches) {
            branch.draw(g);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);

        if (isGameOver && gameOverImage != null) {
            g.drawImage(gameOverImage, 300, 0, 200, 250, null);
            g.drawImage(playButtonImage, 350, 300, 100, 50, null);
            g.drawString("" + score, 395, 240);
            g.drawString("" + highScore, 395, 185);
        }

        int barX = 10;
        int barY = 40;
        int barHeight = 20;

        g.drawImage(barContainerImage, barX, barY, (int) maxTimeBarWidth + 4, barHeight + 4, null);
        g.drawImage(barBlackImage, barX + 2, barY + 2, (int) maxTimeBarWidth, barHeight, null);
        g.drawImage(barImage, barX + 2, barY + 2, (int) timeBarWidth, barHeight, null);
    }

    private void loadScores() {
    	  try (BufferedReader reader = new BufferedReader(new FileReader(scoreFilePath))) {
    	        String line;
    	        while ((line = reader.readLine()) != null) {
    	            line = line.trim(); 
    	        }
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    }

    private void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(scoreFilePath))) {
            writer.write("Điểm cao nhất:"+highScore + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkHighScore() {
        if (score > highScore) {
            highScore = score;
        }
    }
}
