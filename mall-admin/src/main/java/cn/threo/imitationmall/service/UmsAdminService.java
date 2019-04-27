package cn.threo.imitationmall.service;


import cn.threo.imitationmall.dto.UmsAdminParam;
import cn.threo.imitationmall.model.UmsAdmin;
import cn.threo.imitationmall.model.UmsPermission;
import cn.threo.imitationmall.model.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author threo
 * 后台管理员 service
 */
public interface UmsAdminService {

    UmsAdmin getAdminByUsername(String username);

    UmsAdmin register(UmsAdminParam umsAdminParam);

    String login(String username, String password);

    String refreshToken(String oldToken);

    UmsAdmin getAdminById(Long id);

    List<UmsAdmin> list(String name, Integer size, Integer page);

    int updateAdmin(Long id, UmsAdmin umsAdmin);

    int delete(Long id);

    @Transactional
    int updateRoles(Long id, List<Long> roles);

    List<UmsRole> getRoleList(Long id);

    @Transactional
    int updatePermission(Long id, List<Long> permissions);

    List<UmsPermission> getPermissionList(Long id);
}
