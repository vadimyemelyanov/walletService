package com.vadim.wallet.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;


@JsonRootName("error")
@JsonIgnoreProperties({"cause", "stackTrace"})
@Getter
public class PlayerNotFoundException extends RuntimeException {
    private int errorCode = 553;
    private String errorMessage = "Player not found";
}
