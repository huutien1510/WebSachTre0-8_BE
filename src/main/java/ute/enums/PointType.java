package ute.enums;

public enum PointType {
    FIRST_PURCHASE("Thưởng điểm lần mua đầu"),
    ORDER_VALUE("Tích điểm từ đơn hàng"),
    BIRTHDAY("Thưởng điểm sinh nhật"),
    CONTEST("Thưởng điểm từ cuộc thi"),
    LOYALTY("Thưởng điểm khách hàng thân thiết"),
    REDEEM("Đổi điểm"),
    REGISTER("Thưởng điểm đăng ký");

    private final String description;

    PointType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}