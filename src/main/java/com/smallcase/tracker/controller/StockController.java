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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smallcase.tracker.model.Stock;
import com.smallcase.tracker.service.StockService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class StockController {
	
	@Autowired
	StockService service;
	
	@GetMapping("/trades")
	@ApiOperation(value = "Finds all the trades",
	notes = "Finds all the trades present in the portfolio",
	response = Stock.class)
	public ResponseEntity<List<Stock>> getAllTrades(){
		List<Stock> trades = service.findAllTrade();
		return new ResponseEntity<List<Stock>>(trades, HttpStatus.OK);
	}
	
	@PostMapping("/trades")
	@ApiOperation(value = "Adds new trade",
	notes = "Provide trade attribute to add new trade in the portfolio",
	response = Stock.class)
	public ResponseEntity<Stock> addTrade(@ApiParam(value = "Trade attributes to be added as new trade.", required = true) 
	@RequestBody Stock toBeAdded){
		try {
			Stock created = service.addTrade(toBeAdded);
			HttpHeaders headers  = new HttpHeaders();
			headers.setLocation(URI.create("/trades/" + created.getId()));
			return new ResponseEntity<Stock>(created, headers, HttpStatus.CREATED);
		}
		catch(IllegalArgumentException e){
			return new ResponseEntity<Stock>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/trades/{symbol}")
	@ApiOperation(value = "Deletes trade by ticker symbol",
	notes = "Provide a ticker symbol to delete specific trade from the portfolio",
	response = Stock.class)
	public ResponseEntity<Stock> deleteTrade(@ApiParam(value = "ticker symbol value for the trade which needs to be deleted.", required = true)
	@PathVariable("symbol") String symbol){
		Stock testStock = service.findTradeBySymbol(symbol);
		if(testStock != null) {
			service.removeTrade(testStock.getId());
			return new ResponseEntity<Stock>(testStock, HttpStatus.OK);
		}
		else 
			return new ResponseEntity<Stock>(HttpStatus.NOT_FOUND);
	}
}
