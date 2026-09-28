package com.jarvis.security;

import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public boolean canExecute(PermissionLevel permissionLevel) {
        return permissionLevel == PermissionLevel.SAFE;
    }
}
