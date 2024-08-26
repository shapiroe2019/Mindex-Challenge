package com.mindex.challenge.service.impl;

import java.util.List;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    //Returns reporting structure with employee
    //and total number of employees reporting to this employee
    @Override
    public ReportingStructure getReportingStructure(String id){
        LOG.debug("Searching for reports for employee [{}]", id);
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        int numReports = getNumReports(employee.getEmployeeId());
        ReportingStructure reportingStructure = new ReportingStructure(employee, numReports);
        return reportingStructure;
    }

    //Recursively finds total number of an employee's suboordinates
    private int getNumReports(String employeeId){
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }
        List<Employee> directReports = employee.getDirectReports();
        if(directReports == null){
            return 0;
        }
        int nReports = directReports.size();
        for(Employee e : directReports){
            nReports += getNumReports(e.getEmployeeId());
        }
        return nReports;
    }

    //Adds compensation to compensation repository
    @Override
    public Compensation createCompensation(Compensation compensation){
        LOG.debug("Creating compensation [{}]", compensation);
        Employee employee = employeeRepository.findByEmployeeId(compensation.getEmployeeId());
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + compensation.getEmployeeId());
        }

        compensationRepository.insert(compensation);

        return compensation;
    }

    //Reads compensation from compensation repository
    @Override
    public Compensation readCompensation(String employeeId){
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        Compensation compensation = compensationRepository.findByEmployeeId(employeeId);
        return compensation;
    }

}
