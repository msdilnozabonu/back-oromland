package uz.oromland.mapper;

import uz.oromland.entity.Job;
import uz.oromland.payload.JobDTO;

public interface JobMapper {

    JobDTO toJobDTO(Job job);
}
