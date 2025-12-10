# MiniMart - Small Supermarket Inventory and Sales Management System

A Java-based supermarket management system that helps store managers maintain product information, track inventory changes caused by purchasing and selling, and calculate basic profit statistics.

**Group Members:** Miles Lu(rl4642), Tuo Zhang(tz2714), Jueying Zhu(jz5028)

## How to Run

1. Compile the Java files:
   ```bash
   javac *.java
   ```

2. Run the application:
   ```bash
   java Main
   ```

## Program Structure

- **Product.java**: Product class with auto-generated IDs, stock management, and low-stock alerts
- **Transaction.java**: Transaction class for recording purchases and sales
- **MiniMart.java**: Main application logic with product management, purchasing, sales, and reporting features
- **Main.java**: Entry point for the application
- **products.bin**: Binary file storing product data (created automatically)
- **transactions.bin**: Binary file storing transaction history (created automatically)

## Core Features

### 1. Product Information Management
- **Add Product**: Create new products with name, purchase price, selling price, initial stock, and low-stock threshold
- **Edit Product**: Update existing product information
- **Delete Product**: Remove products that are no longer sold
- **Display Inventory**: View all products with current stock levels, prices, and low-stock alerts

### 2. Purchasing (Restocking)
- Record purchase operations by selecting a product and specifying quantity
- Automatically increase product stock quantity
- Record each purchase as a transaction for auditing

### 3. Sales - Inventory Out & Profit Calculation
- Record sales by selecting products and entering sold quantity
- System checks stock availability before confirming sale
- Automatically decreases stock quantity after valid sale
- Calculates revenue and profit for each sale transaction

### 4. Queries and Reports
- **Inventory Overview**: Display full list of products with current stock levels, purchase prices, and selling prices, including low-stock alerts
- **Total Profit Calculation**: Calculate and display total profit based on all recorded sales
  - Total profit = sum over all sale transactions of (selling price − purchase price) × quantity
- **View All Transactions**: Display all purchase and sale transactions with details

## Data Persistence

The system automatically saves all data to binary files:
- Product information is saved to `products.bin`
- Transaction history is saved to `transactions.bin`
- Data is automatically loaded when the application starts

## Usage Example

1. Start the application and you'll see the main menu with 3 options:
   - (1) Display inventory overview
   - (2) Display total profit report
   - (3) View all transactions
   - (4) Quit

2. Select option (1) to view inventory overview, which will show:
   - All products with their current stock levels
   - Low-stock alerts
   - A submenu with options to:
     - Add a new product
     - Edit product information
     - Delete a product
     - Record purchase (restocking)
     - Record sale
     - Back to Main Window

3. Select option (2) to view the total profit report based on all sales

4. Select option (3) to view all transactions (both purchases and sales)

## Notes

- The system validates stock availability before processing sales
- Low-stock alerts are displayed when product stock falls below the threshold
- All transactions are recorded for auditing purposes
- Product IDs are auto-generated and managed automatically
- All product management operations are accessed through the inventory overview menu



