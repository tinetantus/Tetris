
/**
 * @author Group 14
 * @version 2021.12.08
 */

 package v2;

import java.awt.event.*;
import java.util.Arrays;
import javax.swing.Timer;

/**
 * BotV2
 * This class is contains all the game logic and simulation logic needed to run
 * a bot played game.
 */

public class BotV2 extends TetrisV2 {

    private int[][] simPiece;
    private int[] instructions = new int[2];
    private int numberOfRowsDeleted;
    private double[] weightScore;

    public BotV2(GameUI GUI) {


        super(GUI);

        INTERVAL = 5;
        // Standard Declaration
        ui = GUI;

        board = new int[BOARD_COLUMN][BOARD_ROW];
        displayBoard = new int[BOARD_COLUMN][BOARD_ROW - 5];

        future = new int[4];

        isPause = false;
        isDone = false;

        // Set weight scoring system
        weightScore = new double[4];
        weightScore[0] = 0.405;
        weightScore[1] = 0.234;
        weightScore[2] = 0.246;
        weightScore[3] = 0.118;

        // Create Timer and set fallrate
        FallRate = new Timer(INTERVAL, new RunGame());

        initUI();
        initBoard();
    }

    class RunGame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fallMachanism();
        }
    }

    /**
     * Adds the piece to the board in a certain position but its used only when a
     * new piece is added
     * 
     * @param x the x coordinate of the place the piece should be added
     * @param y the y coordinate of the place the piece should be added
     */
    private void addPiece(int x, int y) {

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
        // The part below is responsible for the movement of the bot.
        movePieces();
    }

    /**
     * This method is responsible for using the informations stored by bot for the
     * movement of pieces
     */
    private void movePieces() {
        for (int index = neededRot(); index > 0; index--) {
            rotate();
            updateUI();

        }
        if (distance() > 0) {
            for (int index = 0; index < distance(); index++) {
                right();
                updateUI();

            }
        }
        if (distance() < 0) {
            for (int index = distance(); index < 0; index++) {
                left();
                updateUI();

            }
        }
        if (distance() == 1) {

            right();
            updateUI();

        }
        if (distance() == 2) {
            right();
            right();
        }
        updateUI();
    }

    /**
     * Generates a new piece to be added to the future array list,
     * which is the three next items that will be added to the board
     */
    public void genPiece() {

        ID = future[0];
        Mut = 0;

        piece = PentominoDatabase.data[ID][Mut];
        x = (int) (Math.random() * (5 - (piece.length - 1)));
        y = 4;
        fallSim();

        for (int i = 0; i < future.length - 1; i++) {
            future[i] = future[i + 1];
        }

        future[future.length - 1] = (int) (Math.random() * 12);

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
                if (board[j][i] != 12) {
                    isFill = false;
                }
            }

            if (isFill) {
                removeRow(i);
                scoreKeep();
                i--;
            }
        }
        fallAfterClear();
        fallAfterClear();
    }

    /**
     * This method gets the users score
     * 
     * @return the users score
     */
    public int getScore() {
        return score;
    }

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
            updateUI();
            genPiece();
            if (isFit(piece, x, y)) {
                addPiece(x, y);
            } else {
                FallRate.stop();
                isDone = true;
            }
        }
        return value;
    }

    /**
     * This method initializes UI
     */
    protected void initUI() {
        // Set key listener
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
                    ui.button1.setText("Resume Game");
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
     * This method is used to calculate needed movement for bot to achieve desired
     * location
     * 
     * @return distance
     */
    public int distance() {
        return instructions[0] - x;
    }

    /**
     * This method is used to calculate needed amount of needed rotations
     * 
     * @return number of needed rotations
     */
    public int neededRot() {
        return instructions[1] - Mut;
    }

    /**
     * This method is used to create a copy of an array instead of reference to the
     * object
     * 
     * @param copye
     * @return
     */
    public int[][] cloneArray(int[][] copye) {
        int[][] copy = Arrays.stream(copye).map(int[]::clone).toArray(int[][]::new);
        return copy;
    }

    /**
     * This method is used to drop things that dont have support after a line is
     * cleared
     */
    public void fallAfterClear() {
        int counter = 13;

        for (int index = 0; index < board.length; index++) {

            for (int index2 = board[0].length - 1; index2 >= 0; index2--) {

                if (board[index][index2] == 12) {

                    board[index][index2] = counter;
                    infectSim(index, index2, counter, board);

                    counter++;

                }
            }
        }
        for (int i = 13; i <= counter; i++) {
            ID = i;
            dropSimulation(board);

        }

        ID = future[0];
    }

    /**
     * This method is used to drop things that dont have support after a line is
     * cleared in simulation
     * 
     * @param copy
     */
    public void fallAfterClearSim(int[][] copy) {
        int counter = 13;
        for (int index = 0; index < board.length; index++) {

            for (int index2 = board[0].length - 1; index2 >= 0; index2--) {

                if (copy[index][index2] == 12) {
                    // System.out.println("AAAAAAA");

                    copy[index][index2] = counter;
                    infectSim(index, index2, counter, copy);

                    counter++;

                }
            }
        }
        for (int i = 13; i <= counter; i++) {
            ID = i;
            dropSimulation(copy);

        }

        ID = future[0];
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
    public void infectSim(int index, int index2, int counter, int[][] copy) {
        // System.out.println("IT IS CALLED");
        // updateUI();

        if (index > 0) {
            if (copy[index - 1][index2] == 12) {
                copy[index - 1][index2] = counter;
                infectSim(index - 1, index2, counter, copy);
            }
        }
        if (index2 > 0) {
            if (copy[index][index2 - 1] == 12) {
                copy[index][index2 - 1] = counter;
                infectSim(index, index2 - 1, counter, copy);
            }
        }
        if (index < board.length - 1) {
            if (copy[index + 1][index2] == 12) {
                copy[index + 1][index2] = counter;
                infectSim(index + 1, index2, counter, copy);

            }
        }
        if (index2 < board[0].length - 1) {
            if (copy[index][index2 + 1] == 12) {
                copy[index][index2 + 1] = counter;
                infectSim(index, index2 + 1, counter, copy);
            }
        }

    }

    /**
     * This method adds pieces to the copy board
     * 
     * @param x    row index
     * @param y    column index
     * @param copy array to which the piece is added
     */
    public void addSim(int x, int y, int[][] copy) {

        // Loop over x position of pentomino
        for (int i = 0; i < simPiece[0].length; i++) {
            // Loop over y position of pentomino
            for (int j = 0; j < simPiece.length; j++) {
                if (simPiece[j][i] == 1) {
                    // Add the ID of the pentomino to the board if the pentomino occupies this
                    // square
                    copy[x + j][y - i] = ID;
                }
            }
        }

    }

    /**
     * This method finds filled rows and calls removeRowSim to clear them
     * 
     * @param copy the copy of the board used currently
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
     * This method is the main body of the simulation required to make decisions by
     * bot. It creates
     * copies of the boards add pieces and drops them simulating every possible
     * location of current piece
     * and 2 pieces into the future assigning value to every one of the boards and
     * storing locations of the best
     * graded combination.
     */
    private void fallSim() {
        // System.out.println("fallSim");
        int copy[][];
        int a;
        double grade = -10000;
        int[][] copy2;
        int[][] copy3;
        double copyBoardgrade = 0;

        for (int i = 0; i < PentominoDatabase.data[future[0]].length; i++) {
            for (int j = 0; j < 6 - PentominoDatabase.data[future[0]][i].length; j++) {
                a = PentominoDatabase.data[future[0]][i][0].length - 1;

                simPiece = PentominoDatabase.data[future[0]][i];
                copy = cloneArray(board);
                double numberOfBlocksBefore = numberOfBlocks(copy);
                addSim(j, a, copy);
                dropSimulation(copy);
                fallAfterClearSim(copy);

                for (int index = 0; index < PentominoDatabase.data[future[1]].length; index++) {
                    for (int jndex = 0; jndex < 6 - PentominoDatabase.data[future[1]][index].length; jndex++) {
                        a = PentominoDatabase.data[future[1]][index][0].length - 1;
                        simPiece = PentominoDatabase.data[future[1]][index];
                        copy2 = cloneArray(copy);
                        addSim(jndex, a, copy2);
                        dropSimulation(copy2);
                        fallAfterClearSim(copy2);

                        for (int index2 = 0; index2 < PentominoDatabase.data[future[2]].length; index2++) {
                            for (int jndex2 = 0; jndex2 < 6
                                    - PentominoDatabase.data[future[2]][index2].length; jndex2++) {
                                a = PentominoDatabase.data[future[2]][index2][0].length - 1;
                                simPiece = PentominoDatabase.data[future[2]][index2];
                                copy3 = cloneArray(copy2);
                                addSim(jndex2, a, copy3);
                                dropSimulation(copy3);
                                fallAfterClearSim(copy3);

                                numberOfRowsDeleted = (int) (numberOfBlocksBefore - (numberOfBlocks(copy) - 15)) / 5;

                                copyBoardgrade = gradeCopyBoardV4(copy3, numberOfRowsDeleted);
                                if (copyBoardgrade > grade) {
                                    grade = copyBoardgrade;
                                    instructions[0] = j;
                                    instructions[1] = i;

                                }
                            }
                        }
                    }
                }
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
            if (isBottomSim(copyy)) {
                break;
            }

        }
    }

    /**
     * This method is grading every board simulated by FallSim
     * 
     * @param copy                the copy of the board used currently
     * @param numberOfRowsDeleted Ammount of rows cleared
     * @return grade of the board copy
     */
    public double gradeCopyBoardV4(int[][] copy, int numberOfRowsDeleted) {
        double grade = 0;

        grade += 0.4 * (numberOfRowsDeleted);
        grade -= 0.147 * heightDiffrence(copy);
        grade -= 0.274 * heightSum(copy);
        grade -= 0.368 * inaccesibleBlocks(copy);

        return grade;
    }

    /**
     * This grade find all the inaccesible blocks meaning that they are empty blocks
     * covered by a block
     * somewhere above
     * 
     * @param copy
     * @return amount of inaccesible blocks
     */
    public double inaccesibleBlocks(int[][] copy) {
        double number = 0;
        for (int index = 0; index < copy.length; index++) {
            for (int index2 = copy[0].length - 1; index2 >= 0; index2--) {
                if (copy[index][index2] == -1) {
                    for (int index3 = index2; index3 > 0; index3--) {
                        if (copy[index][index3] == 12) {
                            number++;
                            break;
                        }
                    }
                }
            }
        }
        return number;
    }

    /**
     * It calculates the number of filled blocks which is used in calculating
     * removed lines
     * 
     * @param copy
     * @return number of blocks
     */
    public double numberOfBlocks(int[][] copy) {
        double number = 0;
        for (int index = 0; index < copy.length; index++) {
            for (int index2 = 0; index2 < copy[0].length; index2++) {
                if (copy[index][index2] == 12) {
                    number++;
                }
            }
        }
        return number;
    }

    /**
     * Is used to summ up height of every individual column
     * 
     * @param copy
     * @return the sum of heights of every column
     */
    public double heightSum(int[][] copy) {
        double grade = 0;
        for (int index = 0; index < copy.length; index++) {
            for (int index2 = 0; index2 < copy.length; index2++) {
                if (copy[index][index2] == 12) {
                    grade += 14 - index2;
                    break;
                }
            }
        }
        return grade;
    }

    /**
     * Is used to calculate sum of differences between every column and the next one
     * 
     * @param copy
     * @return the sum of differences
     */
    public double heightDiffrence(int[][] copy) {
        int diffrence = 0;
        int newValue = 0;
        int oldvalue = 0;
        for (int index = 0; index < copy[0].length; index++) {
            if (copy[0][index] == 12) {
                oldvalue = 14 - index;
                break;
            }
        }
        for (int index = 0; index < copy.length; index++) {
            for (int index2 = 0; index2 < copy[0].length; index2++) {
                if (copy[index][index2] == 12) {
                    oldvalue = newValue;
                    newValue = 14 - index2;
                    diffrence += Math.abs(newValue - oldvalue);
                    break;
                }
            }
        }
        return diffrence;
    }

}