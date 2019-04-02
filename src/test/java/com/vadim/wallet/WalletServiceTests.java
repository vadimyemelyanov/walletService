package com.vadim.wallet;

import com.vadim.wallet.dao.PlayerEntity;
import com.vadim.wallet.dto.Balance;
import com.vadim.wallet.dto.PlayerRequest;
import com.vadim.wallet.exceptions.NotEnoughMoneyOnBalanceException;
import com.vadim.wallet.exceptions.PlayerAlreadyExistsException;
import com.vadim.wallet.exceptions.PlayerNotFoundException;
import com.vadim.wallet.repository.PlayerRepository;
import com.vadim.wallet.service.WalletService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WalletServiceTests {
    @MockBean
    private PlayerRepository playerRepository;
    @Autowired
    private WalletService walletService;
    private PlayerEntity playerEntity;
    private PlayerRequest playerRequest;

    @Before
    public  void initTests() {
        playerRequest = new PlayerRequest();
        playerEntity = new PlayerEntity();
        playerEntity.setId(1);
        playerEntity.setAmount(0);
        playerRequest.setId(1);
        playerRequest.setAmount(0);
        when(playerRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.ofNullable(playerEntity));
        when(playerRepository.saveAndFlush(playerEntity)).thenReturn(playerEntity);
    }


    @Test
    public void testRegisterPlayerSuccess() {
        when(playerRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Balance balance = walletService.registerPlayer(playerRequest);
        Assert.assertEquals(0, balance.getAmount());
    }

    @Test(expected = PlayerAlreadyExistsException.class)
    public void testRegisterPlayerAlreadyExists() {
        walletService.registerPlayer(playerRequest);
    }

    @Test
    public void testGetUserBalanceByPlayerIdSuccess() {
        Balance userBalanceByPlayerId = walletService.getUserBalanceByPlayerId(1L);
        Assert.assertNotNull(userBalanceByPlayerId);
        Assert.assertEquals(playerEntity.getAmount(), userBalanceByPlayerId.getAmount());
    }

    @Test(expected = PlayerNotFoundException.class)
    public void testGetUserBalanceByPlayerIdPlayerNotFound() {
        when(playerRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        walletService.getUserBalanceByPlayerId(1L);
    }

    @Test
    public void testDepositMoneySuccess() {
        playerRequest.setAmount(150);
        playerEntity.setAmount(111);
        when(playerRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.ofNullable(playerEntity));
        Balance balance = walletService.depositMoney(playerRequest);
        Assert.assertEquals(261, balance.getAmount());
    }

    @Test(expected = PlayerNotFoundException.class)
    public void testDepositPlayerNotFound() {
        when(playerRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        walletService.depositMoney(playerRequest);
    }

    @Test(expected = PlayerNotFoundException.class)
    public void testWithdrawPlayerNotFound() {
        when(playerRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        walletService.withdrawMoney(playerRequest);
    }

    @Test(expected = NotEnoughMoneyOnBalanceException.class)
    public void testWithdrawMoneyNotEnoughMoney() {
        playerRequest.setAmount(150);
        playerEntity.setAmount(111);
        when(playerRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.ofNullable(playerEntity));
        walletService.withdrawMoney(playerRequest);
    }

    @Test
    public void testWithdrawMoneySuccess() {
        playerRequest.setAmount(50);
        playerEntity.setAmount(111);
        when(playerRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.ofNullable(playerEntity));
        Balance balance = walletService.withdrawMoney(playerRequest);
        Assert.assertEquals(61, balance.getAmount());
    }


}
