package com.swymnation.dms.service;

import com.swymnation.dms.model.ReportCriteria;
import com.swymnation.dms.model.ReportData;
import com.swymnation.dms.repository.ReportDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportAggregator {

    private final ReportDataSource reportDataSource;

    @Autowired
    public ReportAggregator(ReportDataSource reportDataSource) {
        this.reportDataSource = reportDataSource;
    }

    public ReportData generateReportData(ReportCriteria criteria) {
        long start = System.currentTimeMillis();
        
        // 1. Fetch Data [FR-05 SR 1]
        ReportData data = reportDataSource.fetchAggregatedData(criteria);

        long duration = System.currentTimeMillis() - start;
        
        // 2. Performance Check [FR-05 SR 5] (<10s limit)
        if (duration > 10000) {
            System.err.println("PERF WARNING: Report generation took too long: " + duration + "ms");
        }

        return data;
    }
}