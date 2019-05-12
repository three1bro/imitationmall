package cn.threo.imitationmall.service.impl;

import cn.threo.imitationmall.mapper.UmsMemberLevelMapper;
import cn.threo.imitationmall.model.UmsMemberLevel;
import cn.threo.imitationmall.model.UmsMemberLevelExample;
import cn.threo.imitationmall.service.UmsMemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author threo
 * 会员等级管理实现类
 */
@Service
public class UmsMemberLevelServiceImpl implements UmsMemberLevelService {

    @Autowired
    private UmsMemberLevelMapper memberLevelMapper;

    @Override
    public List<UmsMemberLevel> list(Integer defaultStatus) {
        UmsMemberLevelExample epl = new UmsMemberLevelExample();
        epl.createCriteria().andDefaultStatusEqualTo(defaultStatus);
        return memberLevelMapper.selectByExample(epl);
    }
}
