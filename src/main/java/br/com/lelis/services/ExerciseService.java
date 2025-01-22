package br.com.lelis.services;

import br.com.lelis.controllers.ExerciseController;
import br.com.lelis.data.vo.ExerciseVO;
import br.com.lelis.exceptions.RequiredObjectIsNullException;
import br.com.lelis.exceptions.ResourceNotFoundException;
import br.com.lelis.mapper.DozerMapper;
import br.com.lelis.model.Exercise;
import br.com.lelis.model.ExerciseGroup;
import br.com.lelis.repositories.ExerciseGroupRepository;
import br.com.lelis.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Service
public class ExerciseService {

    private Logger logger = Logger.getLogger(ExerciseService.class.getName());

    @Autowired
    ExerciseRepository repository;

    @Autowired
    ExerciseGroupRepository groupRepository;

    @Autowired
    PagedResourcesAssembler<ExerciseVO> assembler;


    public PagedModel<EntityModel<ExerciseVO>> findAll(Pageable pageable) {

        logger.info("Finding all exercises!");

        // using paging to prevent performing problems
        var exercisePage = repository.findAll(pageable);
        var exerciseVosPage = exercisePage.map(p -> DozerMapper.parseObject(p, ExerciseVO.class));

        exerciseVosPage.map(
                p -> p.add(
                        linkTo(methodOn(ExerciseController.class).findById(p.getKey())).withSelfRel()
                )
        );

        Link link = linkTo(methodOn(ExerciseController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "ASC")
        ).withSelfRel();

        return assembler.toModel(exerciseVosPage, link);
    }

    public ExerciseVO findById(Long id) {

        logger.info("Finding one exercise!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        var vo = DozerMapper.parseObject(entity, ExerciseVO.class);
        vo.add(linkTo(methodOn(ExerciseController.class).findById(id)).withSelfRel());

        return vo;
    }

    public ExerciseVO create(ExerciseVO exercise) {
        if (exercise == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one person!");

        var group = groupRepository.findById(exercise.getGroup())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        Exercise entity = DozerMapper.parseObject(exercise, Exercise.class);
        entity.setGroup(group);

        ExerciseVO vo = DozerMapper.parseObject(repository.save(entity), ExerciseVO.class);
        vo.add(linkTo(methodOn(ExerciseController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }


    public ExerciseVO update(ExerciseVO exercise) {
        if (exercise == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one exercise!");

        var entity = repository.findById(exercise.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        // Since 'exercise.getGroup()' returns a 'long', and the entity.setGroup expects an ExerciseGroup,
        // it must need to retrieve it from the database using the groupRepository
        var group = groupRepository.findById(exercise.getGroup())
                        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.setGroup(group);
        entity.setName(exercise.getName());
        entity.setVideoUrl(exercise.getVideoUrl());

        var vo = DozerMapper.parseObject(repository.save(entity), ExerciseVO.class);
        vo.add(linkTo(methodOn(ExerciseController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public void delete(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }


}