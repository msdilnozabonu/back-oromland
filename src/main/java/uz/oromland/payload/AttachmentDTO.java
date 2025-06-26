package uz.oromland.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.oromland.entity.Attachment;

import java.io.Serializable;

/**
 * DTO for {@link Attachment}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDTO implements Serializable {
    private Long id;
    private String originalName;
    private String contentType;
    private Long fileSize;
    private String path;
}