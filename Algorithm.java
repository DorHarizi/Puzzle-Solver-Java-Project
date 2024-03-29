import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * The Algorithm class encapsulates different search algorithms to solve NxM puzzle games.
 * It supports Depth-First Iterative Deepening (DFID), A*, Iterative Deepening A* (IDA*),
 * and Depth-First Branch-and-Bound (DFBnB) algorithms. The class manages the execution
 * of these algorithms, including initialization, goal state checking, and result reporting.
 */
public class Algorithm {
    String algorithm; // The name of the algorithm to be used
    boolean timeFlag; // Flag to indicate if timing information should be reported
    boolean openFlag; // Flag to indicate if open list details should be printed
    int[][] goal; // The goal state configuration of the puzzle
    String[] operator; // The set of possible moves (LEFT, UP, RIGHT, DOWN)

    /**
     * Constructs an Algorithm instance configured with specific parameters for solving NxM puzzle games.
     *
     * @param algorithm  The name of the algorithm to be used for solving the puzzle. Supported algorithms
     *                   include "DFID", "A*", "IDA*", and "DFBnB".
     * @param timeFlag   Indicates whether the algorithm should record and report the time taken to find a solution.
     * @param openFlag   Indicates whether details of the open list should be printed during the algorithm's execution,
     *                   useful for debugging and analysis.
     * @param numOfRow   The number of rows in the puzzle's goal state configuration.
     * @param numOfCol   The number of columns in the puzzle's goal state configuration.
     */
    public Algorithm(String algorithm, boolean timeFlag, boolean openFlag, int numOfRow, int numOfCol) {
        this.algorithm = algorithm;
        this.timeFlag = timeFlag;
        this.openFlag = openFlag;
        // Initialization of operators array with possible moves
        this.operator = new String[]{"LEFT", "UP", "RIGHT", "DOWN"};
        // Construction of the goal state configuration based on the puzzle's dimensions
        this.goal = new int[numOfRow][numOfCol];
        int count = 1;
        for (int i = 0; i < numOfRow; i++) {
            for (int j = 0; j < numOfCol; j++) {
                this.goal[i][j] = count++;
            }
        }
        this.goal[numOfRow - 1][numOfCol - 1] = 0; // Setting the last position to 0 to represent the empty space
    }

    /**
     * Retrieves the goal state configuration for the puzzle.
     *
     * @return A 2D integer array representing the goal state of the puzzle.
     */
    public int[][] getGoal() {
        return goal;
    }

