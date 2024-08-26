package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    @Test
    public void TestReportingStructure() {
        Employee testEmployee1 = new Employee();
        testEmployee1.setFirstName("John");
        testEmployee1.setLastName("Doe");
        testEmployee1.setDepartment("Engineering");
        testEmployee1.setPosition("Developer");

        Employee testEmployee2 = new Employee();
        testEmployee2.setFirstName("Piter");
        testEmployee2.setLastName("Smith");
        testEmployee2.setDepartment("Engineering");
        testEmployee2.setPosition("Developer");

        Employee testEmployee3 = new Employee();
        testEmployee3.setFirstName("Jim");
        testEmployee3.setLastName("Taylor");
        testEmployee3.setDepartment("Engineering");
        testEmployee3.setPosition("Developer");

        Employee createdEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();

        List<Employee> directReports2 = new ArrayList<>();
        directReports2.add(createdEmployee3);

        testEmployee2.setDirectReports(directReports2);
        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();

        List<Employee> directReports1 = new ArrayList<>();
        directReports1.add(createdEmployee2);
        testEmployee1.setDirectReports(directReports1);

        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();

        String employeeIdReportingStructureUrl = "http://localhost:" + port + "/employee/" + createdEmployee1.getEmployeeId() + "/reportingstructure";

        ReportingStructure reportingStructure = restTemplate.getForEntity(employeeIdReportingStructureUrl, ReportingStructure.class, createdEmployee1.getEmployeeId()).getBody();
        int numberOfReports = reportingStructure.getNumberOfReports();

        System.out.println("Expected: " + numberOfReports);
        assertEquals(2, numberOfReports);
    }

    @Test
    public void TestCompensation() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        Compensation compensation = new Compensation();
        compensation.setEmployeeId(createdEmployee1.getEmployeeId());
        compensation.setSalary(30000);

        Date effectiveDate = new Date();
        compensation.setEffectiveDate(effectiveDate);

        String employeeIdCompensationUrl = "http://localhost:" + port + "/employee/" + createdEmployee1.getEmployeeId() + "/compensation";

        //Test POST compensation request
        Compensation createdCompensation = restTemplate.postForEntity(employeeIdCompensationUrl, compensation, Compensation.class).getBody();
        assertCompensationEquivalence(compensation, createdCompensation);

        //Test GET compensation request
        Compensation foundCompensation = restTemplate.getForEntity(employeeIdCompensationUrl, Compensation.class).getBody();
        assertCompensationEquivalence(createdCompensation, foundCompensation);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
