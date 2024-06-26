package backend;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

/**
 * The class that maintains a map of all the cacti and the dino.
 */
public class DinoMap
{
    private ArrayList<Rectangle> cacti;
    private Rectangle dino;

    private final int PANEL_WIDTH = 800;
    private final int PANEL_HEIGHT = 300;
    private final int GROUND_HEIGHT = 250;

    private boolean falling;
    private boolean jumping;

    private int dinoYVelocity = 0;

    private final int initialSPEED = 12;
    private int SPEED = initialSPEED;
    private int points = 0;
    private int cactiCounter = 0;
    private final int initialLives = 5;
    private int lives = initialLives;
    private int initialCounter = 20;
    private int counter = initialCounter; //frames for time between lives
    public DinoMap()
    {
        dino = new Rectangle(50, GROUND_HEIGHT - 50, 50, 50);
        cacti = new ArrayList<>();
        cacti.add(new Rectangle(PANEL_WIDTH, GROUND_HEIGHT - 40, 13, 40));
    }

    public boolean isGround()
    {
        if(!falling && !jumping)
            return true;
        else
            return false;
    }

    public void moveHorizontal()
    {
        // Update cacti positions
        ArrayList<Rectangle> toRemove = new ArrayList<>();
        for (Rectangle cactus : cacti)
        {
            cactus.x -= SPEED;
            if (cactus.x + cactus.width < 0) {
                toRemove.add(cactus);
                points++;
                cactiCounter++;
                if(cactiCounter%8 == 5) SPEED++;
//                System.out.println(points);
            }
        }
        cacti.removeAll(toRemove);
    }

    public void moveVertical()
    {
        // Handle dinosaur jump
        if (jumping) {
            if (dino.y > GROUND_HEIGHT - 150) {
                dinoYVelocity = -10;
            } else {
                jumping = false;
                falling = true;
            }
        }
        if (falling) {
            if (dino.y < GROUND_HEIGHT - 50) {
                dinoYVelocity = 10;
            } else {
                falling = false;
                dinoYVelocity = 0;
                dino.y = GROUND_HEIGHT - 50;
                updateSPEED();
            }
        }

        dino.y += dinoYVelocity;
    }

    public void setJumping()
    {
        this.jumping = true;
    }
    public void updateSPEED(){
        if(points%6==3){
            SPEED++;
        }
    }

    public int getLives()
    {
        return lives;
    }

    public void addNewCactus()
    {
        if(counter>0) counter--;
        else if (cacti.isEmpty() || cacti.get(cacti.size() - 1).x < 600) {
            addCactus();
        }
    }

    private void addCactus()
    {
        int positionX = PANEL_WIDTH + new Random().nextInt(200 + (SPEED*6));
        cacti.add(new Rectangle(positionX, GROUND_HEIGHT - 40, 13, 40));
    }

    public ArrayList<Rectangle> getCactus()
    {
        return this.cacti;
    }

    public int checkCollision()
    {
        for (Rectangle cactus : cacti) {
            if (dino.intersects(cactus)) {
                if(lives>1){
                    lives--;
                    clearAll();
                    return 1;
                }
//                reset();
                return -1;
            }

        }
        return 0;
    }

    public void clearAll(){
        ArrayList<Rectangle> toRemove = new ArrayList<>();
        for (Rectangle cactus : cacti)
        {
            toRemove.add(cactus);

        }
        cacti.removeAll(toRemove);
        SPEED=initialSPEED;
        counter = initialCounter;
        cactiCounter = 0;
    }
    public void reset(){
        clearAll();
        lives = initialLives;
        points = 0;
    }
    public Rectangle getDino()
    {
        return this.dino;
    }
    public int getPoints() {
        return points;
    }
}