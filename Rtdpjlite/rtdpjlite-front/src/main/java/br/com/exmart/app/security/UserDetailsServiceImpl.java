package br.com.exmart.app.security;


import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.rtdpjlite.model.Usuario;
import br.com.exmart.rtdpjlite.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    protected static Logger logger= LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UsuarioService userService;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UsuarioService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Senha ruy20s@sc0 criptografada: "+ passwordEncoder.encode("ruy20s@sc0"));

        Usuario user = userService.findByEmail(username);
        if (null == user) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getSenha(),
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
        }
    }
}