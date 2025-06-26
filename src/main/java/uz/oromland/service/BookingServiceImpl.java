package uz.oromland.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.oromland.entity.*;
import uz.oromland.enums.DocumentStatus;
import uz.oromland.mapper.BookingCampMapper;
import uz.oromland.mapper.BookingSanatoriumMapper;
import uz.oromland.payload.BookingCampDTO;
import uz.oromland.payload.BookingSanatoriumDTO;
import uz.oromland.payload.PageDTO;
import uz.oromland.repository.BookingCampRepository;
import uz.oromland.repository.BookingSanatoriumRepository;
import uz.oromland.repository.SanatoriumRepository;
import uz.oromland.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {


    private static final String BASE_FOLDER = "src/main/resources/document";
    private final BookingSanatoriumRepository bookingSanatoriumRepository;
    private final BookingSanatoriumMapper bookingSanatoriumMapper;
    private final SanatoriumRepository sanatoriumRepository;
    private final UserRepository userRepository;
    private final BookingCampRepository bookingCampRepository;
    private final BookingCampMapper bookingCampMapper;


    @Override
    public PageDTO<BookingSanatoriumDTO> getAllBookings(Integer page, Integer size) {

        Sort sort = Sort.by(BookingSanatorium.Fields.id);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<BookingSanatorium> bookingSanatoriums = bookingSanatoriumRepository.findAll(pageRequest);

        return new PageDTO<>(
                bookingSanatoriums.getContent().stream().map(bookingSanatoriumMapper::toDto).toList(),
                bookingSanatoriums.getNumber(),
                bookingSanatoriums.getSize(),
                bookingSanatoriums.getTotalElements(),
                bookingSanatoriums.getTotalPages(),
                bookingSanatoriums.isLast(),
                bookingSanatoriums.isFirst(),
                bookingSanatoriums.getNumberOfElements(),
                bookingSanatoriums.isEmpty()
        );


    }

    @Override
    public BookingSanatoriumDTO getBookingById(Long id) {

        BookingSanatorium bookingSanatorium = bookingSanatoriumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        return bookingSanatoriumMapper.toDto(bookingSanatorium);


    }

    @Override
    public BookingSanatoriumDTO createBooking(BookingSanatoriumDTO bookingSanatoriumDTO,
                                              MultipartFile passportCopy,
                                              MultipartFile medicalForm086,
                                              MultipartFile vaccinationCard,
                                              MultipartFile photo,
                                              MultipartFile givenDocumentByWorkplace) {

        User user = userRepository.findById(bookingSanatoriumDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + bookingSanatoriumDTO.getUserId()));

        Sanatorium sanatorium = sanatoriumRepository.findById(bookingSanatoriumDTO.getSanatoriumId())
                .orElseThrow(() -> new RuntimeException("Sanatorium not found with id: " + bookingSanatoriumDTO.getSanatoriumId()));

        BookingSanatorium bookingSanatorium = new BookingSanatorium(
                null,
                user,
                sanatorium,
                bookingSanatoriumDTO.getStartDate(),
                bookingSanatoriumDTO.getEndDate(),
                bookingSanatoriumDTO.getDurationDays(),
                bookingSanatoriumDTO.getTotalPrice(),
                bookingSanatoriumDTO.getStatus(),
                saveFileToStorage(passportCopy),
                saveFileToStorage(medicalForm086),
                saveFileToStorage(vaccinationCard),
                saveFileToStorage(photo),
                saveFileToStorage(givenDocumentByWorkplace)
        );

        bookingSanatorium = bookingSanatoriumRepository.save(bookingSanatorium);
        return bookingSanatoriumMapper.toDto(bookingSanatorium);


    }

    @Override
    public BookingSanatoriumDTO updateBooking(Long id, BookingSanatoriumDTO bookingSanatoriumDTO,
                                              MultipartFile passportCopy,
                                              MultipartFile medicalForm086,
                                              MultipartFile vaccinationCard,
                                              MultipartFile photo,
                                              MultipartFile givenDocumentByWorkplace) {

        BookingSanatorium existingBooking = bookingSanatoriumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        User user = userRepository.findById(bookingSanatoriumDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + bookingSanatoriumDTO.getUserId()));

        Sanatorium sanatorium = sanatoriumRepository.findById(bookingSanatoriumDTO.getSanatoriumId())
                .orElseThrow(() -> new RuntimeException("Sanatorium not found with id: " + bookingSanatoriumDTO.getSanatoriumId()));

        existingBooking.setUser(user);
        existingBooking.setSanatoriumId(sanatorium);
        existingBooking.setStartDate(bookingSanatoriumDTO.getStartDate());
        existingBooking.setEndDate(bookingSanatoriumDTO.getEndDate());
        existingBooking.setDurationDays(bookingSanatoriumDTO.getDurationDays());
        existingBooking.setTotalPrice(bookingSanatoriumDTO.getTotalPrice());
        existingBooking.setStatus(bookingSanatoriumDTO.getStatus());

        if (passportCopy != null) {
            existingBooking.setPassportCopy(saveFileToStorage(passportCopy));
        }
        if (medicalForm086 != null) {
            existingBooking.setMedicalForm086(saveFileToStorage(medicalForm086));
        }
        if (vaccinationCard != null) {
            existingBooking.setVaccinationCard(saveFileToStorage(vaccinationCard));
        }
        if (photo != null) {
            existingBooking.setPhoto(saveFileToStorage(photo));
        }
        if (givenDocumentByWorkplace != null) {
            existingBooking.setGivenDocumentByWorkplace(saveFileToStorage(givenDocumentByWorkplace));
        }

        BookingSanatorium updatedBooking = bookingSanatoriumRepository.save(existingBooking);
        return bookingSanatoriumMapper.toDto(updatedBooking);


    }

    @Override
    public void deleteBooking(Long id) {

        BookingSanatorium bookingSanatorium = bookingSanatoriumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        bookingSanatoriumRepository.delete(bookingSanatorium);


    }

    @Override
    public PageDTO<BookingCampDTO> getAllCampBookings(Integer page, Integer size) {
        Sort sort = Sort.by(BookingCamp.Fields.id);
        PageRequest pageRequest = PageRequest.of(page, size, sort);


        Page<BookingCamp> bookingCamps = bookingCampRepository.findAll(pageRequest);

        return new PageDTO<>(
                bookingCamps.getContent().stream().map(bookingCampMapper::toDTO).toList(),
                bookingCamps.getNumber(),
                bookingCamps.getSize(),
                bookingCamps.getTotalElements(),
                bookingCamps.getTotalPages(),
                bookingCamps.isLast(),
                bookingCamps.isFirst(),
                bookingCamps.getNumberOfElements(),
                bookingCamps.isEmpty()
        );

    }

    @Override
    public BookingCampDTO getBookingCampById(Long id) {

        BookingCamp bookingCamp = bookingCampRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking Camp not found with id: " + id));

        return bookingCampMapper.toDTO(bookingCamp);
    }

    @Override
    public BookingCampDTO createBookingCamp(BookingCampDTO bookingCampDTO, MultipartFile healthNoteFile, MultipartFile birthCertificateFile, MultipartFile photoFile, MultipartFile parentPassportFile, MultipartFile guardianPermissionFile, MultipartFile privilegeDocument) {

        BookingCamp bookingCamp = new BookingCamp(
                null,
                bookingCampDTO.getFirstName(),
                bookingCampDTO.getLastName(),
                bookingCampDTO.getBirthDate(),
                bookingCampDTO.getGender(),
                bookingCampDTO.getDocumentNumber(),
                bookingCampDTO.getAddress(),
                bookingCampDTO.getGuardianFirstName(),
                bookingCampDTO.getGuardianLastName(),
                bookingCampDTO.getGuardianPhone(),
                bookingCampDTO.getGuardianDocument(),
                bookingCampDTO.getGuardianJob(),
                saveFileToStorage(healthNoteFile),
                saveFileToStorage(birthCertificateFile),
                saveFileToStorage(photoFile),
                saveFileToStorage(parentPassportFile),
                saveFileToStorage(guardianPermissionFile),
                saveFileToStorage(privilegeDocument),
                DocumentStatus.PENDING
        );

        bookingCamp = bookingCampRepository.save(bookingCamp);
        return bookingCampMapper.toDTO(bookingCamp);

    }

    @Override
    public BookingCampDTO updateBookingCamp(Long id, BookingCampDTO bookingCampDTO, MultipartFile healthNoteFile, MultipartFile birthCertificateFile, MultipartFile photoFile, MultipartFile parentPassportFile, MultipartFile guardianPermissionFile, MultipartFile privilegeDocument) {

        BookingCamp existingBookingCamp = bookingCampRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking Camp not found with id: " + id));

        existingBookingCamp.setFirstName(bookingCampDTO.getFirstName());
        existingBookingCamp.setLastName(bookingCampDTO.getLastName());
        existingBookingCamp.setBirthDate(bookingCampDTO.getBirthDate());
        existingBookingCamp.setGender(bookingCampDTO.getGender());
        existingBookingCamp.setDocumentNumber(bookingCampDTO.getDocumentNumber());
        existingBookingCamp.setAddress(bookingCampDTO.getAddress());
        existingBookingCamp.setGuardianFirstName(bookingCampDTO.getGuardianFirstName());
        existingBookingCamp.setGuardianLastName(bookingCampDTO.getGuardianLastName());
        existingBookingCamp.setGuardianPhone(bookingCampDTO.getGuardianPhone());
        existingBookingCamp.setGuardianDocument(bookingCampDTO.getGuardianDocument());
        existingBookingCamp.setGuardianJob(bookingCampDTO.getGuardianJob());

        if (healthNoteFile != null) {
            existingBookingCamp.setHealthNoteFile(saveFileToStorage(healthNoteFile));
        }
        if (birthCertificateFile != null) {
            existingBookingCamp.setBirthCertificateFile(saveFileToStorage(birthCertificateFile));
        }
        if (photoFile != null) {
            existingBookingCamp.setPhotoFile(saveFileToStorage(photoFile));
        }
        if (parentPassportFile != null) {
            existingBookingCamp.setParentPassportFile(saveFileToStorage(parentPassportFile));
        }
        if (guardianPermissionFile != null) {
            existingBookingCamp.setGuardianPermissionFile(saveFileToStorage(guardianPermissionFile));
        }
        if (privilegeDocument != null) {
            existingBookingCamp.setPrivilegeDocument(saveFileToStorage(privilegeDocument));
        }

        BookingCamp updatedBooking = bookingCampRepository.save(existingBookingCamp);
        return bookingCampMapper.toDTO(updatedBooking);


    }

    @Override
    public void deleteBookingCamp(Long id) {

        BookingCamp bookingCamp = bookingCampRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking Camp not found with id: " + id));
        bookingCampRepository.delete(bookingCamp);

    }

    private Attachment saveFileToStorage(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();
            String contentType = file.getContentType();

            String extension = extractExtension(originalFilename);
            Path directoryPath = buildDirectoryPath();
            Files.createDirectories(directoryPath);

            Path filePath = generateUniqueFilePath(directoryPath, extension);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath);
            }

            return new Attachment(
                    null,
                    originalFilename,
                    contentType,
                    size,
                    filePath.toString()
            );

        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + file.getOriginalFilename());
        }
    }

    private String extractExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }

    private Path buildDirectoryPath() {
        LocalDate now = LocalDate.now();
        String year = String.valueOf(now.getYear());
        String month = now.getMonth().name().charAt(0) + now.getMonth().name().substring(1).toLowerCase();
        return Path.of(BASE_FOLDER, year, month);
    }

    private Path generateUniqueFilePath(Path directoryPath, String extension) throws IOException {
        Path filePath;
        String uniqueName;
        do {
            uniqueName = UUID.randomUUID() + extension;
            filePath = directoryPath.resolve(uniqueName);
        } while (Files.exists(filePath));
        return filePath;
    }
}
