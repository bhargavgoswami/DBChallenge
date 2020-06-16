package com.db.awmd.challenge.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.TransferAmountException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.EmailNotificationService;
import com.db.awmd.challenge.service.NotificationService;
import com.db.awmd.challenge.service.TransferService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/transfer")
@Slf4j
public class TransferController {
	
	private final TransferService transactionService;
	private final EmailNotificationService notificationService;
	private final AccountsService accountsService;
	
	@Autowired
	public TransferController(TransferService transactionService,EmailNotificationService notificationService,AccountsService accountsService) {
		this.transactionService = transactionService;
		this.notificationService = notificationService;
		this.accountsService = accountsService;
	}
	
	@PutMapping(path = "/fromId/{fromId}/toId/{toId}/amount/{amount}")
	  public ResponseEntity<Object> TransferAmount(@PathVariable("fromId") String fromId,@PathVariable("toId") String toId,@PathVariable("amount") String amount) {
	    try{
			if(Integer.parseInt(amount) < 0)  
				throw new TransferAmountException("The amount cannot be negative;Overdraft not supported");
			Account fromAcct = this.accountsService.getAccount(fromId);
			Account toAcct = this.accountsService.getAccount(toId);	
			
			if (fromAcct == null || toAcct == null) {
				throw new TransferAmountException("Account does not exist");
			}
			
			if(this.transactionService.transfer(fromAcct,toAcct,amount) == "success"){
				this.notificationService.notifyAboutTransfer(fromAcct,  "Debitted with amount "+amount);
				this.notificationService.notifyAboutTransfer(toAcct , "Creditted with amount "+amount);
			}
	    } catch (TransferAmountException ex) {
			log.debug("Error while  transfering money:{}",ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	  }

}
