import java.util.Scanner;

class PlayerBoard {
    private String[][] ownBoard;
    private String[][] trackingBoard;

    public PlayerBoard() {
        ownBoard = new String[10][10];
        trackingBoard = new String[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                ownBoard[i][j] = "🔵";  // Blue square emoji for empty space
                trackingBoard[i][j] = "🔵";  // Blue square emoji for empty space
            }
        }
    }

    public void setOwnBoardCell(int row, int col, String value) {
        ownBoard[row][col] = value;
    }

    public void setTrackingBoardCell(int row, int col, String value) {
        trackingBoard[row][col] = value;
    }

    public String[][] getOwnBoard() {
        return ownBoard;
    }

    public String[][] getTrackingBoard() {
        return trackingBoard;
    }
}

class BoardView {
    public void displayGrid(String[][] grid) {
        System.out.print("  ");
        for (int i = 1; i <= grid[0].length; i++) {
            System.out.print(i + "  ");
        }
        System.out.println();
        char rowLabel = 'A';
        for (String[] row : grid) {
            System.out.print(rowLabel +  " ");
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
            rowLabel++;
        }
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

    public static void placeShip(PlayerBoard playerBoard, String shipType, char startRow, int startCol, char direction) {
        int shipLength = shipType(shipType);
        int rowIndex = startRow - 'A';
        int colIndex = startCol - 1;

        if (colIndex < 0 || colIndex + (direction == 'H' ? shipLength : 1) > 10) {
            throw new IllegalArgumentException("Invalid column placement");
        }

        if (rowIndex < 0 || rowIndex + (direction == 'V' ? shipLength : 1) > 10) {
            throw new IllegalArgumentException("Invalid row placement");
        }

        if (direction == 'H') {
            for (int i = 0; i < shipLength; i++) {
                playerBoard.setOwnBoardCell(rowIndex, colIndex + i, "🚢");  // Ship emoji for ship
            }
        } else if (direction == 'V') {
            for (int i = 0; i < shipLength; i++) {
                playerBoard.setOwnBoardCell(rowIndex + i, colIndex, "🚢");  // Ship emoji for ship
            }
        } else {
            throw new IllegalArgumentException("Invalid direction");
        }
    }

    public static boolean isGameOver(PlayerBoard playerBoard) {
        String[][] ownBoard = playerBoard.getOwnBoard();
        for (String[] row : ownBoard) {
            for (String cell : row) {
                if (cell.equals("🚢")) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        PlayerBoard player1 = new PlayerBoard();
        PlayerBoard player2 = new PlayerBoard();

        BoardView view = new BoardView();

        try (Scanner battleship = new Scanner(System.in)) {
            for (int player = 1; player <= 2; player++) {
                for (int i = 0; i < 5; i++) {
                    view.displayGrid(player == 1 ? player1.getOwnBoard() : player2.getOwnBoard());
                    System.out.println("Please turn on caps lock. Player " + player + " you will first place your ships. First enter the type of ship. You can only use a ship one time. Choose between: Carrier, Battleship, Cruiser, Submarine, and Destroyer.");
                    String shipType = battleship.next();  // Convert to uppercase once
                    while (!shipType.equals("CARRIER") && !shipType.equals("BATTLESHIP") && !shipType.equals("CRUISER") && !shipType.equals("SUBMARINE") && !shipType.equals("DESTROYER")) {
                        System.out.println("Invalid ship type. Please try again.");
                        shipType = battleship.next().toUpperCase();  // Convert to uppercase in the loop as well
                    }
                    System.out.println("Enter starting row (A-J):");
                    char row;
                    while (true) {
                        String inputRow = battleship.next().toUpperCase();
                        if (inputRow.length() == 1 && inputRow.charAt(0) >= 'A' && inputRow.charAt(0) <= 'J') {
                            row = inputRow.charAt(0);
                            break;
                        } else {
                            System.out.println("Invalid row. Please enter a valid row (A-J):");
                        }
                    }

                    System.out.println("Enter starting column (1-10):");
                    int col;
                    while (true) {
                        if (battleship.hasNextInt()) {
                            col = battleship.nextInt();
                            if (col >= 1 && col <= 10) {
                                break;
                            } else {
                                System.out.println("Invalid column. Please enter a valid column (1-10):");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a valid column (1-10):");
                            battleship.next(); // Consume invalid input
                        }
                    }

                    System.out.println("Enter direction (H for horizontal, V for vertical):");
                    char direction = battleship.next().toUpperCase().charAt(0);

                    try {
                        if (player == 1) {
                            placeShip(player1, shipType, row, col, direction);
                        } else {
                            placeShip(player2, shipType, row, col, direction);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                        i--; // Decrement i to re-prompt for the current ship
                    }
                }
            }

            int currentPlayer = 1;
            PlayerBoard currentPlayerBoard = player1;
            PlayerBoard opponentPlayerBoard = player2;

            while (!isGameOver(opponentPlayerBoard)) {
                System.out.println("Player " + currentPlayer + ", it's now your turn!");

                view.displayGrid(currentPlayerBoard.getTrackingBoard());

                boolean hitAgain = false;

                do {
                    System.out.println("Enter target row (A-J):");
                    char targetRow = battleship.next().toUpperCase().charAt(0);
                    System.out.println("Enter target column (1-10):");
                    int targetCol = battleship.nextInt();

                    String targetCell = opponentPlayerBoard.getOwnBoard()[targetRow - 'A'][targetCol - 1];
                    if (targetCell.equals("🚢")) {
                        System.out.println("It's a HIT!");
                        currentPlayerBoard.setTrackingBoardCell(targetRow - 'A', targetCol - 1, "❌");  // Red X emoji for hit
                        hitAgain = true;
                    } else if (targetCell.equals("🔵")) {
                        System.out.println("It's a MISS!");
                        currentPlayerBoard.setTrackingBoardCell(targetRow - 'A', targetCol - 1, "⚪");  // White circle emoji for miss
                        hitAgain = false;
                    } else {
                        System.out.println("Invalid move. You've already targeted the position.");
                        hitAgain = false;
                    }
                } while (hitAgain);

                currentPlayer = (currentPlayer == 1) ? 2 : 1;

                if (currentPlayer == 1) {
                    currentPlayerBoard = player1;
                    opponentPlayerBoard = player2;
                } else {
                    currentPlayerBoard = player2;
                    opponentPlayerBoard = player1;
                }
            }

            System.out.println("Game over! Player " + currentPlayer + " wins!");
        }
    }
}
