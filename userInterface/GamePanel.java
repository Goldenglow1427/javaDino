package userInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import backend.DinoMap;

public class GamePanel extends JPanel implements ActionListener, KeyListener
{
    private Timer timer;
    
    private final int PANEL_WIDTH = 800;
    private final int PANEL_HEIGHT = 300;
    private final int GROUND_HEIGHT = 250;

    private DinoMap be;

    private BufferedImage kanImage;
    private BufferedImage plantImage;
    private BufferedImage heartImage;

    private int flashCounter = 0;
    private boolean[] flashStatus;

    // private final boolean developerMode = true;

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.addKeyListener(this);

        flashStatus = new boolean[40];
        for(int i=1; i<=5; i++)
            flashStatus[i] = true;
        for(int i=11; i<=15; i++)
            flashStatus[i] = true;
        for(int i=21; i<=25; i++)
            flashStatus[i] = true;
        for(int i=31; i<=35; i++)
            flashStatus[i] = true;

        // dino = new Rectangle(50, GROUND_HEIGHT - 50, 50, 50);
        // cacti = new ArrayList<>();
        // cacti.add(new Rectangle(PANEL_WIDTH, GROUND_HEIGHT - 40, 40, 40));

        be = new DinoMap();

        try
        {
            File path1, path2, path3;
            if(System.getProperty("os.name").indexOf("Windows") != -1)
            {
                path1 = new File("userInterface\\images", "kangaroo.jpg");
                path2 = new File("userInterface\\images", "plant.jpg");
                path3 = new File("userInterface\\images", "heart20.jpg");
            }
            else
            {
                path1 = new File("userInterface/images", "kangaroo.jpg");
                path2 = new File("userInterface/images", "plant.jpg");
                path3 = new File("userInterface/images", "heart20.jpg");
            }

            // System.err.println(path1.getAbsolutePath());

            kanImage = ImageIO.read(path1);
            plantImage = ImageIO.read(path2);
            heartImage = ImageIO.read(path3);
        }
        catch(IOException ex)
        {
            System.out.println(ex + "\nFailed to capture the kangaroo clipart!");
        }

        timer = new Timer(20, this);
        timer.start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if(this.flashCounter > 0)
        {
            this.flashCounter--;
            if(flashStatus[this.flashCounter+1] == true)
                return;
        }

        // Draw ground
        g.setColor(Color.BLACK);
        g.drawLine(0, GROUND_HEIGHT, PANEL_WIDTH, GROUND_HEIGHT);

        // Draw dinosaur
        g.setColor(Color.BLUE);

        Rectangle dino = be.getDino();
        g.drawImage(kanImage, dino.x, dino.y, this);
        // g.fillRect(dino.x, dino.y, dino.width, dino.height);
        
        // Draw cacti
        ArrayList<Rectangle> cacti = be.getCactus();
        g.setColor(Color.GREEN);
        for(Rectangle cactus: cacti)
            g.drawImage(plantImage, cactus.x, cactus.y, this);
            // g.fillRect(cactus.x, cactus.y, cactus.width, cactus.height);

        
        g.setFont(new Font("TimesRoman", Font.PLAIN, 14));

        // Draw lives.
        g.setColor(Color.RED);
        for(int i=1; i<=be.getLives(); i++)
            g.drawImage(heartImage, PANEL_WIDTH-i*23-10, 10, this );
        // g.drawString(String.format("Remaining Lives: %d", be.getLives()), PANEL_WIDTH-130, 20);

        // Draw points
        g.setColor(Color.BLUE);
        g.drawString(String.format("Score: %d", be.getPoints()), 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update cacti positions
        be.moveHorizontal();

        // Handle dinosaur jump
        be.moveVertical();

        // Add new cacti
        be.addNewCactus();

        // Check for collisions
        if(be.checkCollision() == 1)
            flashCounter = 37;
        else if(be.checkCollision() == -1)
        {
            String[] options = {"Restart", "Quit"};

            timer.stop();
            // JOptionPane.showMessageDialog(this, "Game Over!");
            int opt = JOptionPane.showOptionDialog(this, "Select One:", 
            "Sadly you have died :(", 0, 3, null, options, options[0]);
            
            if(opt == 1)
                System.exit(0);
            else {
                be.reset();
                timer.start();
            }
        }
        
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_SPACE && be.isGround())
            be.setJumping();
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
    }

    @Override
    public void keyTyped(KeyEvent e){}
}