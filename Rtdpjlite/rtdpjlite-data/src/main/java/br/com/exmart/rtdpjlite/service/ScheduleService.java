package br.com.exmart.rtdpjlite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class ScheduleService {
    private static Logger log = LoggerFactory.getLogger(ScheduleService.class.toString());
    @Scheduled(cron = "0 50 6 ? * *")
    public void verificarDecursoPrazo(){
        //TODO deve cancelar protocolos atrasados
        log.info("Deve verificar diariamente os protocolos que devem ser cancelados por causa do prazo");
    }
}
