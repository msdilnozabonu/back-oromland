package uz.oromland.service;

import org.springframework.web.multipart.MultipartFile;
import uz.oromland.payload.AttachmentDTO;

public interface AttachmentService {

    AttachmentDTO getById(Long id);

    AttachmentDTO upload(MultipartFile file);

    AttachmentDTO update(Long id, MultipartFile file);

    void delete(Long id);


}
