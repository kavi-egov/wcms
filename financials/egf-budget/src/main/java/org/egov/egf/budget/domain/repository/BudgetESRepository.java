/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *
 *       Copyright (C) <2015>  eGovernments Foundation
 *
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.budget.domain.repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.domain.model.Pagination;
import org.egov.common.persistence.repository.ESRepository;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.egov.egf.budget.web.contract.BudgetSearchContract;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetESRepository extends ESRepository {

	private TransportClient esClient;
	private ElasticSearchQueryFactory elasticSearchQueryFactory;

	public BudgetESRepository(TransportClient esClient, ElasticSearchQueryFactory elasticSearchQueryFactory) {
		this.esClient = esClient;
		this.elasticSearchQueryFactory = elasticSearchQueryFactory;
	}

	public Pagination<Budget> search(BudgetSearchContract budgetSearchContract) {
		final SearchRequestBuilder searchRequestBuilder = getSearchRequest(budgetSearchContract);
		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		return mapToBudgetList(searchResponse);
	}

	@SuppressWarnings("deprecation")
	private Pagination<Budget> mapToBudgetList(SearchResponse searchResponse) {
		Pagination<Budget> page = new Pagination<>();
		if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() == 0L) {
			return page;
		}
		List<Budget> budgets = new ArrayList<Budget>();
		Budget budget = null;
		for (SearchHit hit : searchResponse.getHits()) {

			ObjectMapper mapper = new ObjectMapper();
			// JSON from file to Object
			try {
				budget = mapper.readValue(hit.sourceAsString(), Budget.class);
			} catch (JsonParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			budgets.add(budget);
		}

		page.setTotalResults(Long.valueOf(searchResponse.getHits().getTotalHits()).intValue());
		page.setPagedData(budgets);

		return page;
	}

	private SearchRequestBuilder getSearchRequest(BudgetSearchContract criteria) {
		List<String> orderByList = new ArrayList<>();
		if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {
			validateSortByOrder(criteria.getSortBy());
			validateEntityFieldName(criteria.getSortBy(), BudgetEntity.class);
			orderByList = elasticSearchQueryFactory.prepareOrderBys(criteria.getSortBy());
		}

		final BoolQueryBuilder boolQueryBuilder = elasticSearchQueryFactory.searchBudget(criteria);
		SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(Budget.class.getSimpleName().toLowerCase())
				.setTypes(Budget.class.getSimpleName().toLowerCase());
		if (!orderByList.isEmpty()) {
			for (String orderBy : orderByList) {
				searchRequestBuilder = searchRequestBuilder.addSort(orderBy.split(" ")[0],
						orderBy.split(" ")[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
			}
		}

		searchRequestBuilder.setQuery(boolQueryBuilder);
		return searchRequestBuilder;
	}

}