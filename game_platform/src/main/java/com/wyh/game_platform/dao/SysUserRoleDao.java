package com.wyh.game_platform.dao;

import com.wyh.game_platform.pojo.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserRoleDao {
    List<SysRole> findRoleListByUserId(@Param("userId") Integer userId);
    void insertDefaultUserRole(@Param("userId") Integer userId);
}
