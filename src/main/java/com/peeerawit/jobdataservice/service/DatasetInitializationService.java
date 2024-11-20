package com.peeerawit.jobdataservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peeerawit.jobdataservice.entity.JobData;
import com.peeerawit.jobdataservice.repository.JobDataRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DatasetInitializationService {
    private final JobDataRepository jobDataRepository;

    public DatasetInitializationService(JobDataRepository jobDataRepository) {
        this.jobDataRepository = jobDataRepository;
    }

    @PostConstruct
    public void initData() {
        Resource resource = new ClassPathResource("static/salary_survey-3.json");

        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = resource.getInputStream()) {
            //todo: improve the algorithm converting 'Salary' from String to BigDecimal
            JobData[] jobData = mapper.readValue(inputStream, JobData[].class);
            List<JobData> employeeList = Arrays.asList(jobData);
            jobDataRepository.saveAll(employeeList);
        } catch (IOException e) {
            throw new UncheckedIOException("Error when initializing data", e);
        }
    }
}
