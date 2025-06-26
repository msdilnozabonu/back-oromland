package uz.oromland.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.oromland.entity.Apply;
import uz.oromland.payload.ApplyDTO;
import uz.oromland.payload.JobDTO;
import uz.oromland.service.JobService;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<JobDTO>> getJobDTO() {

        List<JobDTO> jobDTOS = jobService.getAllJobs();

        return ResponseEntity.ok(jobDTOS);

    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long jobId) {
        JobDTO jobDTO = jobService.getJobById(jobId);
        if (jobDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobDTO);
    }

    @PostMapping("/{jobId}/apply")
    private ResponseEntity<?> applyForJob(@PathVariable Long jobId, @RequestBody ApplyDTO applyDTO, @RequestPart(name = "cv") MultipartFile cv) {

        Long id = jobService.applyForJob(jobId, applyDTO, cv);

        if (id == null) {
            return ResponseEntity.badRequest().body("Failed to apply for the job. Please check the job ID and try again.");
        }

        return ResponseEntity.ok("Application submitted successfully with ID: " + id);

    }


}
