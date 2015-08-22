import java.util.Scanner;
import java.io.*;

public class Sudoku {

    private int[][] matrix;
    // The puzzle is stored in a 9x9 matrix.
    // Zeros indicate cells that don't yet have a value

    //////////////////////////////////////////////////////////////////////////////////////////
    // Read the puzzle from a text file
    public Sudoku(String fileName) throws IOException {
        Scanner fileReader = new Scanner(new File(fileName));

        // Read the puzzle
        matrix = new int[9][9];
        for (int i = 0; i < 9; i++)                   // 9 rows
            for (int j = 0; j < 9; j++)               // 9 columns per row
                matrix[i][j] = fileReader.nextInt();

        fileReader.close();
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    // Return a string representation of the puzzle.
    // Use spaces rather than zeros.
    public String toString(){
        String str = "\n";
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                if (matrix[i][j] != 0)
                    str += matrix[i][j] + " ";
                else
                    str += "  ";
                if (j == 2 || j == 5)
                    str += "| ";
            }
            str += "\n";
            if (i == 2 || i == 5)
                str += "---------------------\n";
        }
        str += "\n";
        return str;     
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    // Rule 1: 
    //   Fill in all blank entries that admit of a single possibility.
    //   Repeat until there are no further changes
    public void rule1(){

        // Apply this rule until there is a complete pass through the matrix
        // without changing any of the cells.
        boolean done = false;
        while (!done){
            done = true;

            // Traverse the puzzle row by row and column by column
            for (int i = 0; i < 9; i++){
                for (int j = 0; j < 9; j++){ 

                    // If a cell hasn't been resolved (contains zero), then set it to the unique value
                    // that goes in the cell. If there is more than one value possible, don't
                    // change the cell.
                    if (matrix[i][j] == 0){
                        int value = findUniqueValueFor(i, j);
                        if (value != 0){
                            matrix[i][j] = value;
                            done = false;
                        } 
                    }
                }
            }
        }   

    }

    public void rule2() {
        boolean done = false;
        while (!done) {
            done = true;
            for (int i = 0; i < 9; i++) {
                int[] values = new int[9];
                for (int j = 0; j < 9; j++) {
                    if (matrix[i][j] == 0) {
                        for (int v = 1; v <= 9; v++) {
                            if (probeIsOkay(v, i, j)) {
                                values[v-1]++;
                            }
                        }
                    }
                }
                for (int k = 0; k < 9; k++) {
                    if (values[k] == 1) {
                        for (int j = 0; j < 9; j++) {
                            if (matrix[i][j] == 0) {
                                for (int v = 1; v <= 9; v++) {
                                    if (probeIsOkay(v, i, j)) {
                                        if (v == k + 1){ 
                                            matrix[i][j] = k + 1;
                                            done = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }       
            }
        }
    }

    public void rule3() {
        boolean done = false;
        while (!done) {
            done = true;
            for (int i = 0; i < 9; i++) {
                int[] values = new int[9];
                for (int j = 0; j < 9; j++) {
                    if (matrix[j][i] == 0) {
                        for (int v = 1; v <= 9; v++) {
                            if (probeIsOkay(v, j, i)) {
                                values[v-1]++;
                            }
                        }
                    }
                }
                for (int k = 0; k < 9; k++) {
                    if (values[k] == 1) {
                        for (int j = 0; j < 9; j++) {
                            if (matrix[j][i] == 0) {
                                for (int v = 1; v <= 9; v++) {
                                    if (probeIsOkay(v, j, i)) {
                                        if (v == k + 1){ 
                                            matrix[j][i] = k + 1;
                                            done = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }       
            }
        }
    }

    public void rule4() {
        boolean done = false;
        while (!done) {
            done = true;
            for (int i = 0; i < 9; i += 3) {
                for (int j = 0; j < 9; j += 3) {
                    int[] values = new int[9];
                    for (int row = i; row < i + 3; row++) {
                        for (int col = j; col < j + 3; col++) {
                            if (matrix[row][col] == 0) {
                                for (int v = 1; v <= 9; v++) {
                                    if (probeIsOkay(v, row, col)) {
                                        values[v-1]++;
                                    }
                                }
                            }
                        }
                    }
                    for (int k = 0; k < 9; k++) {
                        if (values[k] == 1) {
                            for (int row2 = i; row2 < i + 3; row2++) {
                                for (int col2 = j; col2 < j + 3; col2++) {
                                    if (matrix[row2][col2] == 0) {
                                        for (int v = 1; v <= 9; v++) {
                                            if (probeIsOkay(v, row2, col2)) {
                                                if (v == k + 1){ 
                                                    matrix[row2][col2] = k + 1;
                                                    done = false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }     
                }
            }
        }
    }
    // Return  the unique value that satisfies cell [i][j] or 0 if there is none.
    private  int findUniqueValueFor(int i, int j){

        int value = 0;

        // Try each of the candidate values
        for (int v = 1; v <= 9; v++){

            if (probeIsOkay(v, i, j)){ // If the value doesn't causes a conflict then
                if (value == 0)        //   If this is the first value that works,
                    value = v;         //     use it.
                else                   //   Else there is more than one possible value
                    return 0;          //     so return 0
            }
        }
        return value;                  // Return the unique value
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    // Determine if a particular value can be placed in a particular cell
    private  boolean probeIsOkay(int probe, int i, int j){
        // Pre     -- none
        // Post    -- no change
        // Returns -- true if matrix[i][j] can be set to probe else false

        // Check row i for conflicts
        for (int jj = 0; jj < 9; jj++)
            if (jj != j){
                if (probe == matrix[i][jj])
                    return false;
        }

        // Check column j for conflicts
        for (int ii = 0; ii < 9; ii++)
            if (ii != i){
                if (probe == matrix[ii][j])
                    return false;
        }

        // Check box containing [i][j] for conflicts
        int topLeftRow = i - i%3;
        int topLeftColumn = j - j%3;
        for (int ii = topLeftRow; ii < topLeftRow + 3; ii++)
            for (int jj = topLeftColumn; jj < topLeftColumn + 3; jj++)
                if (ii != i || jj != j){
                    if (probe == matrix[ii][jj])
                        return false;
        }

        // There are no conflicts
        return true;
    }   

    public int countNumberOfZeros() {
        int numOfZeros = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (matrix[i][j] == 0) {
                    numOfZeros++;
                }
            }
        }
        return numOfZeros;
    }
}