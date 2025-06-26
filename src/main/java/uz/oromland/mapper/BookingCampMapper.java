package uz.oromland.mapper;

import uz.oromland.entity.BookingCamp;
import uz.oromland.payload.BookingCampDTO;

public interface BookingCampMapper {

    BookingCampDTO toDTO(BookingCamp bookingCamp);

}
