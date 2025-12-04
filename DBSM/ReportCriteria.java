package com.swymnation.dms.model;

import java.time.LocalDate;

public record ReportCriteria(
        LocalDate startDate,
        LocalDate endDate,
        Long classId,
        Long studentId,
        String reportType // e.g., "FINANCIAL", "ATTENDANCE"
) {
}
