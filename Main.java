package minesweeper;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static int[][] neighbors = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},         { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };
    static final int SIZE = 9;
    public static void main(String[] args) {
        System.out.print("""
                How to play:
                1. chose how many mines you want on the field
                2. Choose a cell by specifying its coordinates and whether you believe it's a free space or a mine.
                   example: 3 3 free ----- (height)(length)(free/mine)
                3. what each symbol represents:
                    . as unexplored cells
                    / as explored free cells without mines around it
                    Numbers from 1 to 8 as explored free cells with 1 to 8 mines around them, respectively
                    X as mines
                    * as unexplored marked cells
                    
                Start playing! (:
                    
                """);
        // ---- get how many mines user wants ----
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many mines do you want on the field? ");
        int n = scanner.nextInt();
        // ---- randomly fill array by user input ----
        String[][] arr = new String[SIZE][SIZE];
        String[][] playerField = new String[SIZE][SIZE];
        fillArr(playerField, 0);
        fillArr(arr, n);
        // ---- ask for positions and printing results ----
        while(true){
            // --- printing result + checking if win
            if (printArr(arr, playerField)){
                System.out.println("Congratulations! You found all the mines!");
                return;
            }
            // getting user input
            System.out.print("Set/unset mines marks or claim a cell as free: ");
            int col = scanner.nextInt() - 1;
            int row = scanner.nextInt() - 1;
            String input = scanner.next();
            // checking user input
            while (col > 8 || row > 8){
                System.out.println("Please chose coordinates from 1 to 9");
                col = scanner.nextInt() - 1;
                row = scanner.nextInt() - 1;
            }
            while (!input.equals("free") &&  !input.equals("mine")){
                System.out.println("Please chose free or mine");
                input = scanner.next();
            }
            // ---- setting open space / mine position
            if (input.equals("free")){
                // ---- checking if player lost ----
                if (arr[row][col].equals("X")){
                    printArr(arr, playerField);
                    System.out.println("You stepped on a mine and failed!");
                    return;
                }
                openingFreeSpots(arr, playerField, row, col);
            } else{
                // setting / unsetting mines
                if (playerField[row][col].equals("*") ) {
                    playerField[row][col] = ".";
                } else{
                    playerField[row][col] = "*";
                }
            }
        }
    }

    // ---- recursion for opening neighbour spots without "X" ----
    public static void openingFreeSpots(String[][] arr, String[][] playerField, int row, int col){
        if (arr[row][col].matches("-?\\d+")){
            playerField[row][col] = arr[row][col];
        } else if (arr[row][col].equals(".") || arr[row][col].equals("*")) {
            playerField[row][col] = "/";
            arr[row][col] = "/";

            for (int[] neighbor : neighbors) {
                int newRow = row + neighbor[0];
                int newCol = col + neighbor[1];
                if (isWithinBounds(arr, newRow, newCol)) {
                    openingFreeSpots(arr, playerField, newRow, newCol);
                }
            }
        }
    }
    
    // ---- randomly fill array like user wants
    public static void fillArr(String[][] arr, int n){
        // fill arr with dots
        for (String[] strings : arr) {
            Arrays.fill(strings, ".");
        }
        if (n < 1){
            return;
        }
        // making arr like user wants
        Random random = new Random();
        while(n > 0){
            int x = random.nextInt(SIZE);
            int y = random.nextInt(SIZE);
            if (!arr[x][y].equals("X")){
                arr[x][y] = "X";
                n--;
            }
        }
        // ---- setting next to mine numbers ----
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                int temp = 0;
                for (int[] neighbor : neighbors) {
                    int newRow = i + neighbor[0];
                    int newCol = j + neighbor[1];
                    if (isWithinBounds(arr, newRow, newCol) && arr[newRow][newCol].equals("X")) {
                        temp++;
                    }
                }
                if (temp > 0 && !arr[i][j].equals("X")){
                    arr[i][j] = String.valueOf(temp);
                }
            }
        }
    }

    public static boolean isWithinBounds(String[][] arr, int i, int j) {
        return i >= 0 && i < arr.length && j >= 0 && j < arr[0].length;
    }

    // ---- printing and checking if player won ----
    public static boolean printArr(String[][] arr, String[][] playerField){
        boolean win = true;
        System.out.println("\n |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < arr.length; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j].equals("X") && playerField[i][j].equals(".")){
                    win = false;
                }
                System.out.print(playerField[i][j]);
            }

            System.out.println("|");
        }
        System.out.println("-|---------|");
        return win;
    }
}