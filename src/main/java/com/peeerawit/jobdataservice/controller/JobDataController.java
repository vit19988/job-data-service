package com.peeerawit.jobdataservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peeerawit.jobdataservice.entity.JobData;
import com.peeerawit.jobdataservice.service.JobDataSearchService;

@RestController
@RequestMapping("/job_data")
public class JobDataController extends BaseController {
    private final JobDataSearchService jobDataSearchService;

    public JobDataController(JobDataSearchService jobDataSearchService) {
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
