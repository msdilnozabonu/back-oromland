package uz.oromland.service;


import org.springframework.web.multipart.MultipartFile;
import uz.oromland.payload.ApplyDTO;
import uz.oromland.payload.JobDTO;

import java.util.List;

public interface JobService {


    List<JobDTO> getAllJobs();

    Long applyForJob(Long jobId, ApplyDTO jobDTO, MultipartFile cv);

    JobDTO getJobById(Long jobId);

}
