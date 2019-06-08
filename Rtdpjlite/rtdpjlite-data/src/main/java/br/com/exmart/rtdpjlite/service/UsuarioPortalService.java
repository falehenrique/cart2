package br.com.exmart.rtdpjlite.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import br.com.exmart.rtdpjlite.model.CartorioParceiro;
import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.portal.UsuarioPortal;
import br.com.exmart.rtdpjlite.repository.portal.UsuarioPortalRepository;
import freemarker.template.TemplateException;

@Service
public class UsuarioPortalService {

    @Autowired
    private UsuarioPortalRepository usuarioPortalRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ConfiguracaoService configuracaoService;
    protected static Logger logger = LoggerFactory.getLogger(UsuarioPortalService.class);

    public List<UsuarioPortal> findByCliente(Cliente cliente){
        return usuarioPortalRepository.findByClienteOrderByNome(cliente);
    }

    public UsuarioPortal novo(CartorioParceiro cartorioParceiro, Cliente cliente, String nome, String email, String senha, String lembrete) throws Exception {
        UsuarioPortal verificar = usuarioPortalRepository.findByEmail(email);
        if(verificar != null){
            throw new Exception("Email já cadastrado para outro Usuário: " + verificar.getNome());
        }
        UsuarioPortal usuario = new UsuarioPortal();
        usuario.setCartorioParceiro(cartorioParceiro);
        usuario.setCliente(cliente);
        usuario.setEmail(email);
        usuario.setNome(nome);
        usuario.setDtCadastro(LocalDateTime.now());
        usuario.setSenha(encrypt(senha));
        usuario.setLembreteSenha(lembrete);
        usuarioPortalRepository.save(usuario);
        if(cartorioParceiro.isIcEnvioEletronico()){

            HashMap model = new HashMap();
            model.put("login", email);
            model.put("senha",senha);
            model.put("cartorio", configuracaoService.findConfiguracao().getNomeCartorio());
            model.put("url", configuracaoService.findConfiguracao().getUrlQrcode());
            emailService.sendMessage(email, "Cadastro no " + configuracaoService.findConfiguracao().getNomeCartorio(), "cadastro_usuario_portal.ftl", model);
        }
        return usuario;
    }

    public String encrypt(String password){
        String algorithm = "SHA";

        byte[] plainText = password.getBytes();

        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);

            md.reset();
            md.update(plainText);
            byte[] encodedPassword = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < encodedPassword.length; i++) {
                if ((encodedPassword[i] & 0xff) < 0x10) {
                    sb.append("0");
                }

                sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
            }

            logger.debug("Plain    : " + password);
            logger.debug("Encrypted: " + sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UsuarioPortal novo(UsuarioPortal novo) throws IOException, TemplateException {

        String senha = novo.getSenha();
        if(Strings.isNullOrEmpty(novo.getSenha())){
            senha = String.valueOf(System.currentTimeMillis());
            novo.setSenha(senha);
            novo.setLembreteSenha("inicial");
        }
        novo.setSenha(encrypt(novo.getSenha()));
        usuarioPortalRepository.save(novo);

        HashMap model = new HashMap();
        model.put("login", novo.getEmail());
        model.put("senha",senha);
        model.put("cartorio", configuracaoService.findConfiguracao().getNomeCartorio());
        model.put("url", configuracaoService.findConfiguracao().getUrlQrcode());
        emailService.sendMessage(novo.getEmail(), "Cadastro no " + configuracaoService.findConfiguracao().getNomeCartorio(), "cadastro_usuario_portal.ftl", model);
        return novo;
    }
}
