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

/**
 * Launch start ui page where user can choose to play game or bot
 */
public class StartUI {

	// UI Element
	Container con;
	JButton button;
	JButton button2;

	// Declare color profile
	Color customcolor = new Color(137, 232, 205);
	Color customcolor2 = new Color(217, 115, 235);
	Color customcolor3 = new Color(41, 193, 231);

	// Sound element
	JButton soundButton;
	String clickSound;
	ButtonHandler bHandler = new ButtonHandler();
	SoundEffect se = new SoundEffect();

	/**
	 * UI constructer class
	 */
	public StartUI() {
		// Create frame and set property
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 320);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Tetris by Group 14");
		frame.setLayout(new BorderLayout());
		frame.setContentPane(new JLabel(new ImageIcon("resources/logoframe1.png")));
		frame.setVisible(true);

		// Create play Game button
		button = new JButton("Play");
		button.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 17));
		button.setBounds(93, 95, 200, 40);
		button.setBackground(Color.WHITE);

		// Set sound effect
		button.setFocusPainted(false);
		button.addActionListener(bHandler);
		button.setActionCommand("soundB");
		clickSound = "resources/mouseclick1.wav";

		// Set actionlistener to call GameUI and pass keyword "Game"
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new GameUI("Game");
			}
		});

		frame.add(button);

		// Create play Bot button
		button2 = new JButton("Play with Bot");
		button2.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 17));
		button2.setBounds(93, 155, 200, 40);
		button2.setBackground(Color.WHITE);

		// Set sound effect
		button2.setFocusPainted(false);
		button2.addActionListener(bHandler);
		button2.setActionCommand("soundB");
		clickSound = "resources/mouseclick1.wav";

		// Set actionlistener to call GameUI and pass keyword "Bot"
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new GameUI("Bot");
			}
		});
		frame.add(button2);

		// Set hover effect
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(customcolor);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(customcolor3);
			}
		});

		button2.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button2.setBackground(customcolor2);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button2.setBackground(customcolor3);
			}
		});
	}

	/**
	 * Sound effect classes
	 */
	public class SoundEffect {
		Clip clip;

		/**
		 * Set audio file to use as sound effect for object Sound effect
		 * 
		 * @param soundFileName filename and address of audio file to use as sound
		 *                      effect
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
	 * Actionlister to activate scound effect when call
	 */
	public class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			se.setFile(clickSound);
			se.play();
		}
	}
}