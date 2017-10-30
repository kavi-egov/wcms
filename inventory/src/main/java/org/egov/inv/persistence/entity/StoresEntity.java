/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.inv.persistence.entity;




import io.swagger.model.AuditDetails;
import io.swagger.model.Department;
import io.swagger.model.Employee;
import io.swagger.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoresEntity {

   private String id = null;
   
    private String code = null;
    
    private String name = null;
    
    private String description = null;
    
    private String department = null;
    
    private String storeInCharge = null;
    
    private String billingAddress = null;
    
    private String deliveryAddress = null;
    
    private String contactno1 = null;
    
    private String contactno2 = null;
    
    private String email = null;
    
    private Boolean isCentralStore = null;
    
    private Boolean active = null;
    
    private String tenantId = null;
    
    private String createdBy = null;
    
    private Long createdTime = null;
    
    private String lastmodifiedBy = null;
    
    private Long lastmodifiedTime = null;
    
    public Store toDomain(){
        Store store = new Store();
        store.setId(id);
        store.setCode(code);
        store.setName(name);
        store.setDescription(description);
        store.setActive(active);
        store.setBillingAddress(billingAddress);
        store.setDeliveryAddress(deliveryAddress);
        store.setContactNo1(contactno1);
        store.setContactNo2(contactno2);
        store.setEmail(email);
        store.setIsCentralStore(isCentralStore);
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(createdBy);
        auditDetails.setCreatedTime(createdTime);
        auditDetails.setLastModifiedBy(lastmodifiedBy);
        auditDetails.setLastModifiedTime(lastmodifiedTime);
        store.setAuditDetails(auditDetails);
        Department dept = new Department();
        dept.setCode(department);
        store.setDepartment(dept);
        Employee employee = new Employee();
        employee.setCode(storeInCharge);
        store.setStoreInCharge(employee);
        return store;
        
    }
    
}
