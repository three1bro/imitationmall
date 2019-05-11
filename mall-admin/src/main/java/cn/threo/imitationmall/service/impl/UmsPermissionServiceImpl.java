package cn.threo.imitationmall.service.impl;

import cn.threo.imitationmall.dto.UmsPermissionNode;
import cn.threo.imitationmall.mapper.UmsPermissionMapper;
import cn.threo.imitationmall.model.UmsPermission;
import cn.threo.imitationmall.model.UmsPermissionExample;
import cn.threo.imitationmall.service.UmsPermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author threo
 * 后台用户权限管理Service实现类
 */
@Service
public class UmsPermissionServiceImpl implements UmsPermissionService {

    @Autowired
    private UmsPermissionMapper permissionMapper;

    @Override
    public int create(UmsPermission permission) {
        permission.setStatus(1);
        permission.setCreateTime(new Date());
        permission.setSort(0);
        return permissionMapper.insert(permission);
    }

    @Override
    public int update(Long id, UmsPermission permission) {
        permission.setId(id);
        return permissionMapper.updateByPrimaryKey(permission);
    }

    @Override
    public int delete(List<Long> ids) {
        UmsPermissionExample epl = new UmsPermissionExample();
        epl.createCriteria().andIdIn(ids);
        return permissionMapper.deleteByExample(epl);
    }

    @Override
    public List<UmsPermission> list() {
        return permissionMapper.selectByExample(new UmsPermissionExample());
    }

    /**
     * 返回以顶级权限的层级结构
     */
    @Override
    public List<UmsPermissionNode> tList() {
        List<UmsPermission> permissionList = list();
        return permissionList.stream()
                .filter(permission -> permission.getPid().equals(0L))
                .map(permission -> convertToNode(permission, permissionList))
                .collect(Collectors.toList());
    }

    /**
     * 将权限列表装换成带层级表示的权限
     */
    private UmsPermissionNode convertToNode(UmsPermission permission, List<UmsPermission> permissionList) {
        UmsPermissionNode node = new UmsPermissionNode();
        BeanUtils.copyProperties(permission, node);
        List<UmsPermissionNode> children = permissionList.stream()
                .filter(subPermission -> subPermission.getPid().equals(permission.getId()))
                .map(subPermission -> convertToNode(subPermission, permissionList))
                .collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
