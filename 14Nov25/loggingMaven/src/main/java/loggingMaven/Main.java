package loggingMaven;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        logger.trace("TRACE → Detailed trace log message.");
        logger.debug("DEBUG → Debug info for developers.");
        logger.info("INFO → Application started.");

        Author author = new Author("George Orwell");
        Edition edition = new Edition(1);
        Book book = new Book("1984", author, edition);

        logger.info("INFO Book created: {}", book.getTitle());
        logger.info("INFO Author: {}", book.getAuthor().getName());
        logger.info("INFO Edition Number: {}", book.getEdition().getEditionNumber());

        logger.warn("WARN Something unusual but not breaking.");

        try {
            int x = 5 / 0; // force an exception
        } catch (Exception e) {
            logger.error("ERROR Exception occurred: {}", e.getMessage());
            logger.fatal("FATAL Critical error! Application cannot continue.", e);
        }

        logger.info("INFO Application ended successfully.");
    }
}
