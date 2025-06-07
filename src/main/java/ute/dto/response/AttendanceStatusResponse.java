package ute.dto.response;

@lombok.Data
@lombok.AllArgsConstructor
public class AttendanceStatusResponse {
    private boolean checkedInToday;
    private int totalPoints;
    private int streak;
    private int remainingRecoveries;
    private boolean canRecover;
    private boolean isFirstCheckIn;
    private boolean isRecovery;
}
