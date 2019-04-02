package com.vadim.wallet.repository;


import com.vadim.wallet.dao.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository  extends JpaRepository<PlayerEntity, Long> {


     @Modifying
     @Query("update PlayerEntity set amount=?2 where id = ?1")
     void updateMoneyByPlayerId(long id, long amount);


}
