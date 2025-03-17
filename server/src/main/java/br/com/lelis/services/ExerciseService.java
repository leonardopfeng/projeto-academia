package br.com.lelis.services;

import br.com.lelis.controllers.ExerciseController;
import br.com.lelis.data.vo.ExerciseGroupVO;
import br.com.lelis.data.vo.ExerciseVO;
import br.com.lelis.data.vo.GroupedExerciseVO;
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

import java.util.*;
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

        var entity = DozerMapper.parseObject(exercise, Exercise.class);

        var vo = DozerMapper.parseObject(repository.save(entity), ExerciseVO.class);
        vo.add(linkTo(methodOn(ExerciseController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }


    public ExerciseVO update(ExerciseVO exercise) {
        if (exercise == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one exercise!");

        var entity = repository.findById(exercise.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.setGroupId(exercise.getGroupId());
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

    public List<GroupedExerciseVO> findAllWithGroup(){
        logger.info("Finding all exercises grouped by group name");

        List<Object[]> results = repository.findAllWithGroup();

        Map<Long, GroupedExerciseVO> groupMap = new LinkedHashMap<>();

        for(Object[] row : results){
            long exerciseId = ((Number) row[0]).longValue();
            String exerciseName = (String) row[1];
            String exerciseUrl = (String) row[2];
            long exerciseGroupId = ((Number) row[3]).longValue();
            long groupId = ((Number) row[4]).longValue();
            String groupName = (String) row[5];

            GroupedExerciseVO groupVo = groupMap.computeIfAbsent(groupId, k -> {
                GroupedExerciseVO vo = new GroupedExerciseVO();
                vo.setId(groupId);
                vo.setName(groupName);
                vo.setExercises(new ArrayList<>());
                return vo;
            });

            ExerciseVO exerciseVO = new ExerciseVO();
            exerciseVO.setKey(exerciseId);
            exerciseVO.setName(exerciseName);
            exerciseVO.setVideoUrl(exerciseUrl);
            exerciseVO.setGroupId(exerciseGroupId);

            exerciseVO.add(linkTo(methodOn(ExerciseController.class).findById(exerciseId)).withSelfRel());

            groupVo.getExercises().add(exerciseVO);
        }


        List<GroupedExerciseVO> result = new ArrayList<>(groupMap.values());
        for(GroupedExerciseVO group : result){
            group.add(linkTo(methodOn(ExerciseController.class).findAllWithGroup()).withSelfRel());
        }

        return result;
    }

}