package uz.oromland.payload;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CampFilterDTO {
    
    private String search; // For general search across name and description
    private String name;
    private String description;
    private String location;
    private Integer ageFrom;
    private Integer ageTo;
    private Integer minDuration;
    private Integer maxDuration;
    private String availableSeasons;
    private String contactPhone;
    private Timestamp startDateFrom;
    private Timestamp startDateTo;
    private Timestamp endDateFrom;
    private Timestamp endDateTo;
    private Integer minParticipants;
    private Integer maxParticipants;
    private Boolean isActive;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String cityName; // For filtering by city name
    private String managerName; // For filtering by manager name
}