package cn.threo.imitationmall.dao;

import cn.threo.imitationmall.model.UmsAdminRoleRelation;
import cn.threo.imitationmall.model.UmsPermission;
import cn.threo.imitationmall.model.UmsRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author threo
 * 后台用户与角色关系 Dao
 */
public interface UmsAdminRoleRelationDao {

    int insertList(@Param("list") List<UmsAdminRoleRelation> umsAdminRoleRelations);

    List<UmsRole> getRoleList(@Param("adminId") Long adminId);

    List<UmsPermission> getPermissionList(@Param("adminId") Long adminId);

    List<UmsPermission> getRolePermissionList(@Param("adminId") Long adminId);
}
