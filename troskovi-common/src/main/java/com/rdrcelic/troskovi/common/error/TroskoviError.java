package com.rdrcelic.troskovi.common.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TroskoviError {
    private String cause;
    private String message;
}
