
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AppPanel extends JPanel {

    BufferedImage carImage;
    BufferedImage carImage1;
    Timer timer;
    int playerCarX = 200;
    int playerCarY = 300;

    int obstacleCarX; // Random X position for obstacle car
    int obstacleCarY = 0; // Starts from the top of the screen
    boolean isGameRunning = false;
    boolean gameOver = false;

    Random rand = new Random(); // For random obstacle position

    // Road parameters
    int roadX = 100;  // Left side of the road
    int roadWidth = 300; // Width of the road
    int laneWidth = 20;  // Width of lane markers

    AppPanel() {
        setSize(500, 500);
        loadCarImage();
        initGameLoop();
        addKeyboardControls();
        setFocusable(true);
        resetObstacleCar(); // Set initial position for obstacle car
    }

    void loadCarImage() {
        try {
            carImage = ImageIO.read(AppPanel.class.getResource("car.png"));
            
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            carImage1 = ImageIO.read(AppPanel.class.getResource("k.png"));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }

    @Override
    protected void paintComponent(Graphics pen) {
        super.paintComponent(pen);

        // Display start or restart message
        if (!isGameRunning) {
            pen.setColor(Color.black);
            if (gameOver) {
                pen.drawString("Collision! Press ENTER to Restart", 160, 250);
            } else {
                pen.drawString("Press ENTER to Start", 200, 250);
            }
            return;
        }

        // Draw the road
        drawRoad(pen);

        // Draw the player's car
        pen.drawImage(carImage, playerCarX, playerCarY, 100, 150, null);

        // Draw the obstacle car coming from the top
        pen.drawImage(carImage1, obstacleCarX, obstacleCarY, 100, 150, null);

        // Move the obstacle car downward
        obstacleCarY += 10;

        // Reset the obstacle car to the top when it goes off-screen
        if (obstacleCarY > getHeight()) {
            resetObstacleCar();
        }

        // Check for collision
        if (checkCollision()) {
            pen.setColor(Color.BLACK);
            pen.drawString("Collision Detected!", 200, 50);
            isGameRunning = false; // Pause game on collision
            gameOver = true;
            timer.stop();
        }
    }

    void drawRoad(Graphics pen) {
        // Draw road boundaries
        pen.setColor(Color.GRAY);
        pen.fillRect(roadX, 0, roadWidth, getHeight());

        // Draw the lane markers (dashed lines)
        pen.setColor(Color.WHITE);
        for (int i = 0; i < getHeight(); i += 40) {
            pen.fillRect(roadX + roadWidth / 2 - laneWidth / 2, i, laneWidth, 20);
        }
    }

    boolean checkCollision() {
        // Create bounding rectangles for player car and obstacle car
        Rectangle playerCarRect = new Rectangle(playerCarX, playerCarY, 100, 150);
        Rectangle obstacleCarRect = new Rectangle(obstacleCarX, obstacleCarY, 100, 150);
        return playerCarRect.intersects(obstacleCarRect);
    }

    void initGameLoop() {
        timer = new Timer(50, (a) -> {
            if (isGameRunning) {
                repaint();
            }
        });
        timer.start();
    }

    void resetGame() {
        // Reset player and obstacle car positions
        playerCarX = 200;
        playerCarY = 300;
        resetObstacleCar();
        gameOver = false;
    }

    void resetObstacleCar() {
        // Randomize X position and reset Y position of the obstacle car
        obstacleCarX = rand.nextInt(getWidth() - 100); // Prevents car from going out of bounds
        obstacleCarY = -150; // Start above the visible screen
    }

    void addKeyboardControls() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Start or restart the game on space press
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!isGameRunning) {
                        if (gameOver) {
                            resetGame(); // Reset positions if game is over
                        }
                        isGameRunning = true;
                        timer.start();
                    }
                }

                // Car movement only if the game is running
                if (isGameRunning) {
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerCarX<=500-100) {
                        playerCarX += 5;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT&& playerCarX>=0+100) {
                        playerCarX -= 5;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_UP &&playerCarY<=150) {
                        playerCarY -= 5;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN &&playerCarY>=500-150) {
                        playerCarY += 5;
                    }
                }
            }
        });
    }
}


