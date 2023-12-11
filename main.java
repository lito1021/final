import java.util.Scanner;

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
    public void displayGrid(char[][] grid) {
        System.out.print("  ");
        for (int i = 1; i <= grid[0].length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        char rowLabel = 'A';
        for (char[] row : grid) {
            System.out.print(rowLabel + " ");
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
            rowLabel++;
        }
    }
}

// Controller
class GridController {
    private Grid model;
    private GridView view;

    public GridController(Grid model, GridView view) {
        this.model = model;
        this.view = view;
    }

    public void updateView() {
        view.displayGrid(model.getGrid());
    }

    public void setCell(int row, int col, char value) {
        model.setCell(row, col, value);
    }

    public char[][] getGrid() {
        return model.getGrid();
    }
}

public class main {
    public static int shipType(String shipType) {
        switch (shipType.toLowerCase()) {
            case "carrier":
                return 5;
            case "battleship":
                return 4;
            case "cruiser":
                return 3;
            case "submarine":
                return 3;
            case "destroyer":
                return 2;
            default:
                throw new IllegalArgumentException("Invalid ship type");
        }
    }

    public static void placeShip(GridController controller, String shipType, char startRow, int startCol, char direction, int player) {
        int shipLength = shipType(shipType);
        int rowIndex = startRow - 'A';
        int colIndex = startCol - 1;

        if (direction == 'H') {
            for (int i = 0; i < shipLength; i++) {
                controller.setCell(rowIndex, colIndex + i, 'X');
            }
        } else if (direction == 'V') {
            for (int i = 0; i < shipLength; i++) {
                controller.setCell(rowIndex + i, colIndex, 'X');
            }
        } else {
            throw new IllegalArgumentException("Invalid direction");
        }
    }

    public static boolean isGameOver(GridController controller) {
        char[][] grid = controller.getGrid();
        for (char[] row : grid) {
            for (char cell : row) {
                if (cell == 'X') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Grid player1Grid = new Grid(10, 10);
        Grid player2Grid = new Grid(10, 10);

        GridView view = new GridView();

        GridController player1Controller = new GridController(player1Grid, view);
        GridController player2Controller = new GridController(player2Grid, view);

        try (Scanner battleship = new Scanner(System.in)) {
            for (int player = 1; player <= 2; player++) {
                for (int i = 0; i < 5; i++) {
                    if (player == 1) {
                        player1Controller.updateView();
                    } else {
                        player2Controller.updateView();
                    }
                    System.out.println("Player " + player + ", place your ship in this order. Carrier, Battleship, Cruiser, Submarine, and Destroyer. First input the ship name, then it will prompt you for the row, column, and direction.");
                    String shipType = battleship.next();
                    System.out.println("Enter starting row (A-J):");
                    char row = battleship.next().toUpperCase().charAt(0);
                    System.out.println("Enter starting column (1-10):");
                    int col = battleship.nextInt();
                    System.out.println("Enter direction (H for horizontal, V for vertical):");
                    char direction = battleship.next().toUpperCase().charAt(0);

                    try {
                        if (player == 1) {
                            placeShip(player1Controller, shipType, row, col, direction, player);
                        } else {
                            placeShip(player2Controller, shipType, row, col, direction, player);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                        i--; // Decrement i to re-prompt for the current ship
                    }
                }
            }

            // Game loop
            int currentPlayer = 1;
            GridController currentController = player1Controller;
            GridController opponentController = player2Controller;

            while (!isGameOver(opponentController)) {
                System.out.println("Player " + currentPlayer + ", it's now your turn!");
                opponentController.updateView();
                System.out.println("Enter target row(A-J):");
                char targetRow = battleship.next().toUpperCase().charAt(0);
                System.out.println("Enter target column (1-10): ");
                int targetCol = battleship.nextInt();

                // Check if the move is a hit or miss
                char targetCell = opponentController.getGrid()[targetRow - 'A'][targetCol - 1];
                if (targetCell == 'X') {
                    System.out.println("It's a HIT!");
                } else if (targetCell == '-') {
                    System.out.println("It's a MISS!");
                } else {
                    System.out.println("Invalid move. You've already targeted the position.");
                    continue;
                }

                // Update opponent's grid based on the move
                opponentController.setCell(targetRow - 'A', targetCol - 1, 'O');

                // Switch players for the next turn
                currentPlayer = (currentPlayer == 1) ? 2 : 1;
                currentController = (currentController == player1Controller) ? player2Controller : player1Controller;
                opponentController = (opponentController == player1Controller) ? player2Controller : player1Controller;
            }

            // Game over, display the final results
            System.out.println("Player " + currentPlayer + " wins! All opponent's ships are destroyed.");
            opponentController.updateView();
        }
    }
}

