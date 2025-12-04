package com.swymnation.dms.model;

public class ReportData {
    private String summary;
    private int recordCount;

    public ReportData(String summary, int recordCount) {
        this.summary = summary;
        this.recordCount = recordCount;
    }

    public String getSummary() { return summary; }
    public int getRecordCount() { return recordCount; }
}