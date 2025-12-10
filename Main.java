/**
 * Main class to run the MiniMart application
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to MiniMart - Small Supermarket Inventory and Sales Management System!");
        System.out.println("=================================================================================");
        System.out.println("Group Members: Miles Lu(rl4642), Tuo Zhang(tz2714), Jueying Zhu(jz5028)");
        System.out.println("=================================================================================");
        
        MiniMart miniMart = new MiniMart();
        
        try {
            miniMart.run();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            miniMart.close();
        }
    }
}



