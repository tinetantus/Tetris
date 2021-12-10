/**
 * @author Group 14
 * @version 2021.12.08
 */

package v2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimerTask;

import javax.swing.Timer;

/**
 * TetrisV2
 */
public class TetrisV2 {
    // Declare UI
    protected GameUI ui;

    // Declare Variable
    protected int x;
    protected int y;
    protected int ID;
    protected int Mut;

    // Board size
    protected final int BOARD_COLUMN = 5;
    protected final int BOARD_ROW = 20;
    protected int[][] displayBoard;

    public int[][] piece;
    public int[][] board;
    public int[] future;
    public int score;

    public boolean isPause;
    public boolean isDone;

    public String firstPlace;
    public String secondPlace;
    public String thirdPlace;
    public String fourthPlace;
    public String fifthPlace;

    // Create the timer
    public static Timer FallRate;
    protected int INTERVAL = 500;

    /**
     * 
     * @param GUI
     */
    public TetrisV2(GameUI GUI) {

        ui = GUI;

        board = new int[BOARD_COLUMN][BOARD_ROW];
        displayBoard = new int[BOARD_COLUMN][BOARD_ROW - 5];

        future = new int[4];

        isPause = false;
        isDone = false;

        // Create Timer and set fallrate
        FallRate = new Timer(INTERVAL, new RunGame());

        initUI();
        initBoard();
    }

