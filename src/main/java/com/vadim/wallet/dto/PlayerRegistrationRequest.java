package com.vadim.wallet.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PlayerRegistrationRequest {

    @Min(value = 1,message = "Min value is 1")
    private long id;
}
