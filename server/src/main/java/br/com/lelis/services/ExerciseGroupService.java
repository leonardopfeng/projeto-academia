package br.com.lelis.services;

import br.com.lelis.controllers.ExerciseGroupController;
import br.com.lelis.data.vo.ExerciseGroupVO;
import br.com.lelis.exceptions.RequiredObjectIsNullException;
import br.com.lelis.exceptions.ResourceNotFoundException;
import br.com.lelis.mapper.DozerMapper;
import br.com.lelis.model.ExerciseGroup;
import br.com.lelis.repositories.ExerciseGroupRepository;
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
public class ExerciseGroupService {

    private Logger logger = Logger.getLogger(ExerciseGroupService.class.getName());

    @Autowired
    ExerciseGroupRepository repository;

    @Autowired
    PagedResourcesAssembler<ExerciseGroupVO> assembler;


    public PagedModel<EntityModel<ExerciseGroupVO>> findAll(Pageable pageable) {

        logger.info("Finding all exercise groups!");

        // using paging to prevent performing problems
        var exerciseGroupPage = repository.findAll(pageable);
        var exerciseGroupsVosPage = exerciseGroupPage.map(p -> DozerMapper.parseObject(p, ExerciseGroupVO.class));

        exerciseGroupsVosPage.map(
                p -> p.add(
                        linkTo(methodOn(ExerciseGroupController.class).findById(p.getKey())).withSelfRel()
                )
        );

        Link link = linkTo(methodOn(ExerciseGroupController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "ASC")
        ).withSelfRel();

        return assembler.toModel(exerciseGroupsVosPage, link);
    }

    public ExerciseGroupVO findById(Long id) {

        logger.info("Finding one exercise group!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        var vo = DozerMapper.parseObject(entity, ExerciseGroupVO.class);
        vo.add(linkTo(methodOn(ExerciseGroupController.class).findById(id)).withSelfRel());

        return vo;
    }

    public ExerciseGroupVO create(ExerciseGroupVO exerciseGroup) {
        if (exerciseGroup == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one exercise group!");

        var entity = DozerMapper.parseObject(exerciseGroup, ExerciseGroup.class);

        var vo = DozerMapper.parseObject(repository.save(entity), ExerciseGroupVO.class);
        vo.add(linkTo(methodOn(ExerciseGroupController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }


    public ExerciseGroupVO update(ExerciseGroupVO exerciseGroup) {
        if (exerciseGroup == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one exercise group!");

        var entity = repository.findById(exerciseGroup.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.setName(exerciseGroup.getName());

        var vo = DozerMapper.parseObject(repository.save(entity), ExerciseGroupVO.class);
        vo.add(linkTo(methodOn(ExerciseGroupController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public void delete(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }


}