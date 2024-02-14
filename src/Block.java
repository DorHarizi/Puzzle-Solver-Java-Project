public class Block {
    private int value;

    private String color = "";

    private int priceOfMove;

    private int numOfMove;
    public Block(int value, String color) {
        this.value = value;
        this.color = color;
    }

    public Block(Block tmp){
        this.value = tmp.value;
        this.color = tmp.color;
        this.priceOfMove = tmp.priceOfMove;
        this.numOfMove = tmp.numOfMove;
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

    @Override
    public String toString() {
        return "Block{" +
                "value=" + value +
                ", color='" + color + '\'' +
                ", priceOfMove=" + priceOfMove +
                ", numOfMove=" + numOfMove +
                '}';
    }
}
