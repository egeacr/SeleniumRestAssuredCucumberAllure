package demo.pages;

import org.openqa.selenium.WebDriver;

public class PageFactoryManager {

    private static BasePage basePage;
    private static BooksPage booksPage;
    private static Utils utils;



    public static BasePage getBasePage(WebDriver driver) {
        return basePage == null ? new BasePage(driver) : basePage;
    }

    public static BooksPage getBooksPage(WebDriver driver) {
        return booksPage == null ? new BooksPage(driver) : booksPage;
    }

    public static Utils getUtils(WebDriver driver) {return utils == null ? new Utils(driver) : utils;}



}
