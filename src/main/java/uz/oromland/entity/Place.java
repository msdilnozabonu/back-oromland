package uz.oromland.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import uz.oromland.enums.PlaceType;


@Entity
@Table(name = "places")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "place_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 128)
    private String name;

    @NotBlank
    @Column(columnDefinition = "jsonb")
    private String descriptionJson;

    @ManyToOne
    @JoinColumn(name = "city_id")
    @NotNull
    private City city;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @NotNull
    private Location location;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PlaceType type;

    @NotBlank
    private String availableSeasons;

    @NotNull
    private Long createdBy;
}
