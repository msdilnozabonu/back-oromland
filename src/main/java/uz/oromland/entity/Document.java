package uz.oromland.entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import uz.oromland.enums.DocumentStatus;


import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "camp_id")
    @NotNull
    private Place camp;

    @Enumerated(EnumType.STRING)
    @NotNull
    private DocumentStatus status;

    @Size(max = 512)
    private String comment;

    @NotNull
    private LocalDateTime submittedAt;
}
