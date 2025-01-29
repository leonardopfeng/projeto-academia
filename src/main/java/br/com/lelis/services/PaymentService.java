package br.com.lelis.services;

import br.com.lelis.controllers.ExerciseController;
import br.com.lelis.controllers.PaymentController;
import br.com.lelis.data.vo.ExerciseVO;
import br.com.lelis.data.vo.PaymentVO;
import br.com.lelis.exceptions.RequiredObjectIsNullException;
import br.com.lelis.exceptions.ResourceNotFoundException;
import br.com.lelis.mapper.DozerMapper;
import br.com.lelis.model.Payment;
import br.com.lelis.repositories.PaymentRepository;
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
public class PaymentService {

    private Logger logger = Logger.getLogger(PaymentService.class.getName());

    @Autowired
    PaymentRepository repository;
    @Autowired
    PagedResourcesAssembler<PaymentVO> assembler;


    public PagedModel<EntityModel<PaymentVO>> findAll(Pageable pageable) {

        logger.info("Finding all payments!");

        // using paging to prevent performing problems
        var paymentPage = repository.findAll(pageable);
        var paymentVosPage = paymentPage.map(p -> DozerMapper.parseObject(p, PaymentVO.class));

        paymentVosPage.map(
                p -> p.add(
                        linkTo(methodOn(PaymentController.class).findById(p.getKey())).withSelfRel()
                )
        );

        Link link = linkTo(methodOn(PaymentController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "ASC")
        ).withSelfRel();

        return assembler.toModel(paymentVosPage, link);
    }

    public PaymentVO findById(Long id) {

        logger.info("Finding one exercise!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        var vo = DozerMapper.parseObject(entity, PaymentVO.class);
        vo.add(linkTo(methodOn(ExerciseController.class).findById(id)).withSelfRel());

        return vo;
    }

    public PaymentVO create(PaymentVO payment) {
        if (payment == null) throw new RequiredObjectIsNullException();

        logger.info("Registering a payment!");

        var entity = DozerMapper.parseObject(payment, Payment.class);

        var vo = DozerMapper.parseObject(repository.save(entity), PaymentVO.class);
        vo.add(linkTo(methodOn(PaymentController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }


    public void delete(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }


}