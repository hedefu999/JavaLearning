package com.ssmr.txpractice;

import java.util.List;

public interface RoleBasicService {
    int saveRoles(List<Role> roleList);

    int saveRole(Role role);

    Role getRoleById(Integer id);

    int updateRole(Role role);

    int deleteRoleById(Integer id);
}
