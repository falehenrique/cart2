package br.com.exmart.rtdpjlite.controller.portal;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import br.com.exmart.rtdpjlite.annotations.NotFoundException;
import br.com.exmart.rtdpjlite.model.portal.UsuarioPortal;
import br.com.exmart.rtdpjlite.repository.portal.UsuarioPortalRepository;
import br.com.exmart.rtdpjlite.service.ConfiguracaoService;
import br.com.exmart.rtdpjlite.service.EmailService;
import br.com.exmart.rtdpjlite.service.UsuarioPortalService;
import freemarker.template.TemplateException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
//@CrossOrigin(origins = {"http://homologa-www.2osasco.com.br","http://homologa-rtdpj.2osasco.com.br","http://*.lumera.com.br","http://localhost"})
@RequestMapping("/portal/usuario")
public class UsuarioPortalController {

    @Autowired
    private UsuarioPortalRepository usuarioPortalRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UsuarioPortalService usuarioPortalService;
    @Autowired
    private ConfiguracaoService configuracaoService;


    @PostMapping("/senha")
    public void teste(@RequestAttribute("usuarioId") Long usuarioId, @RequestBody UsuarioPortal usuarioPortal){
        UsuarioPortal user = usuarioPortalRepository.findOne(usuarioId);
        user.setSenha(usuarioPortalService.encrypt(usuarioPortal.getSenha()));
        user.setLembreteSenha(usuarioPortal.getLembreteSenha());
        usuarioPortalRepository.save(user);
    }


    @GetMapping("/login/novasenha")
    public void novasenha(@RequestParam(name = "email") String email) throws IOException, TemplateException {
        UsuarioPortal user = usuarioPortalRepository.findByEmail(email);
        if(user == null)
            throw new NotFoundException("Usuário não encontrado: " + email);
        String senha = String.valueOf(Calendar.getInstance().getTimeInMillis());
        user.setSenha(usuarioPortalService.encrypt(senha));
        user.setLembreteSenha("Senha aleatória");
        usuarioPortalRepository.save(user);
        HashMap model = new HashMap();
        model.put("login", user.getEmail());
        model.put("senha",senha);
        model.put("cartorio", configuracaoService.findConfiguracao().getNomeCartorio());
        model.put("url", configuracaoService.findConfiguracao().getUrlQrcode());
        emailService.sendMessage(email,"Nova senha alteatória", "nova_senha.ftl", model);
    }

    @GetMapping("/login/lembrete")
    public String lembreteSenha(@RequestParam(name = "email") String email){
        UsuarioPortal user = usuarioPortalRepository.findByEmail(email);
        if(user == null){
            throw new NotFoundException("Usuario não encontrado");
        }
        return user.getLembreteSenha();
    }


    @PutMapping("/{id}")
    public Bootstrap login(@PathVariable Long id, @RequestBody UsuarioPortal usuario){
        UsuarioPortal user = usuarioPortalRepository.findOne(id);
        user.setNome(usuario.getNome());
        user.setLembreteSenha(usuario.getLembreteSenha());
        if(!Strings.isNullOrEmpty(usuario.getSenha())){
            user.setSenha(usuarioPortalService.encrypt(usuario.getSenha()));
        }
        usuarioPortalRepository.save(user);
        return new Bootstrap(user, null, null, configuracaoService.findConfiguracao().getSiteAssinaturaDigital());

    }
    @PostMapping("/login")
    public Bootstrap login(@RequestBody UsuarioPortal usuario){
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.DAY_OF_MONTH, 2);;
        // decode the base64 encoded string


        UsuarioPortal user = usuarioPortalRepository.findByEmail(usuario.getEmail());
        if(user == null){
            throw  new NotFoundException("Usuário não encontrado");
        }else{
            if(user.getSenha().equalsIgnoreCase(usuarioPortalService.encrypt(usuario.getSenha()))){
                String jws = Jwts.builder()
                        .setExpiration(expires.getTime())
                        .signWith(SignatureAlgorithm.HS512, "RTDPJ")
                        .setId(user.getId().toString())
                        .compact();
                return new Bootstrap(user, expires.getTime(), jws, configuracaoService.findConfiguracao().getSiteAssinaturaDigital());
            }else{
                throw  new NotFoundException("Senha inválida");
            }
        }

    }


    @GetMapping("/bootstrap")
    public Bootstrap bootstrap(@RequestAttribute("usuarioId") Long usuarioId){
        return new Bootstrap(usuarioPortalRepository.findOne(usuarioId), null, null, configuracaoService.findConfiguracao().getSiteAssinaturaDigital());
    }



    public class Bootstrap {
        private Long usuarioId;
        private String usuarioNome;
        private String usuarioEmail;
        private String tipo;
        private Long tipoId;
        private String tipoNome;
        private Date tokenExpires;
        private String token;
        private String lembreteSenha;
        private String siteAssinaturaDigital;

        public Bootstrap(UsuarioPortal usuario, Date tokenExpires, String token, String siteAssinaturaDigital) {
            this.token = token;
            this.tokenExpires = tokenExpires;
            this.usuarioId = usuario.getId();
            this.usuarioNome = usuario.getNome();
            this.usuarioEmail = usuario.getEmail();
            this.lembreteSenha =usuario.getLembreteSenha();
            if(usuario.getCartorioParceiro() != null){
                this.tipoId =usuario.getCartorioParceiro().getId();
                this.tipoNome = usuario.getCartorioParceiro().getNome();
                this.tipo = "PARCEIRO";
            }else if(usuario.getCliente() != null){
                this.tipoId =usuario.getCliente().getId();
                this.tipoNome = usuario.getCliente().getNome();
                this.tipo = "CLIENTE";
            }else{
                this.tipo = "CARTORIO";
                this.tipoNome= "OSASCO";
                this.tipoId = 0l;
            }
            this.siteAssinaturaDigital = siteAssinaturaDigital;

        }

        public Long getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
        }

        public String getUsuarioNome() {
            return usuarioNome;
        }

        public void setUsuarioNome(String usuarioNome) {
            this.usuarioNome = usuarioNome;
        }

        public String getUsuarioEmail() {
            return usuarioEmail;
        }

        public void setUsuarioEmail(String usuarioEmail) {
            this.usuarioEmail = usuarioEmail;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public Long getTipoId() {
            return tipoId;
        }

        public void setTipoId(Long tipoId) {
            this.tipoId = tipoId;
        }

        public String getTipoNome() {
            return tipoNome;
        }

        public void setTipoNome(String tipoNome) {
            this.tipoNome = tipoNome;
        }

        public Date getTokenExpires() {
            return tokenExpires;
        }

        public void setTokenExpires(Date tokenExpires) {
            this.tokenExpires = tokenExpires;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getLembreteSenha() {
            return lembreteSenha;
        }

        public void setLembreteSenha(String lembreteSenha) {
            this.lembreteSenha = lembreteSenha;
        }

        public String getSiteAssinaturaDigital() {
            return siteAssinaturaDigital;
        }

        public void setSiteAssinaturaDigital(String siteAssinaturaDigital) {
            this.siteAssinaturaDigital = siteAssinaturaDigital;
        }
    }
}
