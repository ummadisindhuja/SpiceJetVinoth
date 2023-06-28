package spiceJetPages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spiceJetbase.BaseClassSJ;

public class HomePage extends BaseClassSJ {
		@FindBy(xpath="//div[text()='Signup']")
		WebElement signUpButton;
		
		@FindBy(xpath="//div[text()='Login']")
		WebElement logInButton;
		
		@FindBy(xpath="//div[@data-testid='round-trip-radio-button']")
		WebElement roundTripRadioBtn;
		
		@FindBy(xpath="//div[text()='From']")
		WebElement fromPlace;
		
		@FindBy(xpath="//div[@data-testid='to-testID-destination']")
		WebElement toPlace;
		
		@FindBy(xpath="//div[@data-testid='departure-date-dropdown-label-test-id']")
		WebElement departureDate;
		
		@FindBy(xpath="//div[@data-testid='return-date-dropdown-label-test-id']")
		WebElement returnDate;
		
		@FindBy(xpath="//div[@class='css-1dbjc4n r-1loqt21 r-u8s1d r-11xbo3g r-1v2oles r-1otgn73 r-16zfatd r-eafdt9 r-1i6wzkk r-lrvibr r-184en5c']")
		WebElement nextOnCalander;
		
		@FindBy(xpath="//div[@data-testid='home-page-flight-cta']")
		WebElement searchFlightButton;
		
		@FindBy(xpath="//div[text()='check-in']")
		WebElement checkIn;
		
		@FindBy(xpath="//div[text()='Web Check-In']")
		WebElement checkInConfirmation;
		
		@FindBy(xpath="//div[text()='flight status']")
		WebElement flightStatus;
		
		@FindBy(xpath="//div[@class='css-76zvg2 r-qsz3a2 r-18tvxmy r-adyw6z r-1kfrs79']")
		WebElement flightStatusConfirmation;
		
		@FindBy(xpath="//div[text()='manage booking']")
		WebElement manageBooking;
		
		@FindBy(xpath="//div[text()='View / Manage Booking']")
		WebElement manageBookingConfirmation;
		
		public HomePage() {
			PageFactory.initElements(driver, this);
		}
		
		public void goToSignInPage() {
			String parentWindowId = getParentWindowId();
			clickOn(signUpButton);
			switchToChildWindow(parentWindowId);
		}
		
		public void goToLoginPage() {
			clickOn(logInButton);
		}
		
		public void selectArrival(String arrivalPlace) {
			clickOn(fromPlace);
			List<WebElement> arrLocation = driver.findElements(
					By.xpath("//div[@data-testid='to-testID-origin']//div[@class='css-76zvg2 r-cqee49 r-ubezar r-1kfrs79']"));
			for(WebElement location : arrLocation) {
				String place = location.getText();
				if(place.equalsIgnoreCase(arrivalPlace)) {
					location.click();
					break;
				}
			}
		}
		
		public void selectDestination(String destinationPlace) {
			clickOn(toPlace);
			List<WebElement> arrLocation = driver.findElements(
					By.xpath("//div[@data-testid='to-testID-destination']//div[@class='css-76zvg2 r-cqee49 r-ubezar r-1kfrs79']"));
			for(WebElement location : arrLocation) {
				String place = location.getText();
				if(place.equalsIgnoreCase(destinationPlace)) {
					clickOn(location);
					break;
				}
			}
		}
		
		public void selectArrivalAndDestination(String arrivalPlace,String destinationPlace) {
			selectArrival(arrivalPlace);
			selectDestination(destinationPlace);
		}
		
		public void setDepartureDate(String depDate,String depMonth) {
			jsClickOn(departureDate);
			WebElement selDate = driver.findElement(By.xpath("//div[@data-testid='undefined-month-"+depMonth+"-2023']//div[text()='"+depDate+"']"));
			clickOn(selDate);
		}
		
		public void setReturnDate(String retDate,String retMonth) {
			WebElement selDate = driver.findElement(By.xpath("//div[@data-testid='undefined-month-"+retMonth+"-2023']//div[text()='"+retDate+"']"));
			clickOn(selDate);
		}
		
		public void oneWayTrip(String arrivalPlace,String destinationPlace,String date,String month) {
			selectArrival(arrivalPlace);
			selectDestination(destinationPlace);
			setDepartureDate(date, month);
			clickOn(searchFlightButton);
		}
		
		public void roundTrip(String arrivalPlace,String destinationPlace,String depDate,String depMonth,String retDate,String retMonth) {
			clickOn(roundTripRadioBtn);
			selectArrival(arrivalPlace);
			selectDestination(destinationPlace);
			setDepartureDate(depDate, depMonth);
			setReturnDate(retDate, retMonth);
			clickOn(searchFlightButton);
		}
		
		public boolean checkInPresence() {
			clickOn(checkIn);
			return isElementDisplayed(checkInConfirmation);
		}
		
		public boolean flightStatusPresence() {
			clickOn(flightStatus);
			return isElementDisplayed(flightStatusConfirmation);
		}
		
		public boolean manageBookingPresence() {
			clickOn(manageBooking);
			return isElementDisplayed(manageBookingConfirmation);
		}
}
