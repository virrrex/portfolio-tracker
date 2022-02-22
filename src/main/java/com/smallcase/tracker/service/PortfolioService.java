package com.smallcase.tracker.service;

import java.util.List;

import com.smallcase.tracker.model.Portfolio;

public interface PortfolioService {
	public Portfolio updatePortfolio(Portfolio updatedPortfolio);
	public List<Portfolio> findPortfolio();
	public Portfolio findPortfolioById(int id);
	public Portfolio findPortfolioBySymbol(String symbol);
	public void removePortfolio(int id);
}
