package uz.oromland.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.oromland.entity.Attachment;
import uz.oromland.exception.FileNotFoundException;
import uz.oromland.mapper.AttachmentMapper;
import uz.oromland.payload.AttachmentDTO;
import uz.oromland.repository.AttachmentRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private static final String BASE_FOLDER = "src/main/resources/document";
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;

    @Override
    public AttachmentDTO getById(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("Fayl topilmadi: id=" + id));
        return attachmentMapper.toDto(attachment);
    }

    @Override
    public AttachmentDTO upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();
            String contentType = file.getContentType();

            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 📅 Fayl joyi yil/oy bo‘yicha
            LocalDate now = LocalDate.now();
            String year = String.valueOf(now.getYear());
            String month = now.getMonth().name();
            month = month.substring(0, 1) + month.substring(1).toLowerCase();

            Path directoryPath = Path.of(BASE_FOLDER, year, month);
            Files.createDirectories(directoryPath);

            Path filePath;
            String uniqueName;

            // Fayl nomi conflict bo‘lmasligi uchun
            do {
                uniqueName = UUID.randomUUID() + extension;
                filePath = directoryPath.resolve(uniqueName);
            } while (Files.exists(filePath));

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath);
            }

            Attachment attachment = new Attachment(
                    null,
                    originalFilename,
                    contentType,
                    size,
                    filePath.toString()
            );

            Attachment saveAttachment = attachmentRepository.save(attachment);

            return attachmentMapper.toDto(saveAttachment);
        } catch (IOException e) {
            throw new RuntimeException("Faylni saqlashda xatolik: " + e.getMessage());
        }
    }

    @Override
    public AttachmentDTO update(Long id, MultipartFile file) {
        Attachment old = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fayl topilmadi: id=" + id));

        // Eski faylni o‘chirib tashlaymiz
        try {
            Files.deleteIfExists(Path.of(old.getPath()));
        } catch (IOException e) {
            System.err.println("Eski faylni o‘chirishda xatolik: " + e.getMessage());
        }

        // Yangi faylni yuklaymiz (upload() dan nusxa)
        try {
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();
            String contentType = file.getContentType();

            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            LocalDate now = LocalDate.now();
            String year = String.valueOf(now.getYear());
            String month = now.getMonth().name();
            month = month.substring(0, 1) + month.substring(1).toLowerCase();

            Path directoryPath = Path.of(BASE_FOLDER, year, month);
            Files.createDirectories(directoryPath);

            Path filePath;
            String uniqueName;

            do {
                uniqueName = UUID.randomUUID() + extension;
                filePath = directoryPath.resolve(uniqueName);
            } while (Files.exists(filePath));

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath);
            }

            // Yangi qiymatlar bilan yangilaymiz
            old.setOriginalName(originalFilename);
            old.setFileSize(size);
            old.setContentType(contentType);
            old.setPath(filePath.toString());

            Attachment updated = attachmentRepository.save(old);
            return attachmentMapper.toDto(updated);
        } catch (IOException e) {
            throw new RuntimeException("Faylni yangilashda xatolik: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById((long) id);
        if (optionalAttachment.isEmpty()) {
            throw new RuntimeException("O‘chirish uchun fayl topilmadi: id=" + id);
        }

        Attachment attachment = optionalAttachment.get();

        // Faylni fayl tizimidan o‘chirish
        try {
            Files.deleteIfExists(Path.of(attachment.getPath()));
        } catch (IOException e) {
            System.err.println("Faylni tizimdan o‘chirishda xatolik: " + e.getMessage());
        }

        attachmentRepository.deleteById((long) id);
    }
}
