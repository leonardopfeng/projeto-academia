package br.com.lelis.services;

import br.com.lelis.controllers.ClientController;
import br.com.lelis.data.vo.ClientVO;
import br.com.lelis.exceptions.RequiredObjectIsNullException;
import br.com.lelis.exceptions.ResourceNotFoundException;
import br.com.lelis.mapper.DozerMapper;
import br.com.lelis.model.Client;
import br.com.lelis.repositories.ClientRepository;
import br.com.lelis.repositories.CoachRepository;
import br.com.lelis.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class ClientService {

    private Logger logger = Logger.getLogger(ClientService.class.getName());

    @Autowired
    ClientRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CoachRepository coachRepository;
    
    @Autowired
    PagedResourcesAssembler<ClientVO> assembler;


    public PagedModel<EntityModel<ClientVO>> findAll(Pageable pageable) {

        logger.info("Finding all clients!");

        // using paging to prevent performing problems
        var clientPage = repository.findAll(pageable);
        var clientVosPage = clientPage.map(p -> DozerMapper.parseObject(p, ClientVO.class));

        clientVosPage.map(
                p -> p.add(
                        linkTo(methodOn(ClientController.class).findById(p.getUserId())).withSelfRel()
                )
        );

        Link link = linkTo(methodOn(ClientController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "ASC")
        ).withSelfRel();

        return assembler.toModel(clientVosPage, link);
    }

    public ClientVO findById(Long id) {

        logger.info("Finding one client!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        var vo = DozerMapper.parseObject(entity, ClientVO.class);
        vo.add(linkTo(methodOn(ClientController.class).findById(id)).withSelfRel());

        return vo;
    }

    public ClientVO create(ClientVO client) {
        if (client == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one client!");

        var entity = DozerMapper.parseObject(client, Client.class);

        var vo = DozerMapper.parseObject(repository.save(entity), ClientVO.class);
        vo.add(linkTo(methodOn(ClientController.class).findById(vo.getUserId())).withSelfRel());

        return vo;
    }

    public ClientVO update(ClientVO client) {
        if (client == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one client!");

        var entity = repository.findById(client.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        var user = userRepository.findById(client.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("No user found for this ID"));

        var coach = coachRepository.findById(client.getCoachId())
                .orElseThrow(() -> new ResourceNotFoundException("No user found for this ID"));

        entity.setUserId(client.getUserId());
        entity.setCoachId(client.getUserId());
        entity.setUser(user);
        entity.setCoach(coach);

        var vo = DozerMapper.parseObject(repository.save(entity), ClientVO.class);
        vo.add(linkTo(methodOn(ClientController.class).findById(vo.getUserId())).withSelfRel());

        return vo;
    }

    public void delete(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }


}