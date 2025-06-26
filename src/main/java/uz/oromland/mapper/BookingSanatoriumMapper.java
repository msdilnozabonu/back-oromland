package uz.oromland.mapper;

import uz.oromland.entity.BookingSanatorium;
import uz.oromland.payload.BookingSanatoriumDTO;

public interface BookingSanatoriumMapper {
    BookingSanatoriumDTO toDto(BookingSanatorium bookingSanatorium);

}
