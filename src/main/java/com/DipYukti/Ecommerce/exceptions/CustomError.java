package com.DipYukti.Ecommerce.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class CustomError
{
    private LocalDateTime timestamp;
    private String message;
    private HttpStatus httpStatus;

    public CustomError()
    {
        this.timestamp=LocalDateTime.now();
    }
    public CustomError(String message, HttpStatus httpStatus)
    {
        this();
        this.message=message;
        this.httpStatus=httpStatus;
    }
}