    /**
     * Writes the final report, which includes the solution path,the total cost of the path solution,
     * the number of nodes that created and the elapsed time to solve the puzzle to an output file.
     *
     * @param finalReport    The final report to be written to the file, include tthe solution path,the total cost
     *                       of the path solution, the number of nodes that created.
     * @param elapsedTimeSec The time taken to achieve the result, measured in seconds. This information
     *                       is appended to the report if `timeFlag` is true, offering insights into
     *                       the performance efficiency of the selected algorithm.
     */
    public void writeText(String finalReport, double elapsedTimeSec) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            // Directly writing the final report to the file
            writer.write(finalReport + "\n");
            // Conditionally appending the elapsed time based on the timeFlag
            if (this.timeFlag) {
                writer.write(elapsedTimeSec + " seconds\n");
            }
        } catch (IOException e) {
            // Log any IOExceptions encountered during file writing
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }


    /**
     * Checks if a given node's state matches the goal state of the puzzle.
     *
     * @param node The node representing the current state of the puzzle to be checked against
     *             the goal state.
     * @param goal A 2D integer array representing the goal state configuration of the puzzle.
     * @return true if the node's state matches the goal state; false otherwise.
     */
    public boolean isGoal(Node node, int[][] goal) {
        // Iterate through each cell in the goal configuration
        for (int i = 0; i < goal.length; i++) {
            for (int j = 0; j < goal[0].length; j++) {
                // Compare the current state's value with the goal state's value
                if (goal[i][j] != node.board[i][j].getValue()) {
                    // If any value does not match, the current state is not the goal
                    return false;
                }
            }
        }
        // If all values match, the current state is the goal
        return true;
    }

    /**
     * Performs a limited depth-first search (DFS) on the puzzle's state space, up to a specified depth limit.
     * This method is designed to support the Depth-First Iterative Deepening (DFID) algorithm by exploring
     * the state space up to a given depth and using loop avoidance to prevent revisiting states. It checks
     * for the goal state, manages the depth limit, and tracks the number of nodes visited. If the goal state
     * is reached within the depth limit, the method returns the solution path, number of nodes visited, and
     * the cost to reach the goal. If the goal is not reached within the depth limit, it returns a cutoff
     * indication or indicates that no path exists if all possibilities are exhausted.
     *
     * @param n       The current node being explored in the search space.
     * @param goal    The goal state configuration of the puzzle.
     * @param limited The current depth limit for the DFS exploration. This value decreases with each recursive call.
     * @param visited A HashMap tracking the nodes that have been visited during the current path exploration to avoid loops.
     *                The key is the unique identifier for each node's state, and the value is the node itself.
     * @return A string indicating the outcome of the search. If the goal is found, it returns the solution path and
     * associated metrics (number of nodes visited and the cost). If the search reaches the depth limit without
     * finding the goal, it returns "cutoff". If no path exists within the depth limit, it returns "no path".
     */
    public String Limited_DFS(Node n, int[][] goal, int limited, HashMap<String, Node> visited) {
        // Check if the current node is the goal state
        if (isGoal(n, goal)) {
            // If so, construct and return the solution details
            String finalPath = n.path.substring(0, n.path.length() - 1);
            return finalPath + "\n" + "Num: "+ Node.totalNodes + "\n" + "Cost: " + n.g;
        }else if(limited == 0) {
            // If the depth limit is reached, return "cutoff"
            return "cutoff";
        }else{
            // Mark the current node as visited
            visited.put(n.getKey(), n);
            boolean isCutOff = false; // Flag to track if any branch was cut off due to depth limit
            if(this.openFlag){
                for(Map.Entry<String, Node> item: visited.entrySet()){
                    System.out.println(item);
                }
            }
            // Explore child nodes for each possible move
            for (int i = 0; i < this.operator.length; i++) {
                // Attempt to move in the current direction
                if (n.Move(this.operator[i])) {
                    // If the move is successful and the child node is not already visited
                    Node child = n.children[i];
                    if (visited.containsKey(child.getKey())) {
                        continue;
                    }
                    // Recursively call Limited_DFS with the child node and decreased depth limit
                    String result = Limited_DFS(child, goal, limited - 1, visited);
                    if (result.equals("cutoff")) {
                        // If a cutoff occurred, set the flag
                        isCutOff = true;
                    } else if (!result.equals("no path")) {
                        // If a solution is found, return the result
                        return result;
                    }
                }
            }
            // Return "cutoff" if any child was cut off, otherwise "no path"
            visited.remove((n.getKey()));
            if (isCutOff) {
                return "cutoff";
            } else {
                return "no path";
            }
        }
    }

    /**
     * Implements the Depth-First Iterative Deepening (DFID) algorithm to find a solution path
     * for a puzzle game. DFID combines the space-efficiency of Depth-First Search (DFS) with the
     * optimality and completeness of Breadth-First Search (BFS) by iteratively deepening the limit
     * for depth-first search. The algorithm starts with a depth limit of 1 and incrementally increases
     * it until the goal state is found _or it determines that no solution exists.
     *
     * @param startGame The initial state of the puzzle represented as a Node. This node contains the
     *                  current configuration of the puzzle and serves as the root of the search tree.
     * @param goal      The goal state configuration of the puzzle. The goal
     * @return A string that either describes the solution path including the sequence of moves to reach
     * the goal state, the number of nodes generated, and the cost of the solution.
     * The method returns "no path" if the goal state is not achievable.
     */
    public String dfid(Node startGame, int[][] goal) {
        String result;
        // Iteratively increase the depth limit starting from 1
        for (int depth = 1; depth < Integer.MAX_VALUE; depth++) {
            HashMap<String, Node> visited = new HashMap<>(); // HashMap to track visited nodes for loop avoidance
            result = Limited_DFS(startGame, goal, depth, visited);
            if (!result.equals("cutoff")) {
                if(result.equals("no path"))
                    return "no path" + "\n" + "Num: "+ Node.totalNodes +"\nCost:";
                return result;

            }
        }
        return "no path" + "\n" + "Num: "+ Node.totalNodes +"\nCost:";
    }

    /**
     * Calculates the heuristic value for a given node based on the Manhattan Distance and Linear Conflict.
     * The heuristic is used to estimate the cost from the current node to the goal state in a puzzle game,
     * helping to guide search algorithms.
     * Manhattan Distance is the sum of the absolute differences between the current positions of blocks
     * and their goal positions. Linear Conflict adds a penalty for two tiles that are in their goal row or
     * column but are reversed, requiring additional moves to resolve.
     *
     * @param node The current node for which the heuristic value is to be calculated.
     * @param goal A 2D integer array representing the goal state configuration of the puzzle.
     *
     * @return The heuristic value calculated as the sum of Manhattan Distance and Linear Conflict, representing
     * an estimate of the cost required to move from the current state to the goal state.
     */
    public static double calculateHeuristic(Node node, int[][] goal) {
        double manhattanDistance = 0; // Initialize Manhattan Distance
        double linearConflict = 0; // Initialize Linear Conflict

        // Iterate through each block in the node's board
        for (int row = 0; row < node.board.length; row++) {
            for (int col = 0; col < node.board[0].length; col++) {
                Block currentBlock = node.board[row][col];
                // Skip calculation for the empty block
                if (currentBlock.getValue() != 0) {
                    // Determine the goal position for the current block
                    int goalRow = (currentBlock.getValue() - 1) / goal[0].length;
                    int goalCol = (currentBlock.getValue() - 1) % goal[0].length;

                    // Calculate Manhattan Distance for the block
                    manhattanDistance += Math.abs(goalRow - row) + Math.abs(goalCol - col);

                    // Calculate Linear Conflict for the block in its row and column
                    linearConflict += calculateRowConflict(node, row, goalRow, col);
                    linearConflict += calculateColumnConflict(node, col, goalCol, row);
                }
            }
        }
        // The heuristic value is the sum of Manhattan Distance and Linear Conflict
        return manhattanDistance + linearConflict;
    }

    /**
     * Calculates the number of linear conflicts along a specific row in the puzzle board.
     * A linear conflict occurs when two tiles are in their goal row but are out of order,
     * requiring additional moves to place them in the correct positions. Each linear conflict
     * adds two to the heuristic cost because it requires a minimum of two tile moves to resolve.
     *
     * @param node    The current node representing the state of the puzzle.
     * @param row     The specific row in the puzzle to check for linear conflicts.
     * @param goalRow The row index in the goal configuration that matches the current row being checked.
     * @param col     The column index of the tile from which to start checking for conflicts in the row.
     *
     * @return The total heuristic cost added by linear conflicts in the specified row.
     */
    private static int calculateRowConflict(Node node, int row, int goalRow, int col) {
        int conflict = 0; // Initialize conflict count
        if (row == goalRow) { // Check only if the row matches the goal row
            // Iterate through tiles to the right of the current tile in the row
            for (int i = col + 1; i < node.board[row].length; i++) {
                // Check if the tile is in its goal row but out of order
                if (node.board[row][i].getValue() != 0 &&
                        (node.board[row][i].getValue() - 1) / node.board[0].length == row &&
                        (node.board[row][i].getValue() - 1) % node.board[0].length < col) {
                    conflict += 2; // Add two for each linear conflict
                }
            }
        }
        return conflict;
    }

    /**
     * Calculates the number of linear conflicts along a specific column in the puzzle board.
     * Similar to row conflicts, a column linear conflict occurs when two tiles are in their
     * goal column but are reversed, necessitating extra moves for correction. Resolving a
     * linear conflict in a column also requires at least two moves, contributing an additional
     * two to the heuristic estimate for each conflict detected.
     *
     * @param node    The current node representing the puzzle's state.
     * @param col     The specific column in the puzzle to check for linear conflicts.
     * @param goalCol The column index in the goal configuration that matches the current column being checked.
     * @param row     The row index of the tile from which to start checking for conflicts in the column.
     *
     * @return The total heuristic cost added by linear conflicts in the specified column.
     */
    private static int calculateColumnConflict(Node node, int col, int goalCol, int row) {
        int conflict = 0;
        if (col == goalCol) { // Check only if the column matches the goal column
            // Iterate through tiles below the current tile in the column
            for (int i = row + 1; i < node.board.length; i++) {
                // Check if the tile is in its goal column but out of order
                if (node.board[i][col].getValue() != 0 &&
                        (node.board[i][col].getValue() - 1) % node.board[0].length == col &&
                        (node.board[i][col].getValue() - 1) / node.board.length < row) {
                    conflict += 2; // Add two for each linear conflict
                }
            }
        }
        return conflict;
    }

    /**
     * Implements the A* search algorithm to find the shortest path from the start state to the goal state
     * of a puzzle game. A* search uses a best-first search strategy, prioritizing paths that appear to lead
     * most directly to the goal. It combines the cost to reach the node (g) and the heuristic estimate of
     * the cost to reach the goal from that node (h) to form a cost estimate (f = g + h) for each node.
     *
     * @param startGame The initial state of the puzzle, represented as a Node.
     * @param goal      A 2D integer array representing the goal state configuration of the puzzle.
     *
     * @return A string that describes the solution path, including the sequence of moves to reach the goal
     * state from the initial state, the number of nodes generated, and the cost of the solution. If
     * no solution is found, the method returns "no path".
     */
    public String aStar(Node startGame, int[][] goal) {
        // Initialize priority queue with comparator based on the f value of nodes
        Comparator<Node> nodeComparator = Comparator.comparingDouble(n -> n.f);
        PriorityQueue<Node> openList = new PriorityQueue<>(nodeComparator);
        Map<String, Node> closedList = new HashMap<>();
        Map<String, Node> tmpList = new HashMap<>();

        // Initialize the starting node's cost values
        startGame.g = 0;
        startGame.h = calculateHeuristic(startGame, goal);
        startGame.f = startGame.g + startGame.h;
        openList.add(startGame);
        tmpList.put(startGame.getKey(), startGame);

        // Main loop of A* search
        while (!openList.isEmpty()) {
            if(this.openFlag){
                for(Map.Entry<String, Node> item: tmpList.entrySet()){
                    System.out.println(item.getValue());
                }
            }
            Node currentNode = openList.poll(); // Get node with lowest f value
            tmpList.remove(currentNode.getKey());// Update tmpList like open list
            // Check if current node is the goal state
            if (isGoal(currentNode, goal)) {
                // Construct and return the solution path and metrics
                String finalPath = currentNode.path.substring(0, currentNode.path.length() - 1);
                return finalPath + "\n" + "Num: " + Node.totalNodes + "\n" + "Cost: " + currentNode.g;
            }
            // Add current node to closed list to avoid revisiting
            closedList.put(currentNode.getKey(), currentNode);
            // Explore all possible moves from the current node
            for (int i = 0; i < this.operator.length; i++) {
                if (currentNode.Move(this.operator[i])) { // Attempt to move in each direction
                    Node child = currentNode.children[i];
                    // Update child's heuristic and total cost values
                    child.h = calculateHeuristic(child, goal);
                    child.f = child.g + child.h;
                    if((!closedList.containsKey(child.getKey())) && (!tmpList.containsKey(child.getKey()))){
                        openList.add(child);
                        tmpList.put(child.getKey(), child);
                        // Add child to open list if not in closed list or already in open list with higher f
                    }else if(tmpList.containsKey(child.getKey())){
                        for (Node openNode : openList) {
                            if (openNode.f > child.f) {
                                openList.remove(openNode);
                                tmpList.remove(openNode.getKey());
                                openList.add(child);
                                tmpList.put(child.getKey(), child);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return "no path" + "\n" + "Num: "+ Node.totalNodes +"\nCost:";
    }

    /**
     * Implements the Iterative Deepening A* (IDA*) search algorithm to find the shortest path from the
     * current state to the goal state of a puzzle game. IDA* combines the depth-first search's memory
     * efficiency with the heuristic-based search of A* to efficiently find a solution. It iteratively
     * increases the cost threshold until the goal state is found.
     *
     * @param startGame The initial state of the puzzle, represented as a Node.
     * @param goal      A 2D integer array representing the goal state configuration of the puzzle.
     *
     * @return A string that describes the solution path, including the sequence of moves to reach the goal
     * state from the initial state, the number of nodes generated, and the cost of the solution. If
     * no solution is found within the iterative deepening thresholds, the method returns "no path".
     */
    public String idaStar(Node startGame, int[][] goal) {
        Map<String, Node> H = new HashMap<>();
        Stack<Node> L = new Stack<>();
        startGame.h = calculateHeuristic(startGame, goal);
        double threshold = startGame.h;
        while (threshold != Double.MAX_VALUE) {
            double minF = Double.MAX_VALUE;
            startGame.setOut(false);
            L.push(startGame);
            H.put(startGame.getKey(), startGame);
            while (!L.isEmpty()) {
                if(this.openFlag){
                    for(Map.Entry<String, Node> item: H.entrySet()){
                        System.out.println(item.getValue());
                    }
                }
                Node currentNode = L.pop();
                if (currentNode.isOut()) {
                    H.remove(currentNode.getKey());
                }else {
                    currentNode.setOut(true);
                    L.push(currentNode);
                    for (int i = 0; i < this.operator.length; i++) {
                        if (currentNode.Move(this.operator[i])) {
                            Node child = currentNode.children[i];
                            child.h = calculateHeuristic(child, goal);
                            child.f = child.g + child.h;
                            if(child.f > threshold){
                                minF = Math.min(minF, child.f);
                                continue;
                            }
                            if((H.containsKey(child.getKey())) && (H.get(child.getKey()).isOut())){
                                continue;
                            }
                            if((H.containsKey(child.getKey())) && (!H.get(child.getKey()).isOut())){
                                if(H.get(child.getKey()).f > child.f){
                                    L.remove(H.get(child.getKey()));
                                    H.remove(child.getKey());
                                }else{
                                    continue;
                                }
                            }
                            if(isGoal(child, goal)){
                                String finalPath = child.path.substring(0, child.path.length() - 1);
                                return finalPath + "\n" + "Num: " + Node.totalNodes + "\n" + "Cost: " + child.g;
                            }
                            L.push(child);
                            H.put(child.getKey(), child);
                        }
                    }
                }
            }
            threshold = minF; // Update threshold for the next iteration
        }
        return "no path" + "\n" + "Num: "+ Node.totalNodes +"\nCost:";
    }


    /**
     * Calculates an initial upper bound for a problem based on the factorial of a given number. This method
     * is designed to set a limit or an initial estimate for problems where the solution space grows factorial
     * with the size of the input. It caps the upper bound at Integer.MAX_VALUE for large inputs to prevent
     * overflow issues.
     *
     * @param n The input number for which the factorial-based upper bound is calculated. It represents the
     *          size of the problem or the number of elements involved.
     *
     * @return The calculated upper bound based on the factorial of n. For n <= 12, it returns n!. For n > 12,
     *         it returns Integer.MAX_VALUE.
     */
    long calculateInitialUpperBound(int n) {
        if (n <= 12)
                return factorial(n);
        else
            return Integer.MAX_VALUE;
    }

    /**
     * Computes the factorial of a given number n. The factorial of n (denoted as n!) is the product of all
     * positive integers less than or equal to n.
     *
     * @param n The number for which the factorial is to be calculated.
     * @return The factorial of n.
     */
    long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }


    /**
     * Implements the Depth-First Branch-and-Bound (DFBnB) algorithm to find an optimal solution
     * path for the puzzle game. DFBnB is a search strategy that uses depth-first traversal combined
     * with pruning of branches that exceed a certain cost threshold, known as the upper bound. This
     * approach ensures that the search space is efficiently explored while discarding paths that are
     * too costly.
     *
     * @param startGame The initial state of the puzzle, represented as a Node.
     * @param goal A 2D integer array representing the goal state configuration of the puzzle.
     *
     * @return A string that describes the solution path, including the sequence of moves to reach the
     *         goal state from the initial state, the number of nodes generated, and the cost of the
     *         solution. If no solution is found, the method returns "no path".
     */
    public String dfbNb(Node startGame, int[][] goal) {
        Stack<Node> L = new Stack<>();
        Map<Node, Boolean> H = new HashMap<>();
        startGame.h = calculateHeuristic(startGame, goal);
        startGame.f = startGame.g + startGame.h;
        L.push(startGame);
        H.put(startGame, false); // False indicates the node is not "out"

        long resultCost = calculateInitialUpperBound(startGame.board.length * startGame.board[0].length -1);
        long totalNodesCreated = 0; // Start node is already created
        String resultPath =  "no path";

        while (!L.isEmpty()) {
            Node currentNode = L.pop();
            if (H.get(currentNode)) {
                H.remove(currentNode); // Remove if marked as "out"
            } else {
                currentNode.setOut(true);
                L.push(currentNode);
                H.put(currentNode, true);

                List<Node> N = new ArrayList<>();
                for (int i = 0; i < this.operator.length; i++) {
                    if (currentNode.Move(this.operator[i])) {
                        Node child = currentNode.children[i];
                        totalNodesCreated++; // Increment for every new node created
                        child.h = calculateHeuristic(child, goal);
                        child.f = child.g + child.h;

                        if (child.f < resultCost) { // Consider only if cost is below the current result
                            N.add(child);
                            if(this.openFlag){
                                System.out.println(child);
                            }
                        }
                    }
                }

                // Sort and prune the list of children based on their f values
                N.sort(Comparator.comparingDouble(node -> node.f));
                List<Node> toAdd = new ArrayList<>();
                for (Node child : N) {
                    if (child.f >= resultCost) {
                        break; // Stop adding children if their cost exceeds the current best
                    }
                    if (isGoal(child, goal)) {
                        resultCost = child.g; // Update the best cost
                        resultPath = child.path; // Update the result path
                    } else {
                        toAdd.add(child);
                    }
                }

                // Add remaining nodes in reverse order for depth-first exploration
                Collections.reverse(toAdd);
                for (Node child : toAdd) {
                    if (!H.containsKey(child) || (H.get(child) != null && !H.get(child))) {
                        L.push(child);
                        H.put(child, false);
                    }
                }
            }
        }

        // Construct the final result string
        if (!resultPath.equals("no path")) {
            String tmp =resultPath.substring(0, resultPath.length() - 1);
            resultPath = tmp + "\nNum: " + totalNodesCreated + "\nCost: " + resultCost;
        }
        return resultPath;
    }
    /**
     * Initiates the puzzle-solving process using the algorithm specified at the instance level.
     * This method serves as a central dispatcher that selects and executes one of the supported
     * search algorithms based on the 'algorithm' field's value. It supports Depth-First Iterative
     * Deepening (DFID), A*, Iterative Deepening A* (IDA*), and Depth-First Branch-and-Bound (DFBnB).
     *
     * @param startGame The initial state of the puzzle represented as a Node.
     *
     * @param goal A 2D integer array representing the goal state configuration of the puzzle.
     *
     * @return A string describing the solution path if one is found, including the sequence of moves,
     *         the number of nodes generated, and the cost of the solution. Returns a message indicating
     *         no solution was found or that the specified algorithm is not supported if applicable.
     */
    public String startGame(Node startGame, int [][] goal){
        String res = "Input Error";
        if(this.algorithm.equals("DFID"))
            return dfid(startGame, goal);
        else if (this.algorithm.equals("A*"))
            return aStar(startGame, goal);
        else if (this.algorithm.equals("IDA*"))
            return idaStar(startGame, goal);
        else if (this.algorithm.equals("DFBnB"))
            return dfbNb(startGame, goal);
        return res;
    }
}


