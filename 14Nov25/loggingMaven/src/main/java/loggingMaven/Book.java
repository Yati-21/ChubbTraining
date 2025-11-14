package loggingMaven;

public class Book {
    private String title;
    private Author author;
    private Edition edition;

    public Book(String title, Author author, Edition edition) {
        this.title = title;
        this.author = author;
        this.edition = edition;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public Edition getEdition() {
        return edition;
    }
}
