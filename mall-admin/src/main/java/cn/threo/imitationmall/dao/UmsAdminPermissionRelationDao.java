package cn.threo.imitationmall.dao;

import cn.threo.imitationmall.model.UmsAdminPermissionRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author threo
 */
public interface UmsAdminPermissionRelationDao {

    int insertList(@Param("list") List<UmsAdminPermissionRelation> list);
}
