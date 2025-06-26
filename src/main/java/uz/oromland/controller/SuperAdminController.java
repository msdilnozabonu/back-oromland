package uz.oromland.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.oromland.payload.*;
import uz.oromland.service.BookingService;
import uz.oromland.service.CampService;
import uz.oromland.service.SanatoriumService;
import uz.oromland.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/dashboard/super-admin")
@RequiredArgsConstructor
public class SuperAdminController {
    private final CampService campService;
    private final SanatoriumService sanatoriumService;
    private final UserService userService;
    private final BookingService bookingService;

    /// -----------------------------------------------------------CAMP------------------------------------------------------------------------------------------------

    @GetMapping
    public List<?> getManagementDashboard() {

        //todo boshqatan yozamiz
        // Bu yerda super admin uchun boshqaruv paneli ma'lumotlarini qaytarish lozim
        // Masalan, statistikalar, foydalanuvchilar soni, kamp ma'lumotlari va h.k.
        return List.of("Super Admin Dashboard Data");
    }

    @GetMapping("/camps")
    public ResponseEntity<?> campList() {

        List<CampDTO> allCamps = campService.getAllCamps();
        return ResponseEntity.ok(allCamps);

    }

    @GetMapping("/camps/{id}")
    public ResponseEntity<?> campById(@PathVariable Long id) {

        CampDTO campById = campService.getCampById(id);
        return ResponseEntity.ok(campById);

    }

