package com.vadim.wallet.service;

import com.vadim.wallet.dao.PlayerEntity;
import com.vadim.wallet.dto.Balance;
import com.vadim.wallet.exceptions.NotEnoughMoneyOnBalanceException;
import com.vadim.wallet.exceptions.PlayerAlreadyExistsException;
import com.vadim.wallet.exceptions.PlayerNotFoundException;
import com.vadim.wallet.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional
public class WalletService {
    private final PlayerRepository playerRepository;

    @Autowired
    public WalletService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    public Balance registerPlayer(PlayerEntity player) {
        Optional<PlayerEntity> playerFromDataBase = playerRepository.findById(player.getId());
        if (!playerFromDataBase.isPresent()) {
            player.setAmount(0);
            return new Balance(playerRepository.saveAndFlush(player).getAmount());
        }
        else {
            log.info("Already exists {}",playerFromDataBase.get().toString());
            throw new PlayerAlreadyExistsException();
        }
    }

    public Balance getUserBalanceByPlayerId(Long id) {
        Optional<PlayerEntity> playerEntity = playerRepository.findById(id);
        if (playerEntity.isPresent()) {
            return new Balance(playerEntity.get().getAmount());
        } else {
            throw new PlayerNotFoundException();
        }
    }


    @Transactional(isolation = Isolation.READ_COMMITTED ,propagation = Propagation.SUPPORTS)
    public Balance depositMoney(PlayerEntity player) {
        Optional<PlayerEntity> playerEntity = playerRepository.findById(player.getId());
        if (playerEntity.isPresent()) {
            playerRepository.depositMoneyByPlayerId(player.getId(), playerEntity.get().getAmount() + player.getAmount());
            playerRepository.flush();
            return new Balance(playerEntity.get().getAmount() + player.getAmount());
        } else {
            throw new PlayerNotFoundException();
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED ,propagation = Propagation.SUPPORTS)
    public Balance withdrawMoney(PlayerEntity player) {
        Optional<PlayerEntity> playerEntity = playerRepository.findById(player.getId());
        if (playerEntity.isPresent()) {
            long amountAfterWithdraw = playerEntity.get().getAmount() - player.getAmount();
            if(amountAfterWithdraw > 0) {
                playerRepository.depositMoneyByPlayerId(player.getId(), playerEntity.get().getAmount() - player.getAmount());
                playerRepository.flush();
                return new Balance(amountAfterWithdraw);
            }
            else throw new NotEnoughMoneyOnBalanceException();
        } else {
            throw new PlayerNotFoundException();
        }
    }

}
