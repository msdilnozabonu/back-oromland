package uz.oromland.payload;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.oromland.entity.Attachment;
import uz.oromland.enums.DocumentStatus;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingCampDTO {


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
    private Long healthNoteFilePath;

    private Long birthCertificateFilePath;

    private Long photoFilePath;

    private Long parentPassportFile;

    private Long guardianPermissionFilePath;

    private Long privilegeDocumentPath;

    private DocumentStatus documentStatus;



}
