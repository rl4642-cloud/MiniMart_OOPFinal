import java.io.Serializable;

/**
 * Product class representing a product in the supermarket
 * Implements Serializable for file I/O operations
 */
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private double purchasePrice;  // Cost price per unit
    private double sellingPrice;   // Retail price per unit
    private int stockQuantity;     // Current stock quantity
    private int lowStockThreshold; // Alert when stock < threshold
    private static int productCount = 0;
    private static int nextId = 1;
    
    /**
     * Constructor to create a new product
     * @param name Product's name
     * @param purchasePrice Cost price per unit
     * @param sellingPrice Retail price per unit
     * @param stockQuantity Initial stock quantity
     * @param lowStockThreshold Low stock alert threshold
     */
    public Product(String name, double purchasePrice, double sellingPrice, int stockQuantity, int lowStockThreshold) {
        this.id = nextId++;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.stockQuantity = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
        productCount++;
    }
    
    /**
     * Constructor with specified ID (for loading from file)
     * @param id Product's ID
     * @param name Product's name
     * @param purchasePrice Cost price per unit
     * @param sellingPrice Retail price per unit
     * @param stockQuantity Current stock quantity
     * @param lowStockThreshold Low stock alert threshold
     */
    public Product(int id, String name, double purchasePrice, double sellingPrice, int stockQuantity, int lowStockThreshold) {
        this.id = id;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.stockQuantity = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
        productCount++;
    }
    
    // Getter methods
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPurchasePrice() {
        return purchasePrice;
    }
    
    public double getSellingPrice() {
        return sellingPrice;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public int getLowStockThreshold() {
        return lowStockThreshold;
    }
    
    // Setter methods
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
    
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }
    
    /**
     * Increase stock quantity (for purchasing/restocking)
     * @param quantity Amount to add to stock
     */
    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }
    
    /**
     * Decrease stock quantity (for sales)
     * @param quantity Amount to subtract from stock
     * @return true if successful, false if insufficient stock
     */
    public boolean decreaseStock(int quantity) {
        if (this.stockQuantity >= quantity) {
            this.stockQuantity -= quantity;
            return true;
        }
        return false;
    }
    
    /**
     * Check if product is low in stock
     * @return true if stock is below threshold
     */
    public boolean isLowStock() {
        return stockQuantity < lowStockThreshold;
    }
    
    /**
     * Get the total number of Product objects created
     * @return number of products created
     */
    public static int getProductCount() {
        return productCount;
    }
    
    /**
     * Reset the product count to zero
     */
    public static void resetProductCount() {
        productCount = 0;
    }
    
    /**
     * Set the product count to a specific value
     * @param count the new product count
     */
    public static void setProductCount(int count) {
        productCount = count;
    }
    
    /**
     * Get the next ID that will be assigned
     * @return next ID
     */
    public static int getNextId() {
        return nextId;
    }
    
    /**
     * Set the next ID (for loading from file)
     * @param id the next ID to use
     */
    public static void setNextId(int id) {
        nextId = id;
    }
    
    /**
     * Reset the next ID counter
     */
    public static void resetNextId() {
        nextId = 1;
    }
    
    /**
     * Display product information in a formatted way
     */
    public void displayProduct() {
        String stockStatus = isLowStock() ? "LOW" : "OK";
        System.out.printf("%-3d | %-20s | $%-10.2f | $%-10.2f | %-8d | %-8d | %s%n", 
                         id, name, purchasePrice, sellingPrice, stockQuantity, lowStockThreshold, stockStatus);
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Purchase Price: $%.2f, Selling Price: $%.2f, Stock: %d, Threshold: %d", 
                           id, name, purchasePrice, sellingPrice, stockQuantity, lowStockThreshold);
    }
}



