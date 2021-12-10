/**
 * @author Group 14
 * @version 2021.12.08
 */

package v2;

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseUI {

    // UI Elements
    private JFrame frame;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    public String name;

    TetrisV2 tetris;

    /**
	 * UI constructor class
	*/
    public PauseUI(TetrisV2 tetris) {
        this.tetris = tetris;
        initEndUI();
    }

    public void initEndUI() {
        // Creating frame, setting it properly, adding  functionality
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setTitle("Tetris by Group 14");
        frame.setVisible(true);

        // Create "Resume Game" button
        button1 = new JButton("Resume Game");
        button1.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 15));
        button1.setBounds(75,50,150,40);
        button1.setBackground(Color.WHITE);
        button1.setFocusable(false); 
        // Adding Action Listener - when button is clicked, resume game from the same state as when the game was paused
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                tetris.pause();
            }
        });

        // Create "Restart Game" button
        button2 = new JButton("Restart Game");
        button2.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 15));
        button2.setBounds(75,120,150,40);
        button2.setBackground(Color.WHITE);
        button2.setFocusable(false);
        // Adding Action Listener - when button is clicked, restart game by resetting the board and restarting gameplay
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                tetris.initBoard();
                tetris.beginGame();
                tetris.pause();
            }
        });

        // Create "Exit Game" button
        button3 = new JButton("Exit Game");
        button3.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 15));
        button3.setBounds(75,190,150,40);
        button3.setBackground(Color.WHITE); 
        button3.setFocusable(false);
        // Adding Action Listener - when button is clicked, exit game
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });

        // Adding buttons to frame
        frame.add(button1);
        frame.add(button2);
        frame.add(button3);

        // Setting frame as visible 
        frame.setVisible(true);

    }
}