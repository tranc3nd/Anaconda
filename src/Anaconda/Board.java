/**
 * Anaconda program to create the display of the board where the snake and 
 * cow is set on the JFrame.
 */

package Anaconda;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 */
public class Board extends JPanel implements ActionListener {

    private final int width = 580; // Playing fields width
    private final int height = 340; // Playing field height
    private final int imgSize = 10; // Max images on board width*height/imgSize*imgSize
    private final int allImg = 2400; // Initial pixel size of images.
    private final int randomPos = 30; // Determines a random postion for food.
    private final int delay = 120; // Speed of snake in miliseconds. Higher the number the slower the snake
   
    private int x[] = new int[allImg];
    private int y[] = new int[allImg];
    private int body;
    private int food_x;
    private int food_y;
    int score = 0;
    
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean playGame = true;

    private Timer timer;
    private Image bodySegment;
    private Image food;
    private Image head;

    /**
     */
    public Board() {
        addKeyListener(new Controller()); // adds key listener to process input from keyboard
        setBackground(Color.green); // sets color of screen
        
        ImageIcon iibs = new ImageIcon(this.getClass().getResource("bodysegment.png"));
        bodySegment = iibs.getImage();
        
        ImageIcon iif = new ImageIcon(this.getClass().getResource("cow2.png"));
        food = iif.getImage();
        
        ImageIcon iih = new ImageIcon(this.getClass().getResource("head.png"));
        head = iih.getImage();
        
        setFocusable(true);
        runGame();
    }
   
    /** 
     * Method that creates the board game.
     */
    public void runGame() {
        body = 5; // initial size of the body of snake is 5 segments long
        // x,y coordinates of where anaconda starts in the game
        for (int z = 0; z < body; z++) {
            x[z] = 50 - z*10;
            y[z] = 50;
        }
        locateFood(); 
        timer = new Timer(delay, this); 
        timer.start();
    }

    /** Method to draw the images onto the array 
      * using java Graphics object
     */
    public void paint(Graphics g) {
        super.paint(g);

        if (playGame) {

            g.drawImage(food, food_x, food_y, this);
            
            for (int z = 0; z < body; z++) {
                if (z == 0)
                    g.drawImage(head, x[z], y[z], this);
                else g.drawImage(bodySegment, x[z], y[z], this);
            }
            // Clears the array of all images before repainting.
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
            
        } else {
            gameOver(g);
        }
    }
    
    /** Method to output game over message when game ends.
     */
    public void gameOver(Graphics g) {
        String message = "Game Over. U Mad Bro?"; // message when player loses
        Font small = new Font("Helvetica", Font.BOLD, 36); // font attributes
        FontMetrics metr = this.getFontMetrics(small);
        g.setColor(Color.red); // sets color of font
        g.setFont(small);
        g.drawString(message, (width - metr.stringWidth(message)) / 2, height / 2); // centers message 
    }

    /** Method that checks if food and head have the same coordinates on array.
      * If true then the body increments by one segment.
     */
    public void checkFood() {

        if ((x[0] == food_x) && (y[0] == food_y)) {
            body++; // increments body of snake by one body segment
            locateFood();
        }
    }
    
    /** Method that locates random coordinates to place food on the array
      */
    public void locateFood() {
        int r = (int) (Math.random() * randomPos);
        food_x = ((r * imgSize));
        r = (int) (Math.random() * randomPos);
        food_y = ((r * imgSize));
    }
    
    /** Method to move the snakes body in the direction 
      * of the head by the image size of the picture of
      * the head and body which is 10x10 pixels. This
      * points to a new location on the array to draw 
      * the snake.
     */
    public void snakeMovement() {

        for (int z = body; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        if (left) {
            x[0] -= imgSize; //if movement left -10 pixels from x axis
        }
        if (right) {
            x[0] += imgSize; //if movement right +10 pixels from x axis
        }
        if (up) {
            y[0] -= imgSize; //if movement up -10 pixels from y axis
        }
        if (down) {
            y[0] += imgSize; //if movement down +10 pixels from y axis
        }
    }

    /** Method that checks if head of the snake has same coordinates as 
      * the snakes body or past the borders of the JFrame.
      */
    public void checkCollision() {

        for (int z = body; z > 0; z--) {
              if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                  playGame = false;
              }
        }
        if (y[0] > height) {
            playGame = false;
        }
        if (y[0] < 0) {
            playGame = false;
        }
        if (x[0] > width) {
            playGame = false;
        }
        if (x[0] < 0) {
            playGame = false;
        }
    }

    /** Makes a score board for the game using a JPanel and JLabel
      */
    public void createScoreBoard (){
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(null);
        bottomPanel.setLocation(0, 350);
        bottomPanel.setSize(600, 22);
        add(bottomPanel);
        
        /** How to use JLabels: 
          * http://docs.oracle.com/javase/tutorial/uiswing/components/label.html
          */
        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setLocation(-270, 0);
        scoreLabel.setSize(600, 22);
        scoreLabel.setHorizontalAlignment(0);
        scoreLabel.setOpaque(true);
       // scoreLabel.setBackground(Color.white);
        scoreLabel.setForeground(Color.black);
        bottomPanel.add(scoreLabel);
        
        /** Used to check if head and food have the same coordinates which 
          * is used to update and keep score
          */
        if ((x[0] == food_x) && (y[0] == food_y)) {
          score += 5; // increments score by 5 points
          scoreLabel.setText("Score: " + score);
      //    scoreLabel.repaint();
        }
    }
   
    /** Method to create an animation for game by repainting 
      * actionPerformed sequence over and over. 
      */ 
    public void actionPerformed(ActionEvent e) {
        if (playGame) {
            checkFood();
            snakeMovement();
            checkCollision();
            createScoreBoard();
        }
        repaint(); 
    }

    /** How to use KeyAdapter:
      * http://docs.oracle.com/javase/1.4.2/docs/api/java/awt/event/KeyAdapter.html
      * Create a class called Controller that inherit fields and methods from KeyAdapter
      * It is a customized controller input in which the movement of the anaconda 
      * cannot move back into the direction of itself.
      */
    
    private class Controller extends KeyAdapter {

        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            if ((key == KeyEvent.VK_LEFT) && (!right)) {
                left = true;
                up = false;
                down = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!left)) {
                right = true;
                up = false;
                down = false;
            }
            if ((key == KeyEvent.VK_UP) && (!down)) {
                up = true;
                right = false;
                left = false;
            }
            if ((key == KeyEvent.VK_DOWN) && (!up)) {
                down = true;
                right = false;
                left = false;
            }
        }
    }
       
}