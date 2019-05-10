package cn.threo.imitationmall.bo;

import lombok.Data;

/**
 * @author threo
 * 用于Controller的日志封装类
 */
@Data
public class WebLog {

    private String description;
    private Long startTime;
    private String username;
    private Integer spendTime;
    private String basePath;
    private String uri;
    private String url;
    private String method;
    private String ip;
    private Object result;
    private Object parameter;
}
