package com.vadim.wallet.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("error")
@JsonIgnoreProperties({"cause", "stackTrace"})
public class PlayerAlreadyExistsException extends RuntimeException {
    public int errorCode = 552;
    public String errorMessage = "Player already exists";
}
