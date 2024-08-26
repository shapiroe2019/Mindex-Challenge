package com.mindex.challenge.data;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Compensation {
    @Id
    private String id;

    private String employeeId;
    private int salary;
    private Date effectiveDate;

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    public String getEmployeeId() {
        return employeeId;
    }
    public void setSalary(int salary) {this.salary = salary;}
    public int getSalary(){return salary;}
    public void setEffectiveDate(Date effectiveDate) {this.effectiveDate = effectiveDate;}
    public Date getEffectiveDate(){return effectiveDate;}
}
