/**
 * @author Group 14
 * @version 2021.12.08
 */

package v2;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Launch game ui that display the board and control
 */
public class GameUI extends JPanel {

    // Declare Frame and board display
    public JFrame window;
    private int[][] state;

    // Declare sizing
    private final int SIZE = 50;
    public final int COLUMN = 5;
    public final int ROW = 15;

    // Delcare color profile
    Color customcolor3 = new Color(116, 155, 227);
    Color customcolor2 = new Color(188, 111, 209);
    Color customcolor = new Color(137, 232, 205);

    // Declare element in Game UI
    JButton button1 = new JButton("Pause Game");
    JButton button2 = new JButton("Restart Game");
    Font normalFont = new Font("Lucida Sans Typewriter", Font.BOLD, 12);
    public JPanel gridPanel;
    public JLabel label = new JLabel();
    public JLabel label1 = new JLabel();
    public JLabel label2 = new JLabel();
    public JLabel label3 = new JLabel();

    /**
     * Construct Game user interface with side panel display score, button control and board game
     * 
     * @param str parameter pass down from user action that tell the Game ui to create either "Game" object or "Bot" object
     */
    public GameUI(String str) {
        
        //If keyword pass down is "Game" then create game instance and call method to begin game
        if (str.equals("Game")) {
            System.out.println("Game UI is here");
            initGameUI();
            TetrisV2 tetris = new TetrisV2(this);
            tetris.beginGame();
        
        //If keyword pass down is "Bot" then create bot instance and call method to begin the bot run
        } else if (str.equals("Bot")) {
            System.out.println("Bot UI is here");
            initGameUI();
            BotV2 bot = new BotV2(this);
            bot.beginGame();
        } else if (str.equals("Manual")) {
            initGameUI();
            System.out.println("Manual UI is here");
        }
    }

    /**
     * Initialize all visual element of the UI
     * 
     */
    public void initGameUI() {

        // Set the size of the board
        setPreferredSize(new Dimension(COLUMN * SIZE, ROW * SIZE));

        // Create Frame and set property
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(425, 800);
        window.setResizable(true);
        window.getContentPane().setBackground(Color.black);
        window.setLocationRelativeTo(null);
        window.setLayout(null);
        window.setTitle("Tetris by Group 14");
        window.setVisible(true);

        // Create container for the board
        Container con = this.getContentPane();
        con = window.getContentPane();
        con.setLayout(new BorderLayout(8, 6));
        con.setBackground(customcolor3);
        window.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, customcolor3));

        // Create and set property for element in game UI
        JPanel lp = new JPanel();
        lp.setBorder(new LineBorder(Color.BLACK));
        lp.setLayout(new FlowLayout(4, 4, 4));
        lp.setBackground(customcolor);

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(2, 1, 5, 5));
        gridPanel.setBorder(new LineBorder(Color.BLACK, 3));
        gridPanel.setBackground(Color.BLACK);

        button1.setBackground(customcolor2);
        button1.setFont(normalFont);
        button1.setBounds(190, 80, 200, 50);
        button1.setFocusable(false);
        button2.setBackground(customcolor2);
        button2.setBounds(190, 80, 200, 50);
        button2.setFont(normalFont);
        button2.setFocusable(false);

        label.setForeground(Color.WHITE);
        label.setFont(normalFont);
        label.setBounds(190, 80, 200, 50);

        label1.setForeground(Color.WHITE);
        label1.setFont(normalFont);

        label2.setForeground(Color.WHITE);
        label2.setFont(normalFont);

        label3.setForeground(Color.WHITE);
        label3.setFont(normalFont);

        // Set layout for side panel
        GroupLayout layout = new GroupLayout(gridPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(button1)
                        .addComponent(button2)
                        .addComponent(label).addComponent(label1).addComponent(label2).addComponent(label3)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(button1))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(button2))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label1))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label2))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label3)));
        layout.linkSize(SwingConstants.HORIZONTAL, button1);
        gridPanel.setLayout(layout);
        
        JPanel l = new JPanel();
        l.setBorder(new LineBorder(Color.BLACK));
        l.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
        l.setBackground(Color.BLACK);

        // Add all element to panel and panel to frame
        lp.add(gridPanel);
        con.add(l);
        con.add(lp, BorderLayout.WEST);
        l.add(this);
        window.add(l);

        // Create and set board to empty
        state = new int[COLUMN][ROW];
        setEmptyBoard();

        //Make the UI visible
        setVisible(true);
        setFocusable(true);
    }

    /**
     * Set state board to empty
     */
    private void setEmptyBoard() {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                state[i][j] = -1;
            }
        }
    }

    private Container getContentPane() {
        return null;
    }

    /**
     * This function is called BY THE SYSTEM if required for a new frame, uses the
     * state stored by the UI class.
     */
    public void paintComponent(Graphics g) {
        Graphics2D localGraphics2D = (Graphics2D) g;

        localGraphics2D.setColor(Color.DARK_GRAY);
        localGraphics2D.fill(getVisibleRect());

        // draw lines
        localGraphics2D.setColor(Color.GRAY);
        for (int i = 0; i <= state.length; i++) {
            localGraphics2D.drawLine(i * SIZE, 0, i * SIZE, state[0].length * SIZE);
        }
        for (int i = 0; i <= state[0].length; i++) {
            localGraphics2D.drawLine(0, i * SIZE, state.length * SIZE, i * SIZE);
        }

        // draw blocks
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                localGraphics2D.setColor(GetColorOfID(state[i][j]));
                localGraphics2D.fill(new Rectangle2D.Double(i * SIZE + 1, j * SIZE + 1, SIZE - 1, SIZE - 1));
            }
        }
    }

    /**
     * Decodes the ID of a pentomino into a color
     * 
     * @param i ID of the pentomino to be colored
     * @return the color to represent the pentomino. It uses the class Color (more
     *         in ICS2 course in Period 2)
     */
    private Color GetColorOfID(int i) {
        if (i == 0) {
            return Color.BLUE;
        } else if (i == 1) {
            return Color.ORANGE;
        } else if (i == 2) {
            return Color.CYAN;
        } else if (i == 3) {
            return Color.GREEN;
        } else if (i == 4) {
            return Color.MAGENTA;
        } else if (i == 5) {
            return Color.PINK;
        } else if (i == 6) {
            return Color.RED;
        } else if (i == 7) {
            return Color.YELLOW;
        } else if (i == 8) {
            return new Color(0, 0, 0);
        } else if (i == 9) {
            return new Color(0, 0, 100);
        } else if (i == 10) {
            return new Color(100, 0, 0);
        } else if (i == 11) {
            return new Color(0, 100, 0);
        } else if (i == 12) {
            return Color.white;
        }

        else {
            return Color.DARK_GRAY;
        }
    }

    /**
     * This function should be called to update the displayed state (makes a copy)
     * 
     * @param _state information about the new state of the GUI
     */
    public void setState(int[][] _state) {
        // Copy new _state to the current state
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                state[i][j] = _state[i][j];
            }
        }
        // Tells the system a frame update is required
        repaint();
    }
}
