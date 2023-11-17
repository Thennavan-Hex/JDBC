package Details;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final Connection connection;

    public UserDao() {
        this.connection = DbConnector.connect();
    }

    public boolean registerUser(User user) {
        String query = "INSERT INTO users (username, password, email, address, balance) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setDouble(5, user.getBalance());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loginUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Item> getAvailableItems() {
        List<Item> items = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT id, name, price, quantity FROM items")) {

            while (resultSet.next()) {
                int itemId = resultSet.getInt("id");
                String itemName = resultSet.getString("name");
                double itemPrice = resultSet.getDouble("price");
                int itemQuantity = resultSet.getInt("quantity");

                Item item = new Item(itemId, itemName, itemPrice, itemQuantity);
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public boolean purchaseItem(String username, int itemId, int quantity) {
        try {
            connection.setAutoCommit(false);

            // Check if the item is available
            String checkAvailabilityQuery = "SELECT id, price, quantity FROM items WHERE id = ?";
            try (PreparedStatement checkAvailabilityStatement = connection.prepareStatement(checkAvailabilityQuery)) {
                checkAvailabilityStatement.setInt(1, itemId);
                ResultSet availabilityResult = checkAvailabilityStatement.executeQuery();

                if (availabilityResult.next()) {
                    double itemPrice = availabilityResult.getDouble("price");
                    int availableQuantity = availabilityResult.getInt("quantity");

                    if (availableQuantity >= quantity) {
                        double cost = itemPrice * quantity;

                        // Update item quantity
                        String updateItemQuantityQuery = "UPDATE items SET quantity = ? WHERE id = ?";
                        try (PreparedStatement updateItemQuantityStatement = connection.prepareStatement(updateItemQuantityQuery)) {
                            updateItemQuantityStatement.setInt(1, availableQuantity - quantity);
                            updateItemQuantityStatement.setInt(2, itemId);
                            updateItemQuantityStatement.executeUpdate();
                        }

                        // Record the purchase
                        String recordPurchaseQuery = "INSERT INTO purchases (username, item_id, quantity, cost) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement recordPurchaseStatement = connection.prepareStatement(recordPurchaseQuery)) {
                            recordPurchaseStatement.setString(1, username);
                            recordPurchaseStatement.setInt(2, itemId);
                            recordPurchaseStatement.setInt(3, quantity);
                            recordPurchaseStatement.setDouble(4, cost);
                            recordPurchaseStatement.executeUpdate();
                        }

                        // Update user balance
                        String updateUserBalanceQuery = "UPDATE users SET balance = balance - ? WHERE username = ?";
                        try (PreparedStatement updateUserBalanceStatement = connection.prepareStatement(updateUserBalanceQuery)) {
                            updateUserBalanceStatement.setDouble(1, cost);
                            updateUserBalanceStatement.setString(2, username);
                            updateUserBalanceStatement.executeUpdate();
                        }

                        connection.commit();
                        return true;
                    }
                }
            }

            connection.rollback();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updateItemQuantity(String itemName, int newQuantity) {
        String query = "UPDATE items SET quantity = ? WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setString(2, itemName);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Purchase> getUserPurchases(String username) {
        List<Purchase> purchases = new ArrayList<>();

        String query = "SELECT purchases.id, items.name, purchases.quantity, purchases.cost " +
                "FROM purchases " +
                "JOIN items ON purchases.item_id = items.id " +
                "WHERE purchases.username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int purchaseId = resultSet.getInt("id");
                String itemName = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                double cost = resultSet.getDouble("cost");

                Purchase purchase = new Purchase(purchaseId, itemName, quantity, cost);
                purchases.add(purchase);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return purchases;
    }

    public double getUserBalance(String username) {
        String query = "SELECT balance FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean isAdmin(String username, String password) {
        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addFundsToUser(String username, double amount) {
        try {
            String updateBalanceQuery = "UPDATE users SET balance = balance + ? WHERE username = ?";
            try (PreparedStatement updateBalanceStatement = connection.prepareStatement(updateBalanceQuery)) {
                updateBalanceStatement.setDouble(1, amount);
                updateBalanceStatement.setString(2, username);
                updateBalanceStatement.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateUserBalance(String username, double newBalance) {
        String query = "UPDATE users SET balance = ? WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addItem(String itemName, double price, int quantity) {
        String query = "INSERT INTO items (name, price, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, itemName);
            preparedStatement.setDouble(2, price);
            preparedStatement.setInt(3, quantity);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
