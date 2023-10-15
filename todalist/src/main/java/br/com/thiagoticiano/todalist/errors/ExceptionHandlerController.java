package br.com.thiagoticiano.todalist.errors;

import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// todo erro do taskcontroller que for do tipo http vai passar poraqui, entao aqui posso colocar todos os erros que tenha na minha aplicacao, cada erro com suas mensagem, que vc queria retorna para usuario de forma mas amigavel mas tartavel uma mesagen X e aqui
@ControllerAdvice
public class ExceptionHandlerController {
    // todo erro vai passar por aqui, antes de ir para o usuário 
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        //  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); se eu fizer assim, o retorno vem com (jason parse error), entao coloco getMostSpecificCause
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());
    }
}
