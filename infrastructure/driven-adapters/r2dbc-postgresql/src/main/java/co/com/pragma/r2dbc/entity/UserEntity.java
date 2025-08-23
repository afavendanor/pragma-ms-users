package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@Table("user")
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
}
