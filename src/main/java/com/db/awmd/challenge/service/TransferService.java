package com.db.awmd.challenge.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.TransferRepository;

import lombok.Getter;
@Service
public class TransferService {
	
	 @Getter
	 private final TransferRepository transferRepository;
	 
	@Autowired
	public TransferService(TransferRepository transferRepository) {
		this.transferRepository = transferRepository;
	}

    public String transfer(Account fromAcct,Account toAcct,String amount) {
			
			return transferRepository.transfer(fromAcct, toAcct, amount);
			
	}
}
