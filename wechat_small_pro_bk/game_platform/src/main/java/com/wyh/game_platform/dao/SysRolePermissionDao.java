package com.wyh.game_platform.dao;

import com.wyh.game_platform.pojo.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRolePermissionDao {
    List<SysPermission> findPermissionListByRoleId(@Param("roleId") Integer roleId);
}
