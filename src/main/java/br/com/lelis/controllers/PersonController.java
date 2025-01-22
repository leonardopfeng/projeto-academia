package br.com.lelis.controllers;

import java.util.List;

import br.com.lelis.data.vo.PersonVO;
import br.com.lelis.services.PersonServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person/v1")
@Tag(name="People", description = "Endpoints for Managing People")
public class PersonController {

    @Autowired
    private PersonServices service;


    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(produces = {
            br.com.lelis.util.MediaType.APPLICATION_JSON,
            br.com.lelis.util.MediaType.APPLICATION_XML,
            br.com.lelis.util.MediaType.APPLICATION_YML
    }
    )
    @Operation(
            summary = "Finds all people",
            description = "Finds all people",
            tags = {"People"},
            responses = {
                @ApiResponse(description = "Success", responseCode = "200",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                            )
                        }),
                @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Passes to the service the pageable object containing:
        // The number of the page; How many elements it has; How it'll be ordered
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(
            value = "/findPersonsByName/{firstName}",
            produces = {
            br.com.lelis.util.MediaType.APPLICATION_JSON,
            br.com.lelis.util.MediaType.APPLICATION_XML,
            br.com.lelis.util.MediaType.APPLICATION_YML
    }
    )
    @Operation(
            summary = "Finds a group of Person by name",
            description = "Finds a group of Person by name",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findPersonsByName(
            @PathVariable(value = "firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Passes to the service the pageable object containing:
        // The number of the page; How many elements it has; How it'll be ordered
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(service.findPersonsByName(firstName ,pageable));
    }

    @CrossOrigin(origins = {"http://localhost:8080", "https://lelis.com.br"})
    @GetMapping(value = "/{id}",
            produces = {
                    br.com.lelis.util.MediaType.APPLICATION_JSON,
                    br.com.lelis.util.MediaType.APPLICATION_XML,
                    br.com.lelis.util.MediaType.APPLICATION_YML
            })
    @Operation(
            summary = "Finds a person",
            description = "Finds person by ID",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content =
                                    @Content(
                                            schema = @Schema(implementation = PersonVO.class)
                                    )
                            ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public PersonVO findById(@PathVariable(value = "id") Long id) {
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
            summary = "Creates a person",
            description = "Creates a person by passing in a JSON, XML or YML representation of People ",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content =
                            @Content(
                                    schema = @Schema(implementation = PersonVO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public PersonVO create(@RequestBody PersonVO person) {
        return service.create(person);
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
            summary = "Updates a person",
            description = "Updates a person by passing in a JSON, XML or YML representation of People ",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content =
                            @Content(
                                    schema = @Schema(implementation = PersonVO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public PersonVO update(@RequestBody PersonVO person) {
        return service.update(person);
    }



    @PatchMapping(value = "/{id}",
            produces = {
                    br.com.lelis.util.MediaType.APPLICATION_JSON,
                    br.com.lelis.util.MediaType.APPLICATION_XML,
                    br.com.lelis.util.MediaType.APPLICATION_YML
            })
    @Operation(
            summary = "Disables a Person",
            description = "Disabling a specific Person by ID",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content =
                            @Content(
                                    schema = @Schema(implementation = PersonVO.class)
                            )
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public PersonVO disablePerson(@PathVariable(value = "id") Long id) {
        return service.disablePerson(id);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Deletes a person",
            description = "Updates a person by passing in a JSON, XML or YML representation of People ",
            tags = {"People"},
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