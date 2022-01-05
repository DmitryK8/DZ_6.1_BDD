package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPageV1;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        val loginPageV1 = new LoginPageV1();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPageV1.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);

    }

    @AfterEach
    void asserting() {
        val dashboardPage = new DashboardPage();
        int card1Balance = dashboardPage.getCardBalance(DataHelper.сardOne().getCardId());
        int card2Balance = dashboardPage.getCardBalance(DataHelper.сardTwo().getCardId());
        if (card1Balance != card2Balance) {
            int meanValue = (card1Balance - card2Balance) / 2;
            if (card1Balance < card2Balance) {
                val replenishmentPage = dashboardPage.replenishmentOfTheCard(1);
                replenishmentPage.replenishment(Integer.toString(meanValue), DataHelper.сardOne().getCardNumber());
            } else {
                val replenishmentPage = dashboardPage.replenishmentOfTheCard(2);
                replenishmentPage.replenishment(Integer.toString(meanValue), DataHelper.сardTwo().getCardNumber());
            }
        }
    }

    @Test
    void shouldReplenishmentOfTheCardOne() {
        val dashboardPage = new DashboardPage();
        int expectedCard1Balance = dashboardPage.getCardBalance(DataHelper.сardOne().getCardId()) + 1;
        int expectedCard2Balance = dashboardPage.getCardBalance(DataHelper.сardTwo().getCardId()) - 1;
        val replenishmentPage = dashboardPage.replenishmentOfTheCard(1);
        replenishmentPage.replenishment("1", DataHelper.сardTwo().getCardNumber());
        int card1Balance = dashboardPage.getCardBalance(DataHelper.сardOne().getCardId());
        int card2Balance = dashboardPage.getCardBalance(DataHelper.сardTwo().getCardId());
        assertEquals(expectedCard1Balance, card1Balance);
        assertEquals(expectedCard2Balance, card2Balance);
    }


    @Test
    void shouldInvalidReplenishmentOfTheCardTwo() {
        val dashboardPage = new DashboardPage();
        int expectedCard1Balance = 1;
        int expectedCard2Balance = 19999;
        val replenishmentPage = dashboardPage.replenishmentOfTheCard(2);
        replenishmentPage.replenishment("10000", DataHelper.сardOne().getCardNumber());
        int card1Balance = dashboardPage.getCardBalance(DataHelper.сardOne().getCardId());
        int card2Balance = dashboardPage.getCardBalance(DataHelper.сardTwo().getCardId());
        assertEquals(expectedCard1Balance, card1Balance);
        assertEquals(expectedCard2Balance, card2Balance);
    }

    @Test
    void shouldMustReceiveAnError() {
        val dashboardPage = new DashboardPage();
        val replenishmentPage = dashboardPage.replenishmentOfTheCard(2);
        replenishmentPage.replenishment("10005", DataHelper.сardOne().getCardNumber());
        val dashboardPageWithError = new DashboardPage();
        dashboardPageWithError.getNotificationVisible();
    }
}
