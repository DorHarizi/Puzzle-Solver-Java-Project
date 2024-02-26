/**
 * Represents a state in the puzzle game, encapsulating a board configuration of NxM-1 blocks.
 * This class serves as a model node for implementing search algorithms such as
 * Depth-First Iterative Deepening (DFID), A*, IDA* (Iterative Deepening A*), and
 * Depth-Bounded Depth-First Search (DBnB).
 */
public class Node {
    // Path taken to reach the current state from the initial state
    String path;
    // The last move of the current state.
    String lastMove;
    // A unique key representing the state configuration.
    String key;
    // Cost to reach this state from the initial state
    int g;
    // Heuristic estimate of the cost to reach the goal state from this state
    double h;
    // Estimated total cost (f = g + h) to reach the goal state from the initial state through this state
    double f;
    // Symbolizes if the state is marked
    boolean isOut;
    // The board configuration.
    Block[][] board;
    // Children nodes generated from this state by applying possible moves
    Node[] children;
    // The index of the empty space (underscore) on the board.
    int[] underscoreIndex;
    // The
    static  int totalNodes = 0;

    /**
     * Default constructor.
     */
    public Node(){
        this.board = null;
        this.path = "";
        this.lastMove = "";
        this.key = "";
        this.g = 0;
        this.isOut = false;
        this.children = new Node[4];
        this.underscoreIndex = new int[2];
        this.underscoreIndex[0] = -1;
        this.underscoreIndex[1] = -1;
    }

