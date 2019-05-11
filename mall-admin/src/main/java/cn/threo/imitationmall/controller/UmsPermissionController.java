package cn.threo.imitationmall.controller;

import cn.threo.imitationmall.common.CommonResult;
import cn.threo.imitationmall.dto.UmsPermissionNode;
import cn.threo.imitationmall.model.UmsPermission;
import cn.threo.imitationmall.service.UmsPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author threo
 * 后台用户权限管理
 */
@RestController
@Api(tags = "UmsPermissionController")
@RequestMapping("/permission")
public class UmsPermissionController {

    @Autowired
    private UmsPermissionService permissionService;

    @ApiOperation("添加权限")
    @PostMapping("/create")
    public CommonResult create(@RequestBody UmsPermission permission) {
        int count = permissionService.create(permission);
        return count > 0 ?
                CommonResult.success(count) :
                CommonResult.failed();
    }

    @ApiOperation("修改权限")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsPermission permission) {
        int count = permissionService.update(id, permission);
        return count > 0 ?
                CommonResult.success(count) :
                CommonResult.failed();
    }

    @ApiOperation("删除权限")
    @PostMapping("/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = permissionService.delete(ids);
        return count > 0 ?
                CommonResult.success(count) :
                CommonResult.failed();

    }

    @ApiOperation("获取所有权限列表")
    @GetMapping("/list")
    public CommonResult<List<UmsPermission>> list() {
        return CommonResult.success(permissionService.list());
    }

    @ApiOperation("获取层级权限列表")
    @GetMapping("/tList")
    public CommonResult<List<UmsPermissionNode>> tList() {
        return CommonResult.success(permissionService.tList());
    }

}
