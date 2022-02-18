package com.smallcase.tracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smallcase.tracker.dao.StockRepository;
import com.smallcase.tracker.model.Stock;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	StockRepository repo;
	
	@Override
	public Stock addTrade(Stock newStock) {
		return repo.save(newStock);
	}

	@Override
	public void removeTrade(int id) {
		repo.deleteById(id);
	}

	@Override
	public Stock updateTrade(Stock updatedStock) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stock findTradeBySymbol(String symbol) {
		return repo.findBySymbol(symbol);
	}

	@Override
	public List<Stock> findAllTrade() {
		return repo.findAll();
	}

}
