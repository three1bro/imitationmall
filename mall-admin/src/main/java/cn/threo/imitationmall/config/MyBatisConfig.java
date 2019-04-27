package cn.threo.imitationmall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author threo
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"cn.threo.imitationmall.mapper", "cn.threo.imitationmall.dao"})
public class MyBatisConfig {
}
