package Details;
public class PurchasedItem {
    private String itemName;
    private int quantity;

    public PurchasedItem(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "PurchasedItem{" +
                "itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
