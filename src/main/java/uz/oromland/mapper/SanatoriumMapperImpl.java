package uz.oromland.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.oromland.entity.Sanatorium;
import uz.oromland.payload.SanatoriumDTO;

@Component
@RequiredArgsConstructor
public class SanatoriumMapperImpl implements SanatoriumMapper {

    private final AttachmentMapper attachmentMapper;
    private final UserMapper userMapper;

    @Override
    public SanatoriumDTO toDTO(Sanatorium sanatorium) {
        return new SanatoriumDTO(
                sanatorium.getId(),
                sanatorium.getName(),
                sanatorium.getDescription(),
                sanatorium.getAvailableSeasons(),
                sanatorium.getCreatedBy(),
                sanatorium.getTotalCapacity(),
                sanatorium.getCurrentCapacity(),
                sanatorium.getIsActive(),
                sanatorium.getPhoneNumber(),
                sanatorium.getLocation(),
                sanatorium.getOneDayPrice(),
                sanatorium.getTotalPrice(),
                sanatorium.getCity() == null ? null : sanatorium.getCity().getId(),
                sanatorium.getManager() == null ? null : sanatorium.getManager().stream()
                        .map(userMapper::toDto)
                        .toList(),
                sanatorium.getAttachment() == null ? null : sanatorium.getAttachment().stream()
                        .map(attachmentMapper::toDto)
                        .toList()
        );
    }


}
