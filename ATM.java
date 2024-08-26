import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

class ATM {
    public static void main(String[] args) {
        // Instantiate the ATM Operations
        ATMOperations operations = new ATMOperations();

        // Display welcome message and ask for user id and pin
        operations.startATM();
    }
}

class User {
    private String userId;
    private String userPin;
    private ArrayList<Transaction> transactionHistory;
    private BankAccount account;

    public User(String userId, String userPin, BankAccount account) {
        this.userId = userId;
        this.userPin = userPin;
        this.account = account;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public boolean verifyPin(String inputPin) {
        return this.userPin.equals(inputPin);
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public BankAccount getAccount() {
        return account;
    }
}

class Transaction {
    private String type;
    private double amount;
    private Date date;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.date = new Date();
    }

    @Override
    public String toString() {
        return "Type: " + type + ", Amount: " + amount + ", Date: " + date;
    }
}

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public boolean transfer(double amount, BankAccount recipientAccount) {
        if (withdraw(amount)) {
            recipientAccount.deposit(amount);
            return true;
        }
        return false;
    }
}

class ATMOperations {
    private User currentUser;

    public void startATM() {
        Scanner scanner = new Scanner(System.in);
        
        // For demonstration purposes, we'll create a test user
        BankAccount account = new BankAccount(1000);
        currentUser = new User("saket2509", "6644", account);

        System.out.println("Welcome to the ATM");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter User PIN: ");
        String userPin = scanner.nextLine();

        if (currentUser.getUserId().equals(userId) && currentUser.verifyPin(userPin)) {
            showMenu();
        } else {
            System.out.println("Invalid credentials");
        }
    }

    private void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    showTransactionHistory();
                    break;
                case 2:
                    performWithdraw();
                    break;
                case 3:
                    performDeposit();
                    break;
                case 4:
                    performTransfer();
                    break;
                case 5:
                    quit = true;
                    System.out.println("Thank you for using the ATM!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showTransactionHistory() {
        System.out.println("\nTransaction History:");
        for (Transaction t : currentUser.getTransactionHistory()) {
            System.out.println(t);
        }
    }

    private void performWithdraw() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();

        if (currentUser.getAccount().withdraw(amount)) {
            currentUser.addTransaction(new Transaction("Withdraw", amount));
            System.out.println("Withdrawal successful. Current balance: " + currentUser.getAccount().getBalance());
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    private void performDeposit() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();

        currentUser.getAccount().deposit(amount);
        currentUser.addTransaction(new Transaction("Deposit", amount));
        System.out.println("Deposit successful. Current balance: " + currentUser.getAccount().getBalance());
    }

    private void performTransfer() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter recipient User ID: ");
        String recipientId = scanner.next();
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        BankAccount recipientAccount = new BankAccount(500);  // Placeholder account
        if (currentUser.getAccount().transfer(amount, recipientAccount)) {
            currentUser.addTransaction(new Transaction("Transfer", amount));
            System.out.println("Transfer successful. Current balance: " + currentUser.getAccount().getBalance());
        } else {
            System.out.println("Transfer failed. Insufficient balance.");
        }
    }
}

