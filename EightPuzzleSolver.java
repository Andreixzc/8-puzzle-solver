import java.util.*;

public class EightPuzzleSolver {
    public static void main(String[] args) {
        int[][] initialBoard = {{1, 2, 3}, {5, 0, 7}, {4, 6, 8}};
        int[][] goalBoard = {{1, 2, 3}, {8, 0, 4}, {7, 6, 5}};

        int astarVisitedStates = aStarSearch(initialBoard, goalBoard);
        int dfsVisitedStates = dfsSearch(initialBoard, goalBoard);
        int bfsVisitedStates = bfsSearch(initialBoard, goalBoard);

        System.out.println("A* Search - Visited States: " + astarVisitedStates);
        System.out.println("DFS Search - Visited States: " + dfsVisitedStates);
        System.out.println("BFS Search - Visited States: " + bfsVisitedStates);
    }

    public static int calculateHeuristic(int[][] board, int[][] goalBoard) {
        int distance = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int value = board[i][j];
                if (value != 0) {
                    int goalRow = (value - 1) / board.length;
                    int goalCol = (value - 1) % board.length;
                    distance += Math.abs(i - goalRow) + Math.abs(j - goalCol);
                }
            }
        }
        return distance;
    }

    public static int calculateHeuristic2(int[][] board, int[][] goalBoard) {
        int misplacedTiles = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != goalBoard[i][j]) {
                    misplacedTiles++;
                }
            }
        }
        return misplacedTiles;
    }

    public static int aStarSearch(int[][] initialBoard, int[][] goalBoard) {
        int visitedStates = 0;
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.fScore));
        Set<Node> closedList = new HashSet<>();

        Node initialNode =
                new Node(initialBoard, null, 0, calculateHeuristic(initialBoard, goalBoard));
        openList.add(initialNode);

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            closedList.add(current);
            visitedStates++;

            if (Arrays.deepEquals(current.board, goalBoard)) {
                printSolutionPath(current);
                return visitedStates;
            }

            List<Node> neighbors = generateNeighbors(current);
            for (Node neighbor : neighbors) {
                if (closedList.contains(neighbor)) {
                    continue;
                }

                int gScore = current.gScore + 1;
                boolean isNewPath = false;

                if (!openList.contains(neighbor)) {
                    openList.add(neighbor);
                    isNewPath = true;
                } else if (gScore < neighbor.gScore) {
                    isNewPath = true;
                }

                if (isNewPath) {
                    neighbor.previous = current;
                    neighbor.gScore = gScore;
                    neighbor.fScore = gScore + calculateHeuristic(neighbor.board, goalBoard);
                }
            }
        }

        System.out.println("No solution found!");
        return visitedStates;
    }
    public static int aStarSearch2(int[][] initialBoard, int[][] goalBoard) {
        int visitedStates = 0;
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.fScore));
        Set<Node> closedList = new HashSet<>();

        Node initialNode =
                new Node(initialBoard, null, 0, calculateHeuristic2(initialBoard, goalBoard));
        openList.add(initialNode);

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            closedList.add(current);
            visitedStates++;

            if (Arrays.deepEquals(current.board, goalBoard)) {
                printSolutionPath(current);
                return visitedStates;
            }

            List<Node> neighbors = generateNeighbors(current);
            for (Node neighbor : neighbors) {
                if (closedList.contains(neighbor)) {
                    continue;
                }

                int gScore = current.gScore + 1;
                boolean isNewPath = false;

                if (!openList.contains(neighbor)) {
                    openList.add(neighbor);
                    isNewPath = true;
                } else if (gScore < neighbor.gScore) {
                    isNewPath = true;
                }

                if (isNewPath) {
                    neighbor.previous = current;
                    neighbor.gScore = gScore;
                    neighbor.fScore = gScore + calculateHeuristic(neighbor.board, goalBoard);
                }
            }
        }

        System.out.println("No solution found!");
        return visitedStates;
    }

    public static int dfsSearch(int[][] initialBoard, int[][] goalBoard) {
        int visitedStates = 0;
        Stack<Node> stack = new Stack<>();
        Set<Node> visited = new HashSet<>();

        Node initialNode = new Node(initialBoard, null, 0, 0);
        stack.push(initialNode);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            visited.add(current);
            visitedStates++;

            if (Arrays.deepEquals(current.board, goalBoard)) {
                printSolutionPath(current);
                return visitedStates;
            }

            List<Node> neighbors = generateNeighbors(current);
            for (Node neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }

        System.out.println("No solution found!");
        return visitedStates;
    }

    public static int bfsSearch(int[][] initialBoard, int[][] goalBoard) {
        int visitedStates = 0;
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();

        Node initialNode = new Node(initialBoard, null, 0, 0);
        queue.offer(initialNode);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visited.add(current);
            visitedStates++;

            if (Arrays.deepEquals(current.board, goalBoard)) {
                printSolutionPath(current);
                return visitedStates;
            }

            List<Node> neighbors = generateNeighbors(current);
            for (Node neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    queue.offer(neighbor);
                }
            }
        }

        System.out.println("No solution found!");
        return visitedStates;
    }

    public static List<Node> generateNeighbors(Node node) {
        int[][] board = node.board;
        int zeroRow = -1;
        int zeroCol = -1;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    break;
                }
            }
        }

        List<Node> neighbors = new ArrayList<>();
        int[][] moves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] move : moves) {
            int newRow = zeroRow + move[0];
            int newCol = zeroCol + move[1];

            if (isValidPosition(newRow, newCol, board.length, board[0].length)) {
                int[][] newBoard = cloneBoard(board);
                swapTiles(newBoard, zeroRow, zeroCol, newRow, newCol);

                Node neighbor = new Node(newBoard, node, node.gScore + 1, 0);
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    public static boolean isValidPosition(int row, int col, int numRows, int numCols) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }

    public static int[][] cloneBoard(int[][] board) {
        int numRows = board.length;
        int numCols = board[0].length;
        int[][] clone = new int[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                clone[i][j] = board[i][j];
            }
        }

        return clone;
    }

    public static void swapTiles(int[][] board, int row1, int col1, int row2, int col2) {
        int temp = board[row1][col1];
        board[row1][col1] = board[row2][col2];
        board[row2][col2] = temp;
    }

    public static void printSolutionPath(Node node) {
        List<Node> path = new ArrayList<>();
        Node current = node;

        while (current != null) {
            path.add(current);
            current = current.previous;
        }

        for (int i = path.size() - 1; i >= 0; i--) {
            Node pathNode = path.get(i);
            System.out.println("Move: ");
            printBoard(pathNode.board);
        }
    }

    public static void printBoard(int[][] board) {
        for (int[] row : board) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static class Node {
        int[][] board;
        Node previous;
        int gScore;
        int fScore;

        public Node(int[][] board, Node previous, int gScore, int hScore) {
            this.board = board;
            this.previous = previous;
            this.gScore = gScore;
            this.fScore = gScore + hScore;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Node other = (Node) obj;
            return Arrays.deepEquals(this.board, other.board);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(this.board);
        }
    }
}
