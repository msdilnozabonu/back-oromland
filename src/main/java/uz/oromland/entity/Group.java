package uz.oromland.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import uz.oromland.enums.Gender;


@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    @Min(1)
    @NotNull
    private Integer ageRangeStart;

    @Min(1)
    @NotNull
    private Integer ageRangeEnd;

    @Min(1)
    @NotNull
    private Integer totalCapacity;

    @Min(0)
    @NotNull
    private Integer availableSpots;
}
