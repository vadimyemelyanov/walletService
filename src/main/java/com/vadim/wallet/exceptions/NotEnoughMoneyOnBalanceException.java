package com.vadim.wallet.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("error")
@JsonIgnoreProperties({"cause", "stackTrace"})
public class NotEnoughMoneyOnBalanceException extends RuntimeException {
    public int errorCode = 551;
    public String errorMessage = "Not enough money";

}
