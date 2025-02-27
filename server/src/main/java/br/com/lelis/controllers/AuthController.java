package br.com.lelis.controllers;

import br.com.lelis.data.vo.security.AccountCredentialsVO;
import br.com.lelis.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthServices authServices;

    private boolean checkIfParamsIsNotNull(@RequestBody AccountCredentialsVO data){
        return  data == null || data.getUsername() == null || data.getUsername().isBlank() || data.getPassword() == null || data.getPassword().isBlank();
    }

    private boolean checkIfRefreshTokenIsNotNull(String username, String refreshToken){
        return  refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank();
    }

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Authenticates an user and returns a token")
    @PostMapping(value = "/signin")
    public ResponseEntity signin(@RequestBody AccountCredentialsVO data){
        if(checkIfParamsIsNotNull(data)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }

        var token = authServices.signIn(data);

        if(token == null){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }

        return token;
    }

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Refresh token for an authenticated user and returns a token")
    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity refresh(@PathVariable("username") String username,
                                  @RequestHeader("Authorization") String refreshToken){
        if(checkIfRefreshTokenIsNotNull(username, refreshToken)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }

        var token = authServices.refreshToken(username, refreshToken);

        if(token == null){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }

        return token;
    }
}
