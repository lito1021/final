import java.util.Scanner;
import java.util.Random;

// Model
class Grid {
    private char[][] grid;

    public Grid(int rows, int cols) {
        grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = '-';
            }
        }
    }

    public void setCell(int row, int col, char value) {
        grid[row][col] = value;
    }

    public char[][] getGrid() {
        return grid;
    }
}

// View
class GridView {
    public void displayGrid(char[][] grid, boolean hideShips) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Print column indices (A, B, C, ...)
        System.out.print("  ");
        for (int i = 0; i < cols; i++) {
            char colLabel = (char) ('A' + i);
            System.out.print(" " + colLabel);
        }
        System.out.println();

        // Print grid with row indices and cell values
        for (int i = 0; i < rows; i++) {
            String rowLabel = (i + 1) == 10 ? "10" : " " + (i + 1);
            System.out.print(rowLabel + " ");

            for (int j = 0; j < cols; j++) {
                char cellValue = grid[i][j];
                if (hideShips && (cellValue == 'S' || cellValue == 'C' || cellValue == 'B' || cellValue == 'R'
                        || cellValue == 'U' || cellValue == 'D')) {
                    System.out.print("- ");
                } else {
                    System.out.print(cellValue + " ");
                }
            }
            System.out.println();
        }
    }
}

// Controller
class GridController {
    Grid playerGrid;
    Grid botGrid;
    private GridView view;

    public GridController(Grid playerGrid, Grid botGrid, GridView view) {
        this.playerGrid = playerGrid;
        this.botGrid = botGrid;
        this.view = view;
    }

    public void updatePlayerView() {
        view.displayGrid(playerGrid.getGrid(), false);
    }

    public void updateBotView() {
        view.displayGrid(botGrid.getGrid(), true);
    }

    public void setPlayerCell(int row, int col, char value) {
        playerGrid.setCell(row, col, value);
    }

    public void setBotCell(int row, int col, char value) {
        botGrid.setCell(row, col, value);
    }

    //Add the setCell method
    public void setCell(int row, int col, char value, boolean isPlayer) {
        if (isPlayer) {
            setPlayerCell(row, col, value);
        } else {
            setBotCell(row, col, value);
        }
    }
}

public class main {
    public static void main(String[] args) {
        Grid playerGrid = new Grid(10, 10);
        Grid botGrid = new Grid(10, 10);
        GridView view = new GridView();
        GridController controller = new GridController(playerGrid, botGrid, view);
        Scanner battleship = new Scanner(System.in);  // Create a Scanner object

        System.out.println("Welcome to Battleship! You will be playing against a bot.");

        // Player setup
        System.out.println("\nHere is your board:");
        controller.updatePlayerView();
        placeShips(controller, battleship, true);
        

        // Bot setup
        System.out.println("\nBot is placing ships...");
        placeShips(controller, battleship, false);

        // Game loop (Player's turn)
        boolean gameOver = false;

        while (!gameOver) {
            System.out.println("\nYour move:");
            controller.updateBotView(); // Display the bot's hidden grid
            // Implement your game logic for player's move here
            // Example: Ask the player for a move (row and column) and update the bot's grid accordingly

            // For simplicity, let's end the game after one move
            gameOver = true;
        }

        battleship.close();
    }

    private static void placeShips(GridController controller, Scanner scanner, boolean isPlayer) {
        String[] shipNames = {"Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"};
        int[] shipSizes = {5, 4, 3, 3, 2};

        for (int i = 0; i < 5; i++) {
            System.out.println("\nPlacing " + shipNames[i] + " (" + shipSizes[i] + " spaces)");

            for (int j = 0; j < shipSizes[i]; j++) {
                int row, col;

                do {
                    System.out.println("Enter the starting position for " + shipNames[i] + " (e.g., A1): ");
                    String input = scanner.nextLine().toUpperCase();

                    if (input.length() < 2 || input.length() > 3) {
                        System.out.println("Invalid input. Try again.");
                        continue;
                    }

                    char column = input.charAt(0);
                    col = column - 'A';

                    if (col < 0 || col >= 10) {
                        System.out.println("Invalid column. Try again.");
                        continue;
                    }

                    try {
                        row = Integer.parseInt(input.substring(1));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid row. Try again.");
                        continue;
                    }

                    if (row < 1 || row > 10) {
                        System.out.println("Invalid row. Try again.");
                        continue;
                    }

                    if (isPlayer) {
                        if (!isShipPlacementValid(controller, row - 1, col, shipSizes[i], true)) {
                            System.out.println("Invalid placement. Try again.");
                            continue;
                        }
                    } else {
                        if (!isShipPlacementValid(controller, row - 1, col, shipSizes[i], false)) {
                            continue; // Try again with a new random position for the bot
                        }
                    }

                    break; // Valid input, break the loop
                } while (true);

                char shipSymbol = isPlayer ? 'S' : 'B'; // 'S' for player, 'B' for bot
                controller.setCell(row - 1, col, shipSymbol, isPlayer);
            }
            controller.updatePlayerView(); // Update player's view after placing each ship
        }
    }

    private static boolean isShipPlacementValid(GridController controller, int row, int col, int shipSize, boolean isPlayer) {
        char[][] grid = isPlayer ? controller.playerGrid.getGrid() : controller.botGrid.getGrid();

        // Check if the ship fits in the grid
        if (col + shipSize > 10 && !isPlayer) {
            return false; // Bot's random placement may exceed the grid, try again
        }

        // Check if the cells are empty
        for (int i = 0; i < shipSize; i++) {
            if (grid[row][col + i] != '-') {
                return false; // There is already a ship in the chosen position
            }
        }

        return true;
    }
}
