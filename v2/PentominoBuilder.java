package v2;

/**
 * @author Group 14
 * @version 2022.0
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * This class contains all the methods that you may need to start developing your project together with the representation of the pentomino's pieces
 */
public class PentominoBuilder {

    //All basic pentominoes that will be rotated and flipped
    private static int[][][] basicDatabase = {
            {
            	// Pentomino representation X
                    {0,1,0},
                    {1,1,1},
                    {0,1,0}
            },
            {
            	// Pentomino representation I
                    {1},
                    {1},
                    {1},
                    {1},
                    {1}
            },
            {
            	// Pentomino representation Z
                    {0,1,1},
                    {0,1,0},
                    {1,1,0}
            },
            {
            	// Pentomino representation T
                    {1,1,1},
                    {0,1,0},
                    {0,1,0}
            },
            {
            	// Pentomino representation U
                    {1,1},
                    {1,0},
                    {1,1}
            },
            {
            	// pentomino representation V
                    {1,1,1},
                    {1,0,0},
                    {1,0,0}
            },
            {
            	// Pentomino representation W
                    {0,0,1},
                    {0,1,1},
                    {1,1,0}
            },
            {
            	// Pentomino representation Y
                    {1,0},
                    {1,1},
                    {1,0},
                    {1,0}
            },
            {
            	// Pentomino representation L
                    {1,0},
                    {1,0},
                    {1,0},
                    {1,1}
            },
            {
        		// Pentomino representation P
                    {1,1},
                    {1,1},
                    {1,0}

            },
            {
                // Pentomino representation N
                    {1,1,0,0},
                    {0,1,1,1}
            },
            {
                // Pentomino representation F
                    {0,1,1},
                    {1,1,0},
                    {0,1,0}
            }
    };

    // All pentominoes, including their rotations
    public static ArrayList<int[][][]> database = new ArrayList<>();

    /**
     * Make the database, created based on the pentomino's pieces defined in basicDatabase
     */
    public static void makeDatabase()
    {
        // Do it for every piece of the basic database
        for(int i=0;i<basicDatabase.length;i++)
        {
            // Make a piece with maximal number of mutations an space
            int[][][] tempDatabase = new int[4][5][5];

            // Take a piece of basic database, make it bigger so it fits in the 5*5, rotate it j times, move it to the left upper corner so duplicates will be the same
            for (int j = 0; j < 4; j++) {
                tempDatabase[j] = moveToAbove(rotate(makeBigger(basicDatabase[i], 5), j));
            }

            // Erase duplicates
            tempDatabase=eraseDuplicates(tempDatabase);

            // Erase empty spaces in every piece
            for(int j=0;j<tempDatabase.length;j++)
            {
                tempDatabase[j]=eraseEmptySpace(tempDatabase[j]);
            }

            // Add the found pieces of just one basic piece to the database
            database.add(tempDatabase);
        }
    }

    /**
     * Rotate the matrix x times over 90 degrees 
     * Assume that the matrix is a square!
     * It does not make a copy, so the return matrix does not have to be used
     * @param data: a matrix
     * @param rotation: amount of rotation
     * @return the rotated matrix
     */
    public static int[][] rotate(int[][] data, int rotation)
    {
        int [][] tempData1 = new int[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                tempData1[i][j]=data[i][j];
            }
        }

