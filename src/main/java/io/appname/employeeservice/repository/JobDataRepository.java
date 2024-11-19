package io.appname.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.appname.employeeservice.entity.JobData;

@Repository
public interface JobDataRepository extends JpaRepository<JobData, Long> {

}
