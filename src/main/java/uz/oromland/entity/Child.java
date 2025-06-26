package uz.oromland.entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import uz.oromland.enums.Gender;


import java.time.LocalDate;

@Entity
@Table(name = "children")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "camp_id")
    @NotNull
    private Place camp;

    @NotBlank
    @Size(max = 128)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private LocalDate createdAt;
}
