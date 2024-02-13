import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Ex1 {
    public static void main(String[] args) {
        BufferedReader reader = null;
        String filePath = "src/input.txt";
        try {
            FileReader fileReader = new FileReader(filePath);
            reader = new BufferedReader(fileReader);
            String line;
            boolean timeFlag;
            boolean openFlag;

            //Get the algorithm name from the file text
            String algorithm = reader.readLine();

            //Get the time flag from the user
            line = reader.readLine();
            timeFlag = line.equals("with time");

            //Get the open flag from the user
            line = reader.readLine();
            openFlag = line.equals("with open");

            //Get the size of the table and corvette to int
            line = reader.readLine();
            String[] tmp = line.split("x");
            int numOfRow = Integer.parseInt(tmp[0]);
            int numOfColumns = Integer.parseInt(tmp[1]);

            //Get the white blocks and corvette to list of tuple
            line = reader.readLine();
            String tuplesString = line.substring(line.indexOf(':') + 1).trim();
            // Split the string into individual tuple strings
            // Regex explanation: splits on "),(" with lookahead and lookbehind to keep parentheses
            String[] tuples = tuplesString.split("(?<=\\)),(?=\\()");
            List<String[]> splitTuples = new ArrayList<>();
            for (String tuple : tuples) {
                String[] numbers = tuple.replaceAll("[()]", "").split(",");
                splitTuples.add(numbers);
            }

            //Get the start state
            String matrix = "";
            while ((line = reader.readLine()) != null) {
                matrix = matrix +"\n"+ line;
            }

            //Send the data from the user and start the process in Algorithm class
            Algorithm myPro = new Algorithm(algorithm, timeFlag, openFlag, numOfRow, numOfColumns, splitTuples, matrix);
//            String res = myPro.start();

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Block class represent a block(number) in the board game
    public static class Block {
        private int value;
        private String color = "";

        private int priceOfMove;
        private int numOfMove;
        
        public Block(int value, String color) {
            this.value = value;
            this.color = color;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public int getPriceOfMove() {
            return priceOfMove;
        }

        public void setPriceOfMove(int priceOfMove) {
            this.priceOfMove = priceOfMove;
        }

        public int getNumOfMove() {
            return numOfMove;
        }

        public void setNumOfMove(int numOfMove) {
            this.numOfMove = numOfMove;
        }
    }

    public static class PuzzleState{
        Block[][] board;
        String path = "";
        String lastMove =  "";
        int totalCost = 0;
        int underscoreIndex;
        int totalState;
        PuzzleState leftChildren;
        PuzzleState rightChildren;
        PuzzleState upChildren;
        PuzzleState downChildren;


        public PuzzleState(Block[][] board) {
            this.board = board;
        }

        public Block[][] getBoard() {
            return board;
        }

        public void setBoard(Block[][] board) {
            this.board = board;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(int totalCost) {
            this.totalCost = totalCost;
        }

        public PuzzleState getLeftChildren() {
            return leftChildren;
        }

        public void setLeftChildren(PuzzleState leftChildren) {
            this.leftChildren = leftChildren;
        }

        public PuzzleState getRightChildren() {
            return rightChildren;
        }

        public void setRightChildren(PuzzleState rightChildren) {
            this.rightChildren = rightChildren;
        }

        public PuzzleState getUpChildren() {
            return upChildren;
        }

        public void setUpChildren(PuzzleState upChildren) {
            this.upChildren = upChildren;
        }

        public PuzzleState getDownChildren() {
            return downChildren;
        }

        public void setDownChildren(PuzzleState downChildren) {
            this.downChildren = downChildren;
        }

        public String getLastMove() {
            return lastMove;
        }

        public void setLastMove(String lastMove) {
            this.lastMove = lastMove;
        }

        public int getUnderscoreIndex() {
            return underscoreIndex;
        }

        public void setUnderscoreIndex(int underscoreIndex) {
            this.underscoreIndex = underscoreIndex;
        }
    }
    public static class Algorithm{
        private String algorithm;
        private boolean timeFlag;
        private boolean openFlag;
        private int numOfRow;
        private int numOfColumns;
        private PuzzleState currentState;
        private int[][] goal;

        public Algorithm(String algorithm, boolean timeFlag, boolean openFlag, int numOfRow, int numOfColumns,List<String[]>  splitTuples, String matrix) {
            this.algorithm = algorithm;
            this.timeFlag = timeFlag;
            this.openFlag = openFlag;
            this.numOfRow = numOfRow;
            this.numOfColumns = numOfColumns;
            this.currentState = new PuzzleState(makeBoard(matrix, splitTuples));
//            this.goal = makeGoal();
        }
//        public String limitedDFS(int limit, Set<PuzzleState> visited) {
//            visited.add(n);
//            boolean isCutoff = false;
//            for (Node child : n.children) {
//                if (H.contains(child)) continue; // Skip if child is already visited
//                String result = limitedDFS(child, goals, limit - 1, H);
//                if (result.equals("cutoff")) isCutoff = true;
//                else if (!result.equals("fail")) return result;
//            H.remove(n);
//            if (isCutoff) return "cutoff";
//            else return "fail";
//    }
//        public String dfid(){
//            for(int depth = 1; depth < Integer.MAX_VALUE; depth ++){
//                Set<PuzzleState> visited = new HashSet<>();
//                String result = limitedDFS(depth, visited);
//                if (!result.equals("cutoff")) {
//                    return result;
//                }
//
//            }
//            return "fail";
//        }
//        public String start(){
//            String res = "";
//            switch (this.algorithm) {
//                case "DFID":
//                    res = dfid();
//                    break;
//                case "A*":
//                    break;
//                case "IDA*":
//                    break;
//                case "DFBnB":
//                    break;
//            }
//            return res;
//        }
//        public int[][] makeGoal(){
//            int[][] res = new int[this.numOfRow][this.numOfColumns];
//            int count = 0;
//            for(int i = 0; i < this.numOfRow; i++){
//                for(int j = 0; j < this.numOfColumns; j++){
//                    res[i][j] = count;
//                    count++;
//                }
//            }
//            res[numOfRow-1][numOfColumns-1] = 0;
//            return res;
//        }
        public Block[][] makeBoard(String matrix, List<String[]> splitTuples){
            // Initialize the board with the correct dimensions
            Block[][] board= new Block[numOfRow][numOfColumns];
            int countWhite = 0;
            String[] rows = matrix.trim().split("\n");
            for (int i = 0; i < rows.length; i++) {
                String[] values = rows[i].split(",");
                for (int j = 0; j < values.length; j++) {
                    if (values[j].trim().equals("_") ) {
                        board[i][j] = new Block(0, "null"); // Represent the blank tile
                    } else if (values[j].trim().equals(splitTuples.get(countWhite)[0])) {
                        int value = Integer.parseInt(values[j].trim());
                        Block tmp = new Block(value, "white");
                        tmp.setNumOfMove(Integer.parseInt(splitTuples.get(countWhite)[1]));
                        tmp.setPriceOfMove(1);
                        board[i][j] = tmp; // Normal tile
                    }else {
                        int value = Integer.parseInt(values[j].trim());
                        Block tmp2 = new Block(value, "red");
                        tmp2.setPriceOfMove(30);
                        board[i][j] = tmp2;
                    }
                }
            }
            return board;
        }
    }
}

//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.Vector;
//
//public class DFIDSearch {
//
//    static class Node {
//        int value;
//        Vector<Node> children;
//
//        public Node(int value) {
//            this.value = value;
//            this.children = new Vector<>();
//        }
//
//        void addChild(Node child) {
//            this.children.add(child);
//        }
//    }
//
//    public static void main(String[] args) {
//        // Example setup
//        Node start = new Node(0); // Starting node
//        Vector<Node> goals = new Vector<>(); // Goals vector
//        goals.add(new Node(3)); // Assuming goal is to find node with value 3
//
//        String result = dfid(start, goals, 10); // 10 is an arbitrary max depth for demonstration
//        if (result.equals("fail")) {
//            System.out.println("Goal not found within the maximum depth.");
//        } else {
//            System.out.println("Goal found: " + result);
//        }
//    }
//
//    public static String dfid(Node start, Vector<Node> goals, int maxDepth) {
//        for (int depth = 0; depth <= maxDepth; depth++) {
//            Set<Node> H = new HashSet<>();
//            String result = limitedDFS(start, goals, depth, H);
//            if (!result.equals("cutoff")) {
//                return result;
//            }
//        }
//        return "fail";
//    }
//
//    private static String limitedDFS(Node n, Vector<Node> goals, int limit, Set<Node> H) {
//        if (isGoal(n, goals)) return "Goal: " + n.value; // Assuming goal check is based on node value
//        if (limit == 0) return "cutoff";
//        else {
//            H.add(n);
//            boolean isCutoff = false;
//            for (Node child : n.children) {
//                if (H.contains(child)) continue; // Skip if child is already visited
//                String result = limitedDFS(child, goals, limit - 1, H);
//                if (result.equals("cutoff")) isCutoff = true;
//                else if (!result.equals("fail")) return result;
//            }
//            H.remove(n);
//            if (isCutoff) return "cutoff";
//            else return "fail";
//        }
//    }
//
//    private static boolean isGoal(Node n, Vector<Node> goals) {
//        for (Node goal : goals) {
//            if (n.value == goal.value) return true;
//        }
//        return false;
//    }
//}


