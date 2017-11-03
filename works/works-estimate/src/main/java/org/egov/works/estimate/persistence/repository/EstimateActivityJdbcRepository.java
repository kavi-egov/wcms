package org.egov.works.estimate.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.persistence.entity.EstimateActivityEntity;
import org.egov.works.estimate.web.contract.EstimateActivity;
import org.egov.works.estimate.web.contract.EstimateActivitySearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class EstimateActivityJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egw_estimate_activity";

	public List<EstimateActivity> search(
			EstimateActivitySearchContract estimateActivitySearchContract) {
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (estimateActivitySearchContract.getSortBy() != null
				&& !estimateActivitySearchContract.getSortBy().isEmpty()) {
			validateSortByOrder(estimateActivitySearchContract.getSortBy());
			validateEntityFieldName(estimateActivitySearchContract.getSortBy(), EstimateActivity.class);
		}

		String orderBy = "order by id";
		if (estimateActivitySearchContract.getSortBy() != null
				&& !estimateActivitySearchContract.getSortBy().isEmpty()) {
			orderBy = "order by " + estimateActivitySearchContract.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		if (estimateActivitySearchContract.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", estimateActivitySearchContract.getTenantId());
		}
		if (estimateActivitySearchContract.getIds() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id in(:ids) ");
			paramValues.put("ids", estimateActivitySearchContract.getIds());
		}

		if (estimateActivitySearchContract.getDetailedEstimateIds() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("detailedEstimate in(:detailedEstimateIds) ");
			paramValues.put("detailedEstimateIds", estimateActivitySearchContract.getDetailedEstimateIds());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(EstimateActivityEntity.class);

		List<EstimateActivityEntity> estimateActivityEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		List<EstimateActivity> estimateActivities = new ArrayList<>();

		for (EstimateActivityEntity estimateOverheadEntity : estimateActivityEntities) {
			estimateActivities.add(estimateOverheadEntity.toDomain());
		}

		return estimateActivities;
	}

}
