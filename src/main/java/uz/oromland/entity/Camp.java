package uz.oromland.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@FieldNameConstants
@Entity(name = "Camp")
public class Camp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, length = 256)
    private String location;

    private Integer ageFrom;

    private Integer ageTo;

    private Integer numberOfExchanges;

    private Integer duration;

    private String availableSeasons;

    private String contactPhone;


    @Column(nullable = false)
    private Timestamp startDate;

    @Column(nullable = false)
    private Timestamp endDate;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false)
    private Integer currentParticipants;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToMany
    private List<Attachment> attachment;

    @ManyToOne
    private User manager;

    private BigDecimal price;


    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;


}


