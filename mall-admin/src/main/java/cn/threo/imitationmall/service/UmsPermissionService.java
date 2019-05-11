package cn.threo.imitationmall.service;

import cn.threo.imitationmall.dto.UmsPermissionNode;
import cn.threo.imitationmall.model.UmsPermission;

import java.util.List;

/**
 * @author threo
 * 后台用户权限管理Service
 */
public interface UmsPermissionService {

    /**
     * 创建权限
     */
    int create(UmsPermission permission);

    /**
     * 修改权限
     */
    int update(Long id, UmsPermission permission);

    /**
     * 批量删除权限
     */
    int delete(List<Long> ids);

    /**
     * 获得所有权限
     */
    List<UmsPermission> list();

    /**
     * 以层级结构获得所有权限
     */
    List<UmsPermissionNode> tList();
}
