package cn.threo.imitationmall.controller;

import cn.threo.imitationmall.common.CommonResult;
import cn.threo.imitationmall.model.UmsPermission;
import cn.threo.imitationmall.model.UmsRole;
import cn.threo.imitationmall.service.UmsRoleServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台用户角色管理
 *
 * @author threo
 */
@RestController
@Api(tags = "UmsRoleController")
@RequestMapping("/role")
public class UmsRoleController {

    @Autowired
    private UmsRoleServices roleServices;

    @ApiOperation("添加角色")
    @PostMapping("/create")
    public CommonResult create(@RequestBody UmsRole role) {
        int count = roleServices.create(role);
        return count > 0 ?
                CommonResult.success(count) :
                CommonResult.failed();
    }

    @ApiOperation("修改角色")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsRole role) {
        int count = roleServices.update(id, role);
        return count > 0 ?
                CommonResult.success(count) :
                CommonResult.failed();
    }

    @ApiOperation(("批量删除角色"))
    @PostMapping("/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = roleServices.delete(ids);
        return count > 0 ?
                CommonResult.success(count) :
                CommonResult.failed();
    }

    @ApiOperation("获取对应角色权限")
    @GetMapping("/permission/{roleId}")
    public CommonResult<List<UmsPermission>> getPermissionList(@PathVariable Long roleId) {
        return CommonResult.success(roleServices.getPermissionList(roleId));
    }

    @ApiOperation("修改对应角色权限")
    @PostMapping("/permission/update")
    public CommonResult updatePermission(@RequestParam Long roleId,
                                         @RequestParam("permissionIds") List<Long> permissionIds) {
        int count = roleServices.updatePermission(roleId, permissionIds);
        return count > 0 ?
                CommonResult.success(count) :
                CommonResult.failed();
    }

    @ApiOperation("获取所有角色")
    @GetMapping("/list")
    public CommonResult<List<UmsRole>> list() {
        return CommonResult.success(roleServices.list());
    }
}
