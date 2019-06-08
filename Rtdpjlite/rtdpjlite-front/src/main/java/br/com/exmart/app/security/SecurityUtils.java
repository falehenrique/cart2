package br.com.exmart.app.security;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.exmart.rtdpjlite.model.Usuario;
import br.com.exmart.rtdpjlite.service.UsuarioService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {
    private SecurityUtils() {
        // Util methods only
    }

    /**
     * Gets the user name of the currently signed in user.
     *
     * @return the user name of the current user or <code>null</code> if the
     *         user has not signed in
     */
    public static String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * Check if currently signed-in user is in the role with the given role
     * name.
     *
     * @param role
     *            the role to check for
     * @return <code>true</code> if user is in the role, <code>false</code>
     *         otherwise
     */
    public static boolean isCurrentUserInRole(String role) {
        return getUserRoles().stream().filter(roleName -> roleName.equals(Objects.requireNonNull(role))).findAny()
                .isPresent();
    }

    /**
     * Gets the roles the currently signed-in user belongs to.
     *
     * @return a set of all roles the currently signed-in user belongs to.
     */
    public static Set<String> getUserRoles() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    /**
     * Gets the user object for the current user.
     *
     * @return the user object
     */
    public static Usuario getCurrentUser(UsuarioService userService) {
        return userService.findByEmail(SecurityUtils.getUsername());
    }
}