package com.smallcase.tracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smallcase.tracker.dao.PortfolioRepository;
import com.smallcase.tracker.model.Portfolio;

@Service
public class PortfolioServiceImpl implements PortfolioService {

	@Autowired
	PortfolioRepository repo;
	
	@Override
	public Portfolio updatePortfolio(Portfolio updatedPortfolio) {
		return repo.save(updatedPortfolio);
	}

	@Override
	public List<Portfolio> findPortfolio() {
		return repo.findAll();
	}

	@Override
	public Portfolio findPortfolioById(int id) {
		return repo.findById(id);
	}

	@Override
	public Portfolio findPortfolioBySymbol(String symbol) {
		return repo.findBySymbol(symbol);
	}

	@Override
	public void removePortfolio(int id) {
		repo.deleteById(id);
	}

}
