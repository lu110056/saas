package cn.taroco.rbac.admin.model.dto;

import cn.taroco.rbac.admin.model.entity.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liuht
 * @date 2018/1/20
 * 角色Dto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDTO extends SysRole {
    /**
     * 角色部门Id
     */
    private Integer roleDeptId;

    /**
     * 部门名称
     */
    private String deptName;
}
