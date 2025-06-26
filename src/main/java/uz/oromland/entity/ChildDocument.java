package uz.oromland.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "child_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_id")
    @NotNull
    private Child child;

    @NotBlank
    @Size(max = 128)
    private String fileName;

    @NotBlank
    @Size(max = 32)
    private String fileType;

    @NotBlank
    @Size(max = 256)
    private String filePath;

    @NotNull
    private LocalDateTime uploadDate;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;
}
