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

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.addKeyListener(this);

        // dino = new Rectangle(50, GROUND_HEIGHT - 50, 50, 50);
        // cacti = new ArrayList<>();
        // cacti.add(new Rectangle(PANEL_WIDTH, GROUND_HEIGHT - 40, 40, 40));

        be = new DinoMap();

        try
        {
            File path1, path2;
            if(System.getProperty("os.name").indexOf("Windows") != -1)
            {
                path1 = new File("userInterface\\images", "kangaroo.jpg");
                path2 = new File("userInterface\\images", "plant.jpg");
            }
            else
            {
                path1 = new File("userInterface/images", "kangaroo.jpg");
                path2 = new File("userInterface/images", "plant.jpg");
            }

            // System.err.println(path1.getAbsolutePath());

            kanImage = ImageIO.read(path1);
            plantImage = ImageIO.read(path2);
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

        // Draw points
        g.setColor(Color.BLUE);

        g.setFont(new Font("TimesRoman", Font.PLAIN, 14));
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
        if(be.checkCollision())
        {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over!");
            System.exit(0);
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
    public void keyReleased(KeyEvent e){}

    @Override
    public void keyTyped(KeyEvent e){}
}