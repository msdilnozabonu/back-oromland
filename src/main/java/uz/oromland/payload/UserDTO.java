package uz.oromland.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.oromland.entity.User;
import uz.oromland.enums.Gender;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {


    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;

    private Boolean isActive = true;

    private LocalDate birthDate;

    private Gender gender;

    private Integer roleId;

    private Long attachmentId;
}