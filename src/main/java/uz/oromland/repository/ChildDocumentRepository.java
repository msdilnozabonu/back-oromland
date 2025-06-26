package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.ChildDocument;

@Repository
public interface ChildDocumentRepository extends JpaRepository<ChildDocument, Long> {
}