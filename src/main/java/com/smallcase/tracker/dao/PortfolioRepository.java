package com.smallcase.tracker.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smallcase.tracker.model.Portfolio;


@Repository
@Transactional
public interface PortfolioRepository extends CrudRepository<Portfolio, Integer> {
	List<Portfolio> findAll();
	Portfolio findById(int id);
	Portfolio findBySymbol(String symbol);
}
