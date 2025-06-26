package uz.oromland.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.oromland.entity.BookingSanatorium;
import uz.oromland.payload.BookingSanatoriumDTO;

@Service
@RequiredArgsConstructor
public class BookingSanatoriumMapperImpl implements BookingSanatoriumMapper {
    @Override
    public BookingSanatoriumDTO toDto(BookingSanatorium bookingSanatorium) {

        return new BookingSanatoriumDTO(
                bookingSanatorium.getId(),
                bookingSanatorium.getUser().getId(),
                bookingSanatorium.getSanatoriumId() != null ? bookingSanatorium.getSanatoriumId().getId() : null,
                bookingSanatorium.getStartDate(),
                bookingSanatorium.getEndDate(),
                bookingSanatorium.getDurationDays(),
                bookingSanatorium.getTotalPrice(),
                bookingSanatorium.getStatus(),
                bookingSanatorium.getPassportCopy() != null ? bookingSanatorium.getPassportCopy().getId() : null,
                bookingSanatorium.getMedicalForm086() != null ? bookingSanatorium.getMedicalForm086().getId() : null,
                bookingSanatorium.getVaccinationCard() != null ? bookingSanatorium.getVaccinationCard().getId() : null,
                bookingSanatorium.getPhoto() != null ? bookingSanatorium.getPhoto().getId() : null,
                bookingSanatorium.getGivenDocumentByWorkplace() != null ? bookingSanatorium.getGivenDocumentByWorkplace().getId() : null
        );

    }
}
