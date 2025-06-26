package uz.oromland.payload;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.oromland.entity.Sanatorium;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link Sanatorium}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanatoriumDTO implements Serializable {

    private Long id;

    @NotNull
    @Size
    @NotEmpty
    private String name;

    @NotNull
    @Size
    @NotEmpty

    private String description;
    @NotNull
    @NotEmpty

    private String availableSeasons;

    private Long createdBy;

    @NotNull
    @Positive
    @PositiveOrZero
    private Integer totalCapacity;

    @PositiveOrZero
    private Integer currentCapacity;

    @NotNull
    private Boolean isActive;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String location;

    @NotNull
    @Positive
    private BigDecimal oneDayPrice;

    @NotNull
    @PositiveOrZero
    private BigDecimal totalPrice;

    private Long cityId;

    @NotNull
    private List<UserDTO> manager;

    private List<AttachmentDTO> attachment;
}