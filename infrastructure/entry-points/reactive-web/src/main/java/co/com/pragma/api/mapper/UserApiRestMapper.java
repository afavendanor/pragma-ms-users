package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.CreateUserDTO;
import co.com.pragma.api.dto.UserDTO;
import co.com.pragma.model.user.Role;
import co.com.pragma.model.user.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserApiRestMapper {

    @Mapping(target = "role", ignore = true)
    User createUserDTOToUser(CreateUserDTO createUserDTO);

    @BeforeMapping
    default void initRole(CreateUserDTO dto, @MappingTarget User user) {
        Role role = new Role();
        role.setId(dto.getRolId());
        user.setRole(role);
    }

    UserDTO userTOUserDTO(User user);

}
