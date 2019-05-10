package cn.threo.imitationmall.service;

import cn.threo.imitationmall.model.UmsPermission;
import cn.threo.imitationmall.model.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author threo
 * 后台角色管理 Services
 */
public interface UmsRoleServices {

    /**
     * 添加一个角色
     */
    int create(UmsRole role);

    /**
     * 修改一个角色
     */
    int update(Long id, UmsRole role);

    /**
     * 批量删除角色
     */
    int delete(List<Long> ids);

    /**
     * 获取指定角色的权限
     */
    List<UmsPermission> getPermissionList(Long roleId);

    /**
     * 修改指定角色的权限
     */
    @Transactional
    int updatePermission(Long roleId, List<Long> permissionId);


    /**
     * 获取所有角色列表
     */
    List<UmsRole> list();
}
