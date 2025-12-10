import java.io.Serializable;

/**
 * Transaction class representing a purchase or sale transaction
 * Implements Serializable for file I/O operations
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum TransactionType {
        PURCHASE,  // Restocking
        SALE       // Sales
    }
    
    private int transactionId;
    private TransactionType type;
    private int productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalAmount;
    private static int transactionCount = 0;
    private static int nextTransactionId = 1;
    
    /**
     * Constructor to create a new transaction
     * @param type Type of transaction (PURCHASE or SALE)
     * @param productId Product ID
     * @param productName Product name
     * @param quantity Quantity involved
     * @param unitPrice Unit price
     */
    public Transaction(TransactionType type, int productId, String productName, int quantity, double unitPrice) {
        this.transactionId = nextTransactionId++;
        this.type = type;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = quantity * unitPrice;
        transactionCount++;
    }
    
    /**
     * Constructor with specified ID (for loading from file)
     */
    public Transaction(int transactionId, TransactionType type, int productId, String productName, 
                      int quantity, double unitPrice, double totalAmount) {
        this.transactionId = transactionId;
        this.type = type;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        transactionCount++;
    }
    
    // Getter methods
    public int getTransactionId() {
        return transactionId;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    /**
     * Get the total number of Transaction objects created
     * @return number of transactions created
     */
    public static int getTransactionCount() {
        return transactionCount;
    }
    
    /**
     * Set the transaction count to a specific value
     * @param count the new transaction count
     */
    public static void setTransactionCount(int count) {
        transactionCount = count;
    }
    
    /**
     * Get the next transaction ID that will be assigned
     * @return next transaction ID
     */
    public static int getNextTransactionId() {
        return nextTransactionId;
    }
    
    /**
     * Set the next transaction ID (for loading from file)
     * @param id the next transaction ID to use
     */
    public static void setNextTransactionId(int id) {
        nextTransactionId = id;
    }
    
    /**
     * Display transaction information in a formatted way
     */
    public void displayTransaction() {
        String typeStr = type == TransactionType.PURCHASE ? "PURCHASE" : "SALE";
        System.out.printf("%-3d | %-10s | %-3d | %-20s | %-8d | $%-10.2f | $%-12.2f%n", 
                         transactionId, typeStr, productId, productName, quantity, 
                         unitPrice, totalAmount);
    }
    
    @Override
    public String toString() {
        return String.format("Transaction ID: %d, Type: %s, Product: %s (ID: %d), Quantity: %d, Unit Price: $%.2f, Total: $%.2f", 
                           transactionId, type, productName, productId, quantity, unitPrice, totalAmount);
    }
}



