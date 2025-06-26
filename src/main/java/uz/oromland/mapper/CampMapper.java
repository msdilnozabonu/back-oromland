package uz.oromland.mapper;

import uz.oromland.entity.Camp;
import uz.oromland.payload.CampDTO;

public interface CampMapper {
    CampDTO toDto(Camp camp);
}
