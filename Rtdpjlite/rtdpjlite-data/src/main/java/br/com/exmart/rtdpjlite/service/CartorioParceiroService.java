package br.com.exmart.rtdpjlite.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.exmart.rtdpjlite.model.CartorioParceiro;
import br.com.exmart.rtdpjlite.model.Cidade;
import br.com.exmart.rtdpjlite.repository.CartorioParceiroRepository;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;

@Service
public class CartorioParceiroService {
    @Autowired
    private CartorioParceiroRepository cartorioParceiroRepository;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private UsuarioPortalService usuarioPortalService;

    public List<CartorioParceiro> findAll(){
        return cartorioParceiroRepository.findAll(new Sort("alias"));
    }

    @Transactional(rollbackFor = Exception.class)
    public CartorioParceiro save(CartorioParceiro cartorioParceiro) throws Exception {
        CartorioParceiro existe = cartorioParceiroRepository.findByEmail(cartorioParceiro.getEmail());
        if(existe != null){
            throw new Exception("Email já cadastrado para um cartório");
        }
        cartorioParceiro = this.cartorioParceiroRepository.save(cartorioParceiro);
        usuarioPortalService.novo(cartorioParceiro, null, cartorioParceiro.getAlias(), cartorioParceiro.getEmail(), String.valueOf(Calendar.getInstance().getTime().getTime()), "Senha aleatória");
        return cartorioParceiro;
    }

    public List<CartorioParceiro> findByCidade(Cidade cidade) {
        return cartorioParceiroRepository.findByCidade(cidade);
    }

    public CartorioParceiro findById(Long id) {
        return this.cartorioParceiroRepository.findOne(id);
    }
}
