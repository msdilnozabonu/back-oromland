package uz.oromland.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldNameConstants
@Entity(name = "sanatoriums")
public class Sanatorium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String availableSeasons;

    private Long createdBy;

    private Integer totalCapacity;

    private Integer currentCapacity;

    private Boolean isActive;

    private String phoneNumber;

    private String location;

    private BigDecimal oneDayPrice;

    private BigDecimal totalPrice;

    @ManyToOne
    @ToString.Exclude
    private City city;

    @OneToMany
    @ToString.Exclude
    private List<User> manager;

    @OneToMany
    @ToString.Exclude
    private List<Attachment> attachment;

}