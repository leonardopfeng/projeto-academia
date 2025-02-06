package br.com.lelis.controllers;

import br.com.lelis.data.vo.SessionExerciseVO;
import br.com.lelis.services.SessionExerciseService;
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
@RequestMapping("/api/sessionExercise/v1")
@Tag(name="Session Exercises", description = "Endpoints for Managing Session Exercises")
public class SessionExerciseController {

    @Autowired
    private SessionExerciseService service;


    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(produces = {
            br.com.lelis.util.MediaType.APPLICATION_JSON,
            br.com.lelis.util.MediaType.APPLICATION_XML,
            br.com.lelis.util.MediaType.APPLICATION_YML
    }
    )
    @Operation(
            summary = "Finds all Session Exercise",
            description = "Finds all Session Exercise",
            tags = {"Session Exercises"},
            responses = {
                @ApiResponse(description = "Success", responseCode = "200",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SessionExerciseVO.class))
                            )
                        }),
                @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<PagedModel<EntityModel<SessionExerciseVO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Passes to the service the pageable object containing:
        // The number of the page; How many elements it has; How it'll be ordered
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(produces = {
            br.com.lelis.util.MediaType.APPLICATION_JSON,
            br.com.lelis.util.MediaType.APPLICATION_XML,
            br.com.lelis.util.MediaType.APPLICATION_YML
    },
            value = "/{sessionId}"
    )
    @Operation(
            summary = "Finds all Session Exercise",
            description = "Finds all Session Exercise",
            tags = {"Session Exercises"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = SessionExerciseVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<PagedModel<EntityModel<SessionExerciseVO>>> findAllBySessionId(
            @PathVariable(name = "sessionId") long sessionId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Passes to the service the pageable object containing:
        // The number of the page; How many elements it has; How it'll be ordered
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        return ResponseEntity.ok(service.findAllBySessionId(sessionId, pageable));
    }

    @CrossOrigin(origins = {"http://localhost:8080", "https://lelis.com.br"})
    @GetMapping(value = "/{sessionId}/{exerciseId}",
            produces = {
                    br.com.lelis.util.MediaType.APPLICATION_JSON,
                    br.com.lelis.util.MediaType.APPLICATION_XML,
                    br.com.lelis.util.MediaType.APPLICATION_YML
            })
    @Operation(
            summary = "Finds a Session Exercise",
            description = "Finds Session Exercise by Session Id and Exercise Id",
            tags = {"Session Exercises"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content =
                                    @Content(
                                            schema = @Schema(implementation = SessionExerciseVO.class)
                                    )
                            ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public SessionExerciseVO findBySessionAndExerciseId(
            @PathVariable(value = "sessionId") long sessionId,
            @PathVariable(value = "exerciseId") long exerciseId
    ) {
        return service.findBySessionAndExerciseId(sessionId, exerciseId);
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
            summary = "Creates a Session Exercise",
            description = "Creates a Session Exercise by passing in a JSON, XML or YML representation of Session Exercise ",
            tags = {"Session Exercises"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content =
                            @Content(
                                    schema = @Schema(implementation = SessionExerciseVO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public SessionExerciseVO create(@RequestBody SessionExerciseVO sessionExercise) {
        return service.create(sessionExercise);
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
            summary = "Updates a Session Exercise",
            description = "Updates a Session Exercise by passing in a JSON, XML or YML representation of Session Exercise ",
            tags = {"Session Exercises"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content =
                            @Content(
                                    schema = @Schema(implementation = SessionExerciseVO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public SessionExerciseVO update(@RequestBody SessionExerciseVO sessionExercise) {
        return service.update(sessionExercise);
    }

    @DeleteMapping(value = "/{sessionId}/{exerciseId}")
    @Operation(
            summary = "Deletes a Session Exercise",
            description = "Deletes a Session Exercise",
            tags = {"Session Exercises"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    // adding ResponseEntity<?> we can return the right error code
    public ResponseEntity<?> delete(
            @PathVariable(value = "sessionId") long sessionId,
            @PathVariable(value = "exerciseId") long exerciseId
    ) {
        service.delete(sessionId, exerciseId);
        // returns 204 code
        return ResponseEntity.noContent().build();
    }
}