package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.Camp;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampRepository extends JpaRepository<Camp, Long> {


    Optional<List<Camp>> findAllByCity_Id(Long cityId);

}