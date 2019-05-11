package cn.threo.imitationmall.dto;

import cn.threo.imitationmall.model.UmsPermission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author threo
 * 用户权限层级显示结构
 */
public class UmsPermissionNode extends UmsPermission {
    @Getter
    @Setter
    private List<UmsPermissionNode> children;
}
