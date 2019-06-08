package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.ModalDetalhesCustas;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.indicadorRTDPJ.ui.vo.ServicosAgrupadoVO;
import br.com.exmart.rtdpjlite.vo.financeiro.CustasCartorio;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.renderers.HtmlRenderer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModalDetalhesCustasImpl extends ModalDetalhesCustas{

    private final int qtdCustas;
    int contador= 0;
    int contadorCustas = 1;
    List<CustasCartorio> custaTituloList = null;

    public ModalDetalhesCustasImpl(List<ServicoCalculado> servicos, List<CustasCartorio> custaList) {

        gridCustas.removeAllColumns();
        gridCustas.addColumn(ServicoCalculado::getNmServico).setCaption("Serviço") .setMinimumWidthFromContent(true);
        gridCustas.addColumn(item -> { return "R$ " + Utils.getDecimalFormat().format(item.getVlBase());}, new HtmlRenderer()).setCaption("Vl. Base") .setMinimumWidthFromContent(true);
        gridCustas.addColumn(ServicoCalculado::getNmFormaCalculo).setCaption("Forma Cálculo") .setMinimumWidthFromContent(true);
        this.custaTituloList = new ArrayList<>(custaList);
        custaTituloList.add(new CustasCartorio(99L,custaTituloList.size()+1,"Total",BigDecimal.ZERO));
        this.qtdCustas = custaTituloList.size();

        Class<ServicoCalculado> classe = ServicoCalculado.class;
        for(CustasCartorio custaTitulo : custaTituloList) {
            gridCustas.addColumn(item -> {
                if(custaTitulo.getNome().equalsIgnoreCase("Total")){
                    this.contadorCustas = 1;
                    return "R$ "+Utils.getDecimalFormat().format(item.getTotal());
                }else {
                    if(this.contadorCustas >= this.custaTituloList.size()){
                        this.contadorCustas = 1;
                    }
                    try {
                        Method metodo = classe.getMethod("getCustas" + this.contadorCustas);
                        this.contadorCustas++;
                        return "R$ "+Utils.getDecimalFormat().format(metodo.invoke(item));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

            }, new HtmlRenderer()).setCaption(custaTitulo.getNome()).setId("custas"+this.contador);
            this.contador++;
        }


        servicos.sort(Comparator.comparing(ServicoCalculado::getNmServico));
        this.contadorCustas = 1;
        gridCustas.setItems(servicos);

        gridCustas.appendFooterRow();
        gridCustas.setHeightByRows(servicos.size() + 1);

//        if(this.protocolo.getNumero() != null){
//            recebimentosList.clear();
//            recebimentosList.addAll(financeiroService.listarPagamentosProtocolo(this.protocolo.getNumero(), ((MyUI) UI.getCurrent()).getNatureza(), false));
//            gridRecebimentos.getDataProvider().refreshAll();
//        }
//        atualizarValores();
//        recalcularTotal();


        calcaulaTotal(servicos);
    }


    private void calcaulaTotal(List<ServicoCalculado> servicos) {
        try{

            FooterRow footer = gridCustas.getFooterRow(0);
            BigDecimal total = BigDecimal.ZERO;

            Class<ServicoCalculado> classe = ServicoCalculado.class;
            this.contadorCustas = 0;
            for(int x = 0 ; x< this.qtdCustas; x++){
                BigDecimal custas = BigDecimal.ZERO;
                if(this.contadorCustas > this.qtdCustas-1){
                    this.contadorCustas = 1;
                }else{
                    this.contadorCustas++;
                }
                for(ServicoCalculado servico : servicos){
                    Method metodo = classe.getMethod("getCustas" + this.contadorCustas);
                    custas = custas.add((BigDecimal) metodo.invoke(servico));
                }
                footer.getCell("custas"+(x)).setText("R$ " + Utils.getDecimalFormat().format(custas));
            }
            for(ServicoCalculado servico : servicos){
                total =  total.add(servico.getTotal());
            }
            footer.getCell("custas"+(qtdCustas-1)).setText("R$ " + Utils.getDecimalFormat().format(total));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
