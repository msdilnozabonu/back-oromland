package uz.oromland.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.oromland.payload.BookingSanatoriumDTO;
import uz.oromland.payload.SanatoriumDTO;
import uz.oromland.payload.SanatoriumFilterDTO;
import uz.oromland.service.SanatoriumService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SanatoriumController {

    private final SanatoriumService sanatoriumService;

    @GetMapping("/open/sanatorium/{cityId}")
    public ResponseEntity<List<SanatoriumDTO>> getAllSanatoriumByCityId(@PathVariable Long cityId) {
        List<SanatoriumDTO> sanatoriums = sanatoriumService.getAllSanatoriumByCityId(cityId);
        return ResponseEntity.ok(sanatoriums);
    }

    @GetMapping("/open/sanatorium/{cityId}/{sanatoriumId}")
    public ResponseEntity<SanatoriumDTO> getSanatoriumById(@PathVariable Long cityId, @PathVariable Long sanatoriumId) {

        SanatoriumDTO sanatoriumDTO = sanatoriumService.getSanatoriumByIdAndCityId(cityId, sanatoriumId);
        return ResponseEntity.ok(sanatoriumDTO);

    }

    @PostMapping("api/sanatoriums/{cityId}/{sanatoriumId}/booking")
    public ResponseEntity<?> bookSanatorium(@PathVariable Long cityId,
                                            @PathVariable Long sanatoriumId,
                                            @RequestBody BookingSanatoriumDTO bookingSanatoriumDTO,
                                            @RequestPart(name = "passportCopy") MultipartFile passportCopyFile,
                                            @RequestPart(name = "medicalForm086") MultipartFile healthNoteFile,
                                            @RequestPart(name = "vaccinationCard") MultipartFile vaccinationFile,
                                            @RequestPart(name = "photo", required = false) MultipartFile photoFile,
                                            @RequestPart(name = "givenDocumentByWorkplace", required = false) MultipartFile givenDocumentByWorkPlace
                                            ) {
        Long saveBooking = sanatoriumService.saveBooking(cityId, sanatoriumId, bookingSanatoriumDTO,
                passportCopyFile, healthNoteFile, vaccinationFile, photoFile, givenDocumentByWorkPlace);

        return ResponseEntity.ok("Booking successful, ID: " + saveBooking);
    }

    @PostMapping("/open/sanatorium/filter")
    public ResponseEntity<?> getFilteredSanatoriums(@RequestBody SanatoriumFilterDTO filterDTO) {
        List<SanatoriumDTO> filteredSanatoriums = sanatoriumService.getFilter(filterDTO);
        return ResponseEntity.ok(filteredSanatoriums);
    }

}
