import java.util.HashSet;
import java.util.Set;

public class Algorithm {
    private String algorithm;

    private boolean timeFlag;

    private boolean openFlag;
    private Node startNode;
    private int[][] goal;
    private String []operator;

    public Algorithm(String algorithm, boolean timeFlag, boolean openFlag, Node startNode) {
        this.algorithm = algorithm;
        this.timeFlag = timeFlag;
        this.openFlag = openFlag;
        this.startNode = startNode;
        int count = 1;
        this.goal = new int[startNode.board.length][startNode.board[0].length];
        for (int i = 0; i < this.startNode.board.length; i++){
            for (int j = 0; j < this.startNode.board[i].length; j++){
                this.goal[i][j] = count;
                count ++;
            }
        }
        this.goal[this.goal.length-1][this.goal[0].length-1] = 0;
        this.operator = new String[4];
        this.operator[0] = "LEFT";
        this.operator[1] = "UP";
        this.operator[2] = "RIGHT";
        this.operator[3] = "DOWN";
    }

    public int[][] getGoal() {
        return goal;
    }

    public boolean isGoal(Node node, int[][]goal){
        for(int i = 0;i < goal.length; i++){
            for(int j=0; j < goal[0].length; j++){
                if(goal[i][j] != node.board[i][j].getValue()){
                    return false;
                }
            }
        }
        return true;
    }
    public String Limited_DFS(Node n, int[][] goal, int limited,  Set<Node> visited) {
        if (isGoal(n, goal)) {
            return n.path;
        } else if (limited == 0) {
            return "cutoff";
        }
        visited.add(n);
        boolean isCutOff = false;
        for (int i = 0; i < this.operator.length; i++) {
            boolean canMove = n.Move(this.operator[i]);
            if (!canMove) {
                continue;
            }
            Node g = n.children[i].deepCopy();
            if (visited.contains(g)) {
                continue;
            }
            String result = Limited_DFS(g, goal, limited - 1, visited);
            if (result.equals("cutoff")) {
                isCutOff = true;
            } else if (!result.equals("fail")) {
                return result;
            }
            visited.remove(n);
            if(isCutOff){
                return "cutoff";
            }
            else
            {
                return "fail";
            }
        }
        return "fail";
    }
    public String dfid(Node startGame, int [][] goal){
        String res;
        int depth = 1;
        while(true){
            Set<Node> visited = new HashSet<>();
            res = Limited_DFS(startGame , goal,depth,visited);
            if(!res.equals("cutoff")){
                return res;
            }
            depth++;
        }
    }
    public String startGame(Node startGame, int [][] goal){
        String res = "";
        switch (this.algorithm){
            case "DFID":
                res = dfid(startGame, goal);

        }
        return res;
    }




}
//
//Block[][] board;
//String path;
//String lastMove;
//int currentCost;
//int[] underscoreIndex;
//int id;
//Node[] children;

