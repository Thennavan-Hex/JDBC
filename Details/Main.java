package Details;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDao userDao = new UserDao();
        String username = null;

        System.out.println("Welcome to Online Shopping!");
        System.out.println("------------------------------");
        System.out.println("Do you have an account? (yes/no)");
        String haveAccount = scanner.nextLine().toLowerCase();

        if (haveAccount.equals("yes")) {
            System.out.println("\nLogin to Your Account:");
            System.out.print("Username: ");
            username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            boolean loginSuccess = userDao.loginUser(username, password);

            if (loginSuccess) {
                System.out.println("\nLogin successful! Welcome, " + username + "!");
            } else {
                System.out.println("Login failed. Exiting...");
                return;
            }
        } else {
            System.out.println("\nCreate a New Account:");
            System.out.print("Username: ");
            username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Address: ");
            String address = scanner.nextLine();
            System.out.print("Initial balance: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();

            User newUser = new User(username, password, email, address, balance);
            boolean registrationSuccess = userDao.registerUser(newUser);
            System.out.println("\nRegistration successful, " + username + "!");
            System.out.println("Your initial balance: $" + balance);
        }

        List<Item> availableItems = userDao.getAvailableItems();
        System.out.println("\nAvailable Items:");
        for (int i = 0; i < availableItems.size(); i++) {
            Item item = availableItems.get(i);
            System.out.println((i + 1) + ". " + item.getItemName() +
                    " - Price: $" + item.getPrice() +
                    ", Quantity available: " + item.getQuantityAvailable());
        }

        System.out.println("\nEnter the item ID you want to purchase:");
        int selectedItemId = scanner.nextInt();

        if (selectedItemId >= 1 && selectedItemId <= availableItems.size()) {
            Item selectedItem = availableItems.get(selectedItemId - 1);

            System.out.println("Item selected: " + selectedItem.getItemName() + " (ID: " + String.valueOf(selectedItem.getId()) + ")");
            System.out.println("Price per unit: $" + selectedItem.getPrice());

            double userBalance = userDao.getUserBalance(username);
            System.out.println("Your current balance: $" + userBalance);

            System.out.print("Enter quantity for " + selectedItem.getItemName() + ": ");
            int quantity = scanner.nextInt();

            double totalCost = quantity * selectedItem.getPrice();
            if (totalCost > userBalance) {
                System.out.println("Insufficient balance. Purchase declined.");
            } else {
                userDao.purchaseItem(username, selectedItem.getId(), quantity);

                double newBalance = userBalance - totalCost;
                userDao.updateUserBalance(username, newBalance);

                System.out.println("\nPurchase successful!");
            }
        } else {
            System.out.println("Invalid item ID. Purchase failed.");
        }

        double finalBalance = userDao.getUserBalance(username);
        System.out.println("\nYour Final Balance after purchases: $" + finalBalance);

        List<Purchase> purchases = userDao.getUserPurchases(username);
        System.out.println("\nPurchased Items:");
        for (Purchase purchase : purchases) {
            System.out.println(purchase.getItemName() + ": " + purchase.getQuantity() +
                    " units, Cost per unit: $" + purchase.getCost() +
                    ", Total cost: $" + (purchase.getQuantity() * purchase.getCost()));
        }
    }
}
