let tarefas = []; // Armazenar as tarefas globalmente

// Função para listar tarefas
async function listarTarefas() {
    const response = await fetch('/api/tarefas/gettarefas'); // API para obter tarefas
    tarefas = await response.json(); // Armazena as tarefas globalmente
    renderizarTabela(tarefas); // Renderiza a tabela com as tarefas
}

// Função para renderizar a tabela
function renderizarTabela(tarefas) {
    const tarefaTableBody = document.querySelector('#tarefaTable tbody');
    tarefaTableBody.innerHTML = '';

    tarefas.forEach((tarefa, index) => {
        const tr = document.createElement('tr');
        tr.className = tarefa.custo >= 1000 ? 'high-cost' : ''; // Marca em amarelo se custo >= 1000
        tr.innerHTML = `
            <td>${tarefa.id}</td>
            <td>${tarefa.nome}</td>
            <td>${tarefa.custo}</td>
            <td>${tarefa.dataLimite}</td>
            <td>
                <button onclick="prepararAtualizacao(${index})"><i class="fas fa-edit"></i></button>
                <button onclick="excluirTarefa(${tarefa.id})"><i class="fas fa-trash"></i></button>
                <button onclick="moverTarefa(${index}, true)">↑</button>
                <button onclick="moverTarefa(${index}, false)">↓</button>
            </td>
        `;
        tarefaTableBody.appendChild(tr);
    });
}

// Função para excluir tarefa
async function excluirTarefa(id) {
    const confirmacao = confirm("Tem certeza que deseja excluir esta tarefa?");
    if (confirmacao) {
        await fetch(`/api/tarefas/deletartarefa/${id}`, { method: 'DELETE' });
        listarTarefas(); // Recarregar a lista de tarefas
    }
}

// Função para preparar a edição
function prepararAtualizacao(index) {
    const tarefa = tarefas[index];
    const nome = prompt("Novo Nome da Tarefa:", tarefa.nome);
    const custo = prompt("Novo Custo:", tarefa.custo);
    const dataLimite = prompt("Nova Data Limite (YYYY-MM-DD):", tarefa.dataLimite);

    if (nome && custo && dataLimite) {
        atualizarTarefa(tarefa.id, nome, custo, dataLimite);
    }
}

// Função para atualizar tarefa
async function atualizarTarefa(id, nome, custo, dataLimite) {
    const response = await fetch(`/api/tarefas/atualizartarefas/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ nome, custo, dataLimite })
    });
    if (response.ok) {
        listarTarefas(); // Recarregar a lista de tarefas
    } else {
        alert("Erro ao atualizar a tarefa!");
    }
}

// Função para incluir nova tarefa
document.getElementById('btnIncluir').addEventListener('click', async () => {
    const nome = prompt("Nome da Tarefa:");
    const custo = prompt("Custo:");
    const dataLimite = prompt("Data Limite (YYYY-MM-DD):");

    if (nome && custo && dataLimite) {
        await fetch('/api/tarefas/criartarefas', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ nome, custo, dataLimite })
        });
        listarTarefas(); // Recarregar a lista de tarefas
    }
});

// Função para mover tarefa
async function moverTarefa(index, paraCima) {
    try {
        // ID da tarefa que será movida
        const tarefaId = tarefas[index].id; 
        
        // Índice da tarefa de destino (para cima ou para baixo)
        const destinoIndex = paraCima ? index - 1 : index + 1; 

        // Verifica se o índice de destino é válido
        if (destinoIndex < 0 || destinoIndex >= tarefas.length) {
            alert("Não é possível mover a tarefa nesta direção.");
            return;
        }

        // ID da tarefa de destino
        const tarefaDestinoId = tarefas[destinoIndex].id; 

        // Faz a requisição para mover a tarefa
        const response = await fetch(`/api/tarefas/mover/${tarefaId}/${tarefaDestinoId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        // Verifica a resposta da requisição
        if (response.ok) {
            listarTarefas(); // Recarrega a lista de tarefas
        } else {
            const errorMessage = await response.text(); // Captura a mensagem de erro
            throw new Error(errorMessage || "Erro ao mover a tarefa.");
        }
    } catch (error) {
        console.error(error);
        alert("Erro ao mover a tarefa!");
    }
}

listarTarefas();
