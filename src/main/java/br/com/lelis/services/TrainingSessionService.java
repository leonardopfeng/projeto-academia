package br.com.lelis.services;

import br.com.lelis.controllers.PersonController;
import br.com.lelis.controllers.TrainingSessionController;
import br.com.lelis.data.vo.PersonVO;
import br.com.lelis.data.vo.TrainingSessionVO;
import br.com.lelis.exceptions.RequiredObjectIsNullException;
import br.com.lelis.exceptions.ResourceNotFoundException;
import br.com.lelis.mapper.DozerMapper;
import br.com.lelis.model.TrainingSession;
import br.com.lelis.repositories.TrainingSessionRepository;
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
public class TrainingSessionService {

    private Logger logger = Logger.getLogger(TrainingSessionRepository.class.getName());

    @Autowired
    TrainingSessionRepository repository;
    
    @Autowired
    PagedResourcesAssembler<TrainingSessionVO> assembler;


    public PagedModel<EntityModel<TrainingSessionVO>> findAll(Pageable pageable) {

        logger.info("Finding all Training Sessions!");

        // using paging to prevent performing problems
        var trainingSessionPage = repository.findAll(pageable);
        var TrainingSessionVOsPage = trainingSessionPage.map(p -> DozerMapper.parseObject(p, TrainingSessionVO.class));

        TrainingSessionVOsPage.map(
                p -> p.add(
                        linkTo(methodOn(TrainingSessionController.class).findById(p.getKey())).withSelfRel()
                )
        );

        Link link = linkTo(methodOn(TrainingSessionController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "ASC")
        ).withSelfRel();

        return assembler.toModel(TrainingSessionVOsPage, link);
    }

    public PagedModel<EntityModel<TrainingSessionVO>> findAllByClientId(long clientId, Pageable pageable) {

        logger.info("Finding all training session by client id!");

        // using paging to prevent performing problems
        var trainingSessionPage = repository.findAllByClientId(clientId, pageable);
        var trainingSessionVosPage = trainingSessionPage.map(p -> DozerMapper.parseObject(p, TrainingSessionVO.class));

        trainingSessionVosPage.map(
                p -> p.add(
                        linkTo(methodOn(TrainingSessionController.class).findById(p.getKey())).withSelfRel()
                )
        );

        Link link = linkTo(methodOn(TrainingSessionController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "ASC")
        ).withSelfRel();

        return assembler.toModel(trainingSessionVosPage, link);
    }


    public TrainingSessionVO findById(Long id) {

        logger.info("Finding a Training Session!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        var vo = DozerMapper.parseObject(entity, TrainingSessionVO.class);
        vo.add(linkTo(methodOn(TrainingSessionController.class).findById(id)).withSelfRel());

        return vo;
    }

    public TrainingSessionVO create(TrainingSessionVO trainingSession) {
        if (trainingSession == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Training Session!");

        var entity = DozerMapper.parseObject(trainingSession, TrainingSession.class);

        var vo = DozerMapper.parseObject(repository.save(entity), TrainingSessionVO.class);
        vo.add(linkTo(methodOn(TrainingSessionController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }


    public TrainingSessionVO update(TrainingSessionVO trainingSession) {
        if (trainingSession == null) throw new RequiredObjectIsNullException();

        logger.info("Updating a Training Session!");

        var entity = repository.findById(trainingSession.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.setName(trainingSession.getName());
        entity.setCoachId(trainingSession.getCoachId());
        entity.setClientId(trainingSession.getClientId());
        entity.setStartDate(trainingSession.getStartDate());
        entity.setStatus(trainingSession.getStatus());

        var vo = DozerMapper.parseObject(repository.save(entity), TrainingSessionVO.class);
        vo.add(linkTo(methodOn(TrainingSessionController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public void delete(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }


}