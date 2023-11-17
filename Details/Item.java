package Details;

public class Item {

    private int id;
    private String itemName;
    private double price;
    private int quantityAvailable;

    public Item(int id, String itemName, double price, int quantityAvailable) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }
}
