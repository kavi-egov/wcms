package org.egov.workflow.domain.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeServiceResponse {

	@JsonProperty("Employees")
	private List<Employee> employee;

}
