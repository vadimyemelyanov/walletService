package com.vadim.wallet.controller;

import com.vadim.wallet.dto.Balance;
import com.vadim.wallet.dto.BasicResponse;
import com.vadim.wallet.dto.PlayerRequest;
import com.vadim.wallet.exceptions.NotEnoughMoneyOnBalanceException;
import com.vadim.wallet.exceptions.PlayerAlreadyExistsException;
import com.vadim.wallet.exceptions.PlayerNotFoundException;
import com.vadim.wallet.service.WalletService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/wallet")
@Slf4j
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;


    @PostMapping("/registration")
    public ResponseEntity<BasicResponse> register(@Valid @RequestBody PlayerRequest player) {
        Balance balance = null;
        log.info("Registration Request {}", player);
        try {
            balance = walletService.registerPlayer(player);
            return ResponseEntity.ok().body(new BasicResponse(balance, null));
        } catch (PlayerAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(new BasicResponse(balance, e));
        }
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<BasicResponse> getUserBalance(@PathVariable Long id) {
        try {
            Balance balance = walletService.getUserBalanceByPlayerId(id);
            return ResponseEntity.ok().body(new BasicResponse(balance, null));
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.badRequest().body(new BasicResponse(null, e));
        }
    }

    @PutMapping("/deposit")
    public ResponseEntity<BasicResponse> deposit(@Valid @RequestBody PlayerRequest player) {
        Balance balance = null;
        log.info("Deposit Request {}", player);
        try {
            balance = walletService.depositMoney(player);
            return ResponseEntity.ok().body(new BasicResponse(balance, null));
        } catch (PlayerNotFoundException e) {
            log.info("Player {} already exists", player);
            return ResponseEntity.badRequest().body(new BasicResponse(balance, e));
        }
    }

    @PutMapping("/withdraw")
    public ResponseEntity<BasicResponse> withdraw(@Valid @RequestBody PlayerRequest player) {
        Balance balance = null;
        log.info("Withdraw Request {}", player);
        try {
            balance = walletService.withdrawMoney(player);
            return ResponseEntity.ok().body(new BasicResponse(balance, null));
        } catch (NotEnoughMoneyOnBalanceException e) {
            log.info("Not enough money ", player);
            return ResponseEntity.badRequest().body(new BasicResponse(balance, e));
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.badRequest().body(new BasicResponse(balance, e));
        }
    }


}
