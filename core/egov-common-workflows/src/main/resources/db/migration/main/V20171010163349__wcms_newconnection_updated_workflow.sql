update eg_wf_types set link='/wc/application/update/:id' where type='WaterConnection'  and typefqn='org.egov.wcms.transanction.model.Connection';

delete from eg_wf_matrix where objecttype='WaterConnection' and tenantid='default' ;

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnection', 'NEW', NULL, NULL, 'Clerk', 'NEWCONNECTION', 'Junior Engineer Approval Pending', 'Junior Engineer Approval Pending', 'Junior Engineer', 'Clerk Approved', 'Forward,Cancel', NULL, NULL, '2017-04-01', '2099-03-31', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnection', 'Junior Engineer Approval Pending', NULL, NULL, 'Junior Engineer', 'NEWCONNECTION', 'Junior Engineer Approved', 'Chief Officer Approval Pending', 'Chief Officer', 'Junior Engineer Approved', 'Forward,Reject', NULL, NULL, '2017-04-01', '2099-03-31', 'default');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnection', 'Junior Engineer Approved', NULL, NULL, 'Chief Officer', 'NEWCONNECTION', 'Chief Officer Approved', 'Chief Officer Approved', 'Clerk', 'Chief Officer approved', 'Approve,Reject', NULL, NULL, '2017-04-01', '2099-03-31', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnection', 'Chief Officer approved', NULL, NULL, 'Clerk', 'NEWCONNECTION', 'Fees Payment Pending', 'Fees Payment Pending', 'Clerk', 'Fees Payment Pending', NULL, NULL, NULL, '2017-04-01', '2099-03-31', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnection', 'Fees Payment Pending', NULL, NULL, 'Clerk', 'NEWCONNECTION', 'WorkOrder print Pending', 'WorkOrder print Pending', 'Junior Engineer', NULL, 'Generate WorkOrder', NULL, NULL, '2017-04-01', '2099-03-31', 'default');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnection', 'WorkOrder print Pending', NULL, NULL, 'Junior Engineer', 'NEWCONNECTION', 'END', 'END', NULL, NULL, 'Execute Tap', NULL, NULL, '2017-04-01', '2099-03-31', 'default');



INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, tenantId) VALUES (NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'WaterConnection', 'Rejected', NULL, NULL, 'Clerk', 'NEWCONNECTION', 'Junior Engineer Approval Pending', 'Junior Engineer Approval Pending', 'Junior Engineer', 'Clerk Approved', 'Forward,Cancel', NULL, NULL, '2017-04-01', '2099-03-31', 'default');
