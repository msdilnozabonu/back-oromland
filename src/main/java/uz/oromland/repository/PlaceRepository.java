package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
}