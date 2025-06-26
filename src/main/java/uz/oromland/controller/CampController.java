package uz.oromland.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.oromland.payload.BookingCampDTO;
import uz.oromland.payload.CampDTO;
import uz.oromland.payload.CampFilterDTO;
import uz.oromland.service.CampService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CampController {

    private final CampService campService;

//    @GetMapping
//    public ResponseEntity<?> getAllCamps() {
//        List<CampDTO> camps = campService.getAllCamps();
//        return ResponseEntity.ok(camps);
//    }

    @GetMapping("/open/camps/{cityId}")
    ///  api/camps/{cityId} – Get all camps by city
    public ResponseEntity<?> getCampsByCity(@PathVariable Long cityId) {
        List<CampDTO> camps = campService.getCampsByCity(cityId);
        return ResponseEntity.ok(camps);
    }

    /// /camps/{city}/{campId} – Camp detail page
    @GetMapping("/open/camps/{cityId}/{campId}")
    public ResponseEntity<?> getCampByCityAndCamp(@PathVariable Long cityId, @PathVariable Long campId) {

        CampDTO campDTO = campService.campByCityAndCamp(cityId, campId);
        return ResponseEntity.ok(campDTO);
    }


    @PostMapping("api/camps/{cityId}/{campId}/booking")
    public ResponseEntity<?> bookCamp(@PathVariable Long cityId,
                                      @PathVariable Long campId,
                                      @RequestBody BookingCampDTO bookingDTO,
                                      @RequestPart(name = "healthNoteFile") MultipartFile healthNoteFile,
                                      @RequestPart(name = "birthCertificateFile") MultipartFile birthCertificateFile,
                                      @RequestPart(name = "photoFile") MultipartFile photoFile,
                                      @RequestPart(name = "parentPassportFile") MultipartFile parentPassportFile,
                                      @RequestPart(required = false, name = "guardianPermissionFile") MultipartFile guardianPermissionFile,
                                      @RequestPart(required = false, name = "privilegeDocument") MultipartFile privilegeDocument
    ) {

        Long saveBooking = campService.saveBooking(cityId, campId, bookingDTO,
                healthNoteFile, birthCertificateFile, photoFile, parentPassportFile,
                guardianPermissionFile, privilegeDocument);

        // Implement booking logic here
        return ResponseEntity.ok("Booking successful, ID: " + saveBooking);
    }

    @PostMapping("/open/camps/filter")
    public ResponseEntity<?> getFilteredCamps(@RequestBody CampFilterDTO filterDTO) {
        List<CampDTO> filteredCamps = campService.getFilter(filterDTO);
        return ResponseEntity.ok(filteredCamps);
    }

}
