package uz.oromland.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import uz.oromland.enums.DocumentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "booking_sanatoriums")
@FieldNameConstants
public class BookingSanatorium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Sanatorium sanatoriumId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer durationDays;

    private BigDecimal totalPrice;

    private DocumentStatus status; // PENDING, APPROVED, REJECTED

    // Hujjatlar
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Attachment passportCopy;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Attachment medicalForm086;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Attachment vaccinationCard;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Attachment photo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Attachment givenDocumentByWorkplace;/// ish joyidan berilgan yollanma agar mavjud bolsa
}
