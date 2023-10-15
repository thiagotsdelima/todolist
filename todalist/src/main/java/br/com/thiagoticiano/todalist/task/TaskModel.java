package br.com.thiagoticiano.todalist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


  /*
   * Id
   * usuario
   * descricao
   * titulo
   * data de inico
   * data de terminio
   * Prioridade
   */
@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;

    // aqui colocamos para que so posso colocar no maximo 50 letras(caracteres):  @Column(length = 50)
    @Column(length = 50)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    private UUID idUser;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    

    // Aqui passamos uma mensagen customizada para meu usuario, para que os erros nao seja todos igual, quando o usuario coclocar mas de 50 letras, vai receber esse erro ao inveis de receber o erro que server para todo erro 
    public void setTitle(String title) throws Exception {
      // aqui verifico se as letras vao dar maior que 50, e se for mostra esse erro, lancando uma Exception a forma de passar o erro mais simoples
      if(title.length() > 50) {
        throw new Exception("O campo title deve conter no maximo 50 caracteres");
      }
      
      this.title = title;

    }
    
    
  }
