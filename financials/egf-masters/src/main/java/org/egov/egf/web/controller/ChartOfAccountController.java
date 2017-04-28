package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContract;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContractRequest;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.ChartOfAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chartofaccounts")
public class ChartOfAccountController {
    @Autowired
    private ChartOfAccountService chartOfAccountService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public ChartOfAccountContractResponse create(@RequestBody @Valid ChartOfAccountContractRequest chartOfAccountContractRequest,
            BindingResult errors) {
        ModelMapper modelMapper = new ModelMapper();
        chartOfAccountService.validate(chartOfAccountContractRequest, "create", errors);
        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }
        chartOfAccountService.fetchRelatedContracts(chartOfAccountContractRequest);
        ChartOfAccountContractResponse chartOfAccountContractResponse = new ChartOfAccountContractResponse();
        chartOfAccountContractResponse.setChartOfAccounts(new ArrayList<ChartOfAccountContract>());
        for (ChartOfAccountContract chartOfAccountContract : chartOfAccountContractRequest.getChartOfAccounts()) {

            ChartOfAccount chartOfAccountEntity = modelMapper.map(chartOfAccountContract, ChartOfAccount.class);
            chartOfAccountEntity = chartOfAccountService.create(chartOfAccountEntity);
            ChartOfAccountContract resp = modelMapper.map(chartOfAccountEntity, ChartOfAccountContract.class);
            chartOfAccountContract.setId(chartOfAccountEntity.getId());
            chartOfAccountContractResponse.getChartOfAccounts().add(resp);
        }

        chartOfAccountContractResponse.setResponseInfo(getResponseInfo(chartOfAccountContractRequest.getRequestInfo()));

