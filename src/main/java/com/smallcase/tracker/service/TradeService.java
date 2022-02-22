package com.smallcase.tracker.service;

import java.util.List;

import com.smallcase.tracker.model.Trade;

public interface TradeService {
	public Trade addTrade(Trade newTrade);
	public void removeTrade(int id);
	public Trade updateTrade(Trade updatedTrade, int id);
	public Trade findTradeById(int id);
	public List<Trade> findAllTrade();
	public double getReturns();
}
