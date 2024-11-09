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
        // Carrega as tarefas com uma única consulta
        Tarefa tarefa = tarefaRepository.findById(idTarefa)
            .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));
        Tarefa tarefaDestino = tarefaRepository.findById(idTarefaDestino)
            .orElseThrow(() -> new IllegalArgumentException("Tarefa de destino não encontrada"));
    
        int ordemOrigem = tarefa.getOrdemApresentacao();
        int ordemDestino = tarefaDestino.getOrdemApresentacao();
    
        // Ajusta as ordens sem duplicar chamadas desnecessárias
        if (ordemOrigem < ordemDestino) {
            tarefaRepository.updateOrdemParaCima(ordemOrigem + 1, ordemDestino);
        } else {
            tarefaRepository.updateOrdemParaBaixo(ordemDestino, ordemOrigem - 1);
        }
    
        // Atualiza a ordem da tarefa movida
        tarefa.setOrdemApresentacao(ordemDestino);
        tarefaRepository.save(tarefa);
    }
    
    
}
