import java.io.*;
import java.util.*;  

class Stock {
    String symbol;
    String name;
    double price;

    Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String toString() {
        return symbol + " - " + name + " @ $" + price;
    }
}

class Transaction {
    String type;
    Stock stock;
    int quantity;
    Date date;

    Transaction(String type, Stock stock, int quantity) {
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
        this.date = new Date();
    }

    public String toString() {
        return date + ": " + type + " " + quantity + " shares of " + stock.symbol;
    }
}

class User {
    String username;
    double balance;
    Map<String, Integer> portfolio;
    List<Transaction> transactions;

    User(String username, double balance) {
        this.username = username;
        this.balance = balance;
        this.portfolio = new HashMap<>();
        this.transactions = new ArrayList<>();
    }

    void buyStock(Stock stock, int quantity) {
        double totalCost = stock.price * quantity;
        if (balance >= totalCost) {
            balance -= totalCost;
            portfolio.put(stock.symbol, portfolio.getOrDefault(stock.symbol, 0) + quantity);
            transactions.add(new Transaction("BUY", stock, quantity));
            System.out.println("Bought " + quantity + " shares of " + stock.symbol);
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    void sellStock(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.symbol, 0);
        if (owned >= quantity) {
            balance += stock.price * quantity;
            portfolio.put(stock.symbol, owned - quantity);
            transactions.add(new Transaction("SELL", stock, quantity));
            System.out.println("Sold " + quantity + " shares of " + stock.symbol);
        } else {
            System.out.println("Not enough shares to sell!");
        }
    }

    void printPortfolio(Map<String, Stock> market) {
        System.out.println("\n Portfolio for " + username);
        System.out.println("Balance: $" + balance);
        double totalValue = balance;

        for (String symbol : portfolio.keySet()) {
            int qty = portfolio.get(symbol);
            Stock stock = market.get(symbol);
            double value = stock.price * qty;
            System.out.println(symbol + ": " + qty + " shares @ $" + stock.price + " = $" + value);
            totalValue += value;
        }

        System.out.println("Total Portfolio Value: $" + totalValue);
    }

    void savePortfolioToFile() {
        try (PrintWriter writer = new PrintWriter(username + "_portfolio.txt")) {
            writer.println("Balance: " + balance);
            for (String symbol : portfolio.keySet()) {
                writer.println(symbol + " " + portfolio.get(symbol));
            }
            System.out.println(" Portfolio saved to file.");
        } catch (IOException e) {
            System.out.println("Failed to save portfolio.");
        }
    }

    void viewTransactions() {
        System.out.println("\nTransaction History:");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }
}

public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Stock> market = new HashMap<>();
        market.put("AAPL", new Stock("AAPL", "Apple Inc.", 170.45));
        market.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 2850.75));
        market.put("TSLA", new Stock("TSLA", "Tesla Inc.", 700.55));
        market.put("MSFT", new Stock("MSFT", "Microsoft Corp.", 350.00));

        System.out.print("Enter your username: ");
        String username = sc.nextLine();
        User user = new User(username, 10000);

        int choice;
        do {
            System.out.println("\n==== STOCK TRADING MENU ====");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transactions");
            System.out.println("6. Save Portfolio to File");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.println("\nMarket Data:");
                    for (Stock s : market.values()) {
                        System.out.println(s);
                    }
                    break;
                case 2:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = sc.nextLine().toUpperCase();
                    if (market.containsKey(buySymbol)) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        user.buyStock(market.get(buySymbol), qty);
                    } else {
                        System.out.println("Invalid symbol.");
                    }
                    break;
                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = sc.nextLine().toUpperCase();
                    if (market.containsKey(sellSymbol)) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        user.sellStock(market.get(sellSymbol), qty);
                    } else {
                        System.out.println("Invalid symbol.");
                    }
                    break;
                case 4:
                    user.printPortfolio(market);
                    break;
                case 5:
                    user.viewTransactions();
                    break;
                case 6:
                    user.savePortfolioToFile();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }

        } while (choice != 0);

        sc.close();
    }
}
