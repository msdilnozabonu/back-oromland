package uz.oromland.service;

import org.springframework.web.multipart.MultipartFile;
import uz.oromland.payload.BookingSanatoriumDTO;
import uz.oromland.payload.PageDTO;
import uz.oromland.payload.SanatoriumDTO;
import uz.oromland.payload.SanatoriumFilterDTO;

import java.util.List;


public interface SanatoriumService {
    List<SanatoriumDTO> getAllSanatoriumByCityId(Long cityId);

    SanatoriumDTO getSanatoriumByIdAndCityId(Long cityId, Long sanatoriumId);


    Long saveBooking(Long cityId, Long sanatoriumId, BookingSanatoriumDTO bookingSanatoriumDTO, MultipartFile passportCopyFile, MultipartFile healthNoteFile, MultipartFile vaccinationFile, MultipartFile photoFile, MultipartFile givenDocumentByWorkplace);

    List<SanatoriumDTO> getFilter(SanatoriumFilterDTO filterDTO);

    List<SanatoriumDTO> getAllSanatoriums();

    SanatoriumDTO getSanatoriumById(Long id);

    SanatoriumDTO createSanatorium(SanatoriumDTO sanatoriumDTO, List<MultipartFile> attachments);

    SanatoriumDTO updateSanatorium(Long id, SanatoriumDTO sanatoriumDTO, List<MultipartFile> attachments);

    void deleteSanatoriumById(Long id);


    PageDTO<SanatoriumDTO> searchSanatoriums(SanatoriumFilterDTO sanatoriumFilterDTO, Integer page, Integer size);
}