        // Do it for the amount of times it needs to be rotated
        for(int k=0;k<rotation;k++) {
            // Make a matrix of the same size
            int[][] tempData2 = new int[tempData1.length][tempData1[0].length];
            // Rotate it once and put it in tempData
            for (int i = 0; i < tempData1.length; i++) {
                for (int j = 0; j < tempData1[i].length; j++) {
                    tempData2[i][j] = tempData1[j][tempData1.length - i - 1];
                }
            }
            // Put it back in the starting matrix so you can do it again
            for (int i = 0; i < tempData1.length; i++) {
                for (int j = 0; j < tempData1[i].length; j++) {
                    tempData1[i][j] = tempData2[i][j];
                }
            }
        }
        return tempData1;
    }

    /**
     * Expands a smaller than size*size matrix to a size*size matrix
     * It makes a copy, the input matrix stays unchanged
     * Assume that the input is smaller than size!!
     * @param data: a matrix
     * @param size: the square size of the new matrix
     * @return the size*size matrix
     */
    public static int[][] makeBigger(int[][] data,int size)
    {
        // Make a matrix of size*size
        int[][] returnData = new int[size][size];
        // Copies the matrix in the new matrix
        for(int i=0;i<data.length;i++)
        {
            for (int j = 0; j < data[i].length; j++)
            {
                returnData[i][j]=data[i][j];
            }
        }
        return returnData;
    }

    /**
     * Move matrix to the left above corner
     * Does not make a copy!
     * @param data: a matrix
     * @return the modified matrix
     */
    public static int[][] moveToAbove(int[][] data)
    {
        // The amount of rows it needs to make empty after moving up
        int amountToCut=0;
        // Do it for the the amount of rows there are to be sure
        for(int i=0;i<data[0].length;i++)
        {
            // Check if the first row is empty
            int empty=0;
            for(int j=0;j<data.length;j++)
            {
                if(data[j][0]==1)
                {
                    empty=1;
                }
            }
            // If empty move everything one up
            if(empty==0)
            {
                for(int j=0;j<data.length;j++)
                {
                    for(int k=1;k<data[j].length;k++)
                    {
                        data[j][k - 1] = data[j][k];
                    }
                }
                amountToCut++;
            }
        }
        // Make the last amountToCut rows empty, because these are copies
        for(int j=0;j<data.length;j++) {
            for (int k = data[j].length - amountToCut; k < data[j].length; k++) {
                data[j][k] = 0;
            }
        }

        // The amount of columns it needs to make empty after moving up
        amountToCut=0;
        // Do it for the the amount of columns there are to be sure
        for(int i=0;i<data.length;i++)
        {
            // Check if the first column is empty
            int empty=0;
            for(int j=0;j<data[0].length;j++)
            {
                if(data[0][j]==1)
                {
                    empty=1;
                }
            }
            // If empty move everything one to the left
            if(empty==0)
            {
                for(int j=0;j<data[0].length;j++)
                {
                    for(int k=1;k<data.length;k++)
                    {
                        data[k - 1][j] = data[k][j];
                    }
                }
                amountToCut++;
            }
        }
        // Make the last amountToCut columns empty, because these are copies
        for(int j=data.length - amountToCut;j<data.length;j++) {
            for (int k = 0; k < data[j].length; k++) {
                data[j][k] = 0;
            }
        }
        return data;
    }

    /**
     * Erase duplicates in a array of matrices
     * The input matrix stays unchanged
     * @param data an array of matrices
     * @return the array of matrices without duplicates
     */
    public static int[][][] eraseDuplicates(int[][][] data)
    {
        // Make a counter that counts how many unique matrices there are
        int counter=0;
        // Check all matrices of the input
        for(int i =0;i<data.length;i++)
        {
            // Make an adder and set it to 1, if you find a duplicate, set it to 0
            int adder=1;
            // Go from the start till the matrix that you are checking now
            for(int j=0;j<i;j++)
            {
                // Check if they are equal
                if(isEqual(data[i],data[j]))
                {
                    adder=0;
                }
            }
            counter+=adder;
        }
        // Make an array of matrices with size counter
        int[][][] returnData = new int[counter][][];
        // A counter that keeps how many matrices you already added to the new array of matrices
        counter=0;
        // Check all matrices of the input
        for(int i =0;i<data.length;i++)
        {
            // Go from the start till the matrix that you are checking now
            boolean alreadyExist=false;
            for(int j=0;j<i;j++)
            {
                if(isEqual(data[i],data[j]))
                {
                    alreadyExist=true;
                }
            }
            // If it's not already added, add it to the array
            if(alreadyExist==false) {
                returnData[counter] = data[i];
                // Add one to counter, so next time you know where to add something
                counter++;
            }
        }
        return returnData;
    }

    /**
     * Check if two matrices are equal
     * Assume they have the same size
     * @param data1: the first matrix
     * @param data2: the second matrix
     * @return true if equal, false otherwise
     */
    public static boolean isEqual(int[][] data1, int[][] data2)
    {
    	// Set the boolean equal to true 
        boolean isEqual = true;
        // Check if the matrices are equal 
        for (int i = 0; i < data1.length; i++) {
            for (int j = 0; j < data1[0].length; j++) {
                // If they are not equal change the boolean to false 
                if (data1[i][j]!=data2[i][j]) {
                    return isEqual = false;
                }
            }
        }
        return isEqual;
    }


    /**
     * Erase rows and columns that contain only zeros
     * @param data a matrix
     * @return the shrinken matrix
     */
    public static int[][]eraseEmptySpace(int[][] data)
    {
        // Stores the first row and column with only 0s
        int amountOfRows=data.length;
        int amountOfColumns=data.length;
        // Check all rows
        for(int i=0;i<data[0].length && amountOfRows==data.length;i++)
        {
            // Check if row i is empty
            int columnIsEmpty=0;
            for(int j=0;j<data.length;j++)
            {
                if(data[j][i]==1)
                {
                    columnIsEmpty=1;
                }
            }
            // If empty, store that row number
            if(columnIsEmpty==0)
            {
                amountOfRows=i;
            }
        }
        // Check all columns
        for(int i=0;i<data.length && amountOfColumns==data.length;i++)
        {
            // Check if columns i is empty
            int rowIsEmpty=0;
            for(int j=0;j<data[i].length;j++)
            {
                if(data[i][j]==1)
                {
                    rowIsEmpty=1;
                }
            }
            // If empty, store that column number
            if(rowIsEmpty==0)
            {
                amountOfColumns=i;
            }
        }
        // Make a matrix of the calculated size
        int[][] returnData = new int[amountOfColumns][amountOfRows];
        // Copy the input matrix to the new matrix
        for(int i=0;i<amountOfColumns;i++)
        {
            for(int j=0;j<amountOfRows;j++)
            {
                returnData[i][j]=data[i][j];
            }
        }
        return returnData;
    }

    
    /** 
     * @param args
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        makeDatabase();

        PrintWriter writer = new PrintWriter("resources/pentominos.csv", "UTF-8");

        for(int i = 0; i < database.size(); i++)
        {
            for(int j = 0; j<database.get(i).length; j++)
            {
                writer.print(i + "," + j + "," + database.get(i)[j].length + "," + database.get(i)[j][0].length);

                for(int k = 0; k < database.get(i)[j].length; k++)
                {
                    for(int l = 0; l < database.get(i)[j][k].length; l++)
                    {
                        writer.print("," + database.get(i)[j][k][l]);
                    }
                }
                writer.println();
            }
        }
        writer.close();
    }
}