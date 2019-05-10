package cn.threo.imitationmall.service.impl;

import cn.threo.imitationmall.dao.UmsRolePermissionRelationDao;
import cn.threo.imitationmall.mapper.UmsRoleMapper;
import cn.threo.imitationmall.mapper.UmsRolePermissionRelationMapper;
import cn.threo.imitationmall.model.*;
import cn.threo.imitationmall.service.UmsRoleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UmsRoleServicesImpl implements UmsRoleServices {

    @Autowired
    private UmsRoleMapper roleMapper;

    @Autowired
    private UmsRolePermissionRelationMapper rolePermissionRelationMapper;

    @Autowired
    private UmsRolePermissionRelationDao rolePermissionRelationDao;


    @Override
    public int create(UmsRole role) {
        role.setCreateTime(new Date());
        role.setStatus(1);
        role.setAdminCount(0);
        role.setSort(0);
        return roleMapper.insert(role);
    }

    @Override
    public int update(Long id, UmsRole role) {
        role.setId(id);
        return roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public int delete(List<Long> ids) {
        UmsRoleExample epl = new UmsRoleExample();
        epl.createCriteria().andIdIn(ids);
        return roleMapper.deleteByExample(epl);
    }

    @Override
    public List<UmsPermission> getPermissionList(Long roleId) {
        return rolePermissionRelationDao.getPermissionList(roleId);
    }

    @Override
    public int updatePermission(Long roleId, List<Long> permissionId) {
//        删除旧有关系
        UmsRolePermissionRelationExample epl = new UmsRolePermissionRelationExample();
        epl.createCriteria().andRoleIdEqualTo(roleId);
        rolePermissionRelationMapper.deleteByExample(epl);
//        创建新的关系
//        使用流批量创建新的关系并加入list
        List<UmsRolePermissionRelation> list = new ArrayList<>();
        permissionId.stream().forEach(id -> {
            UmsRolePermissionRelation relation = new UmsRolePermissionRelation();
            relation.setRoleId(roleId);
            relation.setPermissionId(id);
            list.add(relation);
        });
        return rolePermissionRelationDao.insertList(list);
    }

    @Override
    public List<UmsRole> list() {
        return roleMapper.selectByExample(new UmsRoleExample());
    }
}
