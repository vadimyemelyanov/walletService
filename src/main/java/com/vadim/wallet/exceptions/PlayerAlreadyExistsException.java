package com.vadim.wallet.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;


@JsonRootName("error")
@JsonIgnoreProperties({"cause", "stackTrace"})
@Getter
public class PlayerAlreadyExistsException extends RuntimeException {
    private int errorCode = 552;
    private String errorMessage = "Player already exists";


}