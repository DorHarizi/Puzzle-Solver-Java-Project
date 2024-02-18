import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Ex1 {
    public static void main(String[] args) {
        BufferedReader reader = null;
        String filePath = "input.txt";
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
            int numOfCol = Integer.parseInt(tmp[1]);

            //Get the white blocks and corvette to list of tuple
            line = reader.readLine();
            Map<Integer, Integer> whiteMoves = parseWhiteData(line);

            String matrix = "";
            while ((line = reader.readLine()) != null) {
                matrix += line;
                matrix += "\n";
            }
            String[] matrixRows = matrix.split("\n");
            Block[][] board = createMatrix(matrixRows, whiteMoves);
            Node start = new Node(board);

            //Send the data from the user and start the process in Algorithm class
            Algorithm myAlgo = new Algorithm(algorithm, timeFlag, openFlag, numOfRow, numOfCol, 0);
            long startTime = System.currentTimeMillis();
            String res = myAlgo.startGame(start,myAlgo.getGoal());
            long endTime = System.currentTimeMillis();
            long elapsedTimeMillis = endTime - startTime;
            double elapsedTimeSec = elapsedTimeMillis / 1000.0;
            myAlgo.writeText(res, elapsedTimeSec);
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



    /**
     * Creates a matrix of {@link Block} objects based on input strings representing rows and a map
     * of white moves. Each cell in the input strings corresponds to a {@link Block} in the matrix.
     * The value of each block is determined by the input string, with "_" representing a block with
     * value 0. The color and other properties of the block are determined by whether the block's
     * value is a key in the whiteMoves map.
     *
     * @param rows An array of strings, where each string represents a row in the matrix. Each value
     *             in the row is separated by a comma, and "_" represents a value of 0.
     * @param whiteMoves A map where each key-value pair represents a white move's value and its
     *                   corresponding number of moves. Used to set specific properties for blocks
     *                   that represent white moves.
     * @return A 2D array of {@link Block} objects representing the matrix. Each block's properties
     *         are set based on the input rows and the whiteMoves map.
     */
    public static Block[][] createMatrix(String[] rows, Map<Integer, Integer> whiteMoves) {
        Block[][] matrix = new Block[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] values = rows[i].split(",");
            matrix[i] = new Block[values.length];
            for (int j = 0; j < values.length; j++) {
                int value = values[j].equals("_") ? 0 : Integer.parseInt(values[j]);
                Block block = new Block(value, "red"); // Default assumption, might need adjustment
                if ((whiteMoves != null) && (whiteMoves.containsKey(value))) {
                    block.setColor("white");
                    block.setNumOfMove(whiteMoves.get(value));
                    block.setPriceOfMove(1);
                } else {
                    block.setColor("red");
                    block.setNumOfMove(-1);
                    block.setPriceOfMove(30);
                }
                matrix[i][j] = block;
            }
        }
        return matrix;
    }

    /**
     * Parses a string representing white player moves in a game and converts it into a map
     * where each key-value pair represents a move and its corresponding number.
     *
     * The input string is expected to follow a specific format, starting with "White:" followed by
     * a list of moves enclosed in parentheses and separated by commas. Each move is represented by
     * two integers: the first integer represents the move's value, and the second integer represents
     * the number of the move.
     *
     * For example, the string "White:(3,1),(5,2),(2,3)" would be parsed into a map with the following
     * key-value pairs: {3=1, 5=2, 2=3}.
     *
     * If the input string is exactly "White:", indicating no moves, the method returns null.
     *
     * @param data The string representing the white player's moves. It must start with "White:" and
     *             follow the format described above.
     * @return A Map<Integer, Integer> where the key is the move's value and the value is the number
     *         of the move. Returns null if the input string is "White:" with no moves specified.
     * @throws NumberFormatException If the parsing of integers fails due to an improper format of the
     *                               input string.
     */
    public static Map<Integer, Integer> parseWhiteData(String data) {
        if(data.equals("White:")){ return null; }
        Map<Integer, Integer> moves = new HashMap<>();
        data = data.substring(data.indexOf(':') + 1).trim(); // Remove "White:" part
        String[] blocksData = data.split("\\),\\(");
        for (String block : blocksData) {
            block = block.replaceAll("[()]", "");
            String[] parts = block.split(",");
            int value = Integer.parseInt(parts[0]);
            int numOfMove = Integer.parseInt(parts[1]);
            moves.put(value, numOfMove);
        }
        return moves;
    }

}

