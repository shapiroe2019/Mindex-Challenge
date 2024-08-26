package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee read request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    //GET request to return employee and total number of suboordinates for a given employee id
    //from employee repository
    @GetMapping("/employee/{id}/reportingstructure")
    public ReportingStructure reportingStructure(@PathVariable String id) {
        LOG.debug("Received employee report request for id [{}]", id);
        return employeeService.getReportingStructure(id);
    }

    //POST request to add new compensation to compensation repository
    //Compensation should have a valid employee id
    @PostMapping("/employee/{id}/compensation")
    public Compensation createCompensation(@PathVariable String id, @RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request for [{}]", compensation);

        return employeeService.createCompensation(compensation);
    }

    //GET request to read compensation from compensation repository
    @GetMapping("/employee/{id}/compensation")
    public Compensation readCompensation(@PathVariable String id) {
        LOG.debug("Received compensation read request for employee id [{}]", id);

        return employeeService.readCompensation(id);
    }
}
