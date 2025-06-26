package uz.oromland.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.oromland.entity.User;
import uz.oromland.payload.RoleDTO;
import uz.oromland.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(
            @PathVariable Integer id,
            @AuthenticationPrincipal User user
    ) {
        RoleDTO roleDTO = roleService.findById(id, user);
        return ResponseEntity.ok(roleDTO);
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles(
            @AuthenticationPrincipal User user
    ) {
        List<RoleDTO> roles = roleService.findByAll(user);
        return ResponseEntity.ok(roles);
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(
            @RequestBody RoleDTO roleDTO,
            @AuthenticationPrincipal User user
    ) {
        RoleDTO createdRole = roleService.createRole(roleDTO, user);
        return ResponseEntity.ok(createdRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(
            @PathVariable Integer id,
            @RequestBody RoleDTO roleDTO,
            @AuthenticationPrincipal User user
    ) {
        RoleDTO updatedRole = roleService.updateRole(id, roleDTO, user);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(
            @PathVariable Integer id,
            @AuthenticationPrincipal User user
    ) {
        roleService.deleteRole(id, user);
        return ResponseEntity.noContent().build();
    }
}
