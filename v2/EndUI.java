/**
 * @author Group 14
 * @version 2021.12.08
 */

package v2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import javax.swing.*;

public class EndUI extends JPanel {

	TetrisV2 tetris;

	// UI Elements
	JFrame window;
	JLabel label;
	JPanel buttonPanel;
	Container con;
	JButton b1;
	JButton b2;
	JButton b3;
	JButton soundButton;
	String clickSound;
	JLabel background = new JLabel(new ImageIcon("resources/tetborder.png"));

	// Sound elements
	ButtonHandler bHandler = new ButtonHandler();
	SoundEffect se = new SoundEffect();

	// Customised fonts
	Font normalFont = new Font("Lucida Sans Typewriter", Font.BOLD, 20);
	Font secondFont = new Font("Lucida Sans Typewriter", Font.PLAIN, 15);

	// Customised colours
	Color customcolor = new Color(137, 232, 205);
	Color customcolor2 = new Color(217, 115, 235);
	Color customcolor3 = new Color(235, 165, 115);

	/**
	 * UI constructor class
	 */
	public EndUI(TetrisV2 tetris) {
		this.tetris = tetris;
		initEndUI();
	}

	public void initEndUI() {
		// Creating frame, setting it properly, adding functionality
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(600, 520);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setLayout(new BorderLayout());
		window.setTitle("Tetris by Group 14");
		window.setVisible(true);
		window.setContentPane(new JLabel(new ImageIcon("resources/tetborder.png")));

		// Create "game over" button
		b1 = new JButton("Game over");
		b1.setFont(normalFont);
		b1.setBounds(190, 135, 200, 50);
		b1.setBackground(customcolor);
		// Adding action listener for exiting the game once the player clicks the button
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				window.dispose();
				System.exit(0);
			}
		});
		window.add(b1);

		// Create "play again" button
		b2 = new JButton("Play Again");
		b2.setFont(secondFont);
		b2.setBounds(215, 220, 150, 40);
		b2.setBackground(customcolor2);
		// Adding "click" sound effect
		b2.setFocusPainted(false);
		b2.addActionListener(bHandler);
		b2.setActionCommand("soundB");
		clickSound = "resources/mouseclick1.wav";
		b2.setFocusPainted(false);
		// Adding action listener for resetting the game once the player clicks the
		// button: dispose of window, reinitialise board and start gameplay
		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				window.dispose();
				tetris.initBoard();
				tetris.beginGame();
			}
		});
		window.add(b2);

		// Create the "Scoreboard" button
		b3 = new JButton("Scoreboard");
		b3.setFont(secondFont);
		b3.setBounds(215, 290, 150, 40);
		b3.setBackground(customcolor2);
		// Adding sound effect to button
		b3.setFocusPainted(false);
		b3.addActionListener(bHandler);
		b3.setActionCommand("soundB");
		clickSound = "resources/mouseclick1.wav";
		b3.setFocusPainted(false);
		// Adding action listener for switching to leaderboard window once the player
		// clicks the button: dispose of current UI and open the ScoreBoard
		b3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ScoreBoard(tetris);
				window.dispose();
			}
		});
		window.add(b3);

		// Adding hover effect for buttons
		b2.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				b2.setBackground(customcolor3);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				b2.setBackground(customcolor2);
			}
		});

		b3.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				b3.setBackground(customcolor3);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				b3.setBackground(customcolor2);
			}
		});

	}

	/**
	 * Sound effect classes
	 */
	public class SoundEffect {

		Clip clip;

		/**
		 * Set audio file to use as sound effect for object SoundEffect
		 * 
		 * @param soundFileName filename and pathway of audio file that has to be used
		 *                      as sound effect
		 */

		public void setFile(String soundFileName) {

			try {
				File file = new File(soundFileName);
				AudioInputStream sound = AudioSystem.getAudioInputStream(file);
				clip = AudioSystem.getClip();
				clip.open(sound);
			} catch (Exception e) {

			}
		}

		/**
		 * Call play method to play sound effect
		 */
		public void play() {

			clip.setFramePosition(0);
			clip.start();
		}

	}

	/**
	 * Action Listener to activate the sound effect when called
	 */
	public class ButtonHandler implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			se.setFile(clickSound);
			se.play();
		}
	}
}