    /**
     * Constructor with a board configuration. Initializes the state with a given board
     * configuration and sets default values for other properties.
     *
     * @param board The initial board configuration as a 2D array of Blocks.
     */
    public Node(Block[][] board) {
        // Deep copy of the board to ensure immutability of states
        Block[][] tmp = new Block[board.length][board[0].length];
        // Initialize default values
        this.path = "";
        this.lastMove = "";
        this.g = 0;
        this.children = new Node[4];
        this.underscoreIndex = new int[2];
        // Copy board and find the empty space if present
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                if(board[i][j].getValue() == 0){
                    this.underscoreIndex[0] = i;
                    this.underscoreIndex[1] = j;
                }
                tmp[i][j] = board[i][j];
            }
        }
        this.board = tmp;
        this.key = this.makeKey();
        this.isOut = false;
    }
    /**
     * Retrieves the unique key of the current node.
     * @return The unique key of the node as a String.
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the unique key of the node.
     * @param key The new key to set for the node.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Retrieves if the state is marked.
     * @return The boolean if the state is marked.
     */
    public boolean isOut() {return isOut;}
    /**
     * Sets if the node is mark or no.
     * @param out The new key to set for the node.
     */
    public void setOut(boolean out) {isOut = out;}

    /**
     * Generates and returns a unique key representing the current state of the board.
     * @return A string that serving as the node's key.
     */
    public String makeKey() {
        StringBuilder keyBuilder = new StringBuilder();
        for (Block[] blocks : this.board) {
            for (Block block : blocks) {
                keyBuilder.append(block.getValue());
            }
        }
        return keyBuilder.toString();
    }

    /**
     * Creates a deep copy of this node.
     * The deep copy includes:
     * - The path to the current node from the start node.
     * - The last move made to reach the current node.
     * - The cost (g) to reach the current node.
     * - The board configuration.
     * - The underscore index, indicating the position of the special block.
     * - The children nodes, recursively deep copied.
     *
     * @return A new Node instance that is a complete deep copy of the current node.
     */
    public Node deepCopy() {
        Node copy = new Node();
        copy.path = this.path; // Copying simple fields directly
        copy.lastMove = this.lastMove;
        copy.g = this.g;

        // Cloning the underscoreIndex array to ensure changes do not affect the original
        if (this.underscoreIndex != null) {
            copy.underscoreIndex = this.underscoreIndex.clone();
        }

        // Deep copying the board array and each Block within it
        if (this.board != null){
            copy.board = new Block[this.board.length][];
            for (int i = 0; i < this.board.length; i++) {
                copy.board[i] = new Block[this.board[i].length];
                for (int j = 0; j < this.board[i].length; j++) {
                    copy.board[i][j] = new Block(this.board[i][j]);
                }
            }
            copy.setKey(this.getKey()); // Setting the key for the copied node
        }
        return copy;
    }

    /**
     * Attempts to move a block in the specified direction if the move is valid.
     * Valid operators are "LEFT", "RIGHT", "UP", and "DOWN". The method checks the feasibility
     * of the move based on the current empty space (underscore) position.
     * @param operator The direction in which to move the block ("LEFT", "RIGHT", "UP", "DOWN").
     * @return true if the move is successful and false otherwise. A move is considered successful
     *         if it results in a valid state transition according to the game's rules.
     */
    public boolean Move(String operator){
        boolean res = false; // Flag to indicate if the move was successful
        int flag = 0; // Used to index into the children array after a successful move
        Node child = this.deepCopy();

        // Attempt to move left unless the last move was right and the operator is available (in the range)
        if(operator.equals("LEFT")) {
            if (!this.lastMove.equals("RIGHT")) {
                if ((0 <= this.underscoreIndex[1] + 1) && (this.underscoreIndex[1] + 1 < this.board[0].length)) {
                    // Update the child node with the results of the move
                    res = true;
                    child.underscoreIndex[1]++;
                    updateChildAfterMove(child, "LEFT", "L-");
                }
            }
        }
        // Attempt to move up unless the last move was up and the operator is available (in the range)
        else if(operator.equals("UP")) {
                if (!this.lastMove.equals("DOWN")) {
                    if ((0 <= this.underscoreIndex[0] + 1) && (this.underscoreIndex[0] + 1 < this.board.length)) {
                        // Update the child node with the results of the move
                        res = true;
                        child.underscoreIndex[0]++;
                        flag = 1;
                        updateChildAfterMove(child, "UP", "U-");
                    }
                }
        }
        // Attempt to move right unless the last move was left and the operator is available (in the range)
        else if(operator.equals("RIGHT")) {
                if (!this.lastMove.equals("LEFT")) {
                    if ((0 <= this.underscoreIndex[1] - 1) && (this.underscoreIndex[1] - 1 < this.board[0].length)) {
                        // Update the child node with the results of the move
                        res = true;
                        child.underscoreIndex[1]--;
                        flag = 2;
                        updateChildAfterMove(child, "RIGHT", "R-");
                    }
                }
        }
        // Attempt to move down unless the last move was up and the operator is available (in the range)
        else if(operator.equals("DOWN")) {
                if (!this.lastMove.equals("UP")) {
                    if ((0 <= this.underscoreIndex[0] - 1) && (this.underscoreIndex[0] - 1 < this.board.length)) {
                        // Update the child node with the results of the move
                        res = true;
                        child.underscoreIndex[0]--;
                        flag = 3;
                        updateChildAfterMove(child, "DOWN", "D-");
                    }
                }
        }
        //If the operator is available and the last move does not become the current move
        if(res){
            //Check if the goal block is white and have a number of move available,
            // if true reducing the number of move
            if(child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].getColor().equals("white")){
                if(child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].getNumOfMove() > 0){
                    child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].setNumOfMove(child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].getNumOfMove() -1);
                }
                else
                    return false;
            }
            // Update the current state after successful the move
            child.g = this.g + child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].getPriceOfMove();
            this.swapBlocks(child);
            child.setKey(child.makeKey());
            totalNodes ++;
            this.children[flag] = child;
        }
        return res;
    }

    /**
     * Helper method to update child node after a successful move.
     * Updates the path, lastMove, and underscoreIndex of the child node.
     *
     * @param child The child node to update.
     * @param move The move that was made ("LEFT", "UP", "RIGHT", "DOWN").
     * @param pathSuffix The suffix to append to the path representing the move.
     */
    private void updateChildAfterMove(Node child, String move, String pathSuffix) {
        String valueBlock = String.valueOf(child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].getValue());
        child.path = this.path + valueBlock + pathSuffix;
        child.lastMove = move;
    }
    /**
     * Helper method to swap the empty space with the moved block in the child node.
     *
     * @param child The child node where the swap will be performed.
     */
    private void swapBlocks(Node child) {
        Block tmp = new Block(child.board[child.underscoreIndex[0]][child.underscoreIndex[1]]);
        child.board[child.underscoreIndex[0]][child.underscoreIndex[1]] = child.board[this.underscoreIndex[0]][this.underscoreIndex[1]];
        child.board[this.underscoreIndex[0]][this.underscoreIndex[1]] = tmp;
    }

    /**
     * Returns a string representation of the Node.
     *
     * @return A string representation of the Node.
     */
    @Override
    public String toString() {
        StringBuilder matrix = new StringBuilder();
        for (Block[] blocks : board) {
            for (Block block : blocks) {
                matrix.append(block.toString());
            }
            matrix.append("\n");
        }
        return "\nThis node number: "+ Node.totalNodes +"\npath= " + this.path + "\n"+ matrix;
    }
}
