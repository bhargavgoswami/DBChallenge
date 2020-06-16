package com.db.awmd.challenge;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.AccountsService;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class TransferControllerTest {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private MockMvc mockMvc;

	@Autowired
	public AccountsService accountService;

	private Account accountTo;
	private Account accountFrom;
	private String accountFromId = "2";
	private String accountToId = "3";
	
	@Before
	public void prepareMockMvc() {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
		MockitoAnnotations.initMocks(this);
		accountService.getAccountsRepository().clearAccounts();
		accountFrom = new Account(accountFromId, new BigDecimal("3500"));
		accountTo = new Account(accountToId, new BigDecimal("2000"));
		accountService.createAccount(accountFrom);
		accountService.createAccount(accountTo);
	}
	
	
	@Test
	public void tranferFund() throws Exception {

		this.mockMvc.perform(put("/v1/transfer/fromId/2/toId/3/amount/50"))
				.andExpect(status().isOk());
	}

	@Test
	public void LowBalanceCheck() throws Exception {
		this.mockMvc.perform(put("/v1/transfer/fromId/2/toId/3/amount/5000"))
				.andExpect(status().isBadRequest());
	}

}
