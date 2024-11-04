package com.Lista.Lista_de_Tarefas.TarefaController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Lista.Lista_de_Tarefas.Model.Tarefa;
import com.Lista.Lista_de_Tarefas.TarefaService.TarefaService;


@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
    
    @Autowired
    private TarefaService tarefaService;

    @GetMapping("/gettarefas")
    public List<Tarefa> listarTarefas(){
        return tarefaService.listarTarefas();
    }

    @PostMapping("/criartarefas")
    public Tarefa criarTarefa(@RequestBody Tarefa tarefa){
      return tarefaService.criarTarefa(tarefa);
    }

    @PutMapping("/atualizartarefas/{id}")
    public Tarefa atualizarTarefa(@PathVariable Long id, @RequestBody Tarefa tarefa){
        return tarefaService.atualizarTarefa(id, tarefa);
    }

    @DeleteMapping("/deletartarefa/{id}")
    public void excluirTarefa(@PathVariable Long id) {
        tarefaService.excluirTarefa(id);
    }

    @PutMapping("/mover/{id}/{id_2}")
    public void moverTarefa(@PathVariable("id") Long id, @PathVariable("id_2") Long id_2) {
        tarefaService.moverTarefa(id, id_2);
    }


}
