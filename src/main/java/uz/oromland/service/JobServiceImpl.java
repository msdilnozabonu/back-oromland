package uz.oromland.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.oromland.entity.Apply;
import uz.oromland.entity.Attachment;
import uz.oromland.entity.Job;
import uz.oromland.exception.JobNotFoundException;
import uz.oromland.mapper.AttachmentMapper;
import uz.oromland.mapper.JobMapper;
import uz.oromland.payload.ApplyDTO;
import uz.oromland.payload.JobDTO;
import uz.oromland.repository.ApplyRepository;
import uz.oromland.repository.JobRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final AttachmentService attachmentService;
    private final AttachmentMapper attachmentMapper;
    private final ApplyRepository applyRepository;

    @Override
    public List<JobDTO> getAllJobs() {

        List<Job> jobList = jobRepository.findAll();
        return jobList.stream().map(jobMapper::toJobDTO).toList();

    }

    @Override
    public Long applyForJob(Long jobId, ApplyDTO applyDTO, MultipartFile cv) {

        Job job = jobRepository.findById(jobId).orElseThrow(() -> new JobNotFoundException("Job not found with ID: " + jobId));

        Attachment cvEntity = attachmentMapper.toEntity(attachmentService.upload(cv));


        Apply apply = new Apply(
                null,
                applyDTO.getFirstName(),
                applyDTO.getLastName(),
                applyDTO.getEmail(),
                applyDTO.getPhoneNumber(),
                applyDTO.getMessage(),
                applyDTO.getStatus(),
                applyDTO.getTelegramUsername(),
                job,
                cvEntity
        );

        Apply save = applyRepository.save(apply);
        return save.getId();

    }

    @Override
    public JobDTO getJobById(Long jobId) {

        Job job = jobRepository.findById(jobId).orElseThrow(() -> new JobNotFoundException("Job not found with ID: " + jobId));
        return jobMapper.toJobDTO(job);


    }
}