    @PostMapping(value = "/camps/create-camp", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createCamp(
            @RequestPart("camp") CampDTO campDTO, // main camp data as JSON
            @RequestPart("attachments") List<MultipartFile> attachments // files (majburiy!)
    ) {
        if (attachments == null || attachments.isEmpty()) {
            // Xatolik qaytaramiz
            return ResponseEntity.badRequest().body("Attachments are required.");
        }
        CampDTO createdCamp = campService.createCamp(campDTO, attachments);
        return ResponseEntity.ok(createdCamp);
    }


    @PutMapping("/camps/{id}")
    public ResponseEntity<?> updateCamp(@PathVariable Long id,
                                        @RequestPart CampDTO campDTO,
                                        @RequestPart(required = false) List<MultipartFile> attachments) {

        CampDTO updatedCamp = campService.updateCamp(id, campDTO, attachments);
        return ResponseEntity.ok(updatedCamp);

    }

    @DeleteMapping("/camps/{id}")
    public ResponseEntity<?> deleteCamp(@PathVariable Long id) {
        // Implement the logic to delete the camp by ID
        // For example, you might call a service method to perform the deletion
        // and return an appropriate response.

        // Assuming campService has a method deleteCampById(id)
        campService.deleteCampById(id);
        return ResponseEntity.ok("Camp deleted successfully");
    }

    /// ----------------------------------------------------------------Sanatorium-------------------------------------------------------------------------------------------

    @GetMapping("/sanatoriums")
    public ResponseEntity<?> sanatoriumList() {
        // Implement the logic to return a list of sanatoriums
        // For example, you might call a service method to fetch the list
        // and return it in the response.

        // Assuming campService has a method getAllSanatoriums()
        List<SanatoriumDTO> allSanatoriums = sanatoriumService.getAllSanatoriums();
        return ResponseEntity.ok(allSanatoriums);
    }

    @GetMapping("/sanatoriums/{id}")
    public ResponseEntity<?> sanatoriumById(@PathVariable Long id) {
        // Implement the logic to return a specific sanatorium by ID
        // For example, you might call a service method to fetch the sanatorium
        // and return it in the response.

        // Assuming sanatoriumService has a method getSanatoriumById(id)
        SanatoriumDTO sanatoriumById = sanatoriumService.getSanatoriumById(id);
        return ResponseEntity.ok(sanatoriumById);
    }

    @PostMapping(value = "/sanatoriums/create-sanatorium", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createSanatorium(
            @RequestPart("sanatorium") SanatoriumDTO sanatoriumDTO, // main sanatorium data as JSON
            @RequestPart("attachments") List<MultipartFile> attachments // files (majburiy!)
    ) {
        if (attachments == null || attachments.isEmpty()) {
            // Xatolik qaytaramiz
            return ResponseEntity.badRequest().body("Attachments are required.");
        }
        SanatoriumDTO createdSanatorium = sanatoriumService.createSanatorium(sanatoriumDTO, attachments);
        return ResponseEntity.ok(createdSanatorium);
    }

    @PutMapping("/sanatoriums/{id}")
    public ResponseEntity<?> updateSanatorium(@PathVariable Long id,
                                              @RequestPart SanatoriumDTO sanatoriumDTO,
                                              @RequestPart(required = false) List<MultipartFile> attachments) {

        SanatoriumDTO updatedSanatorium = sanatoriumService.updateSanatorium(id, sanatoriumDTO, attachments);
        return ResponseEntity.ok(updatedSanatorium);

    }

    @DeleteMapping("/sanatoriums/{id}")
    public ResponseEntity<?> deleteSanatorium(@PathVariable Long id) {
        // Implement the logic to delete the sanatorium by ID
        // For example, you might call a service method to perform the deletion
        // and return an appropriate response.

        // Assuming sanatoriumService has a method deleteSanatoriumById(id)
        sanatoriumService.deleteSanatoriumById(id);
        return ResponseEntity.ok("Sanatorium deleted successfully");
    }

    @GetMapping("/search/sanatoriums")
    public ResponseEntity<?> searchSanatoriums(SanatoriumFilterDTO sanatoriumFilterDTO,
                                               @RequestParam(required = false, defaultValue = "0") Integer page,
                                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        // Implement the logic to search for sanatoriums based on the query
        // For example, you might call a service method to perform the search
        // and return the results in the response.

        // Assuming sanatoriumService has a method searchSanatoriums(query)
        PageDTO<SanatoriumDTO> searchResults = sanatoriumService.searchSanatoriums(sanatoriumFilterDTO, page, size);
        return ResponseEntity.ok(searchResults);
    }


    /// ----------------------------------------------------------------User------------------------------------------------------------------------------------------


    @GetMapping("/users")
    public ResponseEntity<?> userList() {
        // Implement the logic to return a list of users
        // For example, you might call a service method to fetch the list
        // and return it in the response.

        List<UserDTO> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> userById(@PathVariable Long id) {
        // Implement the logic to return a specific user by ID
        // For example, you might call a service method to fetch the user
        // and return it in the response.

        UserDTO userById = userService.getUserById(id);
        return ResponseEntity.ok(userById);
    }


    @PostMapping("/users/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        // Implement the logic to create a new user
        // For example, you might call a service method to perform the creation
        // and return the created user in the response.

        userService.createUser(userDTO);
        return ResponseEntity.ok("User created successfully");
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        // Implement the logic to update an existing user
        // For example, you might call a service method to perform the update
        // and return the updated user in the response.

        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // Implement the logic to delete a user by ID
        // For example, you might call a service method to perform the deletion
        // and return an appropriate response.

        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    /// ----------------------------------------------------------------Booking-----------------------------------------------------------------------------------------

    @GetMapping("/bookings/sanatoriums")
    public ResponseEntity<?> bookingList(@RequestParam(required = false, defaultValue = "0") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        // Implement the logic to return a list of bookings
        // For example, you might call a service method to fetch the list
        // and return it in the response.

        // Assuming campService has a method getAllBookings()
        PageDTO<BookingSanatoriumDTO> pageDTO = bookingService.getAllBookings(page, size);
        return ResponseEntity.ok(pageDTO);
    }

    @GetMapping("/bookings/sanatoriums/{id}")
    public ResponseEntity<?> bookingById(@PathVariable Long id) {
        // Implement the logic to return a specific booking by ID
        // For example, you might call a service method to fetch the booking
        // and return it in the response.

        BookingSanatoriumDTO bookingById = bookingService.getBookingById(id);
        return ResponseEntity.ok(bookingById);
    }

    @PostMapping("/bookings/sanatoriums/create-booking")
    public ResponseEntity<?> createBooking(@RequestBody BookingSanatoriumDTO bookingSanatoriumDTO,
                                           @RequestPart(name = "passportCopy", required = false) MultipartFile passportCopy,
                                           @RequestPart(name = "medicalForm086", required = false) MultipartFile medicalForm086,
                                           @RequestPart(name = "vaccinationCard", required = false) MultipartFile vaccinationCard,
                                           @RequestPart(name = "photo", required = false) MultipartFile photo,
                                           @RequestPart(name = "givenDocumentByWorkplace", required = false) MultipartFile givenDocumentByWorkplace) {
        // Implement the logic to create a new booking
        // For example, you might call a service method to perform the creation
        // and return the created booking in the response.

        BookingSanatoriumDTO createdBooking = bookingService.createBooking(bookingSanatoriumDTO,
                passportCopy, medicalForm086, vaccinationCard, photo, givenDocumentByWorkplace);
        return ResponseEntity.ok(createdBooking);
    }

    @PutMapping("/bookings/sanatoriums/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody BookingSanatoriumDTO bookingSanatoriumDTO,
                                           @RequestPart(name = "passportCopy") MultipartFile passportCopy,
                                           @RequestPart(name = "medicalForm086") MultipartFile medicalForm086,
                                           @RequestPart(name = "vaccinationCard") MultipartFile vaccinationCard,
                                           @RequestPart(name = "photo") MultipartFile photo,
                                           @RequestPart(name = "givenDocumentByWorkplace") MultipartFile givenDocumentByWorkplace) {
        // Implement the logic to update an existing booking
        // For example, you might call a service method to perform the update
        // and return the updated booking in the response.

        BookingSanatoriumDTO updatedBooking = bookingService.updateBooking(id, bookingSanatoriumDTO,
                passportCopy, medicalForm086, vaccinationCard, photo, givenDocumentByWorkplace);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/bookings/sanatoriums/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        // Implement the logic to delete a booking by ID
        // For example, you might call a service method to perform the deletion
        // and return an appropriate response.

        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Booking deleted successfully");
    }

    @GetMapping("/bookings/camp")
    public ResponseEntity<?> bookingCampList(@RequestParam(required = false, defaultValue = "0") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        // Implement the logic to return a list of camp bookings
        // For example, you might call a service method to fetch the list
        // and return it in the response.

        // Assuming campService has a method getAllCampBookings()
        PageDTO<BookingCampDTO> allCampBookings = bookingService.getAllCampBookings(page, size);
        return ResponseEntity.ok(allCampBookings);
    }

    @GetMapping("/bookings/camp/{id}")
    public ResponseEntity<?> bookingCampById(@PathVariable Long id) {
        // Implement the logic to return a specific camp booking by ID
        // For example, you might call a service method to fetch the booking
        // and return it in the response.

        BookingCampDTO bookingCampById = bookingService.getBookingCampById(id);
        return ResponseEntity.ok(bookingCampById);
    }

    /**
     * @param bookingCampDTO
     * @return
     * @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
     * private Attachment healthNoteFile;
     * @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
     * private Attachment birthCertificateFile;
     * @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
     * private Attachment photoFile;
     * @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
     * private Attachment parentPassportFile;
     * @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
     * private Attachment guardianPermissionFile;
     * @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
     * private Attachment privilegeDocument;
     */
    @PostMapping("/bookings/camp/create-booking")
    public ResponseEntity<?> createBookingCamp(@RequestBody BookingCampDTO bookingCampDTO,
                                               @RequestPart(name = "healthNoteFile", required = false) MultipartFile healthNoteFile,
                                               @RequestPart(name = "birthCertificateFile", required = false) MultipartFile birthCertificateFile,
                                               @RequestPart(name = "photoFile", required = false) MultipartFile photoFile,
                                               @RequestPart(name = "parentPassportFile", required = false) MultipartFile parentPassportFile,
                                               @RequestPart(name = "guardianPermissionFile", required = false) MultipartFile guardianPermissionFile,
                                               @RequestPart(name = "privilegeDocument", required = false) MultipartFile privilegeDocument) {
        // Implement the logic to create a new camp booking
        // For example, you might call a service method to perform the creation
        // and return the created booking in the response.

        BookingCampDTO createdBookingCamp = bookingService.createBookingCamp(bookingCampDTO,
                healthNoteFile, birthCertificateFile, photoFile, parentPassportFile, guardianPermissionFile, privilegeDocument);

        return ResponseEntity.ok(createdBookingCamp);
    }

    @PutMapping("/bookings/camp/{id}")
    public ResponseEntity<?> updateBookingCamp(@PathVariable Long id, @RequestBody BookingCampDTO bookingCampDTO,
                                               @RequestPart(name = "healthNoteFile") MultipartFile healthNoteFile,
                                               @RequestPart(name = "birthCertificateFile") MultipartFile birthCertificateFile,
                                               @RequestPart(name = "photoFile") MultipartFile photoFile,
                                               @RequestPart(name = "parentPassportFile") MultipartFile parentPassportFile,
                                               @RequestPart(name = "guardianPermissionFile") MultipartFile guardianPermissionFile,
                                               @RequestPart(name = "privilegeDocument") MultipartFile privilegeDocument) {
        // Implement the logic to update an existing camp booking
        // For example, you might call a service method to perform the update
        // and return the updated booking in the response.

        BookingCampDTO updatedBookingCamp = bookingService.updateBookingCamp(id, bookingCampDTO,
                healthNoteFile, birthCertificateFile, photoFile, parentPassportFile, guardianPermissionFile, privilegeDocument);
        return ResponseEntity.ok(updatedBookingCamp);
    }

    @DeleteMapping("/bookings/camp/{id}")
    public ResponseEntity<?> deleteBookingCamp(@PathVariable Long id) {
        // Implement the logic to delete a camp booking by ID
        // For example, you might call a service method to perform the deletion
        // and return an appropriate response.

        bookingService.deleteBookingCamp(id);
        return ResponseEntity.ok("Camp booking deleted successfully");
    }


}
