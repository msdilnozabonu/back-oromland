package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.Sanatorium;

import java.util.List;

@Repository
public interface SanatoriumRepository extends JpaRepository<Sanatorium, Long> {
    List<Sanatorium> findAllByCity_Id(Long cityId);
}