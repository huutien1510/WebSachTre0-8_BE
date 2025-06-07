package ute.dto.response;

@lombok.Data
@lombok.AllArgsConstructor
public class AttendanceCheckinResponse {
    private boolean success;
    private int pointsReceived;
    private int totalPoints;
    private int streak;
    private boolean isRecovery;
}
