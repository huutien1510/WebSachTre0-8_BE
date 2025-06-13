package ute.dto.request;

import lombok.Data;

@Data
public class ReadingProgressRequest {
    private Integer userId;
    private Integer secondsRead;
} 