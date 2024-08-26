package com.mindex.challenge.data;

public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;

    public ReportingStructure(Employee e, int numReports){
        employee = e;
        numberOfReports = numReports;
    }

    public Employee getEmployee(){
        return employee;
    }

    public int getNumberOfReports(){
        return numberOfReports;
    }
}
