package co.com.pragma.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class User {
    private String identification;
    private String name;
    private String lastName;
    private LocalDate birthDay;
    private String address;
    private String phone;
    private String email;
    private Double baseSalary;
}
