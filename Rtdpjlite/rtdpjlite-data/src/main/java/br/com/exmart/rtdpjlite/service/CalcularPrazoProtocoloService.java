package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Feriado;
import br.com.exmart.rtdpjlite.repository.FeriadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class CalcularPrazoProtocoloService {
    @Autowired
    private FeriadoRepository feriadoRepository;
    List<Feriado> feriadosRecorrentes;
    public Date calcularDataToDate(int dias, boolean icDiasUteis, LocalDate dtInicial){
        return Date.from(calcularData(dias, icDiasUteis, dtInicial).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    public LocalDate calcularData(int dias, boolean icDiasUteis, LocalDate dtInicial){
        feriadosRecorrentes = feriadoRepository.findByRecorrenteTrue();

        if(dtInicial == null){
            dtInicial = LocalDate.now();
        }

        if(!icDiasUteis){
            dtInicial = dtInicial.plusDays(dias);
            boolean verificarNovamente = false;
            do {
                verificarNovamente = false;
                while (isFeriado(dtInicial)) {
                    dtInicial = dtInicial.plusDays(1);
                    verificarNovamente = true;
                }
                while (isFinalSemana(dtInicial)) {
                    dtInicial = dtInicial.plusDays(1);
                    verificarNovamente = true;
                }
            }while (verificarNovamente);
        }else{
            for(int x = 0; x < dias; x++){
                dtInicial = dtInicial.plusDays(1);
                boolean verificarNovamente = false;
                do {
                    verificarNovamente = false;
                    while (isFeriado(dtInicial)) {
                        dtInicial = dtInicial.plusDays(1);
                        verificarNovamente = true;
                    }
                    while (isFinalSemana(dtInicial)) {
                        dtInicial = dtInicial.plusDays(1);
                        verificarNovamente = true;
                    }
                }while (verificarNovamente);
            }
        }



        return dtInicial;
    }
    private boolean isFeriado(LocalDate dia){
        Feriado feriado = feriadoRepository.findByDiaEquals(dia);
        if(feriado != null){
            return true;
        }else{
            for(Feriado feriadoRecorrente: feriadosRecorrentes){
                LocalDate diaFeriado = feriadoRecorrente.getDia().withYear(dia.getYear());
                if(dia.compareTo(diaFeriado) ==0){
                    return true;
                }
            }
        }

        return false;
    }
    private boolean isFinalSemana(LocalDate dia){
        if(dia.getDayOfWeek() == DayOfWeek.SATURDAY){
            return true;
        }else if(dia.getDayOfWeek() == DayOfWeek.SUNDAY){
            return true;
        }

        return false;
    }
}
