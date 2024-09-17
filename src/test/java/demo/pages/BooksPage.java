package demo.pages;

import com.github.javafaker.Faker;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.List;

public class BooksPage extends BasePage{


    Faker faker = new Faker();



    public BooksPage(WebDriver driver) {
        super(driver);
    }


    //LOCATORS
    @FindBy(id = "searchBox")
    public WebElement searchBox;
    @FindBy(xpath = "//*[@role='rowgroup']//a")
    public List<WebElement> listedBookNames;
    @FindBy(xpath = "//*[normalize-space()='No rows found']")
    public WebElement canotFindAnyResultLabelForTable;


    //METHODS
    public void searchBook(String bookName){
        writeText(searchBox,bookName);
    }

    public List<String> getAllBooks(){
        return getListOfElementTexts(listedBookNames);
    }

    public boolean isListContainString(List <String> list, String keyword) {
        for(String item : list){
            if(item.contains(keyword)){
                return true;
            }
        }
        return false;
    }

    public String getRandomBookName(){
        int sizeOfList = getAllBooks().size();
        int randomNumber = faker.number().numberBetween(1,sizeOfList);
        return getAllBooks().get(randomNumber-1);
    }

}
