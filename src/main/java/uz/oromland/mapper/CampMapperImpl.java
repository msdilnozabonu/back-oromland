package uz.oromland.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.oromland.entity.Camp;
import uz.oromland.payload.AttachmentDTO;
import uz.oromland.payload.CampDTO;
import uz.oromland.payload.UserDTO;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CampMapperImpl implements CampMapper {

    private final AttachmentMapper attachmentMapper;
    private final UserMapper userMapper;

    @Override
    public CampDTO toDto(Camp camp) {

        List<AttachmentDTO> attachmentDTO = camp.getAttachment()
                .stream()
                .map(attachmentMapper::toDto)
                .toList();

        UserDTO managerDTO = userMapper.toDto(camp.getManager());

        return new CampDTO(
                camp.getId(),
                camp.getName(),
                camp.getDescription(),
                camp.getLocation(),
                camp.getAgeFrom(),
                camp.getAgeTo(),
                camp.getNumberOfExchanges(),
                camp.getDuration(),
                camp.getAvailableSeasons(),
                camp.getContactPhone(),
                camp.getStartDate(),
                camp.getEndDate(),
                camp.getMaxParticipants(),
                camp.getCurrentParticipants(),
                camp.getIsActive(),
                attachmentDTO,
                managerDTO,
                camp.getPrice(),
                camp.getCity() == null ? null : camp.getCity().getId()
        );
    }
}
