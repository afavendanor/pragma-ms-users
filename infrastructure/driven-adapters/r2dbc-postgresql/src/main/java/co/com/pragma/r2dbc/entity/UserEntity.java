package co.com.pragma.r2dbc.entity;

import co.com.pragma.model.user.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@Table("users")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    private Long id;
    private String identification;
    private String name;
    @Column("last_name")
    private String lastName;
    @Column("birth_day")
    private LocalDate birthDay;
    private String address;
    private String phone;
    private String email;
    @Column("base_salary")
    private Double baseSalary;
    @Column("id_rol")
    private Long rolId;
    private String password;

    @JsonProperty("role")
    public void setRole(Role role) {
        if (role != null) {
            this.rolId = role.getId();
        }
    }

    @JsonIgnore
    public Role getRole() {
        if (rolId != null) {
            Role role = new Role();
            role.setId(rolId);
            return role;
        }
        return null;
    }
}
