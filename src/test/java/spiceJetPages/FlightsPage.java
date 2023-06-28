package spiceJetPages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spiceJetbase.BaseClassSJ;

public class FlightsPage extends BaseClassSJ{
	@FindBy(xpath="//span[text()='One Way :']")
	WebElement oneWayMsg;
	
	@FindBy(xpath="//span[text()='Round Trip :']")
	WebElement roundTripMsg;
	
	@FindBy(xpath="//div[@data-testid='continue-search-page-cta']")
	WebElement continueSearch;
	
	public FlightsPage() {
		PageFactory.initElements(driver, this);
	}
	
	public String oneWayConfirmationMsg() {
		String actMsg = extractText(oneWayMsg);
		return actMsg;
	}
	
	public String roundTripConfirmationMsg() {
		String actMsg = extractText(roundTripMsg);
		return actMsg;
	}
	
	public void goToPassengerPage() {
		clickOn(continueSearch);
	}
}
