package com.smallcase.tracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smallcase.tracker.dao.TradeRepository;
import com.smallcase.tracker.model.Portfolio;
import com.smallcase.tracker.model.Trade;

@Service
public class TradeServiceImpl implements TradeService {

	@Autowired
	TradeRepository repo;
	
	@Autowired
	PortfolioService portfolioService;
	
	@Override
	public Trade addTrade(Trade newTrade) {
		
		// Data Validation
		boolean isValid = validateTrade(newTrade);
		if(isValid == false) {
			throw new IllegalArgumentException("Invalid data. Please verify.");
		}
		
		Portfolio testPortfolio = portfolioService.findPortfolioBySymbol(newTrade.getSymbol().toUpperCase());
		int currentQuantity = 0;
		int tradeQuantity = newTrade.getQuantity();
		if(testPortfolio != null)
			currentQuantity = testPortfolio.getQuantity();
		if(newTrade.getType().equalsIgnoreCase("SELL") && currentQuantity < tradeQuantity)
			throw new IllegalArgumentException("Final quantity cannot be negative. You don't have enough stocks to sell");
		
		if(newTrade.getType().equalsIgnoreCase("SELL")) 
			tradeQuantity *= -1;
		
		testPortfolio.setQuantity(currentQuantity + tradeQuantity);
		testPortfolio.setSymbol(newTrade.getSymbol().toUpperCase());
		double averagePrice = 0.0;
		if(testPortfolio == null)
			averagePrice = newTrade.getPrice();
		else {
			averagePrice = (testPortfolio.getAveragePrice() * currentQuantity + newTrade.getPrice() * tradeQuantity) / testPortfolio.getQuantity();
		}
		testPortfolio.setAveragePrice(averagePrice);
		portfolioService.updatePortfolio(testPortfolio);
		
		return repo.save(newTrade);
	}
	
	boolean validateTrade(Trade newTrade) {
		boolean isValid = true;
		
		if(newTrade.getPrice() < 0.0 || newTrade.getQuantity() < 1) isValid = false;
		if(!newTrade.getType().equalsIgnoreCase("BUY") && !newTrade.getType().equalsIgnoreCase("SELL"))
			isValid = false;
		
		return isValid;
	}

	@Override
	public void removeTrade(int id) {
		repo.deleteById(id);
	}

	@Override
	public Trade updateTrade(Trade updatedTrade, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Trade findTradeById(int id) {
		return repo.findById(id);
	}

	@Override
	public List<Trade> findAllTrade() {
		return repo.findAll();
	}

}
