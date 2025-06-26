package uz.oromland.payload;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.oromland.enums.Gender;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {


    private String username;


    private String firstName;


    private String lastName;


    private String email;


    private String password;


    private String phoneNumber;

    private LocalDate birthDate;


    private Gender gender;
}