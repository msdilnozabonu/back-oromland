package uz.oromland.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.oromland.entity.Job;
import uz.oromland.enums.JobType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link Job}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    @PositiveOrZero

    private BigDecimal salary;

    @NotNull
    private JobType jobType;

    private Long campId;

    private Long sanatoriumId;

    private Boolean isActive = true;

    private Long createdBy;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}