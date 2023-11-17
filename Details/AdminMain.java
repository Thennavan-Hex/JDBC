package Details;

import java.util.List;
import java.util.Scanner;

public class AdminMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDao userDao = new UserDao();
        Admin admin = new Admin(userDao);

        System.out.println("Admin Panel:");
        System.out.print("Enter admin username: ");
        String adminUsername = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String adminPassword = scanner.nextLine();

        if (!userDao.isAdmin(adminUsername, adminPassword)) {
            System.out.println("Invalid admin credentials. Exiting admin panel.");
            return;
        }

        System.out.println("\nAdmin Panel:");
        System.out.println("1. Add funds to user");
        System.out.println("2. Add new item");
        System.out.println("3. Update item quantity");
        System.out.print("Enter your choice (1, 2, or 3): ");
        int adminChoice = scanner.nextInt();
        scanner.nextLine();

        if (adminChoice == 1) {
            System.out.print("\nEnter username to add funds: ");
            String userToAddFunds = scanner.nextLine();
            System.out.print("Enter amount to add: $");
            double amountToAdd = scanner.nextDouble();
            scanner.nextLine();

            admin.addFundsToUser(userToAddFunds, amountToAdd);
            System.out.println("Funds added successfully to user: " + userToAddFunds);
        } else if (adminChoice == 2) {
            System.out.print("\nEnter new item name: ");
            String newItemName = scanner.nextLine();
            System.out.print("Enter price per unit: $");
            double newItemPrice = scanner.nextDouble();
            System.out.print("Enter initial quantity available: ");
            int newItemQuantity = scanner.nextInt();
            scanner.nextLine();

            admin.addItem(newItemName, newItemPrice, newItemQuantity);
            System.out.println("Item added successfully: " + newItemName);
        } else if (adminChoice == 3) {
            List<Item> existingItems = userDao.getAvailableItems();
            System.out.println("\nExisting Items:");
            for (Item item : existingItems) {
                System.out.println(item.getId() + ". " + item.getItemName() +
                        " - Quantity available: " + item.getQuantityAvailable());
            }

            System.out.print("\nEnter item ID to update quantity: ");
            int itemIdToUpdate = scanner.nextInt();
            scanner.nextLine();

            if (itemIdToUpdate >= 1 && itemIdToUpdate <= existingItems.size()) {
                Item selectedItem = existingItems.get(itemIdToUpdate - 1);

                System.out.print("Enter new quantity for " + selectedItem.getItemName() + ": ");
                int newQuantity = scanner.nextInt();
                scanner.nextLine();

                admin.updateItemQuantity(selectedItem.getItemName(), newQuantity);
                System.out.println("Quantity updated for " + selectedItem.getItemName());
            } else {
                System.out.println("Invalid item ID. Exiting admin panel.");
                return;
            }
        } else {
            System.out.println("Invalid choice. Exiting admin panel.");
            return;
        }
    }
}
