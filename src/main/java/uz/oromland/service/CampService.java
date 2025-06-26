package uz.oromland.service;

import org.springframework.web.multipart.MultipartFile;
import uz.oromland.entity.City;
import uz.oromland.payload.BookingCampDTO;
import uz.oromland.payload.CampDTO;
import uz.oromland.payload.CampFilterDTO;

import java.util.List;

public interface CampService {
    List<CampDTO> getAllCamps();

    List<CampDTO> getCampsByCity(Long city);

    CampDTO campByCityAndCamp(Long cityId, Long campId);

    Long saveBooking(Long cityId, Long campId, BookingCampDTO bookingDTO, MultipartFile healthNoteFile, MultipartFile birthCertificateFile, MultipartFile photoFile, MultipartFile parentPassportFile, MultipartFile guardianPermissionFile, MultipartFile privilegeDocument);

    List<CampDTO> getFilter(CampFilterDTO filterDTO);

    CampDTO getCampById(Long id);

    CampDTO createCamp(CampDTO campDTO, List<MultipartFile> attachments);

    CampDTO updateCamp(Long id, CampDTO campDTO,
                       List<MultipartFile> attachments);

    void deleteCampById(Long id);
}
