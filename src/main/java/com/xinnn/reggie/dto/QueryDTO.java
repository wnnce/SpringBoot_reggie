package com.xinnn.reggie.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 封装查询参数
 */
@Data
public class QueryDTO {
    private Long userId;
    private Integer page;
    private Integer pageSize;
    private Long number;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
}
