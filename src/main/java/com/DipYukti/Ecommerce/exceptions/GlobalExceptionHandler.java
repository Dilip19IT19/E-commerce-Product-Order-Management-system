package com.DipYukti.Ecommerce.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex)
    {
        CustomError customError=new CustomError("Entity not found : "+ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex)
    {
        CustomError customError=new CustomError("Username/email not found : "+ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex)
    {
        CustomError customError=new CustomError("Authentication failed : "+ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex)
    {
        CustomError customError=new CustomError("Invalid arguments are passed: "+ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex)
    {
        CustomError customError=new CustomError("Access is denied : "+ex.getMessage(), HttpStatus.FORBIDDEN);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
        CustomError customError=new CustomError("Bean validation failed (@Valid etc.):  "+ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex)
    {
        CustomError customError=new CustomError("Validation constraint violated:  "+ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex)
    {
        CustomError customError=new CustomError("JWT token is expired:  "+ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException ex)
    {
        CustomError customError=new CustomError("Corrupted or tampered token:  "+ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handleSignatureException(SignatureException ex)
    {
        CustomError customError=new CustomError("Token signature doesn't match with secret:  "+ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleJwtException(JwtException ex)
    {
        CustomError customError=new CustomError("General JWT parsing/validation error:  "+ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex)
    {
        CustomError customError=new CustomError("Required request parameter missing:  "+ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserExists(UserAlreadyExistsException ex) {
        CustomError customError=new CustomError("User already exists: "+ex.getMessage(), HttpStatus.CONFLICT);
        return  ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidToken(InvalidTokenException ex) {
        CustomError customError=new CustomError("User already exists: "+ex.getMessage(), HttpStatus.CONFLICT);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobal(Exception ex) {
        CustomError customError=new CustomError("Internal server error has occurred :  "+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(customError.getHttpStatus()).body(customError);
    }

}
