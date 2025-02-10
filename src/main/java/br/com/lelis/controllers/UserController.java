package br.com.lelis.controllers;

import br.com.lelis.data.vo.UserVO;
import br.com.lelis.services.UserServices;
import br.com.lelis.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/v1")
@Tag(name = "Users", description = "Endpoints for Managing Users")
public class UserController {

    @Autowired
    private UserServices userService;

    @PostMapping(
            consumes = {
                    "application/json",
                    "application/xml",
                    "application/yml"
            },
            produces = {
                    "application/json",
                    "application/xml",
                    "application/yml"
            })
    @Operation(
            summary = "Creates a user",
            description = "Creates a new user by passing in a JSON, XML or YML representation of User",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = UserVO.class)
                            )),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<UserVO> create(@RequestBody UserVO userVO) {
        UserVO createdUser = userService.create(userVO);
        return ResponseEntity.ok(createdUser);
    }

}
