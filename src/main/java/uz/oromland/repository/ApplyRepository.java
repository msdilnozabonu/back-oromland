package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.oromland.entity.Apply;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
}