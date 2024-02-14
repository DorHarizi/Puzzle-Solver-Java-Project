import java.util.Arrays;

public class Node {
    Block[][] board;
    String path;
    String lastMove;
    int currentCost;
    int[] underscoreIndex;
    int id;
    Node[] children;


    public Node(){
        this.board = null;
        path = "";
        lastMove = "";
        currentCost = 0;
        this.underscoreIndex = new int[2];
        underscoreIndex[0] = -1;
        underscoreIndex[1] = -1;
        id = 0;
        this.children = new Node[4];
    }

    public Node(Block[][] board) {
        Block[][] tmp = new Block[board.length][board[0].length];
        path = "";
        lastMove = "";
        currentCost = 0;
        id = 0;
        this.children = new Node[4];
        this.underscoreIndex = new int[2];
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
    }

    public Block[][] getBoard() {
        return board;
    }

    public void setBoard(Block[][] board) {
        this.board = board;
    }

    public Node deepCopy() {
        Node copy = new Node();
        if (this.board != null) {
            copy.board = new Block[this.board.length][];
            for (int i = 0; i < this.board.length; i++) {
                copy.board[i] = new Block[this.board[i].length];
                for (int j = 0; j < this.board[i].length; j++) {
                    // Assuming Block has a copy constructor or method to deep copy
                    copy.board[i][j] = new Block(this.board[i][j]);
                }
            }
        }

        copy.path = this.path; // Strings are immutable, direct assignment is okay
        copy.lastMove = this.lastMove;
        copy.currentCost = this.currentCost;

        if (this.underscoreIndex != null) {
            copy.underscoreIndex = this.underscoreIndex.clone(); // Clones the array, since it's an array of primitives
        }

        copy.id = this.id;

        if (this.children != null) {
            copy.children = new Node[4];
            for (int i = 0; i < this.children.length; i++) {
                // Recursively copy children if they are not null
                if (this.children[i] != null) {
                    copy.children[i] = this.children[i].deepCopy();
                }
            }
        }

        return copy;
    }

    public boolean Move(String operator){
        boolean res = false;
        int flag = 0;
        Node child = new Node(this.getBoard());
        if(operator.equals("LEFT")) {
            if (!this.lastMove.equals("RIGHT")) {
                if ((0 <= this.underscoreIndex[1] - 1) && (this.underscoreIndex[1] - 1 < this.board[0].length)) {
                    res = true;
                    child.underscoreIndex[0] = this.underscoreIndex[0];
                    child.underscoreIndex[1] = this.underscoreIndex[1] - 1;
                    String valueBlock = String.valueOf(child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].getValue());
                    child.path = child.path + valueBlock + "R-";
                    child.lastMove = "LEFT";
                }
            }
        }
        if(operator.equals("UP")) {
            if (!this.lastMove.equals("DOWN")) {
                if ((0 <= this.underscoreIndex[0] - 1) && (this.underscoreIndex[0] - 1 < this.board.length)) {
                    res = true;
                    child.underscoreIndex[0] = this.underscoreIndex[0] - 1;
                    child.underscoreIndex[1] = this.underscoreIndex[1];
                    String valueBlock = String.valueOf(child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].getValue());
                    child.path += valueBlock + "D-";
                    child.lastMove = "UP";
                    flag = 1;
                }
            }
        }
        if(operator.equals("RIGHT")){
            if (!this.lastMove.equals("LEFT")) {
                if ((0 <= this.underscoreIndex[1] + 1) && (this.underscoreIndex[1] + 1 < this.board[0].length)) {
                    res = true;
                    child.underscoreIndex[0] = this.underscoreIndex[0];
                    child.underscoreIndex[1] = this.underscoreIndex[1] + 1;
                    String valueBlock = String.valueOf(child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].getValue());
                    child.path += valueBlock + "L-";
                    child.lastMove = "RIGHT";
                    flag = 2;
                }
            }
        }
        if(operator.equals("DOWN")) {
            if (!this.lastMove.equals("UP")) {
                if ((0 <= this.underscoreIndex[0] + 1) && (this.underscoreIndex[0] + 1 < this.board.length)) {
                    res = true;
                    child.underscoreIndex[0] = this.underscoreIndex[0] + 1;
                    child.underscoreIndex[1] = this.underscoreIndex[1];
                    String valueBlock = String.valueOf(child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].getValue());
                    child.path += valueBlock + "U-";
                    child.lastMove = "DOWN";
                    flag = 3;
                }
            }
        }

        if(res){
            child.currentCost += child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].getPriceOfMove();
            if(child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].getColor().equals("white")){
                child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].setNumOfMove(child.board[child.underscoreIndex[0]][child.underscoreIndex[1]].getNumOfMove() -1);
            }
            child.id = this.id + 1;
            Block tmp = new Block(child.board[child.underscoreIndex[0]][child.underscoreIndex[1]]);
            child.board[child.underscoreIndex[0]][child.underscoreIndex[1]] = child.board[this.underscoreIndex[0]][this.underscoreIndex[1]];
            child.board[this.underscoreIndex[0]][this.underscoreIndex[1]] = tmp;
            this.children[flag] = child;
            System.out.println(child.id);
            System.out.println(child.path);
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Block[] blocks : this.board) {
            for (Block block : blocks) {
                res.append("\n").append(block.toString());
            }
        }
        return "Node{" +res +
                " \npath='" + path + '\'' +
                " \nlastMove='" + lastMove + '\'' +
                " \ncurrentCost=" + currentCost +
                " \nunderscoreIndex=" + Arrays.toString(underscoreIndex) +
                " \nid=" + id +
                '}';
    }
}
