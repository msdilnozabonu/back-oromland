package uz.oromland.service;

import org.springframework.web.multipart.MultipartFile;
import uz.oromland.entity.BookingCamp;
import uz.oromland.payload.BookingCampDTO;
import uz.oromland.payload.BookingSanatoriumDTO;
import uz.oromland.payload.PageDTO;

public interface BookingService {
    PageDTO<BookingSanatoriumDTO> getAllBookings(Integer page, Integer size);

    BookingSanatoriumDTO getBookingById(Long id);

    BookingSanatoriumDTO createBooking(BookingSanatoriumDTO bookingSanatoriumDTO, MultipartFile passportCopy,
                                       MultipartFile medicalForm086, MultipartFile vaccinationCard,
                                       MultipartFile photo, MultipartFile givenDocumentByWorkplace);

    BookingSanatoriumDTO updateBooking(Long id, BookingSanatoriumDTO bookingSanatoriumDTO,
                                       MultipartFile passportCopy, MultipartFile medicalForm086,
                                       MultipartFile vaccinationCard, MultipartFile photo,
                                       MultipartFile givenDocumentByWorkplace);

    void deleteBooking(Long id);

    PageDTO<BookingCampDTO> getAllCampBookings(Integer page, Integer size);

    BookingCampDTO getBookingCampById(Long id);


    BookingCampDTO updateBookingCamp(Long id, BookingCampDTO bookingCampDTO, MultipartFile healthNoteFile, MultipartFile birthCertificateFile, MultipartFile photoFile, MultipartFile parentPassportFile, MultipartFile guardianPermissionFile, MultipartFile privilegeDocument);


    void deleteBookingCamp(Long id);


    BookingCampDTO createBookingCamp(BookingCampDTO bookingCampDTO, MultipartFile healthNoteFile, MultipartFile birthCertificateFile, MultipartFile photoFile, MultipartFile parentPassportFile, MultipartFile guardianPermissionFile, MultipartFile privilegeDocument);


}
