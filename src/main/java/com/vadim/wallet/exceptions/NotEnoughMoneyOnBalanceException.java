package com.vadim.wallet.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;

@JsonRootName("error")
@JsonIgnoreProperties({"cause", "stackTrace"})
@Getter
public class NotEnoughMoneyOnBalanceException extends RuntimeException {
    private int errorCode = 551;
    private String errorMessage = "Not enough money";

}
