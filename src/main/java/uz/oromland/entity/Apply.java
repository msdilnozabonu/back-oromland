package uz.oromland.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.oromland.enums.DocumentStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Apply")
public class Apply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String message;

    private DocumentStatus status;

    private String telegramUsername;

    @ManyToOne
    private Job job;

    @OneToOne
    private Attachment cv;





}
