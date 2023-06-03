import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class PuzzleSolverGUI extends JFrame {
    private JPanel boardPanel;
    private JButton solveButton;
    private JButton shuffleButton;
    private JTextArea resultTextArea;

    private int[][] initialBoard = {
            {1, 2, 3},
            {5, 0, 7},
            {4, 6, 8}
    };

    private int[][] goalBoard = {
            {1, 2, 3},
            {8, 0, 4},
            {7, 6, 5}
    };

    public PuzzleSolverGUI() {
        setTitle("8-Puzzle Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createBoardPanel();
        createButtonPanel();
        createResultPanel();

        setVisible(true);
    }

    private void createBoardPanel() {
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));

        for (int i = 0; i < initialBoard.length; i++) {
            for (int j = 0; j < initialBoard[i].length; j++) {
                JButton button = new JButton(Integer.toString(initialBoard[i][j]));
                button.setPreferredSize(new Dimension(60, 60));
                button.setFont(new Font("Arial", Font.BOLD, 24));
                button.setEnabled(false);
                boardPanel.add(button);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solvePuzzle();
            }
        });
        buttonPanel.add(solveButton);

        shuffleButton = new JButton("Shuffle");
        shuffleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shuffleBoard();
            }
        });
        buttonPanel.add(shuffleButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createResultPanel() {
        JPanel resultPanel = new JPanel();
        resultTextArea = new JTextArea(10, 30);
        resultTextArea.setEditable(false);
        resultPanel.add(new JScrollPane(resultTextArea));
        add(resultPanel, BorderLayout.NORTH);
    }

    private void solvePuzzle() {
        resultTextArea.setText("");
        double comeco;
        double fim;

        comeco = System.currentTimeMillis();
        int astarVisitedStates = EightPuzzleSolver.aStarSearch(initialBoard, goalBoard);
        fim = System.currentTimeMillis() - comeco;
        resultTextArea.append("Numero de nodes visitados A*: " + astarVisitedStates +"\n");
        resultTextArea.append("Tempo de execucao  A*: " + fim +"ms\n");

        comeco = System.currentTimeMillis();
        int dfsVisitedStates = EightPuzzleSolver.dfsSearch(initialBoard, goalBoard);
        fim = System.currentTimeMillis() - comeco;
        resultTextArea.append("Numero de nodes visitados DFS: " + dfsVisitedStates + "\n");
        resultTextArea.append("Tempo de execucao  DFS: " + fim +"ms\n");

        comeco = System.currentTimeMillis();
        int bfsVisitedStates = EightPuzzleSolver.bfsSearch(initialBoard, goalBoard);
        fim = System.currentTimeMillis() - comeco;
        resultTextArea.append("Numero de nodes visitados BFS: " + bfsVisitedStates + "\n");
        resultTextArea.append("Tempo de execucao  BFS: " + fim +"ms\n");
    }

    private void shuffleBoard() {
        int[][] shuffledBoard = new int[3][3];
        Random random = new Random();

        // Copy the initial board to the shuffled board
        for (int i = 0; i < initialBoard.length; i++) {
            System.arraycopy(initialBoard[i], 0, shuffledBoard[i], 0, initialBoard[i].length);
        }

        // Shuffle the board by performing random moves
        for (int i = 0; i < 1000; i++) {
            int[] emptyCell = findEmptyCell(shuffledBoard);
            int emptyRow = emptyCell[0];
            int emptyCol = emptyCell[1];
            int randomDirection = random.nextInt(4); // 0: Up, 1: Down, 2: Left, 3: Right

            switch (randomDirection) {
                case 0: // Up
                    if (emptyRow > 0) {
                        shuffledBoard[emptyRow][emptyCol] = shuffledBoard[emptyRow - 1][emptyCol];
                        shuffledBoard[emptyRow - 1][emptyCol] = 0;
                    }
                    break;
                case 1: // Down
                    if (emptyRow < 2) {
                        shuffledBoard[emptyRow][emptyCol] = shuffledBoard[emptyRow + 1][emptyCol];
                        shuffledBoard[emptyRow + 1][emptyCol] = 0;
                    }
                    break;
                case 2: // Left
                    if (emptyCol > 0) {
                        shuffledBoard[emptyRow][emptyCol] = shuffledBoard[emptyRow][emptyCol - 1];
                        shuffledBoard[emptyRow][emptyCol - 1] = 0;
                    }
                    break;
                case 3: // Right
                    if (emptyCol < 2) {
                        shuffledBoard[emptyRow][emptyCol] = shuffledBoard[emptyRow][emptyCol + 1];
                        shuffledBoard[emptyRow][emptyCol + 1] = 0;
                    }
                    break;
            }
        }

        // Update the GUI with the shuffled board
        updateBoard(shuffledBoard);
        initialBoard = shuffledBoard;
    }

    private int[] findEmptyCell(int[][] board) {
        int[] emptyCell = new int[2];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    emptyCell[0] = i;
                    emptyCell[1] = j;
                    return emptyCell;
                }
            }
        }

        return emptyCell;
    }

    private void updateBoard(int[][] board) {
        boardPanel.removeAll();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                JButton button = new JButton(Integer.toString(board[i][j]));
                button.setPreferredSize(new Dimension(60, 60));
                button.setFont(new Font("Arial", Font.BOLD, 24));
                button.setEnabled(false);
                boardPanel.add(button);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PuzzleSolverGUI();
            }
        });
    }
}
