package com.maycdevs.jobsearch.api;


import com.maycdevs.jobsearch.model.Role;
import com.maycdevs.jobsearch.service.RoleService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/roles")
public class RoleApiController {


    @Autowired
    private RoleService roleService;

    @GetMapping(value = {"","/"})
    public List<Role> listAllRoles() {
        return roleService.getAll();
    }

    @PostMapping(value = {"","/"})
    public Role createRole(@RequestBody @NonNull Role r) throws Exception {
        return roleService.create(r);
    }

    @PutMapping("/{id}")
    public Role updateRole(
            @PathVariable String id,
            @RequestBody @NonNull Role r
    ) throws Exception {
        return roleService.update(id, r);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteRole(@PathVariable String id) throws Exception {
        return roleService.delete(id);
    }
}
