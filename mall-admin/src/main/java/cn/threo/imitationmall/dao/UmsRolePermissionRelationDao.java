package cn.threo.imitationmall.dao;

import cn.threo.imitationmall.model.UmsPermission;
import cn.threo.imitationmall.model.UmsRolePermissionRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author threo
 * 后台角色管理系统自定义的Dao
 */
public interface UmsRolePermissionRelationDao {

    /**
     * 批量插入角色和权限关系
     */
    int insertList(@Param("List") List<UmsRolePermissionRelation> List);

    /**
     * 根据角色获取权限
     */
    List<UmsPermission> getPermissionList(@Param("roleId") Long roleId);
}
