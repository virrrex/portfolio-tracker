package com.smallcase.tracker.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smallcase.tracker.model.Trade;

@Repository
@Transactional
public interface TradeRepository extends CrudRepository<Trade, Integer> {
	List<Trade> findAll();
	Trade findById(int id);
}
