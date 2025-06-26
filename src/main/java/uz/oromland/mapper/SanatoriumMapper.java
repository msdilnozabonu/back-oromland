package uz.oromland.mapper;

import uz.oromland.entity.Sanatorium;
import uz.oromland.payload.SanatoriumDTO;

public interface SanatoriumMapper {
    SanatoriumDTO toDTO(Sanatorium sanatorium);
}
