package uz.oromland.payload;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SanatoriumFilterDTO {
    
    private String search; // For general search across name and description
    private String name;
    private String description;
    private String availableSeasons;
    private Integer minTotalCapacity;
    private Integer maxTotalCapacity;
    private Integer minCurrentCapacity;
    private Integer maxCurrentCapacity;
    private Boolean isActive;
    private String phoneNumber;
    private String location;
    private BigDecimal minOneDayPrice;
    private BigDecimal maxOneDayPrice;
    private BigDecimal minTotalPrice;
    private BigDecimal maxTotalPrice;
    private String cityName; // For filtering by city name
    private String managerName; // For filtering by manager name
}