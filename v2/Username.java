/**
 * @author Group 14
 * @version 2021.12.08
 */

package v2;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * UsernameUI
 */
public class Username {
    //Declare UI element to collect username of the user
    private JFrame frame;
    private JTextField textField;
    private JButton button;
    private JButton button1;
    public String name;
    private TetrisV2 tetris;
    public Container con;
    Font normalFont = new Font("Lucida Sans Typewriter", Font.BOLD, 12);
    Color customcolor = new Color(137, 232, 205);
	Color customcolor2 = new Color(217, 115, 235);	
	Color customcolor3 = new Color(115, 215, 235);

    /**
     * 
     * @param tetris object of the current game where the username ui is called
     */
    public Username(TetrisV2 tetris) {
        this.tetris = tetris;
        initUsernameUI();
    }

    /**
     * Initilize the UI
     */
    public void initUsernameUI() {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(380, 120);
        frame.setResizable(true);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setTitle("Tetris by Group 14");
        frame.setVisible(true);

        con = frame.getContentPane();

        con.setLayout(new BorderLayout(8, 6));
        con.setBackground(customcolor3);
        frame.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, customcolor3));

        JPanel lp = new JPanel(); 
        lp.setBorder(new LineBorder(Color.BLACK));
        lp.setLayout(new FlowLayout(4,4,4));
        lp.setBackground(Color.BLACK);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(1, 1, 5, 5));
        gridPanel.setBorder(new LineBorder(Color.BLACK, 3));
        gridPanel.setBackground(Color.BLACK);        

        // Create UI elements
        button1 = new JButton("Enter your username");
        button1.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 15));
        //button1.setBounds(10,10,180,35);
        button1.setBackground(customcolor2);

        textField = new JTextField();
        textField.setBounds(110,55,180,35);
        textField.setBackground(Color.WHITE);

        button = new JButton("Save score");
        button.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 15));
        button.setBounds(140,100,120,35);
        button.setBackground(customcolor);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = gettextField().getText();
                System.out.println(name);
                try {
                    tetris.saveScore(name);
                } catch (IOException e1) {
                    System.out.println("Sorry could not find file");
                }
                new EndUI(tetris);
                frame.dispose();
            }
        });

        gridPanel.add(button);

        JPanel rp = new JPanel(); 
        rp.setBorder(new LineBorder(Color.BLACK));
        rp.setLayout(new FlowLayout(4,4,4));
        rp.setBackground(Color.BLACK);

        JPanel l = new JPanel(); 
        l.setLayout(new GridLayout(2, 1, 5, 5));     
        l.setBorder(new LineBorder(Color.BLACK));
        l.setBackground(Color.BLACK);

        lp.add(gridPanel);
        con.add(lp, BorderLayout.EAST);

        l.add(button1);
        l.add(textField);
        rp.add(l);
        con.add(rp, BorderLayout.WEST);
    }

    /**
     * 
     * @return get text field
     */
    public JTextField gettextField() {
        return textField;
    }

    /**
     * 
     * @param textField pass in text field
     */
    public void settextField(JTextField textField) {
        this.textField = textField;
    }

}