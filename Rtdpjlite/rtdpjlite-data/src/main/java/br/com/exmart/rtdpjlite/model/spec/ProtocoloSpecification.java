package br.com.exmart.rtdpjlite.model.spec;

import br.com.exmart.rtdpjlite.model.*;
import com.google.common.base.Strings;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


//https://stackoverflow.com/questions/39167189/spring-boot-dynamic-query
public class ProtocoloSpecification {
    public static Specification<Protocolo> withNaturezaNome(String natureza) {
        if (Strings.isNullOrEmpty(natureza) ) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.join("natureza").get("nome"), natureza);
        }
    }
    public static Specification<Protocolo> withNatureza(Natureza natureza) {
        if (natureza == null ) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.join("natureza"), natureza);
        }
    }
    public static Specification<Protocolo> withNumero(Long numero) {
        if (numero == null) {
            return null;
        } else {
            // Specification using Java 8 lambdas
            return (root, query, cb) -> cb.equal(root.get("numero"), numero);
        }
    }

    public static Specification<Protocolo> withAndamento(Status status) {
        if (status == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.join("statusProtocolo").join("status"), status);
        }
    }

    public static Specification<Protocolo> withDataBetween(String campo, LocalDateTime dtInicial, LocalDateTime dtFinal) {
        if (Strings.isNullOrEmpty(campo) || dtInicial == null || dtFinal == null) {
            return null;
        } else {
            // Specification using Java 8 lambdas
            if(campo.equalsIgnoreCase("data_vencimento")){
                return (root, query, cb) -> cb.between(root.get(campo), dtInicial.toLocalDate(), dtFinal.toLocalDate());
            }
            return (root, query, cb) -> cb.between(root.get(campo), dtInicial.with(LocalTime.MIN), dtFinal.with(LocalTime.MAX));
        }
    }
    public static Specification<Protocolo> withTipo(List<TipoProtocolo> tipo) {
        if (tipo == null) {
            return null;
        } else {
            // Specification using Java 8 lambdas
            return (root, query, cb) -> cb.and(root.get("tipo").in(tipo));
        }
    }
    public static Specification<Protocolo> withCliente(Cliente cliente) {
        if (cliente == null) {
            return null;
        } else {
            // Specification using Java 8 lambdas
            return (root, query, cb) -> cb.equal(root.get("cliente"), cliente);
        }
    }
}
