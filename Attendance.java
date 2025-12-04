package com.swymnation.dms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_records")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // In a real app, this would link to a Student entity.
    // For now, we store the Student ID directly to keep it simple.
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "status", nullable = false)
    private String status; // PRESENT, ABSENT, EXCUSED

    @Column(name = "attendance_date", nullable = false)
    private LocalDateTime attendanceDate;

    // Audit fields
    @Column(name = "marked_by_user_id", nullable = false)
    private Long markedByUserId;

    @Column(name = "modified_timestamp", nullable = false)
    private LocalDateTime modifiedOn;

    public Attendance() {
        this.attendanceDate = LocalDateTime.now();
        this.modifiedOn = LocalDateTime.now();
    }

    // Getters and Setters 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status;
        this.modifiedOn = LocalDateTime.now();
    }
    public LocalDateTime getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDateTime date) { this.attendanceDate = date; }
    public Long getMarkedByUserId() { return markedByUserId; }
    public void setMarkedByUserId(Long userId) { this.markedByUserId = userId; }
} 