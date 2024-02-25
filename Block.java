/**
 * Represents a block within a puzzle board. Each block is characterized by its value,
 * color, cost associated with number of moving and the number of moves made.
 */
public class Block {
    // Value of the block
    private int value;
    // Color of the block
    private String color;
    // Cost associated with moving the block
    private int priceOfMove;
    // Number of moves made with this block
    private int numOfMove;

    /**
     * Constructs a new Block with specified value and color.
     *
     * @param value The value of the block.
     * @param color The color of the block.
     */
    public Block(int value, String color) {
        this.value = value;
        this.color = color;
    }

    /**
     * Copy constructor. Creates a new Block instance by copying the properties of another block.
     *
     * @param copyBlock The Block instance to copy from.
     */
    public Block(Block copyBlock){
        this.value = copyBlock.value;
        this.color = copyBlock.color;
        this.priceOfMove = copyBlock.priceOfMove;
        this.numOfMove = copyBlock.numOfMove;
    }

    /**
     * Returns the value of the block.
     *
     * @return The value of the block.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the color of the block.
     *
     * @return The color of the block.
     */
    public String getColor() {return color;}

    /**
     * Sets the color of the block.
     *
     * @param color The new color of the block.
     */
    public void setColor(String color){
        this.color = color;
    }

    /**
     * Returns the price associated with moving the block.
     *
     * @return The price of moving the block.
     */
    public int getPriceOfMove() {
        return priceOfMove;
    }

    /**
     * Sets the price associated with moving the block.
     *
     * @param priceOfMove The new price of moving the block.
     */
    public void setPriceOfMove(int priceOfMove) {
        this.priceOfMove = priceOfMove;
    }

    /**
     * Returns the number of moves made with this block.
     *
     * @return The number of moves made.
     */
    public int getNumOfMove() {
        return numOfMove;
    }

    /**
     * Sets the number of moves made with this block.
     *
     * @param numOfMove The new number of moves made.
     */
    public void setNumOfMove(int numOfMove) {
        this.numOfMove = numOfMove;
    }

    /**
     * Returns a string representation of the block, primarily its value.
     *
     * @return A string representation of the block.
     */
    @Override
    public String toString() {
        return value + " ";
    }
}
