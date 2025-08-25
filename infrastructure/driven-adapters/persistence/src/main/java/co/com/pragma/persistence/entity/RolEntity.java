package co.com.pragma.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("roles")
@AllArgsConstructor
@NoArgsConstructor
public class RolEntity {
    @Id
    private Long id;
    private String name;
    private String description;
}
