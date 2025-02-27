package br.com.lelis.services;

import br.com.lelis.controllers.CoachController;
import br.com.lelis.data.vo.CoachVO;
import br.com.lelis.exceptions.RequiredObjectIsNullException;
import br.com.lelis.exceptions.ResourceNotFoundException;
import br.com.lelis.mapper.DozerMapper;
import br.com.lelis.model.Coach;
import br.com.lelis.repositories.CoachRepository;
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
public class CoachService {

    private Logger logger = Logger.getLogger(CoachService.class.getName());

    @Autowired
    CoachRepository repository;
    
    @Autowired
    PagedResourcesAssembler<CoachVO> assembler;


    public PagedModel<EntityModel<CoachVO>> findAll(Pageable pageable) {

        logger.info("Finding all coaches!");

        // using paging to prevent performing problems
        var coachPage = repository.findAll(pageable);
        var coachVosPage = coachPage.map(p -> DozerMapper.parseObject(p, CoachVO.class));

        coachVosPage.map(
                p -> p.add(
                        linkTo(methodOn(CoachController.class).findById(p.getKey())).withSelfRel()
                )
        );

        Link link = linkTo(methodOn(CoachController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "ASC")
        ).withSelfRel();

        return assembler.toModel(coachVosPage, link);
    }

    public CoachVO findById(Long id) {

        logger.info("Finding one coach!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        var vo = DozerMapper.parseObject(entity, CoachVO.class);
        vo.add(linkTo(methodOn(CoachController.class).findById(id)).withSelfRel());

        return vo;
    }

    public CoachVO create(CoachVO coach) {
        if (coach == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one coach!");

        var entity = DozerMapper.parseObject(coach, Coach.class);

        var vo = DozerMapper.parseObject(repository.save(entity), CoachVO.class);
        vo.add(linkTo(methodOn(CoachController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public CoachVO update(CoachVO coach) {
        if (coach == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one coach!");

        var entity = repository.findById(coach.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.setCertification(coach.getCertification());
        entity.setHiredDate(coach.getHiredDate());

        var vo = DozerMapper.parseObject(repository.save(entity), CoachVO.class);
        vo.add(linkTo(methodOn(CoachController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public void delete(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }


}