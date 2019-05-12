package cn.threo.imitationmall.service;

import cn.threo.imitationmall.model.UmsMemberLevel;

import java.util.List;

/**
 * @author threo
 * 会员等级管理Service
 */
public interface UmsMemberLevelService {
    /**
     * 获取所有的会员等级
     *
     * @param defaultStatus 是否是默认会员
     */
    List<UmsMemberLevel> list(Integer defaultStatus);
}
