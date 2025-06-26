package uz.oromland.entity;

import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.FieldNameConstants;
import uz.oromland.enums.DocumentStatus;
import uz.oromland.enums.Gender;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "BookingCamp")
@FieldNameConstants
public class BookingCamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👦 Bola haqida
    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String gender;

    private String documentNumber;

    private String address;

    // 👨‍👩‍👧 Ota-ona yoki vasiy haqida
    private String guardianFirstName;

    private String guardianLastName;

    private String guardianPhone;

    private String guardianDocument; // e.g. passport series and number

    private String guardianJob;

    // 🩺 Sog‘liq va fayllar
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Attachment healthNoteFile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Attachment birthCertificateFile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Attachment photoFile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Attachment parentPassportFile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Attachment guardianPermissionFile;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private Attachment privilegeDocument;

    private DocumentStatus documentStatus;

//    private Boolean approved; // Admin tasdiqlaganmi

//    private String firstName;
//
//    private String lastName;
//
//    private LocalDate birthDate;
//
//    private String passportNumber;
//
//    private String parentFullName;
//
//    private Gender gender;
//
//    @ManyToOne
//    private User userId;
//
//    @Column( nullable = false)
//    @OneToMany
//    private List<Group> groupId;
//
//    @OneToMany
//    private List<Attachment> documents;
//
//
//    @Column( nullable = false)
//    private DocumentStatus status;
//
//    @Column( nullable = false)
//    private Timestamp createdAt;
}
