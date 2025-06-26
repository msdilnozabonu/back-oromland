package uz.oromland.mapper;

import uz.oromland.entity.Attachment;
import uz.oromland.payload.AttachmentDTO;

public interface AttachmentMapper {
    AttachmentDTO toDto(Attachment attachment);

    Attachment toEntity(AttachmentDTO attachmentDTO);

}
