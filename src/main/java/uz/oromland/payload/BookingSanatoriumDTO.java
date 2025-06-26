package uz.oromland.payload;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.oromland.entity.BookingSanatorium;
import uz.oromland.enums.DocumentStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link BookingSanatorium}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingSanatoriumDTO implements Serializable {

    private Long id;

    private Long userId;

    private Long sanatoriumId;

    @NotNull
    @Future
    private LocalDate startDate;

    @NotNull
    @Future
    private LocalDate endDate;

    @PositiveOrZero
    private Integer durationDays;

    @PositiveOrZero

    private BigDecimal totalPrice;

    @NotNull
    private DocumentStatus status;

    private Long passportCopy;

    private Long medicalForm086;

    private Long vaccinationCard;

    private Long photo;

    private Long givenDocumentByWorkplace;
}