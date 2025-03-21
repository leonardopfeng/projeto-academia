package br.com.lelis.services;

import br.com.lelis.data.vo.security.AccountCredentialsVO;
import br.com.lelis.data.vo.security.TokenVO;
import br.com.lelis.repositories.UserRepository;
import br.com.lelis.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @SuppressWarnings("rawtypes")
    public ResponseEntity signIn(AccountCredentialsVO data){
        try{
            var username = data.getUsername();
            var password = data.getPassword();

            var user = repository.findByUserName(username);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );


            var tokenResponse = new TokenVO();

            if(user != null){
                tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
            }
            else {
                throw new UsernameNotFoundException("Username " + username + " not found");
            }

            return  ResponseEntity.ok(tokenResponse);
        }
        catch (Exception e){
            throw new BadCredentialsException("Invalid username/password " + e.getMessage());
        }
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity refreshToken(String username, String refreshToken){
        var user = repository.findByUserName(username);
        var tokenResponse = new TokenVO();

        if(user != null){
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        }
        else {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }

        return  ResponseEntity.ok(tokenResponse);
    }
}

