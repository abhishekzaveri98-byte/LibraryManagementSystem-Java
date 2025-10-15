import java.util.*;

// =====Base Class (Encapsulation + Abstraction)=====
abstract class Book {
    private String title;
    private String author;
    private int copies;

    public Book(String title, String author, int copies) {
        this.title = title;
        this.author = author;
        this.copies = copies;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getCopies() { return copies; }

    public void setCopies(int copies) { this.copies = copies; }

    public abstract double calculateLateFee(int daysLate);
}

// =====Subclass: EBook=====
class EBook extends Book {
    public EBook(String title, String author, int copies) {
        super(title, author, copies);
    }

    @Override
    public double calculateLateFee(int daysLate) {
        return daysLate * 0.50; // Cheaper late fee
    }
}

// =====Subclass: PrintedBook=====
class PrintedBook extends Book {
    public PrintedBook(String title, String author, int copies) {
        super(title, author, copies);
    }

    @Override
    public double calculateLateFee(int daysLate) {
        return daysLate * 2.00; // Higher late fee for printed books
    }
}

// =====SOLID: Payment Interface=====
interface Payment {
    void pay(double amount);
}

// ===== Payment Implementations =====
class CashPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Payment of $" + amount + " completed in cash.");
    }
}

class CardPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Payment of $" + amount + " completed via card.");
    }
}

// ===== Library Management System (Main Logic) =====
public class LibraryManagementSystem {
    private Map<String, Book> books = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public void addBook(Book book) {
        books.put(book.getTitle(), book);
    }

    public void showAvailableBooks() {
        System.out.println("\nAvailable Books:");
        for (Book book : books.values()) {
            System.out.println(book.getTitle() + " by " + book.getAuthor() + " | Copies: " + book.getCopies());
        }
    }

    public void borrowBook() {
        System.out.print("Enter book title to borrow: ");
        String title = scanner.nextLine();
        Book book = books.get(title);

        if (book != null && book.getCopies() > 0) {
            book.setCopies(book.getCopies() - 1);
            System.out.println("You borrowed: " + book.getTitle());
        } else {
            System.out.println("Book not available or out of stock.");
        }
    }

    public void returnBook() {
        System.out.print("Enter book title to return: ");
        String title = scanner.nextLine();
        Book book = books.get(title);

        if (book != null) {
            book.setCopies(book.getCopies() + 1);
            System.out.print("Enter number of days late (0 if on time): ");
            int daysLate = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (daysLate > 0) {
                double lateFee = book.calculateLateFee(daysLate);
                System.out.println("Late fee: $" + lateFee);

                System.out.print("Choose payment method (1 = Cash, 2 = Card): ");
                int method = scanner.nextInt();
                scanner.nextLine();

                Payment payment = (method == 1) ? new CashPayment() : new CardPayment();
                payment.pay(lateFee);
            } else {
                System.out.println("Book returned on time. No late fee.");
            }
        } else {
            System.out.println("Book not found in library records.");
        }
    }

    public static void main(String[] args) {
        LibraryManagementSystem system = new LibraryManagementSystem();

        // Adding sample data
        system.addBook(new EBook("Java Basics", "James Gosling", 3));
        system.addBook(new PrintedBook("Data Structures", "Robert Lafore", 2));
        system.addBook(new PrintedBook("Clean Code", "Robert C. Martin", 4));

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Library Menu =====");
            System.out.println("1. Show Available Books");
            System.out.println("2. Borrow a Book");
            System.out.println("3. Return a Book");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    system.showAvailableBooks();
                    break;
                case 2:
                    system.borrowBook();
                    break;
                case 3:
                    system.returnBook();
                    break;
                case 4:
                    System.out.println("Exiting Library System...");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 4);
    }
}
