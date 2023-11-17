package Details;

public class Purchase {

    private int id;
    private String itemName;
    private int quantity;
    private double cost;

    public Purchase(int id, String itemName, int quantity, double cost) {
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getCost() {
        return cost;
    }
}