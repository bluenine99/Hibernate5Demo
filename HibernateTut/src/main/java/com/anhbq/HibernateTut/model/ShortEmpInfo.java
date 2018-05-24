package com.anhbq.HibernateTut.model;

public class ShortEmpInfo {
	private Long empId;
	private String empNo;
	private String empName;

	//Cấu tử có 3 tham số, sẽ được sử dụng trong Hibernate Query.
	public ShortEmpInfo(Long empId, String empNo, String empName) {
		this.empId = empId;
		this.empNo = empNo;
		this.empName = empName;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}
}
