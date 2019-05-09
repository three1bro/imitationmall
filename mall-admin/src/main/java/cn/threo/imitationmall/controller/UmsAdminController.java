package cn.threo.imitationmall.controller;

import cn.threo.imitationmall.common.CommonPage;
import cn.threo.imitationmall.common.CommonResult;
import cn.threo.imitationmall.dto.UmsAdminParam;
import cn.threo.imitationmall.model.UmsAdmin;
import cn.threo.imitationmall.model.UmsPermission;
import cn.threo.imitationmall.model.UmsRole;
import cn.threo.imitationmall.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author threo
 */
@RestController
@Api(tags = "UmsAdminController")
@RequestMapping("/admin")
public class UmsAdminController {
    @Autowired
    private UmsAdminService adminService;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public CommonResult<UmsAdmin> register(@RequestBody UmsAdminParam umsAdminParam, BindingResult result) {
        UmsAdmin umsAdmin = adminService.register(umsAdminParam);
        if (umsAdmin == null) {
            CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation("用户登录且返回Token")
    @PostMapping("/login")
    public CommonResult login(@RequestBody UmsAdminParam umsAdminParam, BindingResult result) {
        String token = adminService.login(umsAdminParam.getUsername(), umsAdminParam.getPassword());
        if (token == null) {
            CommonResult.validateFailed("用户名或密码错误！");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation("刷新token")
    @GetMapping("/token/refresh")
    public CommonResult refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = adminService.refreshToken(token);
        if (refreshToken == null) {
            return CommonResult.failed();
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/info")
    public CommonResult getAdminInfo(Principal principal) {
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", umsAdmin.getUsername());
        data.put("roles", new String[]{"TEST"});
        data.put("icon", umsAdmin.getIcon());
        return CommonResult.success(data);
    }

    @ApiOperation("登出")
    @PostMapping("/logout")
    public CommonResult logout() {
        return CommonResult.success(null);
    }

    @ApiOperation("获取带分页的用户列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<UmsAdmin>> list(@RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "5") Integer size) {
        List<UmsAdmin> umsAdminList = adminService.list(name, size, page);
        return CommonResult.success(CommonPage.restPage(umsAdminList));
    }

    @ApiOperation("获取指定Id用户信息")
    @GetMapping("/{id}")
    public CommonResult<UmsAdmin> getItem(@PathVariable Long id) {
        UmsAdmin admin = adminService.getAdminById(id);
        return CommonResult.success(admin);
    }

    @ApiOperation("修改指定用户信息")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsAdmin admin) {
        int count = adminService.updateAdmin(id, admin);
        return count > 0 ?
                CommonResult.success(count) :
                CommonResult.failed();
    }

    @ApiOperation("删除指定用户信息")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = adminService.delete(id);
        return count > 0 ?
                CommonResult.success(count) :
                CommonResult.failed();
    }

    @ApiOperation("给用户分配角色")
    @PostMapping("/role/update")
    public CommonResult updateRole(@RequestParam("adminId") Long adminId,
                                   @RequestParam("roleIds") List<Long> ids) {
        int count = adminService.updateRoles(adminId, ids);
        return count > 0 ?
                CommonResult.success(count) :
                CommonResult.failed();
    }

    @ApiOperation("获取指定用户的角色")
    @GetMapping("/role/{id}")
    public CommonResult<List<UmsRole>> getRoleList(@PathVariable Long id) {
        List<UmsRole> roleList = adminService.getRoleList(id);
        return CommonResult.success(roleList);
    }

    @ApiOperation("给用户分配权限")
    @PostMapping("/permission/update")
    public CommonResult updatePermission(@RequestParam Long adminIds,
                                         @RequestParam("permissionIds") List<Long> permissionIds) {
        int count = adminService.updatePermission(adminIds, permissionIds);
        return count > 0 ?
                CommonResult.success(count) :
                CommonResult.failed();
    }

    @ApiOperation("获取用户权限")
    @GetMapping("/permission/{id}")
    public CommonResult<List<UmsPermission>> getPermissionList(@PathVariable Long id) {
        List<UmsPermission> list = adminService.getPermissionList(id);
        return CommonResult.success(list);
    }
}
