package com.Lista.Lista_de_Tarefas.TarefaRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.Lista.Lista_de_Tarefas.Model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    // metodo feito para evitar duplicatas no banco de dados
    // metodo "findbynome" faz a verificação de duplicatas a partir de campo nome
    Optional<Tarefa> findByNome(String nome);

    // metodo feito para evitar duplicatas no processo de edição dos dados
    // ambos os metodos serão chamados nos metodos feitos na classe de serviço
    // "TarefaService"
    Optional<Tarefa> findByNomeAndIdNot(String nome, Long id);

    // Adiciona o método para buscar por ordem de apresentação
    Optional<Tarefa> findByOrdemApresentacao(int ordemApresentacao);

    @Modifying
    @Query("UPDATE Tarefa t SET t.ordemApresentacao = t.ordemApresentacao - 1 WHERE t.ordemApresentacao BETWEEN :ordemInicial AND :ordemFinal")
    void updateOrdemParaCima(int ordemInicial, int ordemFinal);

    @Modifying
    @Query("UPDATE Tarefa t SET t.ordemApresentacao = t.ordemApresentacao + 1 WHERE t.ordemApresentacao BETWEEN :ordemInicial AND :ordemFinal")
    void updateOrdemParaBaixo(int ordemInicial, int ordemFinal);

}
