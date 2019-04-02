package com.vadim.wallet;

import com.vadim.wallet.controller.WalletController;
import com.vadim.wallet.dao.PlayerEntity;
import com.vadim.wallet.dto.Balance;
import com.vadim.wallet.dto.BasicResponse;
import com.vadim.wallet.dto.PlayerRegistrationRequest;
import com.vadim.wallet.dto.PlayerRequest;
import com.vadim.wallet.exceptions.NotEnoughMoneyOnBalanceException;
import com.vadim.wallet.exceptions.PlayerAlreadyExistsException;
import com.vadim.wallet.exceptions.PlayerNotFoundException;
import com.vadim.wallet.service.WalletService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WalletControllerTests {
    @MockBean
    private WalletService walletService;
    @Autowired
    private WalletController walletController;

    private PlayerRequest playerRequest;
    private Balance balance;
    private PlayerEntity playerEntity;
    private PlayerRegistrationRequest playerRegistrationRequest;
    @Before
   public void beforeTests() {
        playerRegistrationRequest = new PlayerRegistrationRequest();
        playerRegistrationRequest.setId(1);
        playerRequest = new PlayerRequest();
        playerRequest.setAmount(0);
        playerRequest.setId(1);
        balance = new Balance(100);
        playerEntity = new PlayerEntity();
        playerEntity.setAmount(0);
        playerEntity.setId(1);
    }


    @Test
    public void testRegistrationSuccess() {
        when(walletService.registerPlayer(playerRegistrationRequest)).thenReturn(new Balance(0));
        ResponseEntity<BasicResponse> register = walletController.register(playerRegistrationRequest);
        Assert.assertEquals(HttpStatus.OK, register.getStatusCode());
        Assert.assertEquals(Balance.class,register.getBody().getResponse().getClass());
    }

    @Test
    public void testRegistrationPlayerAlreadyExists() {
        when(walletService.registerPlayer(playerRegistrationRequest)).thenThrow(new PlayerAlreadyExistsException());
        ResponseEntity<BasicResponse> register = walletController.register(playerRegistrationRequest);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, register.getStatusCode());
        PlayerAlreadyExistsException playerAlreadyExistsException = (PlayerAlreadyExistsException) register.getBody().getError();
        Assert.assertEquals(552, playerAlreadyExistsException.getErrorCode() );
    }

    @Test
    public void testGetUserBalanceSuccess(){
        when(walletService.getUserBalanceByPlayerId(Mockito.anyLong())).thenReturn(balance);
        ResponseEntity<BasicResponse> userBalance = walletController.getUserBalance(1L);
        Assert.assertEquals(HttpStatus.OK, userBalance.getStatusCode());
        Assert.assertNull(userBalance.getBody().getError());
    }

    @Test
    public void testGetUserBalancePlayerNotFoundException(){
        when(walletService.getUserBalanceByPlayerId(Mockito.anyLong())).thenThrow(new PlayerNotFoundException());
        ResponseEntity<BasicResponse> userBalance = walletController.getUserBalance(1L);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, userBalance.getStatusCode());
        Assert.assertNotNull(userBalance.getBody().getError());
    }

    @Test
    public void testDepositSuccess(){
        when(walletService.depositMoney(playerRequest)).thenReturn(balance);
        ResponseEntity<BasicResponse> deposit = walletController.deposit(playerRequest);
        Assert.assertEquals(HttpStatus.OK, deposit.getStatusCode());
        Assert.assertEquals(Balance.class,deposit.getBody().getResponse().getClass());
        Assert.assertNull(deposit.getBody().getError());
    }

    @Test
    public void testDepositPlayerNotFoundException(){
        when(walletService.depositMoney(playerRequest)).thenThrow(new PlayerNotFoundException());
        ResponseEntity<BasicResponse> userBalance = walletController.deposit(playerRequest);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, userBalance.getStatusCode());
        Assert.assertNotNull(userBalance.getBody().getError());
    }

    @Test
    public void testWithdrawSuccess(){
        when(walletService.withdrawMoney(playerRequest)).thenReturn(balance);
        ResponseEntity<BasicResponse> deposit = walletController.withdraw(playerRequest);
        Assert.assertEquals(HttpStatus.OK, deposit.getStatusCode());
        Assert.assertEquals(Balance.class,deposit.getBody().getResponse().getClass());
        Assert.assertNull(deposit.getBody().getError());
    }

    @Test
    public void testWithdrawPlayerNotFoundException() {
        when(walletService.withdrawMoney(playerRequest)).thenThrow(new PlayerNotFoundException());
        ResponseEntity<BasicResponse> userBalance = walletController.withdraw(playerRequest);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, userBalance.getStatusCode());
        Assert.assertNotNull(userBalance.getBody().getError());
    }

    @Test
    public void testWithdrawNotEnoughMoneyException() {
        when(walletService.withdrawMoney(playerRequest)).thenThrow(new NotEnoughMoneyOnBalanceException());
        ResponseEntity<BasicResponse> userBalance = walletController.withdraw(playerRequest);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, userBalance.getStatusCode());
        Assert.assertNotNull(userBalance.getBody().getError());
        Assert.assertEquals(NotEnoughMoneyOnBalanceException.class, userBalance.getBody().getError().getClass());
    }




}
