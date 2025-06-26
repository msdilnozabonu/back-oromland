package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}