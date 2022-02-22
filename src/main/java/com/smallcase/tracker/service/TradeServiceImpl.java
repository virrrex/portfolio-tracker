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
		if(testPortfolio.getQuantity() == 0)
			portfolioService.removePortfolio(testPortfolio.getId());
		else
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
		Trade currentTrade = findTradeById(id);
		if(currentTrade == null) {
			throw new IllegalArgumentException(Constants.NOT_EXIST);
		}
		Portfolio testPortfolio = portfolioService.findPortfolioBySymbol(currentTrade.getSymbol());
		if(currentTrade.getType().equalsIgnoreCase(Constants.BUY) && testPortfolio == null) {
			throw new IllegalArgumentException(Constants.PORTFOLIO_NOT_EXIST);
		}
		
		int portfolioQuantity = (testPortfolio != null ? testPortfolio.getQuantity(): 0);
		int removeQuantity = currentTrade.getQuantity();
		
		if(currentTrade.getType().equalsIgnoreCase(Constants.BUY)) {
			if(portfolioQuantity == removeQuantity)
				portfolioService.removePortfolio(testPortfolio.getId());
			else if(portfolioQuantity > removeQuantity) {
				testPortfolio.setQuantity(portfolioQuantity - removeQuantity);
				portfolioService.updatePortfolio(testPortfolio);
			} else {
				throw new IllegalArgumentException(Constants.NEGATIVE_QUANTITY);
			}
		} else {
			testPortfolio.setQuantity(portfolioQuantity + removeQuantity);
			if(testPortfolio == null) {
				testPortfolio.setAveragePrice(currentTrade.getPrice());
				testPortfolio.setSymbol(currentTrade.getSymbol());
			}
			else {
				double averagePrice = (testPortfolio.getAveragePrice() * portfolioQuantity + currentTrade.getPrice() * removeQuantity) / testPortfolio.getQuantity();
				testPortfolio.setAveragePrice(averagePrice);
			}
		}
		
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
		
		removeTrade(currentTrade.getId());
		return addTrade(updatedTrade);
	}

	@Override
	public Trade findTradeById(int id) {
		return repo.findById(id);
	}

	@Override
	public List<Trade> findAllTrade() {
		return repo.findAll();
	}

	@Override
	public double getReturns() {
		List<Portfolio> portfolioList = portfolioService.findPortfolio();
		double returns = 0.0;
		for(Portfolio p: portfolioList) {
			returns += (Constants.CURRENT_PRICE - p.getAveragePrice()) * p.getQuantity();
		}
		return returns;
	}

}
