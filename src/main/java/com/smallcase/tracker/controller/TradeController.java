package com.smallcase.tracker.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smallcase.tracker.model.Portfolio;
import com.smallcase.tracker.model.Trade;
import com.smallcase.tracker.service.PortfolioService;
import com.smallcase.tracker.service.TradeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class TradeController {
	
	@Autowired
	TradeService service;
	
	@Autowired
	PortfolioService portfolioService;
	
	@GetMapping("/trades")
	@ApiOperation(value = "Finds all the trades",
	notes = "Finds all the trades executed in the portfolio",
	response = Trade.class)
	public ResponseEntity<List<Trade>> getAllTrades(){
		List<Trade> trades = service.findAllTrade();
		return new ResponseEntity<List<Trade>>(trades, HttpStatus.OK);
	}
	
	@PostMapping("/trades")
	@ApiOperation(value = "Adds new trade",
	notes = "Provide trade attribute to add new trade in the portfolio",
	response = Trade.class)
	public ResponseEntity<Trade> addTrade(@ApiParam(value = "Trade attributes to be added as new trade.", required = true) 
	@RequestBody Trade toBeAdded){
		try {
			Trade created = service.addTrade(toBeAdded);
			HttpHeaders headers  = new HttpHeaders();
			headers.setLocation(URI.create("/trades/" + created.getId()));
			return new ResponseEntity<Trade>(created, headers, HttpStatus.CREATED);
		}
		catch(IllegalArgumentException e){
			return new ResponseEntity<Trade>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/trades/{id}")
	@ApiOperation(value = "Deletes trade by trade id",
	notes = "Provide a trade id to delete specific trade from the portfolio",
	response = Trade.class)
	public ResponseEntity<Trade> deleteTrade(@ApiParam(value = "trade id for the trade which needs to be deleted.", required = true)
	@PathVariable("id") int id){
		Trade testStock = service.findTradeById(id);
		if(testStock != null) {
			service.removeTrade(testStock.getId());
			return new ResponseEntity<Trade>(testStock, HttpStatus.OK);
		}
		else 
			return new ResponseEntity<Trade>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/portfolio")
	public ResponseEntity<List<Portfolio>> getPortfolio() {
		List<Portfolio> portfolio = portfolioService.findPortfolio();
		return new ResponseEntity<List<Portfolio>>(portfolio, HttpStatus.OK);
	}
	
	@GetMapping("/returns")
	public ResponseEntity<Integer> getReturns(){
		int returnValue = 0;
		return new ResponseEntity<Integer>(returnValue, HttpStatus.OK);
	}
	
	@PutMapping("/trades/{id}")
	@ApiOperation(value = "Updates trade by id",
	notes = "Provide an id and trade attributes to update existing trade",
	response = Trade.class)
	public ResponseEntity<Trade> updateTrade(@ApiParam(value = "ID value and trade attributes for the existing trade you need to update", required = true)
										@RequestBody Trade toBeAdded, @PathVariable("id") int id){
		try {
			HttpHeaders headers =  new HttpHeaders();
			Trade added = service.updateTrade(toBeAdded, id);
			headers.setLocation(URI.create("/trades/"+id));
			return new ResponseEntity<Trade>(added, headers, HttpStatus.ACCEPTED);
		}
		catch(IllegalArgumentException e) {
			return new ResponseEntity<Trade>(HttpStatus.BAD_REQUEST);
		}
	}
}
