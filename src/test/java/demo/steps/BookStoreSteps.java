package demo.steps;

import demo.helper.ConfigurationReader;
import demo.helper.factory.LoggerFactory;
import demo.helper.utils.TestContext;
import demo.pages.BooksPage;
import demo.pages.PageFactoryManager;
import demo.pages.Utils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.Logger;

public class BookStoreSteps {

    private final TestContext context;
    private final BooksPage booksPage;
    private static final Logger logger = LoggerFactory.getLogger(BookStoreSteps.class);
    private Utils utils;

    public BookStoreSteps(TestContext context) {
        this.context = context;
        booksPage = PageFactoryManager.getBooksPage(context.driver);
        utils = PageFactoryManager.getUtils(context.driver);
    }



    @Given("User navigates to books store page")
    public void userNavigatesToBooksStorePage() {
        booksPage.navigateTo(ConfigurationReader.get("baseURL"), booksPage.searchBox);
    }

    @And("user searches books with {string} keyword")
    public void userSearchesBooksWithKeyword(String searchKeyword) {
        booksPage.searchBook(searchKeyword);
        booksPage.pressEnter(booksPage.searchBox);
        context.getGlobalData().set("searchKeyword", searchKeyword);
    }


    @Then("user verifies that there is not any displayed result")
    public void userVerifiesThatThereIsNotAnyDisplayedResult() {
        booksPage.AssertVisibilityOfElement(booksPage.canotFindAnyResultLabelForTable,
                "Application found results !!");
    }

    @Then("user verifies that result are displayed")
    public void userVerifiesThatResultAreDisplayed() {
        String keyword = context.getGlobalData().getString("searchKeyword");
        booksPage.AssertTrue(booksPage.isListContainString(booksPage.getAllBooks(), keyword),
                "Book cannot found with this keyword");
    }

    @And("user searches existing book with full book name")
    public void userSearchesExistingBookWithFullBookName() {
        String bookName = booksPage.getRandomBookName();
        booksPage.searchBook(bookName);
        context.getGlobalData().set("searchKeyword", bookName);
    }
}
