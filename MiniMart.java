import java.io.*;
import java.util.*;

/**
 * MiniMart class to manage products, purchases, sales, and inventory
 */
public class MiniMart {
    private ArrayList<Product> products;
    private ArrayList<Transaction> transactions;
    private static final String PRODUCTS_FILE = "products.bin";
    private static final String TRANSACTIONS_FILE = "transactions.bin";
    private Scanner scanner;
    
    /**
     * Constructor to initialize the MiniMart system
     */
    public MiniMart() {
        this.products = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        loadData();
    }
    
    /**
     * Load products and transactions from data files
     */
    private void loadData() {
        loadProducts();
        loadTransactions();
    }
    
    /**
     * Load products from the data file
     */
    private void loadProducts() {
        File file = new File(PRODUCTS_FILE);
        if (!file.exists()) {
            System.out.println("No existing products file found. Starting with empty product catalog.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PRODUCTS_FILE))) {
            // Read the number of products
            int productCount = ois.readInt();
            Product.setProductCount(productCount);
            
            // Read the next ID
            int nextId = ois.readInt();
            Product.setNextId(nextId);
            
            // Read products
            for (int i = 0; i < productCount; i++) {
                Product product = (Product) ois.readObject();
                products.add(product);
            }
            
            System.out.println("Loaded " + productCount + " products from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading products from file: " + e.getMessage());
            System.out.println("Starting with empty product catalog.");
        }
    }
    
    /**
     * Load transactions from the data file
     */
    private void loadTransactions() {
        File file = new File(TRANSACTIONS_FILE);
        if (!file.exists()) {
            return; // No transactions file is okay
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TRANSACTIONS_FILE))) {
            // Read the number of transactions
            int transactionCount = ois.readInt();
            Transaction.setTransactionCount(transactionCount);
            
            // Read the next transaction ID
            int nextTransactionId = ois.readInt();
            Transaction.setNextTransactionId(nextTransactionId);
            
            // Read transactions
            for (int i = 0; i < transactionCount; i++) {
                Transaction transaction = (Transaction) ois.readObject();
                transactions.add(transaction);
            }
            
            System.out.println("Loaded " + transactionCount + " transactions from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading transactions from file: " + e.getMessage());
        }
    }
    
    /**
     * Save products to the data file
     */
    private void saveProducts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PRODUCTS_FILE))) {
            // Write the number of products
            oos.writeInt(products.size());
            
            // Write the next ID
            oos.writeInt(Product.getNextId());
            
            // Write all products
            for (Product product : products) {
                oos.writeObject(product);
            }
            
            oos.flush();
        } catch (IOException e) {
            System.out.println("Error saving products to file: " + e.getMessage());
        }
    }
    
    /**
     * Save transactions to the data file
     */
    private void saveTransactions() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TRANSACTIONS_FILE))) {
            // Write the number of transactions
            oos.writeInt(transactions.size());
            
            // Write the next transaction ID
            oos.writeInt(Transaction.getNextTransactionId());
            
            // Write all transactions
            for (Transaction transaction : transactions) {
                oos.writeObject(transaction);
            }
            
            oos.flush();
        } catch (IOException e) {
            System.out.println("Error saving transactions to file: " + e.getMessage());
        }
    }
    
    /**
     * Add a new product to the catalog
     */
    public void addProduct() {
        System.out.println("\nMain Window --> Inventory Overview --> Add a new product window: (Enter the following information)");
        System.out.println("================================================");
        
        System.out.print("Product Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Purchase Price (cost per unit): $");
        double purchasePrice = 0;
        try {
            purchasePrice = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format. Please enter a valid number.");
            System.out.println("Press Enter to continue");
            scanner.nextLine();
            return;
        }
        
        System.out.print("Selling Price (retail per unit): $");
        double sellingPrice = 0;
        try {
            sellingPrice = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format. Please enter a valid number.");
            System.out.println("Press Enter to continue");
            scanner.nextLine();
            return;
        }
        
        // Initial stock quantity is always 0 for new products
        int stockQuantity = 0;
        
        System.out.print("Low Stock Threshold: ");
        int lowStockThreshold = 0;
        try {
            lowStockThreshold = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid threshold format. Please enter a valid number.");
            System.out.println("Press Enter to continue");
            scanner.nextLine();
            return;
        }
        
        // Create new product with initial stock of 0
        Product newProduct = new Product(name, purchasePrice, sellingPrice, stockQuantity, lowStockThreshold);
        products.add(newProduct);
        
        // Save to file
        saveProducts();
        
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Product added successfully!");
        System.out.println("Press Enter to continue");
        scanner.nextLine();
    }
    
    /**
     * Edit an existing product
     */
    public void editProduct() {
        System.out.println("\nMain Window --> Inventory Overview --> Edit product window");
        System.out.println("================");
        
        if (products.isEmpty()) {
            System.out.println("No products in the catalog.");
            System.out.println("Press Enter to continue");
            scanner.nextLine();
            return;
        }
        

        System.out.print("\nEnter the Product ID to edit: ");
        
        try {
            int productId = Integer.parseInt(scanner.nextLine().trim());
            Product product = findProductById(productId);
            
            if (product == null) {
                System.out.println("Product with ID " + productId + " not found.");
                System.out.println("Press Enter to continue");
                scanner.nextLine();
                return;
            }
            
            System.out.println("\nCurrent product information:");
            System.out.println("ID: " + product.getId());
            System.out.println("Name: " + product.getName());
            System.out.println("Purchase Price: $" + product.getPurchasePrice());
            System.out.println("Selling Price: $" + product.getSellingPrice());
            System.out.println("Stock Quantity: " + product.getStockQuantity());
            System.out.println("Low Stock Threshold: " + product.getLowStockThreshold());
            System.out.println("\nEnter new information (press Enter to keep current value):");
            
            System.out.print("Product Name [" + product.getName() + "]: ");
            String name = scanner.nextLine();
            if (!name.trim().isEmpty()) {
                product.setName(name);
            }
            
            System.out.print("Purchase Price [" + product.getPurchasePrice() + "]: $");
            String purchasePriceStr = scanner.nextLine();
            if (!purchasePriceStr.trim().isEmpty()) {
                try {
                    product.setPurchasePrice(Double.parseDouble(purchasePriceStr.trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format. Keeping current value.");
                }
            }
            
            System.out.print("Selling Price [" + product.getSellingPrice() + "]: $");
            String sellingPriceStr = scanner.nextLine();
            if (!sellingPriceStr.trim().isEmpty()) {
                try {
                    product.setSellingPrice(Double.parseDouble(sellingPriceStr.trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format. Keeping current value.");
                }
            }
            
            System.out.print("Stock Quantity [" + product.getStockQuantity() + "]: ");
            String stockStr = scanner.nextLine();
            if (!stockStr.trim().isEmpty()) {
                try {
                    product.setStockQuantity(Integer.parseInt(stockStr.trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity format. Keeping current value.");
                }
            }
            
            System.out.print("Low Stock Threshold [" + product.getLowStockThreshold() + "]: ");
            String thresholdStr = scanner.nextLine();
            if (!thresholdStr.trim().isEmpty()) {
                try {
                    product.setLowStockThreshold(Integer.parseInt(thresholdStr.trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid threshold format. Keeping current value.");
                }
            }
            
            // Save to file
            saveProducts();
            
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("Product updated successfully....Press Enter to continue");
            scanner.nextLine();
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a number.");
            System.out.println("Press Enter to continue");
            scanner.nextLine();
        }
    }
    
    /**
     * Delete a product from the catalog
     */
    public void deleteProduct() {
        System.out.println("\nMain Window --> Inventory Overview --> Delete product window");
        System.out.println("================");
        
        if (products.isEmpty()) {
            System.out.println("No products in the catalog.");
            System.out.println("Press Enter to continue");
            scanner.nextLine();
            return;
        }
        
        System.out.print("\nEnter the Product ID to delete: ");
        
        try {
            int productId = Integer.parseInt(scanner.nextLine().trim());
            
            // Find and remove the product
            boolean removed = false;
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId() == productId) {
                    products.remove(i);
                    removed = true;
                    break;
                }
            }
            
            if (removed) {
                saveProducts(); // Save changes to file
                System.out.println("Product deleted successfully....Press Enter to continue");
                scanner.nextLine();
            } else {
                System.out.println("Product with ID " + productId + " not found.");
                System.out.println("Press Enter to continue");
                scanner.nextLine();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a number.");
            System.out.println("Press Enter to continue");
            scanner.nextLine();
        }
    }
    
    /**
     * Record a purchase (restocking) operation
     */
    public void recordPurchase() {
        System.out.println("\nMain Window --> Inventory Overview --> Record Purchase (Restocking) window");
        System.out.println("================");
        
        if (products.isEmpty()) {
            System.out.println("No products in the catalog. Please add products first.");
            System.out.println("Press Enter to continue");
            scanner.nextLine();
            return;
        }
    

        System.out.print("\nEnter the Product ID: ");
        
        try {
            int productId = Integer.parseInt(scanner.nextLine().trim());
            Product product = findProductById(productId);
            
            if (product == null) {
                System.out.println("Product with ID " + productId + " not found.");
                System.out.println("Press Enter to continue");
                scanner.nextLine();
                return;
            }
            
            System.out.print("Enter the quantity to purchase: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());
            
            if (quantity <= 0) {
                System.out.println("Quantity must be positive.");
                System.out.println("Press Enter to continue");
                scanner.nextLine();
                return;
            }
            
            // Increase stock
            product.increaseStock(quantity);
            
            // Record transaction
            Transaction purchase = new Transaction(Transaction.TransactionType.PURCHASE, 
                                                 product.getId(), product.getName(), 
                                                 quantity, product.getPurchasePrice());
            transactions.add(purchase);
            
            // Save to files
            saveProducts();
            saveTransactions();
            
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("Purchase recorded successfully!");
            System.out.println("Product: " + product.getName());
            System.out.println("Quantity: " + quantity);
            System.out.println("Unit Price: $" + product.getPurchasePrice());
            System.out.println("Total Cost: $" + (quantity * product.getPurchasePrice()));
            System.out.println("New Stock Level: " + product.getStockQuantity());
            System.out.println("Press Enter to continue");
            scanner.nextLine();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter valid numbers.");
            System.out.println("Press Enter to continue");
            scanner.nextLine();
        }
    }
    
    /**
     * Record a sale operation
     */
    public void recordSale() {
        System.out.println("\nMain Window --> Inventory Overview --> Record Sale window");
        System.out.println("================");
        
        if (products.isEmpty()) {
            System.out.println("No products in the catalog. Please add products first.");
            System.out.println("Press Enter to continue");
            scanner.nextLine();
            return;
        }
        
       
        System.out.print("\nEnter the Product ID: ");
        
        try {
            int productId = Integer.parseInt(scanner.nextLine().trim());
            Product product = findProductById(productId);
            
            if (product == null) {
                System.out.println("Product with ID " + productId + " not found.");
                System.out.println("Press Enter to continue");
                scanner.nextLine();
                return;
            }
            
            System.out.print("Enter the quantity to sell: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());
            
            if (quantity <= 0) {
                System.out.println("Quantity must be positive.");
                System.out.println("Press Enter to continue");
                scanner.nextLine();
                return;
            }
            
            // Check stock availability
            if (product.getStockQuantity() < quantity) {
                System.out.println("------------------------------------------------------------------------------------");
                System.out.println("ERROR: Insufficient stock!");
                System.out.println("Available stock: " + product.getStockQuantity());
                System.out.println("Requested quantity: " + quantity);
                System.out.println("Sale rejected. Please adjust the quantity or restock the product.");
                System.out.println("Press Enter to continue");
                scanner.nextLine();
                return;
            }
            
            // Decrease stock
            product.decreaseStock(quantity);
            
            // Calculate revenue and profit
            double revenue = quantity * product.getSellingPrice();
            double profit = quantity * (product.getSellingPrice() - product.getPurchasePrice());
            
            // Record transaction
            Transaction sale = new Transaction(Transaction.TransactionType.SALE, 
                                             product.getId(), product.getName(), 
                                             quantity, product.getSellingPrice());
            transactions.add(sale);
            
            // Save to files
            saveProducts();
            saveTransactions();
            
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("Sale recorded successfully!");
            System.out.println("Product: " + product.getName());
            System.out.println("Quantity: " + quantity);
            System.out.println("Unit Selling Price: $" + product.getSellingPrice());
            System.out.println("Revenue: $" + revenue);
            System.out.println("Profit for this sale: $" + profit);
            System.out.println("New Stock Level: " + product.getStockQuantity());
            System.out.println("Press Enter to continue");
            scanner.nextLine();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter valid numbers.");
            System.out.println("Press Enter to continue");
            scanner.nextLine();
        }
    }
    
    /**
     * Display inventory overview and show submenu for operations
     */
    public void displayInventoryOverview() {
        while (true) {
            System.out.println("\nMain Window --> Inventory Overview");
            System.out.println("================");
            
        if (products.isEmpty()) {
            System.out.println("No products in the catalog.");
        } else {
            // Sort products by name
            ArrayList<Product> sortedProducts = new ArrayList<>(products);
            sortedProducts.sort(Comparator.comparing(Product::getName));
            
            System.out.println("------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-3s | %-20s | %-12s | %-12s | %-8s | %-8s | %s%n", 
                             "ID", "Name", "Purchase Price", "Selling Price", "Stock", "Threshold", "Status");
            System.out.println("------------------------------------------------------------------------------------------------------------------------");
            
            for (Product product : sortedProducts) {
                product.displayProduct();
            }
            
            System.out.println("------------------------------------------------------------------------------------------------------------------------");
            
            // Display low stock alerts
            ArrayList<Product> lowStockProducts = new ArrayList<>();
            for (Product product : products) {
                if (product.isLowStock()) {
                    lowStockProducts.add(product);
                }
            }
            
            if (!lowStockProducts.isEmpty()) {
                System.out.println("\n*** LOW STOCK ALERT ***");
                System.out.println("The following products are below their threshold:");
                for (Product product : lowStockProducts) {
                    System.out.println("  - " + product.getName() + " (ID: " + product.getId() + 
                                     ", Stock: " + product.getStockQuantity() + 
                                     ", Threshold: " + product.getLowStockThreshold() + ")");
                }
            }
        }
        
        // Show submenu for operations
        displayInventorySubMenu();
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                addProduct();
                break;
            case "2":
                editProduct();
                break;
            case "3":
                deleteProduct();
                break;
            case "4":
                recordPurchase();
                break;
            case "5":
                recordSale();
                break;
            case "6":
                return; // Go back to main menu
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
        }
        }
    }
    
    /**
     * Display submenu for inventory operations
     */
    private void displayInventorySubMenu() {
        System.out.println("\nChoose one of the following options:");
        System.out.println("(1) Add a new product");
        System.out.println("(2) Edit product information");
        System.out.println("(3) Delete a product");
        System.out.println("(4) Record purchase (restocking)");
        System.out.println("(5) Record sale");
        System.out.println("(6) Back to Main Window");
        System.out.print("Enter Your Choice: ");
    }
    
    /**
     * Calculate and display total profit
     */
    public void displayTotalProfit() {
        System.out.println("\nMain Window --> Total Profit Report");
        System.out.println("================");
        
        double totalProfit = 0.0;
        int saleCount = 0;
        
        // Calculate profit from all sale transactions
        for (Transaction transaction : transactions) {
            if (transaction.getType() == Transaction.TransactionType.SALE) {
                Product product = findProductById(transaction.getProductId());
                if (product != null) {
                    double profit = transaction.getQuantity() * 
                                  (transaction.getUnitPrice() - product.getPurchasePrice());
                    totalProfit += profit;
                    saleCount++;
                }
            }
        }
        
        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.println("Total Number of Sales: " + saleCount);
        System.out.println("Total Profit: $" + String.format("%.2f", totalProfit));
        System.out.println("---------------------------------------------------------------------------------------------------");
        
        if (saleCount > 0) {
            System.out.println("\nSale Transactions:");
            System.out.println("------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-3s | %-10s | %-3s | %-20s | %-8s | %-12s | %-12s%n", 
                             "ID", "Type", "PID", "Product Name", "Quantity", "Unit Price", "Total");
            System.out.println("------------------------------------------------------------------------------------------------------------------------");
            
            for (Transaction transaction : transactions) {
                if (transaction.getType() == Transaction.TransactionType.SALE) {
                    transaction.displayTransaction();
                }
            }
            System.out.println("------------------------------------------------------------------------------------------------------------------------");
        }
        
        System.out.println("\nPress Enter to continue");
        scanner.nextLine();
    }
    
    /**
     * Display all products
     */
    private void displayAllProducts() {
        if (products.isEmpty()) {
            System.out.println("No products in the catalog.");
            return;
        }
        
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-3s | %-20s | %-12s | %-12s | %-8s | %-8s | %s%n", 
                         "ID", "Name", "Purchase Price", "Selling Price", "Stock", "Threshold", "Status");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        
        for (Product product : products) {
            product.displayProduct();
        }
        
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
    }
    
    /**
     * Find a product by ID
     * @param id Product ID
     * @return Product object or null if not found
     */
    private Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }
    
    /**
     * Display the main menu and handle user choices
     */
    public void run() {
        while (true) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    displayInventoryOverview();
                    break;
                case "2":
                    displayTotalProfit();
                    break;
                case "3":
                    displayAllTransactions();
                    break;
                case "4":
                    System.out.println("Thank you for using MiniMart. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
            }
        }
    }
    
    /**
     * Display all transactions (both purchase and sale)
     */
    public void displayAllTransactions() {
        System.out.println("\nMain Window --> View All Transactions");
        System.out.println("================");
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded.");
            System.out.println("Press Enter to continue");
            scanner.nextLine();
            return;
        }
        
        // Count transactions by type
        int purchaseCount = 0;
        int saleCount = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getType() == Transaction.TransactionType.PURCHASE) {
                purchaseCount++;
            } else {
                saleCount++;
            }
        }
        
        System.out.println("Total Transactions: " + transactions.size() + " (Purchases: " + purchaseCount + ", Sales: " + saleCount + ")");
        System.out.println("\nAll Transactions:");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-3s | %-10s | %-3s | %-20s | %-8s | %-12s | %-12s%n", 
                         "ID", "Type", "PID", "Product Name", "Quantity", "Unit Price", "Total");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        
        for (Transaction transaction : transactions) {
            transaction.displayTransaction();
        }
        
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        System.out.println("\nPress Enter to continue");
        scanner.nextLine();
    }
    
    /**
     * Display the main menu
     */
    private void displayMainMenu() {
        System.out.println("\nMain Window:");
        System.out.println("============");
        System.out.println("Choose one of the following options:");
        System.out.println("(1) Display inventory overview");
        System.out.println("(2) Display total profit report");
        System.out.println("(3) View all transactions");
        System.out.println("(4) Quit");
        System.out.print("Enter Your Choice: ");
    }
    
    /**
     * Initialize the system with sample data for demonstration
     */
    private void initializeSampleData() {
        // Reset counters to start from 1
        Product.setNextId(1);
        Transaction.setNextTransactionId(1);
        
        // Create sample products with initial stock
        Product p1 = new Product("Milk", 2.50, 3.99, 50, 20);
        Product p2 = new Product("Bread", 1.20, 2.49, 30, 15);
        Product p3 = new Product("Eggs", 2.00, 3.49, 40, 25);
        Product p4 = new Product("Apples", 1.50, 2.99, 60, 30);
        Product p5 = new Product("Chicken Breast", 5.00, 8.99, 25, 10);
        Product p6 = new Product("Rice", 3.00, 5.49, 35, 15);
        Product p7 = new Product("Tomatoes", 1.80, 3.29, 45, 20);
        Product p8 = new Product("Orange Juice", 2.30, 4.49, 20, 10);
        
        products.add(p1);
        products.add(p2);
        products.add(p3);
        products.add(p4);
        products.add(p5);
        products.add(p6);
        products.add(p7);
        products.add(p8);
        
        // Create some sample purchase transactions (restocking)
        // These represent initial purchases that brought stock to current levels
        Transaction t1 = new Transaction(Transaction.TransactionType.PURCHASE, 1, "Milk", 50, 2.50);
        Transaction t2 = new Transaction(Transaction.TransactionType.PURCHASE, 2, "Bread", 30, 1.20);
        Transaction t3 = new Transaction(Transaction.TransactionType.PURCHASE, 3, "Eggs", 40, 2.00);
        Transaction t4 = new Transaction(Transaction.TransactionType.PURCHASE, 4, "Apples", 60, 1.50);
        Transaction t5 = new Transaction(Transaction.TransactionType.PURCHASE, 5, "Chicken Breast", 25, 5.00);
        Transaction t6 = new Transaction(Transaction.TransactionType.PURCHASE, 6, "Rice", 35, 3.00);
        Transaction t7 = new Transaction(Transaction.TransactionType.PURCHASE, 7, "Tomatoes", 45, 1.80);
        Transaction t8 = new Transaction(Transaction.TransactionType.PURCHASE, 8, "Orange Juice", 20, 2.30);
        
        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);
        transactions.add(t4);
        transactions.add(t5);
        transactions.add(t6);
        transactions.add(t7);
        transactions.add(t8);
        
        // Create some sample sale transactions
        // First, record sales and decrease stock accordingly
        p1.decreaseStock(10); // Milk: 50 -> 40
        Transaction s1 = new Transaction(Transaction.TransactionType.SALE, 1, "Milk", 10, 3.99);
        
        p2.decreaseStock(15); // Bread: 30 -> 15
        Transaction s2 = new Transaction(Transaction.TransactionType.SALE, 2, "Bread", 15, 2.49);
        
        p3.decreaseStock(20); // Eggs: 40 -> 20
        Transaction s3 = new Transaction(Transaction.TransactionType.SALE, 3, "Eggs", 20, 3.49);
        
        p4.decreaseStock(30); // Apples: 60 -> 30
        Transaction s4 = new Transaction(Transaction.TransactionType.SALE, 4, "Apples", 30, 2.99);
        
        p5.decreaseStock(10); // Chicken Breast: 25 -> 15
        Transaction s5 = new Transaction(Transaction.TransactionType.SALE, 5, "Chicken Breast", 10, 8.99);
        
        p1.decreaseStock(5); // Milk: 40 -> 35 (another sale)
        Transaction s6 = new Transaction(Transaction.TransactionType.SALE, 1, "Milk", 5, 3.99);
        
        p6.decreaseStock(20); // Rice: 35 -> 15
        Transaction s7 = new Transaction(Transaction.TransactionType.SALE, 6, "Rice", 20, 5.49);
        
        p7.decreaseStock(25); // Tomatoes: 45 -> 20
        Transaction s8 = new Transaction(Transaction.TransactionType.SALE, 7, "Tomatoes", 25, 3.29);
        
        transactions.add(s1);
        transactions.add(s2);
        transactions.add(s3);
        transactions.add(s4);
        transactions.add(s5);
        transactions.add(s6);
        transactions.add(s7);
        transactions.add(s8);
        
        // Save the sample data to files
        saveProducts();
        saveTransactions();
        
        System.out.println("Sample data initialized successfully!");
        System.out.println("  - 8 products added to catalog");
        System.out.println("  - 8 purchase transactions recorded");
        System.out.println("  - 8 sale transactions recorded");
    }
    
    /**
     * Close the scanner when done
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}

