package com.vadim.wallet.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@JsonRootName("response")
public class BasicResponse {

    Object response;
    Throwable error;
}
