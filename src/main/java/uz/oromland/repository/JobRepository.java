package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.Camp;
import uz.oromland.entity.Job;
import uz.oromland.entity.Sanatorium;
import uz.oromland.payload.JobDTO;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<JobDTO> findAllByCampAndSanatorium(Camp camp, Sanatorium sanatorium);
}