    /**
     * Initalises the board by making all the blocks empty, getting the first three
     * pieces
     * that will be placed and setting the score to 0.
     */
    public void initBoard() {
        // Create empty board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = -1;
            }
        }
        // Create piece array
        for (int i = 0; i < future.length; i++) {
            future[i] = (int) (Math.random() * 12);
        }
        // Set score
        score = 0;
    }

    /**
     * Initialises the UI and action listener so that when the buttons are pressed
     * the corresponding methods are called
     */
    protected void initUI() {
        // Set key listener
        ui.window.addKeyListener(new UserInput());
        ui.window.setFocusable(true);

        ui.button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPause) {
                    isPause = false;
                    FallRate.restart();
                    ui.button1.setText("Pause Game");
                } else {
                    isPause = true;
                    FallRate.stop();
                    callPauseUI();
                }
            }
        });

        ui.button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initBoard();
                beginGame();
            }
        });
    }

    /**
     * Begin the game so the piece is added to the board and starts to fall
     */
    public void beginGame() {

        // Initialize
        genPiece();
        add(x, y);
        FallRate.start();
        System.out.println("Begin Game");
    }

    /**
     * 
     * @return fall which is the TimerTask that has been created
     */
    public TimerTask createTimerTask() {
        TimerTask fall = new TimerTask() {
            public void run() {
                if (isBottom()) {
                    return;
                }
                fall();
            }
        };
        return fall;
    }

    /**
     * Moves the piece one block to the left, first checking if the piece is able to
     * move there
     */
    public void left() {
        if (x == 0) {
            return;
        }
        if (isHZMovable(-1)) {
            remove();
            x--;
            add(x, y);
            updateUI();
        }
    }

    /**
     * Moves the piece one block to the right, first checking if the piece is able
     * to move there
     */
    public void right() {
        if (x == 5 - PentominoDatabase.data[ID][Mut].length) {
            return;
        }
        if (isHZMovable(1)) {
            remove();
            x++;
            add(x, y);
            updateUI();
        }
    }

    /**
     * Moves the piece down one block, first checking if the piece is able to move
     * there
     */
    public void down() {
        if (isVZMovable(1)) {
            remove();
            y++;
            add(x, y);
            updateUI();
            System.out.println("DOWN");
            isBottom();
        }
    }

    /**
     * Checks if the piece is able to move 1 space to the left or right
     * 
     * @param n the amount of space the piece wants to move
     * @return true if the piece can move there without hitting another piece or
     *         going out of bounds, false otherwise.
     */
    protected boolean isHZMovable(int n) {
        int checkCol = x + n;

        if (checkCol < 0 | checkCol >= board.length) {
            // Out of bound
            // System.out.println("oob");
            return false;
        }

        for (int i = 0; i < piece[0].length; i++) {
            if (n < 0) {
                // System.out.println("Col " + checkCol +" Row " + y);
                if (board[checkCol][y - i] != -1 & piece[0][piece[0].length - 1 - i] != 0) {
                    // Left is not movable
                    // System.out.println(n + " l");
                    return false;
                }
            } else {
                if (board[checkCol + piece.length - 1][y - i] != -1
                        & piece[piece.length - 1][piece[0].length - 1 - i] != 0) {
                    // Right is not movable
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if the piece is able to move 1 space up or down
     * 
     * @param n the amount of space the piece wants to move
     * @return true if the piece can move there without hitting another piece or
     *         going out of bounds, false otherwise.
     */
    protected boolean isVZMovable(int n) {
        int checkRow = y + n;

        if (checkRow - piece[0].length + 1 < 0 | checkRow >= board[0].length) {
            // Out of bound
            return false;
        }

        for (int i = 0; i < piece.length; i++) {
            if (n < 0) {
                if (board[x + i][checkRow] != -1 & piece[i][0] != 0) {
                    // Up is not movable
                    return false;
                }
            } else {
                if (board[x + i][checkRow] != -1 & piece[i][piece[0].length - 1] != 0) {
                    // Down is not movable
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Rotates the piece once, making sure that it does not go out of bounds or into
     * another piece
     */
    public void rotate() {
        int[][] rotatePiece;
        int newMut;

        if (Mut < PentominoDatabase.data[ID].length - 1) {
            newMut = Mut + 1;
            rotatePiece = PentominoDatabase.data[ID][newMut];
        } else {
            newMut = 0;
            rotatePiece = PentominoDatabase.data[ID][0];
        }
        int index = 0;
        for (index = 0; index < 5; index++) {
            for (int i = 0; i < 5; i++) {

                if (isFit(rotatePiece, x - i, y + index)) {
                    remove();
                    Mut = newMut;
                    getPiece();
                    x = x - i;
                    y = y + index;
                    add(x, y);
                    isBottom();
                    return;
                }
            }
        }
        updateUI();
    }

    /**
     * Drops the piece to the bottom of the board or until it can no longer fall
     */
    public void speedDrop() {
        while (!isBottom()) {
            fall();
        }
    }

    /**
     * Checks if the piece fits in the particular position of the board without
     * going out of bounds
     * and checking the overlap with the exisiting pieces on the board
     * 
     * @param checkPiece the array that represents the piece
     * @param x          the x coordinate
     * @param y          the y coordinate
     * @return true if the piece fits and false otherwise
     */
    protected boolean isFit(int[][] checkPiece, int x, int y) {
        if (x == -1) {
            return false;
        }
        if (x + checkPiece.length > board.length | y - checkPiece[0].length + 1 < 0) {
            return false;
        }

        for (int i = 0; i < checkPiece[0].length; i++) {
            for (int j = 0; j < checkPiece.length; j++) {
                if (checkPiece[j][i] != -1 & checkPiece[j][i] != 12 & board[x + j][y - piece[0].length + 1 + i] == 12) {
                    // Check Overlap
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This methods gets the piece with a particular piece ID and Mut
     * 
     * @return the piece it just got
     */
    protected int[][] getPiece() {
        piece = PentominoDatabase.data[ID][Mut];
        return piece;
    }

    /**
     * Adds the piece to the board in a certain position
     * 
     * @param x the x coordinate of the place the piece should be added
     * @param y the y coordinate of the place the piece should be added
     */
    public void add(int x, int y) {
        // Loop over x position of pentomino
        for (int i = 0; i < piece[0].length; i++) {
            // Loop over y position of pentomino
            for (int j = 0; j < piece.length; j++) {
                if (piece[j][i] == 1) {
                    // Add the ID of the pentomino to the board if the pentomino occupies this
                    // square
                    board[x + j][y - i] = ID;
                }
            }
        }
        updateUI();
    }

    /**
     * Removes the piece from the board if it has the given item ID
     */
    protected void remove() {
        for (int i = 0; i < piece[0].length; i++) {
            for (int j = 0; j < piece.length; j++) {
                if (board[x + j][y - i] == ID)
                    board[x + j][y - i] = -1;
            }
        }
        updateUI();
    }

    /**
     * Makes the piece fall down the board until it reaches the bottom or until it
     * can no longer fall
     */
    protected void fall() {
        if (isBottom()) {
            return;
        } else {
            for (int i = board[0].length - 1; i > 0; i--) {
                for (int j = 0; j < board.length; j++) {
                    if (board[j][i] != 12 && board[j][i - 1] != 12) {
                        board[j][i] = board[j][i - 1];
                        board[j][i - 1] = -1;
                    }
                }
            }
            y++;
            updateUI();
        }
    }

    /**
     * Generates a new piece to be added to the future array list,
     * which is the three next items that will be added to the board
     */
    public void genPiece() {
        Mut = 0;
        ID = future[0];
        piece = PentominoDatabase.data[ID][0];
        x = (int) (Math.random() * (5 - (piece.length - 1)));
        y = 6;

        for (int i = 0; i < future.length - 1; i++) {
            future[i] = future[i + 1];
        }

        future[future.length - 1] = (int) (Math.random() * 12);
    }

    /**
     * Checks to see if the piece is at the bottom or can no longer fall
     * because the block of the other pieces that had previously fallen are in the
     * way
     * 
     * @return true if the piece is at the bottom of the board or can no longer
     *         fall, false otherwise
     */
    protected boolean isBottom() {
        boolean value = false;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == ID) {
                    if (j == board[0].length - 1 || board[i][j + 1] == 12) {
                        value = true;
                    }
                }
            }
        }
        if (value) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == ID) {
                        board[i][j] = 12;
                    }
                }
            }
        }

        if (value) {
            clearRow();
            fallAfterClear();
            genPiece();

            if (!isCeiling()) {
                add(x, y);
            } else {
                FallRate.stop();
                isDone = true;
                System.out.println("Game Done");
                new Username(this);
            }
        }
        return value;
    }

    /**
     * Cutting the extended part of the board to display only the 5x15 board size
     */
    protected void genDisplayBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 5; j < board[0].length; j++) {
                displayBoard[i][j - 5] = board[i][j];
            }
        }
    }

    /**
     * Convert Pentominoes ID to text string to display
     * 
     * @param id
     * @return String of letter representing the ID
     */
    protected String IDtoString(int id) {
        switch (id) {
            case 0:
                return "X";
            case 1:
                return "I";
            case 2:
                return "Z";
            case 3:
                return "T";
            case 4:
                return "U";
            case 5:
                return "V";
            case 6:
                return "W";
            case 7:
                return "Y";
            case 8:
                return "L";
            case 9:
                return "P";
            case 10:
                return "N";
            default:
                return "F";
        }
    }

    /**
     * Update the UI of the board so that the position of the pieces are upto date
     * and so is the score and future pieces
     */
    protected void updateUI() {
        genDisplayBoard();
        ui.setState(displayBoard);
        // Set Score
        ui.label.setText("  Score: " + String.valueOf(score));
        // Set display future piece
        ui.label1.setText(" Next piece: " + IDtoString(future[0]));
        ui.label2.setText(" Next piece: " + IDtoString(future[1]));
        ui.label3.setText(" Next piece: " + IDtoString(future[2]));
    }

    /**
     * Checks if the ceiling has been hit and depending on that, if the game should
     * continue or not
     * 
     * @return true if the ceiling has been hit, false otherwise
     */
    protected boolean isCeiling() {
        for (int i = 0; i < board.length; i++) {
            if (board[i][5] != -1) {
                isDone = true;
                return true;
            }
        }
        isDone = false;
        return false;
    }

    /**
     * Goes through the board and checks if there are any full rows and if there is
     * clear those rows
     * and the pieces above should cascade down until they reach the bottom or can
     * no longer fall
     */
    public void clearRow() {
        boolean isFill = false;

        for (int i = 0; i < board[0].length; i++) {
            isFill = true;
            for (int j = 0; j < board.length & isFill; j++) {
                if (board[j][i] == -1) {
                    isFill = false;
                }
            }

            if (isFill) {
                removeRow(i);
                scoreKeep();
                i--;
            }
        }
    }

    /**
     * This method is repsponsible for simulating the outcome of letting a piece
     * added fall down in a position
     * it currently is in.
     * 
     * @param copyy the copy of the board used currently
     */
    public void dropSimulation(int[][] copyy) {
        int bugfix = 0;
        while (!isBottomSim(copyy) & bugfix < 13) {
            bugfix++;
            simFall(copyy);
        }
    }

    /**
     * Cascade falling - simulate falling
     * 
     * @param copy
     */
    protected void simFall(int[][] copy) {
        for (int i = copy[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < copy.length; j++) {
                if (copy[j][i] == ID) {
                    copy[j][i + 1] = copy[j][i];
                    copy[j][i] = -1;
                }
            }
        }
    }

    /**
     * This method uses recursion to achieve infection like spread which we use to
     * find seperate blocks after
     * line is cleared in the tested board
     * 
     * @param index   row index
     * @param index2  column index
     * @param counter ID to which the block is set
     */
    public void infect(int index, int index2, int counter) {
        // System.out.println("IT IS CALLED");
        updateUI();

        if (index > 0) {
            if (board[index - 1][index2] == 12) {
                board[index - 1][index2] = counter;
                infect(index - 1, index2, counter);
            }
        }
        if (index2 > 0) {
            if (board[index][index2 - 1] == 12) {
                board[index][index2 - 1] = counter;
                infect(index, index2 - 1, counter);
            }
        }
        if (index < board.length - 1) {
            if (board[index + 1][index2] == 12) {
                board[index + 1][index2] = counter;
                infect(index + 1, index2, counter);

            }
        }
        if (index2 < board[0].length - 1) {
            if (board[index][index2 + 1] == 12) {
                board[index][index2 + 1] = counter;
                infect(index, index2 + 1, counter);
            }
        }
    }

    /**
     * Cascade falling - check if the blocks hit the bottom
     * 
     * @param copy
     * @return
     */
    protected boolean isBottomSim(int[][] copy) {
        boolean valueSim = false;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (copy[i][j] == ID) {
                    if (j == copy[0].length - 1 || (copy[i][j + 1] != -1 && copy[i][j + 1] != ID)) {
                        valueSim = true;
                    }
                }
            }
        }
        if (valueSim) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (copy[i][j] == ID) {
                        copy[i][j] = 12;
                    }
                }
            }

        }
        if (valueSim) {
            clearRowSim(copy);

        }
        return valueSim;
    }

    /**
     * Cascade falling - simulate clear row after
     * 
     * @param copy
     */
    public void clearRowSim(int[][] copy) {
        boolean isFillsim = false;
        for (int i = 0; i < copy[0].length; i++) {
            isFillsim = true;
            for (int j = 0; j < copy.length & isFillsim; j++) {
                if (copy[j][i] == -1) {
                    isFillsim = false;
                }
            }

            if (isFillsim) {
                removeRowSim(i, copy);
            }
        }
    }

    /**
     * 
     * @param k
     * @param copy
     */
    protected void removeRowSim(int k, int[][] copy) {
        for (int i = k; i >= 0; i--) {
            for (int j = 0; j < copy.length; j++) {
                // board[j][i] = -1;
                if (i != 0)
                    copy[j][i] = copy[j][i - 1];
            }
        }
    }

    public void fallAfterClear() {
        int counter = 13;
        updateUI();
        for (int index2 = board[0].length - 1; index2 >= 0; index2--) {
            for (int index = 0; index < board.length; index++) {

                if (board[index][index2] == 12) {
                    // System.out.println("AAAAAAA");

                    board[index][index2] = counter;
                    infect(index, index2, counter);
                    counter++;
                }
            }
        }

        updateUI();

        for (int i = counter; i > 12; i--) {
            ID = i;
            dropSimulation(board);
            updateUI();
        }
    }

    /**
     * Removes the row by turning all the blocks on that given row to -1, which
     * represents empty blocks
     * 
     * @param k the row that you want to remove
     */
    protected void removeRow(int k) {
        for (int i = k; i >= 0; i--) {
            for (int j = 0; j < board.length; j++) {
                // board[j][i] = -1;
                if (i != 0)
                    board[j][i] = board[j][i - 1];
            }
        }
        updateUI();
    }

    /**
     * Adds 1 to the score every time a row is cleared
     */
    protected void scoreKeep() {
        score++;
        // nui.label.setText(" Score: " + String.valueOf(score));
        System.out.println("Current Score: " + score);
    }

    /**
     * This method gets the users score
     * 
     * @return the users score
     */
    public int getScore() {
        return score;
    }

    /**
     * Saves the score in a Scores text file with the username once the player has
     * lost the game
     * 
     * @param name the username of the user playing the game
     * @throws IOException throws exception if no file is found
     */
    public void saveScore(String name) throws IOException {
        FileWriter file = new FileWriter("resources/Scores.txt", true);
        PrintWriter out = new PrintWriter(file);

        System.out.println(name);
        out.println(name + ": " + score);

        out.close();
        sortScores();
        getHighScore();
    }

    /**
     * Sorts the scores from highest to lowest in a the HighScores file so this can
     * be used later to show the high scores
     * 
     * @throws IOException throws exception if no file is found
     */
    private void sortScores() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("resources/Scores.txt"));
        ArrayList<User> scores = new ArrayList<User>();
        String currentLine = reader.readLine();
        while (currentLine != null) {
            String[] userDetails = currentLine.split(": ");
            String name = userDetails[0];
            int score = Integer.valueOf(userDetails[1]);
            scores.add(new User(name, score));
            currentLine = reader.readLine();
        }
        Collections.sort(scores, new scoreCompare());
        BufferedWriter writer = new BufferedWriter(new FileWriter("resources/HighScores.txt"));
        for (User User : scores) {
            writer.write(User.name);
            writer.write(" " + User.score);
            writer.newLine();
        }
        reader.close();
        writer.close();
    }

    /**
     * Gets the top 5 highest scores from the sorted highest scores text file
     * 
     * @throws IOException throws exception if no file is found
     */
    private void getHighScore() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("resources/HighScores.txt"));
        ArrayList<String> str = new ArrayList<>();
        String line = "";
        while ((line = reader.readLine()) != null) {
            str.add(line);
        }
        reader.close();

        firstPlace = str.get(0);
        secondPlace = str.get(1);
        thirdPlace = str.get(2);
        fourthPlace = str.get(3);
        fifthPlace = str.get(4);
    }

    /**
     * This pauses or restarts the game depending on which corresponding buttton is
     * pressed
     */
    public void pause() {
        if (isPause) {
            isPause = false;
            FallRate.restart();
        } else {
            isPause = true;
            FallRate.stop();
        }
    }

    /**
     * Calls the Pause UI to pop up when the pause button is pressed
     */
    private void callPauseUI() {
        new PauseUI(this);
    }

    /**
     * When the the game is not paused then the piece falls
     */
    protected void fallMachanism() {
        if (isPause) {
        } else {
            fall();
        }
    }

    /**
     * Adds a key listener to the game so that the user can move the pieces
     */
    class UserInput implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                System.out.println("Left");
                left();
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                System.out.println("Right");
                right();
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                System.out.println("Drop");
                down();
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                System.out.println("Rotate");
                rotate();
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                System.out.println("Speed drop");
                speedDrop();
            }
            if (e.getKeyCode() == KeyEvent.VK_P) {
                System.out.println("pause");
                pause();
            }
            if (e.getKeyCode() == KeyEvent.VK_H) {
                System.out.println("Hold");
                // hold();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

    }

    /**
     * This class runs the game, starting the fall machanism while making sure the
     * game isn't paused.
     */
    class RunGame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fallMachanism();
        }
    }

    /**
     * This class stores the username and score of the players so they can be easily
     * seperated
     * so that the scores can be compared without the username,
     * and then the username is readded to the score.
     */
    class User {
        String name;
        int score;

        public User(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    /**
     * This class is used to find the highest scores and compares two scores to see
     * which one is bigger
     */
    class scoreCompare implements Comparator<User> {
        @Override
        public int compare(User u1, User u2) {
            return u2.score - u1.score;
        }
    }
}