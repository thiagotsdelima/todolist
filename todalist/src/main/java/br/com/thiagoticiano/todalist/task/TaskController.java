package br.com.thiagoticiano.todalist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thiagoticiano.todalist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  
  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    taskModel.setIdUser((UUID) idUser);

     // e para ver se a data ja passou, no caso a data de inicio do cadastro, se eu colocar || para validar se a data de inico ou de terminio nao e valida, nesse caso ate a hora se nao for certa da o erro
    var currentDate = LocalDateTime.now();
    if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("A data de início / data de término deve ser maior que a data atual");
    }

     if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("A data de início deve ser menor que a data de término");
    }

    var task = this.taskRepository.save(taskModel);
    return ResponseEntity.status(HttpStatus.OK).body(task);
  }

  // Aqui e a validacao para o usuario, quer diser que quando vc busca no banco de dados vc tera acesso so a um usuario, o que vc faz com o GET(o retorno), vc tem retorno dos usuarios, e assim ele so retorna o usurio certo, resumindo se fizer alguma alteracao pode comfima aqui
  @GetMapping("/")
  public List<TaskModel> list(HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    var tasks = this.taskRepository.findByIdUser((UUID) idUser);
    return tasks;
  }

  // Para que o usuario consiga fazer o update da tarefa, o retorno da o idUser, as crendencias do usuario, podendo assim altera alguma informacao, faz isso com PUT(para obeter o retorno)
  @PutMapping("/{id}")
  public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request) {
    
    // busca no banco de dados, e retorna uma tags exitente o seja as informacoes real
    var task = this.taskRepository.findById(id).orElse(null);

    // aqui e para saber se a tarefa existe
    if (task == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Tarefa não encontrada");
    }

    // aqui agente aproveita que ele ja faz uma busca, e com esse codigo abaixo validamos se a tarefa e do mesmo usuario, aqui nao deixa que outro usuario faca a mesma tarefa ou altera a tarefa do outro
    var idUser = request.getAttribute("idUser");
    if (!task.getIdUser().equals(idUser)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Usuário não tem permissão para alterar esta tarefa");
    }

    // converte tudo que for null, passar o quem tem dentro do source para dentro do target, chama a fucao na pasta ultis.java
    Utils.copyNonNullProperties(taskModel, task);

    // Aqui eu salvo, salvo os dados da alteracao, quando altera essa linha salva
    var taskUpadated = this.taskRepository.save(task);

    return ResponseEntity.ok().body(taskUpadated);
  }
}
