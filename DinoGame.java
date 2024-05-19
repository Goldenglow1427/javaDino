import javax.swing.*;

import userInterface.GamePanel;

/**
 * The class that carries the frontend and initiates the game.
 */
public class DinoGame extends JFrame
{
    GamePanel panel;

    public DinoGame()
    {
        super("Dinosaur Game");
        panel = new GamePanel();
        add(panel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        new DinoGame();
    }
}