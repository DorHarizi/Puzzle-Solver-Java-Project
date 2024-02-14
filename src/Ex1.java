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
            StringBuilder matrix = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                matrix.append("\n").append(line);
            }

            Block[][] board = makeBoard(matrix.toString(), splitTuples, numOfRow, numOfColumns);
            Node start = new Node(board);

            //Send the data from the user and start the process in Algorithm class
            Algorithm myPro = new Algorithm(algorithm, timeFlag, openFlag, start);
            Node startGame = start.deepCopy();
            String res = myPro.startGame(startGame,myPro.getGoal());
            System.out.println(res);

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

    public static Block[][] makeBoard(String matrix, List<String[]> splitTuples, int numOfRow, int numOfColumns){
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

