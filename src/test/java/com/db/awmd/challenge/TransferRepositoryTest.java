package com.db.awmd.challenge;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.Mockito.when;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.repository.TransferRepository;
import com.db.awmd.challenge.service.AccountsService;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransferRepositoryTest {
	
	@Mock
	public AccountsRepository accountsRepository;
	
	@Autowired
	public AccountsService accountsService;
	
	@Autowired
	public TransferRepository transferRepository;
	
	private Account toAcct;
	private Account fromAcct;
	
	@Before
	public void prepareAccounts() {
		accountsService.getAccountsRepository().clearAccounts();
		fromAcct = new Account("2", new BigDecimal("4100"));
		toAcct = new Account("3", new BigDecimal("2000"));
		accountsService.createAccount(fromAcct);
		accountsService.createAccount(toAcct);

	}
	
	@Test
	public void transferMoney() {
		when(accountsRepository.update(fromAcct)).thenReturn(true);
		when(accountsRepository.update(toAcct)).thenReturn(true);
		Assert.assertEquals(transferRepository.transfer(fromAcct, toAcct, "500"), true);

	}

}


