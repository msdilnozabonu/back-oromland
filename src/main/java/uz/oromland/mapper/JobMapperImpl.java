package uz.oromland.mapper;

import org.springframework.stereotype.Component;
import uz.oromland.entity.Job;
import uz.oromland.payload.JobDTO;

@Component
public class JobMapperImpl implements JobMapper{
    @Override
    public JobDTO toJobDTO(Job job) {
        return new JobDTO(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getSalary(),
                job.getJobType(),
                job.getCamp() != null ? job.getCamp().getId() : null,
                job.getSanatorium() != null ? job.getSanatorium().getId() : null,
                job.getIsActive(),
                job.getCreatedBy(),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }
}
