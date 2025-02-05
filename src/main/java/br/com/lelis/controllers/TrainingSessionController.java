package br.com.lelis.controllers;

import br.com.lelis.data.vo.PersonVO;
import br.com.lelis.data.vo.TrainingSessionVO;
import br.com.lelis.services.TrainingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainingSession/v1")
@Tag(name="Training Sessions", description = "Endpoints for Managing Training Sessions")
public class TrainingSessionController {

    @Autowired
    private TrainingSessionService service;


    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(produces = {
            br.com.lelis.util.MediaType.APPLICATION_JSON,
            br.com.lelis.util.MediaType.APPLICATION_XML,
            br.com.lelis.util.MediaType.APPLICATION_YML
    }
    )
    @Operation(
            summary = "Finds all Training Session",
            description = "Finds all Training Session",
            tags = {"Training Sessions"},
            responses = {
                @ApiResponse(description = "Success", responseCode = "200",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TrainingSessionVO.class))
                            )
                        }),
                @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<PagedModel<EntityModel<TrainingSessionVO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Passes to the service the pageable object containing:
        // The number of the page; How many elements it has; How it'll be ordered
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(
            value = "/findAllByClientId/{clientId}",
            produces = {
                    br.com.lelis.util.MediaType.APPLICATION_JSON,
                    br.com.lelis.util.MediaType.APPLICATION_XML,
                    br.com.lelis.util.MediaType.APPLICATION_YML
            }
    )
    @Operation(
            summary = "Finds all Training Sessions by Client Id",
            description = "Finds all Training Sessions by Client Id",
            tags = {"Training Sessions"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = TrainingSessionVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<PagedModel<EntityModel<TrainingSessionVO>>> findAllByClientId(
            @PathVariable(value = "clientId") long clientId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Passes to the service the pageable object containing:
        // The number of the page; How many elements it has; How it'll be ordered
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok(service.findAllByClientId(clientId ,pageable));
    }

    @CrossOrigin(origins = {"http://localhost:8080", "https://lelis.com.br"})
    @GetMapping(value = "/{id}",
            produces = {
                    br.com.lelis.util.MediaType.APPLICATION_JSON,
                    br.com.lelis.util.MediaType.APPLICATION_XML,
                    br.com.lelis.util.MediaType.APPLICATION_YML
            })
    @Operation(
            summary = "Finds a Training Session",
            description = "Finds Training Session by ID",
            tags = {"Training Sessions"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content =
                                    @Content(
                                            schema = @Schema(implementation = TrainingSessionVO.class)
                                    )
                            ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public TrainingSessionVO findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PostMapping(
            consumes = {
                    br.com.lelis.util.MediaType.APPLICATION_JSON,
                    br.com.lelis.util.MediaType.APPLICATION_XML,
                    br.com.lelis.util.MediaType.APPLICATION_YML
            },
            produces = {
                    br.com.lelis.util.MediaType.APPLICATION_JSON,
                    br.com.lelis.util.MediaType.APPLICATION_XML,
                    br.com.lelis.util.MediaType.APPLICATION_YML
            })
    @Operation(
            summary = "Creates a Training Session",
            description = "Creates a Training Session by passing in a JSON, XML or YML representation of Training Session ",
            tags = {"Training Sessions"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content =
                            @Content(
                                    schema = @Schema(implementation = TrainingSessionVO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public TrainingSessionVO create(@RequestBody TrainingSessionVO trainingSession) {
        return service.create(trainingSession);
    }

    @PutMapping(
            consumes = {
                    br.com.lelis.util.MediaType.APPLICATION_JSON,
                    br.com.lelis.util.MediaType.APPLICATION_XML,
                    br.com.lelis.util.MediaType.APPLICATION_YML
            },
            produces = {
                    br.com.lelis.util.MediaType.APPLICATION_JSON,
                    br.com.lelis.util.MediaType.APPLICATION_XML,
                    br.com.lelis.util.MediaType.APPLICATION_YML
            })
    @Operation(
            summary = "Updates a Training Session",
            description = "Updates a Training Session by passing in a JSON, XML or YML representation of Training Session ",
            tags = {"Training Sessions"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content =
                            @Content(
                                    schema = @Schema(implementation = TrainingSessionVO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public TrainingSessionVO update(@RequestBody TrainingSessionVO trainingSession) {
        return service.update(trainingSession);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Deletes a Training Session",
            description = "Deletes a Training Session",
            tags = {"Training Sessions"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    // adding ResponseEntity<?> we can return the right error code
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        // returns 204 code
        return ResponseEntity.noContent().build();
    }
}