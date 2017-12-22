package org.egov.lcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.AuditDetails;
import org.egov.lcms.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/** 
* 
* Author		Date			eGov-JIRA ticket	Commit message
* ---------------------------------------------------------------------------
* Veswanth		15th Nov 2017						Initial commit for EventRowMapper
*/
@Component
public class EventRowMapper implements RowMapper<Event> {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@Override
	public Event mapRow(ResultSet rs, int rowNum) throws SQLException {

		Event event = new Event();
		event.setCode(rs.getString("code"));
		event.setEntity(getString(rs.getString("entity")));
		event.setModuleName(getString(rs.getString("modulename")));
		event.setEntityCode(getString(rs.getString("entitycode")));
		event.setCaseNo(getString(rs.getString("caseno")));
		event.setDepartmentConcernPerson(getString(rs.getString("departmentconcernperson")));
		event.setNextHearingTime(getString(rs.getString("nexthearingtime")));
		event.setNextHearingDate(getLong(rs.getLong("nexthearingdate")));
		event.setTenantId(getString(rs.getString("tenantid")));

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(rs.getString("createdby"));
		auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
		auditDetails.setCreatedTime(rs.getBigDecimal("createdtime"));
		auditDetails.setLastModifiedTime(rs.getBigDecimal("lastmodifiedtime"));
		event.setAuditDetails(auditDetails);

		return event;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
	}
}