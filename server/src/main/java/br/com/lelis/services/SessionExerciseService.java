package br.com.lelis.services;

import br.com.lelis.controllers.SessionExerciseController;
import br.com.lelis.data.vo.SessionExerciseVO;
import br.com.lelis.exceptions.RequiredObjectIsNullException;
import br.com.lelis.exceptions.ResourceNotFoundException;
import br.com.lelis.mapper.DozerMapper;
import br.com.lelis.model.SessionExercise;
import br.com.lelis.model.SessionExerciseId;
import br.com.lelis.repositories.SessionExerciseRepository;
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
public class SessionExerciseService {

    private Logger logger = Logger.getLogger(SessionExerciseRepository.class.getName());

    @Autowired
    SessionExerciseRepository repository;

    @Autowired
    PagedResourcesAssembler<SessionExerciseVO> assembler;

    public PagedModel<EntityModel<SessionExerciseVO>> findAll(Pageable pageable) {
        logger.info("Finding all Session Exercises!");

        var sessionExercisePage = repository.findAll(pageable);
        var SessionExerciseVOsPage = sessionExercisePage.map(p -> DozerMapper.parseObject(p, SessionExerciseVO.class));

        SessionExerciseVOsPage.map(
                p -> p.add(
                        linkTo(methodOn(SessionExerciseController.class).findBySessionAndExerciseId(p.getSessionId(), p.getExerciseId())).withSelfRel()
                )
        );

        Link link = linkTo(methodOn(SessionExerciseController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "ASC")
        ).withSelfRel();

        return assembler.toModel(SessionExerciseVOsPage, link);
    }

    public PagedModel<EntityModel<SessionExerciseVO>> findAllBySessionId(long sessionId, Pageable pageable) {
        logger.info("Finding all Exercises from a Training Session!");

        var sessionExercisePage = repository.findAllBySessionId(sessionId, pageable);
        var SessionExerciseVOsPage = sessionExercisePage.map(p -> DozerMapper.parseObject(p, SessionExerciseVO.class));

        SessionExerciseVOsPage.map(
                p -> p.add(
                        linkTo(methodOn(SessionExerciseController.class).findBySessionAndExerciseId(p.getSessionId(), p.getExerciseId())).withSelfRel()
                )
        );

        Link link = linkTo(methodOn(SessionExerciseController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "ASC")
        ).withSelfRel();

        return assembler.toModel(SessionExerciseVOsPage, link);
    }

    public SessionExerciseVO findBySessionAndExerciseId(long sessionId, long exerciseId) {
        logger.info("Finding a Session Exercise!");

        var id = new SessionExerciseId(sessionId, exerciseId);
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SessionExercise not found!"));

        var vo = DozerMapper.parseObject(entity, SessionExerciseVO.class);
        vo.add(linkTo(methodOn(SessionExerciseController.class).findBySessionAndExerciseId(sessionId, exerciseId)).withSelfRel());

        return vo;
    }

    public SessionExerciseVO create(SessionExerciseVO sessionExercise) {
        if (sessionExercise == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Session Exercise!");

        var entity = DozerMapper.parseObject(sessionExercise, SessionExercise.class);
        entity.setId(new SessionExerciseId(sessionExercise.getSessionId(), sessionExercise.getExerciseId()));

        var vo = DozerMapper.parseObject(repository.save(entity), SessionExerciseVO.class);
        vo.add(linkTo(methodOn(SessionExerciseController.class).findBySessionAndExerciseId(vo.getSessionId(), vo.getExerciseId())).withSelfRel());

        return vo;
    }

    public SessionExerciseVO update(SessionExerciseVO sessionExercise) {
        if (sessionExercise == null) throw new RequiredObjectIsNullException();

        logger.info("Updating a Session Exercise!");


        var id = new SessionExerciseId(sessionExercise.getSessionId(), sessionExercise.getExerciseId());
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SessionExercise not found!"));

        entity.setSequence(sessionExercise.getSequence());
        entity.setSets(sessionExercise.getSets());
        entity.setReps(sessionExercise.getReps());
        entity.setWeight(sessionExercise.getWeight());

        var vo = DozerMapper.parseObject(repository.save(entity), SessionExerciseVO.class);
        vo.add(linkTo(methodOn(SessionExerciseController.class).findBySessionAndExerciseId(vo.getSessionId(), vo.getExerciseId())).withSelfRel());

        return vo;
    }

    public void delete(long sessionId, long exerciseId) {
        logger.info("Deleting a Session Exercise!");

        var id = new SessionExerciseId(sessionId, exerciseId);
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SessionExercise not found!"));

        repository.delete(entity);
    }
}
