package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}