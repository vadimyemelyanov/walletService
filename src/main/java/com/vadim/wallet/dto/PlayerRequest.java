package com.vadim.wallet.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PlayerRequest {
    @Min(value = 1,message = "Min value is 1")
    private long id;
    @Min(value = 1,message = "Min value is 1")
    private long amount;
}
