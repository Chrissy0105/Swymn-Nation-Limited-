package com.swymnation.dms.service;

import com.swymnation.dms.model.*;
import com.swymnation.dms.repository.AttendanceRepository;
import com.swymnation.dms.repository.ClientRepository; // Required for AC 2
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class AttendanceManager {

    private final AttendanceRepository attendanceRepository;
    private final ClientRepository clientRepository; // For Validation (AC 2)
    private final AccessControl accessControl;
    private final NotificationScheduler notificationScheduler; // For Notifications (SR 5)

    @Autowired 
    public AttendanceManager(AttendanceRepository repo, 
                             ClientRepository clientRepo,
                             AccessControl accessControl,
                             NotificationScheduler notificationScheduler) {
        this.attendanceRepository = repo;
        this.clientRepository = clientRepo;
        this.accessControl = accessControl;
        this.notificationScheduler = notificationScheduler;
    }

    @Transactional
    public List<Attendance> recordBatchAttendance(User instructor, List<Attendance> records) {
        // 1. Security Check [SR 2]
        accessControl.enforceAuthorization(instructor, "Mark Attendance", UserRole.INSTRUCTOR, UserRole.ADMINISTRATOR);

        for (Attendance record : records) {
            // 2. Validation Check [AC 2]: Ensure student exists
            if (!clientRepository.existsById(record.getStudentId())) {
                throw new IllegalArgumentException("Cannot mark attendance: Student ID " + record.getStudentId() + " is unregistered.");
            }

            // 3. Identity Stamping [SR 4]: Force the instructor ID to be the current user
            record.setMarkedByUserId(instructor.getId());

            // 4. Notification Logic [SR 5 / AC 5]
            if ("ABSENT".equalsIgnoreCase(record.getStatus())) {
                triggerAbsenceNotification(record);
            }
        }

        // 5. Save All [SR 3]
        return attendanceRepository.saveAll(records);
    }

    private void triggerAbsenceNotification(Attendance record) {
        // Fetch client details to get email
        Client student = clientRepository.findById(record.getStudentId()).orElse(null);
        
        if (student != null) {
            // Create a temporary User object for the notification target (since Client isn't User in this simple model)
            User target = new User(student.getFirstName(), student.getEmail(), UserRole.CLIENT);
            target.setNotificationPreferences(Set.of("EMAIL")); // Assume consent for absence alerts

            Notification n = new Notification(
                "ABSENCE_ALERT",
                target,
                "Missed Class Alert",
                "You have been marked absent for the class on " + record.getAttendanceDate().toLocalDate(),
                "EMAIL"
            );
            
            // Schedule immediate dispatch
            notificationScheduler.scheduleNotification(n, 0);
        }
    }

    public List<Attendance> getHistory(User user, Long studentId) {
        // Security Check
        accessControl.enforceStudentAccess(user, studentId);
        return attendanceRepository.findByStudentId(studentId); 
    }
}