package com.peeerawit.jobdataservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.peeerawit.jobdataservice.entity.JobData;

@Repository
public interface JobDataRepository extends JpaRepository<JobData, Long> {

}
