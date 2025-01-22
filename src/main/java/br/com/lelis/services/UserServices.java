package br.com.lelis.services;

import br.com.lelis.controllers.PersonController;
import br.com.lelis.data.vo.PersonVO;
import br.com.lelis.exceptions.RequiredObjectIsNullException;
import br.com.lelis.exceptions.ResourceNotFoundException;
import br.com.lelis.mapper.DozerMapper;
import br.com.lelis.model.Person;
import br.com.lelis.model.User;
import br.com.lelis.repositories.PersonRepository;
import br.com.lelis.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class UserServices implements UserDetailsService {

    private Logger logger = Logger.getLogger(UserServices.class.getName());

    @Autowired
    UserRepository repository;

    public UserServices(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserDetails user = repository.findByUsername(username);
        System.out.println("USER + " + user.getUsername());
        if(user != null){
            return user;
        }
        else{
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
    }
}