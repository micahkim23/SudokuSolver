import java.util.Scanner;
import java.io.*;

public class PlaySudoku {

    public static void main(String[] args) throws IOException{
        while (true){
            Scanner reader = new Scanner(System.in);
            System.out.print("Enter the name of a puzzle " +
                "file or Q to quit: ");
            String fileName = reader.next();
            if (fileName.equalsIgnoreCase("q")) 
                break;
            Sudoku puzzle = new Sudoku(fileName);
            solve(puzzle);
        }             
    }

    private static void solve(Sudoku puzzle){    
        System.out.println("Initial configuration");
        System.out.print(puzzle);          
        int numZeros = puzzle.countNumberOfZeros();
        int oldNumZeros = numZeros + 1;

        // Apply the rules until there is no further improvement
        while(numZeros < oldNumZeros){
            oldNumZeros = numZeros;
            puzzle.rule1();
            System.out.println("Rule 1: results after filling" +
                " in cells that allow one possibility");
            System.out.print(puzzle);
            if (puzzle.countNumberOfZeros() == 0) 
                return;
            puzzle.rule2();
            System.out.println("Rule 2: results after filling" +
                " in rows that allow one possibility");
            System.out.println(puzzle);
            if (puzzle.countNumberOfZeros() == 0) 
                return;
            puzzle.rule3();
            System.out.println("Rule 3: results after filling" +
                " in columns that allow one possibility");
            System.out.println(puzzle);
            if (puzzle.countNumberOfZeros() == 0) 
                return;
            puzzle.rule4();
            System.out.println("Rule 4: results after filling" +
                " in 3X3 that allow one possibility");
            System.out.println(puzzle);
            if (puzzle.countNumberOfZeros() == 0) 
                return;

            // Attempt to solve by applying another rule here
            numZeros = puzzle.countNumberOfZeros();
        }
    }
}           

