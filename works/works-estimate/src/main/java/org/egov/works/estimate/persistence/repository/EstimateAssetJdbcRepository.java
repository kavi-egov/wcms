package org.egov.works.estimate.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.persistence.entity.AssetsForEstimateEntity;
import org.egov.works.estimate.web.contract.AssetsForEstimate;
import org.egov.works.estimate.web.contract.EstimateAssetSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class EstimateAssetJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egw_estimate_assets";

	public List<AssetsForEstimate> search(
			EstimateAssetSearchContract assetSearchContract) {
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (assetSearchContract.getSortBy() != null
				&& !assetSearchContract.getSortBy().isEmpty()) {
			validateSortByOrder(assetSearchContract.getSortBy());
			validateEntityFieldName(assetSearchContract.getSortBy(), AssetsForEstimate.class);
		}

		String orderBy = "order by id";
		if (assetSearchContract.getSortBy() != null
				&& !assetSearchContract.getSortBy().isEmpty()) {
			orderBy = "order by " + assetSearchContract.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		if (assetSearchContract.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", assetSearchContract.getTenantId());
		}
		if (assetSearchContract.getIds() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id in(:ids) ");
			paramValues.put("ids", assetSearchContract.getIds());
		}

		if (assetSearchContract.getDetailedEstimateIds() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("detailedEstimate in(:detailedEstimateIds) ");
			paramValues.put("detailedEstimateIds", assetSearchContract.getDetailedEstimateIds());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(AssetsForEstimateEntity.class);

		List<AssetsForEstimateEntity> assetsForEstimateEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		List<AssetsForEstimate> assetsForEstimates = new ArrayList<>();

		for (AssetsForEstimateEntity assetsForEstimateEntity : assetsForEstimateEntities) {
			assetsForEstimates.add(assetsForEstimateEntity.toDomain());
		}

		return assetsForEstimates;
	}

}
