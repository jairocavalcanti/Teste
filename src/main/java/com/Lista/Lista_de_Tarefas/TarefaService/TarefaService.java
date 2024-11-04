package com.Lista.Lista_de_Tarefas.TarefaService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.Lista.Lista_de_Tarefas.Model.Tarefa;
import com.Lista.Lista_de_Tarefas.TarefaRepository.TarefaRepository;

import jakarta.transaction.Transactional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    // Metodo que lista as tarefas e as ordena pelo campo "ordemApresentacao", conforme solicitado
    // @Transactional(readOnly = true)
    public List<Tarefa> listarTarefas(){
        return tarefaRepository.findAll(Sort.by("ordemApresentacao"));
    }
    
    public Tarefa criarTarefa(Tarefa tarefa){
        //chamando o metodo criado no repository para que faça a verificação dos dados (nome)
        if(tarefaRepository.findByNome(tarefa.getNome()).isPresent()){
            throw new RuntimeException("Já existe uma tarefa com este nome");
        }

        int ultimaOrdem = tarefaRepository.findAll().size() + 1;
        tarefa.setOrdemApresentacao(ultimaOrdem);
        return tarefaRepository.save(tarefa);
    } 

    public Tarefa atualizarTarefa(Long id, Tarefa tarefaatualizada){
        Tarefa tarefaExistente = tarefaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Tarefa não encontrada!"));

        if(tarefaRepository.findByNomeAndIdNot(tarefaatualizada.getNome(), id).isPresent()){
            throw new RuntimeException("Já existe uma tarefa com este nome!");
        }

        tarefaExistente.setNome(tarefaatualizada.getNome());
        tarefaExistente.setCusto(tarefaatualizada.getCusto());
        tarefaExistente.setDataLimite(tarefaatualizada.getDataLimite());

        return tarefaRepository.save(tarefaExistente);
    }

    public void excluirTarefa(Long id){
        tarefaRepository.deleteById(id);
    }

    @Transactional
    public void moverTarefa(Long idTarefa, Long idTarefaDestino) {
        // Recupera as duas tarefas pelos IDs
        Tarefa tarefa = tarefaRepository.findById(idTarefa)
            .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));
        Tarefa tarefaDestino = tarefaRepository.findById(idTarefaDestino)
            .orElseThrow(() -> new IllegalArgumentException("Tarefa de destino não encontrada"));
    
        // Troca as ordens das tarefas
        int ordemTemporaria = tarefa.getOrdemApresentacao();
        tarefa.setOrdemApresentacao(tarefaDestino.getOrdemApresentacao());
        tarefaDestino.setOrdemApresentacao(ordemTemporaria);
    
        // Salva as alterações
        tarefaRepository.save(tarefa);
        tarefaRepository.save(tarefaDestino);
    }
    
    
}
