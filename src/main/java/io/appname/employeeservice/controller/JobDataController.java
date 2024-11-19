package io.appname.employeeservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.appname.employeeservice.entity.JobData;
import io.appname.employeeservice.service.JobDataSearchSearchService;

@RestController
@RequestMapping("/job_data")
public class JobDataController extends BaseController {
    private final JobDataSearchSearchService jobDataSearchService;

    public JobDataController(JobDataSearchSearchService jobDataSearchService) {
        this.jobDataSearchService = jobDataSearchService;
    }

    @GetMapping
    public List<JobData> getAllEmployee(@RequestParam Map<String, String> queryParams,
                                        @RequestParam(required = false) List<String> fields,
                                        @RequestParam(required = false) List<String> sort,
                                        @RequestParam(required = false, name = "sort_type") List<String> sortType
    ) {
        return jobDataSearchService.getAllEmployees(queryParams, fields, combineListsToMap(sort, sortType));
    }
}
