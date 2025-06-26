package uz.oromland.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.oromland.entity.Apply;
import uz.oromland.enums.DocumentStatus;

import java.io.Serializable;

/**
 * DTO for {@link Apply}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyDTO implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String message;
    private DocumentStatus status;
    private String telegramUsername;
    private Long jobId;
    private Long cvId;
}