package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class ReplenishmentPage {
  private SelenideElement amount = $("[data-test-id=amount] input");
  private SelenideElement from = $("[data-test-id=from] input");
  private SelenideElement replenishment = $("[data-test-id=action-transfer]");

  public ReplenishmentPage() {
    replenishment.shouldBe(Condition.visible);
  }

  public DashboardPage replenishment(String sum, String card) {
    amount.sendKeys(Keys.chord(Keys.CONTROL + "a"), Keys.DELETE);
    amount.setValue(sum);
    from.sendKeys(Keys.chord(Keys.CONTROL + "a"), Keys.DELETE);
    from.setValue(card);
    replenishment.click();
    return new DashboardPage();
  }
}
