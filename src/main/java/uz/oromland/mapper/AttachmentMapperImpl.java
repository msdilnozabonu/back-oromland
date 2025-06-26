package uz.oromland.mapper;

import org.springframework.stereotype.Component;
import uz.oromland.entity.Attachment;
import uz.oromland.payload.AttachmentDTO;

@Component
public class AttachmentMapperImpl implements AttachmentMapper{
    @Override
    public AttachmentDTO toDto(Attachment attachment) {
        return new AttachmentDTO(
                attachment.getId(),
                attachment.getOriginalName(),
                attachment.getContentType(),
                attachment.getFileSize(),
                attachment.getPath()
        );
    }

    @Override
    public Attachment toEntity(AttachmentDTO attachmentDTO) {
        return new Attachment(
                attachmentDTO.getId() != null ? attachmentDTO.getId() : null,
                attachmentDTO.getOriginalName() != null ? attachmentDTO.getOriginalName() : "",
                attachmentDTO.getContentType() != null ? attachmentDTO.getContentType() : "",
                attachmentDTO.getFileSize() != null ? attachmentDTO.getFileSize() : 0L,
                attachmentDTO.getPath() != null ? attachmentDTO.getPath() : ""
        );
    }
}
