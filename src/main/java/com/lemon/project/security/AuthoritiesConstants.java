package com.lemon.project.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String ROLE_MGT = "ROLE_MGT";

    public static final String ROLE_HOSPITAL = "ROLE_HOSPITAL";

    public static final String ROLE_CLINIC = "ROLE_CLINIC";

    public static final String ROLE_DOCTOR = "ROLE_DOCTOR";

    private AuthoritiesConstants() {
    }

    public static String[] getMgtRoles() {
        return new String[]{ADMIN,ROLE_MGT};
    }
}
