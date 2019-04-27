package cn.threo.imitationmall.service.impl;

import cn.threo.imitationmall.dao.UmsAdminPermissionRelationDao;
import cn.threo.imitationmall.dao.UmsAdminRoleRelationDao;
import cn.threo.imitationmall.dto.UmsAdminParam;
import cn.threo.imitationmall.mapper.UmsAdminLoginLogMapper;
import cn.threo.imitationmall.mapper.UmsAdminMapper;
import cn.threo.imitationmall.mapper.UmsAdminPermissionRelationMapper;
import cn.threo.imitationmall.mapper.UmsAdminRoleRelationMapper;
import cn.threo.imitationmall.model.*;
import cn.threo.imitationmall.service.UmsAdminService;
import cn.threo.imitationmall.utils.JwtUtil;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author threo
 * UmsAdminService 实现类
 */
@Service
public class UmsAdminServiceImpl implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UmsAdminMapper umsAdminMapper;

    @Autowired
    private UmsAdminRoleRelationMapper umsAdminRoleRelationMapper;

    @Autowired
    private UmsAdminRoleRelationDao umsAdminRoleRelationDao;

    @Autowired
    private UmsAdminPermissionRelationMapper umsAdminPermissionRelationMapper;

    @Autowired
    private UmsAdminPermissionRelationDao umsAdminPermissionRelationDao;

    @Autowired
    private UmsAdminLoginLogMapper umsAdminLoginLogMapper;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdminExample epl = new UmsAdminExample();
        epl.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = umsAdminMapper.selectByExample(epl);
        return adminList != null && adminList.size() > 0
                ? adminList.get(0)
                : null;
    }

    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        if (getAdminByUsername(umsAdmin.getUsername()) != null) {
            return null;
        }
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        umsAdminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            token = jwtUtil.generatorToken(userDetails);
            updateLoginTimeByUsername(username);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    private void updateLoginTimeByUsername(String username) {
        UmsAdmin record = new UmsAdmin();
        record.setLoginTime(new Date());
        UmsAdminExample epl = new UmsAdminExample();
        epl.createCriteria().andUsernameEqualTo(username);
        umsAdminMapper.updateByExampleSelective(record, epl);
    }

    private void insertLoginLog(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        umsAdminLoginLogMapper.insert(loginLog);
    }


    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        if (jwtUtil.canRefresh(oldToken)) {
            return jwtUtil.refreshToken(oldToken);
        }
        return null;
    }

    @Override
    public UmsAdmin getAdminById(Long id) {
        return umsAdminMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UmsAdmin> list(String name, Integer size, Integer page) {
        PageHelper.startPage(page, size);
        UmsAdminExample epl = new UmsAdminExample();
        UmsAdminExample.Criteria criteria = epl.createCriteria();
        if (!StringUtils.isEmpty(name)) {
            criteria.andUsernameLike("%" + name + "%");
            epl.or(epl.createCriteria().andNickNameLike("%" + name + "%"));
        }
        return umsAdminMapper.selectByExample(epl);
    }

    @Override
    public int updateAdmin(Long id, UmsAdmin umsAdmin) {
        umsAdmin.setId(id);
        // 密码做了加密处理， 需要单独修改
        umsAdmin.setPassword(null);
        return umsAdminMapper.updateByPrimaryKeySelective(umsAdmin);
    }

    @Override
    public int delete(Long id) {
        return umsAdminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateRoles(Long id, List<Long> roles) {
        int count = roles == null ? 0 : roles.size();
        // 先删除旧有的关系
        UmsAdminRoleRelationExample epl = new UmsAdminRoleRelationExample();
        epl.createCriteria().andAdminIdEqualTo(id);
        umsAdminRoleRelationMapper.deleteByExample(epl);

        // 添加新的关系
        if (!CollectionUtils.isEmpty(roles)) {
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            roles.forEach(
                    roleId -> {
                        UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                        roleRelation.setAdminId(id);
                        roleRelation.setRoleId(roleId);
                        list.add(roleRelation);
                    }
            );
            umsAdminRoleRelationDao.insertList(list);
        }
        return count;
    }

    @Override
    public List<UmsRole> getRoleList(Long id) {
        return umsAdminRoleRelationDao.getRoleList(id);
    }

    @Override
    public int updatePermission(Long id, List<Long> permissions) {
        // 删除原来的权限关系
        UmsAdminPermissionRelationExample epl = new UmsAdminPermissionRelationExample();
        epl.createCriteria().andAdminIdEqualTo(id);
        umsAdminPermissionRelationMapper.deleteByExample(epl);
        // 获取对应角色权限
        List<UmsPermission> permissionList = umsAdminRoleRelationDao.getRolePermissionList(id);
        List<Long> rolePermissionList = permissionList.stream()
                .map(UmsPermission::getId)
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(permissions)) {
            List<UmsAdminPermissionRelation> relationList = new ArrayList<>();
            // 从指定的权限表中筛选出原角色不含有的权限, 即需要添加的权限
            List<Long> addPermissionIdList = permissions.stream()
                    .filter(permissionId -> !rolePermissionList.contains(id))
                    .collect(Collectors.toList());

            // 从角色权限表中筛选出不再指定权限表里的权限， 即需要取消的权限
            List<Long> subPermissionRelation = rolePermissionList.stream()
                    .filter(permissionId -> !permissions.contains(permissionId))
                    .collect(Collectors.toList());

            relationList.addAll(convert(id, 1, addPermissionIdList));
            relationList.addAll(convert(id, -1, subPermissionRelation));
            return umsAdminPermissionRelationDao.insertList(relationList);
        }
        return 0;
    }

    private List<UmsAdminPermissionRelation> convert(Long id, int type, List<Long> permissionIdList) {
        List<UmsAdminPermissionRelation> relationList = permissionIdList.stream()
                .map(permissionId -> {
                    UmsAdminPermissionRelation relation = new UmsAdminPermissionRelation();
                    relation.setAdminId(id);
                    relation.setType(type);
                    relation.setPermissionId(permissionId);
                    return relation;
                }).collect(Collectors.toList());
        return relationList;
    }

    @Override
    public List<UmsPermission> getPermissionList(Long id) {
        return umsAdminRoleRelationDao.getPermissionList(id);
    }
}
