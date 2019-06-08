package br.com.exmart.util;

/**
 * Created by Heryk on 30/01/2018.
 */

public enum Disposicao {
        ESQUERDA_SUPERIOR(1), DIREITA_SUPERIOR(2), ESQUERDA_INFERIOR(3), DIREITA_INFERIOR(4);

        private final int valor;
        Disposicao(int valorOpcao){
            valor = valorOpcao;
        }
        public int getValor(){
            return valor;
        }
}
