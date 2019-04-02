package com.vadim.wallet.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PlayerRequest {
    @Min(1)
    private long id;
    @Min(1)
    private long amount;
}
