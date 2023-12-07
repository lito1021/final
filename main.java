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
        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
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
}

// Main
public class Main {
    public static void main(String[] args) {
        Grid model = new Grid(10, 10);
        GridView view = new GridView();
        GridController controller = new GridController(model, view);
        Scanner battleship = new Scanner(System.in); // create a scanner object

        System.out.println("Enter row for battleship:");
        int row = battleship.nextInt(); // Use nextInt() to read an integer input
        int col = battleship.nextInt(); // Read column as well, if needed

        controller.setCell(row, col, 'X');
        controller.updateView();
    }
}
