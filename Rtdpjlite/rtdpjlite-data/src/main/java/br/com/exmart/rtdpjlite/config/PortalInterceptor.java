package br.com.exmart.rtdpjlite.config;

import br.com.exmart.rtdpjlite.model.portal.UsuarioPortal;
import br.com.exmart.rtdpjlite.repository.portal.UsuarioPortalRepository;
import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import org.springframework.aop.framework.DefaultAdvisorChainFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;

@Component
public class PortalInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException, IOException {
        String token = request.getHeader("X-Authorization");
        if(Strings.isNullOrEmpty(token)){
            throw new ServletException("X-Authorization não encontrado");
        }else{

            Jwts.parser().setSigningKey("RTDPJ").parseClaimsJws(token).getBody().getExpiration();

            Long user = Long.parseLong(Jwts.parser()
                    .setSigningKey("RTDPJ")
                    .parseClaimsJws(token)
                    .getBody().getId());

            // we can safely trust the JWT

            request.setAttribute("usuarioId", user);
        }
//         Jwts.parser().parse(jws).getBody();


        return true;
    }
}
