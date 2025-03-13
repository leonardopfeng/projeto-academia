package br.com.lelis.services;

import br.com.lelis.controllers.UserController;
import br.com.lelis.data.vo.UserVO;
import br.com.lelis.exceptions.RequiredObjectIsNullException;
import br.com.lelis.exceptions.ResourceNotFoundException;
import br.com.lelis.mapper.DozerMapper;
import br.com.lelis.model.Permission;
import br.com.lelis.model.User;
import br.com.lelis.repositories.PermissionRepository;
import br.com.lelis.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServices implements UserDetailsService {

    private Logger logger = Logger.getLogger(UserServices.class.getName());

    @Autowired
    private UserRepository repository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    PagedResourcesAssembler<UserVO> assembler;

    public UserServices(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método para carregar o usuário pelo nome de usuário (para autenticação)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = repository.findByUserName(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
    }

    // Método para converter roles (String) em Permissions
    private List<Permission> convertRolesToPermissions(List<String> roles) {
        return roles.stream()
                .map(roleName -> permissionRepository.findByDescription(roleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission " + roleName + " not found")))
                .collect(Collectors.toList());
    }

    public UserVO create(UserVO userVO) {
        if (userVO == null) {
            throw new RequiredObjectIsNullException("UserVO");
        }

        // Verifica se o nome de usuário já existe
        if (repository.existsByUserName(userVO.getUserName())) {
            throw new ResourceNotFoundException("Username already exists");
        }

        // Codifica a senha antes de armazenar
        String encodedPassword = passwordEncoder.encode(userVO.getPassword());

        // Converte as roles em Permissions
        List<Permission> permissions = convertRolesToPermissions(userVO.getRoles());

        // Cria o objeto User com os dados do UserVO
        User user = new User();
        user.setUserName(userVO.getUserName());
        user.setPassword(encodedPassword);  // Armazena a senha criptografada
        user.setPermissions(permissions);   // Associa as permissões ao usuário
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        // Salva o usuário no banco de dados
        user = repository.save(user);


        // Retorna o UserVO com os dados do usuário criado
        return new UserVO(user.getUserName(), user.getPassword(), user.getRoles());
    }

    public PagedModel<EntityModel<UserVO>> findAll(Pageable pageable) {

        logger.info("Finding all users!");

        // using paging to prevent performing problems
        var userPage = repository.findAll(pageable);
        var userVosPage = userPage.map(p -> DozerMapper.parseObject(p, UserVO.class));

        userVosPage.map(
                p -> p.add(
                        linkTo(methodOn(UserController.class).findById(p.getKey())).withSelfRel()
                )
        );

        Link link = linkTo(methodOn(UserController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "ASC")
        ).withSelfRel();

        return assembler.toModel(userVosPage, link);
    }

    public UserVO findById(Long id) {

        logger.info("Finding one user!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        var vo = DozerMapper.parseObject(entity, UserVO.class);
        vo.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());

        return vo;
    }
}
