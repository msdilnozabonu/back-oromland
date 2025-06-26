package uz.oromland.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@FieldNameConstants
@Entity(name = "City")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String name;
}
