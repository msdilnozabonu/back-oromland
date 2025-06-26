package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.BookingCamp;

@Repository
public interface BookingCampRepository extends JpaRepository<BookingCamp, Long> {
}