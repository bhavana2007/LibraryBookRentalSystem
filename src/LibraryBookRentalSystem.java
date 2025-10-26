import java.util.*;

class Book {
    int id;
    String title;
    String author;
    int totalCopies;
    int availableCopies;

    Book(int id, String title, String author, int totalCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public String toString() {
        return "Book ID: " + id + " | Title: " + title + " | Author: " + author +
               " | Total Copies: " + totalCopies + " | Available: " + availableCopies;
    }
}

class Rental {
    int bookId;
    String renterName;
    Date rentDate;
    Date dueDate;
    boolean returned;

    Rental(int bookId, String renterName, Date rentDate, Date dueDate) {
        this.bookId = bookId;
        this.renterName = renterName;
        this.rentDate = rentDate;
        this.dueDate = dueDate;
        this.returned = false;
    }
}

class LibraryRentalSystem {
    HashMap<Integer, Book> books = new HashMap<>();
    ArrayList<Rental> rentals = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    void addBook() {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("Enter Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Author: ");
        String author = sc.nextLine();
        System.out.print("Enter Total Copies: ");
        int copies = sc.nextInt();
        if (books.containsKey(id)) {
            System.out.println("Book ID already exists!");
        } else {
            books.put(id, new Book(id, title, author, copies));
            System.out.println("✅ Book added successfully!");
        }
    }

    void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        System.out.println("\n------ BOOK LIST ------");
        for (Book b : books.values()) {
            System.out.println(b);
        }
    }

    void searchBook() {
        System.out.print("Enter keyword (title/author): ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;
        for (Book b : books.values()) {
            if (b.title.toLowerCase().contains(keyword) || b.author.toLowerCase().contains(keyword)) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) System.out.println("No matching books found.");
    }

    void rentBook() {
        System.out.print("Enter Book ID to rent: ");
        int id = sc.nextInt(); sc.nextLine();
        if (!books.containsKey(id)) {
            System.out.println("❌ Book not found.");
            return;
        }

        Book b = books.get(id);
        if (b.availableCopies <= 0) {
            System.out.println("❌ No copies available for rent.");
            return;
        }

        System.out.print("Enter renter name: ");
        String renter = sc.nextLine();

        Date rentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(rentDate);
        c.add(Calendar.DATE, 7); // 7-day rental
        Date dueDate = c.getTime();

        rentals.add(new Rental(id, renter, rentDate, dueDate));
        b.availableCopies--;

        System.out.println("📘 Book rented successfully to " + renter);
        System.out.println("Due Date: " + dueDate);
    }

    void returnBook() {
        System.out.print("Enter Book ID to return: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("Enter renter name: ");
        String renter = sc.nextLine();

        Rental target = null;
        for (Rental r : rentals) {
            if (r.bookId == id && r.renterName.equalsIgnoreCase(renter) && !r.returned) {
                target = r;
                break;
            }
        }

        if (target == null) {
            System.out.println("❌ No matching rental record found.");
            return;
        }

        Date today = new Date();
        long diff = today.getTime() - target.dueDate.getTime();
        long daysLate = diff / (1000 * 60 * 60 * 24);
        double fine = 0;

        if (daysLate > 0) {
            fine = daysLate * 10; // ₹10 per late day
            System.out.println("⚠️ Returned " + daysLate + " days late. Fine: ₹" + fine);
        } else {
            System.out.println("✅ Returned on time. No fine.");
        }

        target.returned = true;
        books.get(id).availableCopies++;
        System.out.println("Book returned successfully!");
    }

    void startSystem() {
        while (true) {
            System.out.println("\n===== LIBRARY BOOK RENTAL SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Search Book");
            System.out.println("4. Rent Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1: addBook(); break;
                case 2: viewBooks(); break;
                case 3: searchBook(); break;
                case 4: rentBook(); break;
                case 5: returnBook(); break;
                case 6:
                    System.out.println("Exiting... Thank you!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}

public class LibraryBookRentalSystem {
    public static void main(String[] args) {
        LibraryRentalSystem system = new LibraryRentalSystem();
        system.startSystem();
    }
}
