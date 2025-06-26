package uz.oromland.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CampDTO {

    private Long id;

    private String name;

    private String description;

    private String location;

    private Integer ageFrom;

    private Integer ageTo;

    private Integer numberOfExchanges;

    private Integer duration;

    private String availableSeasons;

    private String contactPhone;

    private Timestamp startDate;

    private Timestamp endDate;

    private Integer maxParticipants;

    private Integer currentParticipants;

    private Boolean isActive;

    private List<AttachmentDTO> attachmentDTOS;

    private UserDTO managerDTO;

    private BigDecimal price;

    private Long cityId;

}
