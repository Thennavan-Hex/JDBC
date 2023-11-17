package Details;

public class Admin {
    private UserDao userDao;

    public Admin(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addFundsToUser(String username, double amount) {
        userDao.addFundsToUser(username, amount);
        System.out.println("Funds added successfully to user " + username + ".");
    }

    public void addItem(String itemName, double price, int quantity) {
        userDao.addItem(itemName, price, quantity);
        System.out.println("Item added successfully: " + itemName);
    }

    public void updateItemQuantity(String itemName, int newQuantity) {
        userDao.updateItemQuantity(itemName, newQuantity);
        System.out.println("Item quantity updated successfully: " + itemName);
    }
}
