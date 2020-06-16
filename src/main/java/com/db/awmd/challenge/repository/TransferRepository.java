package com.db.awmd.challenge.repository;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.TransferAmountException;

@Repository
public class TransferRepository {
	
	@Autowired
	public AccountsRepository accountsRepository;
	
	//@Transactional
	  public synchronized String transfer(Account fromAcct,Account toAcct,String amount) {
		  	BigDecimal  fromBalance  = fromAcct.getBalance();
			BigDecimal  toBalance  = toAcct.getBalance();
			fromBalance = fromBalance.subtract(new BigDecimal(Integer.parseInt(amount))) ;
			if(fromBalance.signum() == -1)
				 throw new TransferAmountException("There is no sufficient balance;Overdraft not supported");
			toBalance = toBalance.add(new BigDecimal(Integer.parseInt(amount))) ;
			
			fromAcct.setBalance(fromBalance);
			toAcct.setBalance(toBalance);
			accountsRepository.update(fromAcct);
			accountsRepository.update(toAcct);
			
		    return "success";
		  }

}
