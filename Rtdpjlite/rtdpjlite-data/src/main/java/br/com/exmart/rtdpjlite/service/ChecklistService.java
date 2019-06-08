package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.ChecklistGrupo;
import br.com.exmart.rtdpjlite.repository.ChecklistGrupoRepository;
import br.com.exmart.rtdpjlite.repository.ChecklistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChecklistService {

    @Autowired
    private ChecklistGrupoRepository checklistGrupoRepository;
    @Autowired
    private ChecklistItemRepository checklistItemRepository;


    public List<ChecklistGrupo> findBySubNatureza(Long idNatureza){
        return checklistGrupoRepository.findBySubNatureza(idNatureza);
    }
}
