package com.stock.api.mock.trading;

import com.stock.api.mock.trading.dao.MockTradingDao;
import com.stock.api.mock.trading.exception.BadRequestException;
import com.stock.api.mock.trading.service.MockTradingService;
import com.stock.api.mock.trading.service.StockApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MockTradingServiceTest {
	private MockTradingDao mockTradingDao = mock(MockTradingDao.class);
	private StockApiService stockApiService = mock(StockApiService.class);

	private MockTradingService mockTradingService = new MockTradingService(mockTradingDao, stockApiService);

	@Test
	public void test_get_total_bankroll() throws Exception {
		when(mockTradingDao.getBankRollByUserId("MOCKUSER")).thenReturn(2.0);

		double totalBankRoll = mockTradingService.getTotalBankRoll("MOCKUSER");

		assertEquals(2.0, totalBankRoll , 0);
	}

	@Test
	public void test_get_total_bankroll_rounding() throws Exception {
		when(mockTradingDao.getBankRollByUserId("MOCKUSER")).thenReturn(2.5555555555);

		double totalBankRoll = mockTradingService.getTotalBankRoll("MOCKUSER");

		assertEquals(2.56, totalBankRoll , 0);
	}

	@Test(expected = BadRequestException.class)
	public void test_get_total_bankroll_cannot_found() throws Exception {
		when(mockTradingDao.getBankRollByUserId("MOCKUSER")).thenReturn(null);

		double totalBankRoll = mockTradingService.getTotalBankRoll("MOCKUSER");
	}

	//Integration tests
	//From this file you would call the API - The API would have an in memory database for the DAO. It would test the controller - service and DAO
	//without being too explicit. You put input and make sure the output is the expected output. I.E.
	//Insert user bankroll of 100 . Call get total bankroll by that user endpoint and make sure the output == 100.
}

