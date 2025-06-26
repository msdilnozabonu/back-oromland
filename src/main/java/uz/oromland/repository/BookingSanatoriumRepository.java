package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.BookingSanatorium;

@Repository
public interface BookingSanatoriumRepository extends JpaRepository<BookingSanatorium, Long> {

}