/**
 * @author Group 14
 * @version 2021.12.08
 */

package v2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ScoreBoard extends JPanel{

    // UI Elements
 	JFrame frame;
	Container con;
    JLabel label;
    JLabel label1;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    JLabel label5;
    JButton button;
    TetrisV2 tetris;

    // Customised colours
    Color gold = new Color(255,215,0);
    Color silver = new Color(192,192,192);
    Color copper = new Color(201,114,34);
    Color back = new Color(119, 230, 198);

    /**
     * UI constructor class
     */
	public ScoreBoard(TetrisV2 tetris) {
        this.tetris = tetris;
        initScoreBoard();
	}

    public void initScoreBoard() {
        // Creating frame, setting it properly, adding  functionality
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 500);
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.black);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.setTitle("Tetris by Group 14");
		frame.setVisible(true);
        frame.setContentPane(new JLabel(new ImageIcon("resources/score.png")));

        // Creating the "Back Button"
        button = new JButton("Back");
        button.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 15));
		button.setBounds(10, 10, 55, 35);    
        button.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));  
        // Adding action listener so as to go back to the previous Interface Frame when button is clicked, which is EndUI
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EndUI(tetris);
                frame.dispose();
            }
        });
        // Adding Mouse Listener to set hover effect to the button
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(back);
            }
        
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE); 
            }
        });
        frame.add(button);

        // Creating five labels for the respective five highest scores
        label1 = new JLabel();
        label1.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 20));
        label1.setBounds(50, 100, 400, 40);  
        label1.setForeground(gold);  
        label1.setText("1.      " + tetris.firstPlace);
        frame.add(label1);

        label2 = new JLabel();
        label2.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 20));
        label2.setBounds(50, 175, 400, 40);  
        label2.setForeground(silver);  
        label2.setText("2.      " + tetris.secondPlace);
        frame.add(label2);

        label3 = new JLabel();
        label3.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 20));
        label3.setBounds(50, 250, 400, 40);  
        label3.setForeground(copper);  
        label3.setText("3.      " + tetris.thirdPlace);
        frame.add(label3);

        label4 = new JLabel();
        label4.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 20));
        label4.setBounds(50, 325, 400, 40);  
        label4.setForeground(Color.WHITE);  
        label4.setText("4.      " + tetris.fourthPlace);
        frame.add(label4);

        label5 = new JLabel();
        label5.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 20));
        label5.setBounds(50, 400, 400, 40);  
        label5.setForeground(Color.WHITE);  
        label5.setText("5.      " + tetris.fifthPlace);
        frame.add(label5);
    }

}