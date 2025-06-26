package uz.oromland.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.oromland.enums.JobType;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @ManyToOne
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @ManyToOne
    @JoinColumn(name = "sanatorium_id")
    private Sanatorium sanatorium;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }


}