        return chartOfAccountContractResponse;
    }

    @PostMapping(value = "/{uniqueId}/_update")
    @ResponseStatus(HttpStatus.OK)
    public ChartOfAccountContractResponse update(@RequestBody @Valid ChartOfAccountContractRequest chartOfAccountContractRequest,
            BindingResult errors,
            @PathVariable Long uniqueId) {

        chartOfAccountService.validate(chartOfAccountContractRequest, "update", errors);

        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }
        chartOfAccountService.fetchRelatedContracts(chartOfAccountContractRequest);
        ChartOfAccount chartOfAccountFromDb = chartOfAccountService.findOne(uniqueId);

        ChartOfAccountContract chartOfAccount = chartOfAccountContractRequest.getChartOfAccount();
        // ignoring internally passed id if the put has id in url
        chartOfAccount.setId(uniqueId);
        ModelMapper model = new ModelMapper();
        model.map(chartOfAccount, chartOfAccountFromDb);
        chartOfAccountFromDb = chartOfAccountService.update(chartOfAccountFromDb);
        ChartOfAccountContractResponse chartOfAccountContractResponse = new ChartOfAccountContractResponse();
        chartOfAccountContractResponse.setChartOfAccount(chartOfAccount);
        chartOfAccountContractResponse.setResponseInfo(getResponseInfo(chartOfAccountContractRequest.getRequestInfo()));
        chartOfAccountContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
        return chartOfAccountContractResponse;
    }

    @GetMapping(value = "/{uniqueId}")
    @ResponseStatus(HttpStatus.OK)
    public ChartOfAccountContractResponse view(@ModelAttribute ChartOfAccountContractRequest chartOfAccountContractRequest,
            BindingResult errors,
            @PathVariable Long uniqueId) {
        chartOfAccountService.validate(chartOfAccountContractRequest, "view", errors);
        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }
        chartOfAccountService.fetchRelatedContracts(chartOfAccountContractRequest);
        RequestInfo requestInfo = chartOfAccountContractRequest.getRequestInfo();
        ChartOfAccount chartOfAccountFromDb = chartOfAccountService.findOne(uniqueId);
        ChartOfAccountContract chartOfAccount = chartOfAccountContractRequest.getChartOfAccount();

        ModelMapper model = new ModelMapper();
        model.map(chartOfAccountFromDb, chartOfAccount);

        ChartOfAccountContractResponse chartOfAccountContractResponse = new ChartOfAccountContractResponse();
        chartOfAccountContractResponse.setChartOfAccount(chartOfAccount);
        chartOfAccountContractResponse.setResponseInfo(getResponseInfo(chartOfAccountContractRequest.getRequestInfo()));
        chartOfAccountContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
        return chartOfAccountContractResponse;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ChartOfAccountContractResponse updateAll(
            @RequestBody @Valid ChartOfAccountContractRequest chartOfAccountContractRequest, BindingResult errors) {
        chartOfAccountService.validate(chartOfAccountContractRequest, "updateAll", errors);
        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }
        chartOfAccountService.fetchRelatedContracts(chartOfAccountContractRequest);

        ChartOfAccountContractResponse chartOfAccountContractResponse = new ChartOfAccountContractResponse();
        chartOfAccountContractResponse.setChartOfAccounts(new ArrayList<ChartOfAccountContract>());
        for (ChartOfAccountContract chartOfAccountContract : chartOfAccountContractRequest.getChartOfAccounts()) {
            ChartOfAccount chartOfAccountFromDb = chartOfAccountService.findOne(chartOfAccountContract.getId());

            ModelMapper model = new ModelMapper();
            model.map(chartOfAccountContract, chartOfAccountFromDb);
            chartOfAccountFromDb = chartOfAccountService.update(chartOfAccountFromDb);
            model.map(chartOfAccountFromDb, chartOfAccountContract);
            chartOfAccountContractResponse.getChartOfAccounts().add(chartOfAccountContract);
        }

        chartOfAccountContractResponse.setResponseInfo(getResponseInfo(chartOfAccountContractRequest.getRequestInfo()));
        chartOfAccountContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

        return chartOfAccountContractResponse;
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ChartOfAccountContractResponse search(@ModelAttribute ChartOfAccountContract chartOfAccountContracts,
            @RequestBody RequestInfo requestInfo, BindingResult errors) {
        final ChartOfAccountContractRequest chartOfAccountContractRequest = new ChartOfAccountContractRequest();
        chartOfAccountContractRequest.setChartOfAccount(chartOfAccountContracts);
        chartOfAccountContractRequest.setRequestInfo(requestInfo);
        chartOfAccountService.validate(chartOfAccountContractRequest, "search", errors);
        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }
        String tenantId = chartOfAccountContracts.getTenantId();
        // chartOfAccountService.fetchRelatedContracts(chartOfAccountContractRequest);
        ChartOfAccountContractResponse chartOfAccountContractResponse = new ChartOfAccountContractResponse();
        chartOfAccountContractResponse.setChartOfAccounts(new ArrayList<ChartOfAccountContract>());
        chartOfAccountContractResponse.setPage(new Pagination());
        Page<ChartOfAccount> allChartOfAccounts;
        ModelMapper model = new ModelMapper();

        allChartOfAccounts = chartOfAccountService.search(chartOfAccountContractRequest);
        ChartOfAccountContract chartOfAccountContract = null;
        for (ChartOfAccount b : allChartOfAccounts) {
            chartOfAccountContract = new ChartOfAccountContract();
            if (b.getParentId() != null) {
                if (!b.getParentId().getTenantId().equalsIgnoreCase(tenantId)) {
                    continue;
                }
            }
            model.map(b, chartOfAccountContract);
            chartOfAccountContractResponse.getChartOfAccounts().add(chartOfAccountContract);
        }
        chartOfAccountContractResponse.getPage().map(allChartOfAccounts);
        chartOfAccountContractResponse.setResponseInfo(getResponseInfo(chartOfAccountContractRequest.getRequestInfo()));
        chartOfAccountContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
        return chartOfAccountContractResponse;
    }

    private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        new ResponseInfo();
        return ResponseInfo.builder()
                .apiId(requestInfo.getApiId())
                .ver(requestInfo.getVer())
                .ts(new Date())
                .resMsgId(requestInfo.getMsgId())
                .resMsgId("placeholder")
                .status("placeholder")
                .build();
    }

}