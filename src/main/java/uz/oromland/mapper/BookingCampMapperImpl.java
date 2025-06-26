package uz.oromland.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.oromland.entity.BookingCamp;
import uz.oromland.payload.BookingCampDTO;

@Service
@RequiredArgsConstructor
public class BookingCampMapperImpl implements BookingCampMapper{

    @Override
    public BookingCampDTO toDTO(BookingCamp bookingCamp) {
        return new BookingCampDTO(
                 bookingCamp.getId(),
                bookingCamp.getFirstName(),
                bookingCamp.getLastName(),
                bookingCamp.getBirthDate(),
                bookingCamp.getGender(),
                bookingCamp.getDocumentNumber(),
                bookingCamp.getAddress(),
                bookingCamp.getGuardianFirstName(),
                bookingCamp.getGuardianLastName(),
                bookingCamp.getGuardianPhone(),
                bookingCamp.getGuardianDocument(),
                bookingCamp.getGuardianJob(),
                bookingCamp.getHealthNoteFile() != null ? bookingCamp.getHealthNoteFile().getId() : null,
                bookingCamp.getBirthCertificateFile() != null ? bookingCamp.getBirthCertificateFile().getId() : null,
                bookingCamp.getPhotoFile() != null ? bookingCamp.getPhotoFile().getId() : null,
                bookingCamp.getParentPassportFile() != null ? bookingCamp.getParentPassportFile().getId() : null,
                bookingCamp.getGuardianPermissionFile() != null ? bookingCamp.getGuardianPermissionFile().getId() : null,
                bookingCamp.getPrivilegeDocument() != null ? bookingCamp.getPrivilegeDocument().getId() : null,
                bookingCamp.getDocumentStatus()

        );

    }
}
