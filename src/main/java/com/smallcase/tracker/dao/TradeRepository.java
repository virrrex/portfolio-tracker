package com.smallcase.tracker.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smallcase.tracker.model.Stock;

@Repository
@Transactional
public interface StockRepository extends CrudRepository<Stock, Integer> {
	List<Stock> findAll();
	Stock findBySymbol(String symbol);
}
