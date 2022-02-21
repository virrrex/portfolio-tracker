package com.smallcase.tracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smallcase.tracker.Constants;
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
			throw new IllegalArgumentException(Constants.INVALID_DATA);
		}
		
		Portfolio testPortfolio = portfolioService.findPortfolioBySymbol(newTrade.getSymbol().toUpperCase());
		int currentQuantity = 0;
		int tradeQuantity = newTrade.getQuantity();
		if(testPortfolio != null)
			currentQuantity = testPortfolio.getQuantity();
		if(newTrade.getType().equalsIgnoreCase(Constants.SELL) && currentQuantity < tradeQuantity)
			throw new IllegalArgumentException(Constants.NEGATIVE_QUANTITY);
		
		if(newTrade.getType().equalsIgnoreCase(Constants.SELL)) 
			tradeQuantity *= -1;
		
		if(testPortfolio == null)
			testPortfolio = new Portfolio();
		newTrade.setSymbol(newTrade.getSymbol().toUpperCase());
		newTrade.setType(newTrade.getType().toUpperCase());
		testPortfolio.setQuantity(currentQuantity + tradeQuantity);
		testPortfolio.setSymbol(newTrade.getSymbol());
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
		if(!newTrade.getType().equalsIgnoreCase(Constants.BUY) && !newTrade.getType().equalsIgnoreCase(Constants.SELL))
			isValid = false;
		
		return isValid;
	}

	@Override
	public void removeTrade(int id) {
		repo.deleteById(id);
	}

	@Override
	public Trade updateTrade(Trade updatedTrade, int id) {
		Trade currentTrade = findTradeById(id);
		if(currentTrade == null) {
			throw new IllegalArgumentException(Constants.NOT_EXIST);
		}
		
		//	validate data
		boolean isValid = validateTrade(updatedTrade);
		if(isValid == false) {
			throw new IllegalArgumentException(Constants.INVALID_DATA);
		}
		
		//	validate ticker symbol
		if(!updatedTrade.getSymbol().equalsIgnoreCase(currentTrade.getSymbol())) {
			throw new IllegalArgumentException(Constants.SYMBOL_NOT_SAME);
		}
		
		Portfolio testPortfolio = portfolioService.findPortfolioBySymbol(currentTrade.getSymbol());
		int portfolioQuantity = 0;
		int updateQuantity = updatedTrade.getQuantity();
		int currentQuantity = currentTrade.getQuantity();
		
		removeTrade(currentTrade.getId());
		
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
