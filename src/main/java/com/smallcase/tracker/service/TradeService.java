package com.smallcase.tracker.service;

import java.util.List;

import com.smallcase.tracker.model.Stock;

public interface StockService {
	public Stock addTrade(Stock newStock);
	public void removeTrade(int id);
	public Stock updateTrade(Stock updatedStock);
	public Stock findTradeBySymbol(String symbol);
	public List<Stock> findAllTrade();
}
