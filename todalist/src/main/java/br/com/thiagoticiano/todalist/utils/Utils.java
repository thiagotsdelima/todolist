package br.com.thiagoticiano.todalist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;



 // Para que o usuario consiga fazer o update da tarefa, o retorno da o idUser, as crendencias do usuario, podendo assim altera alguma informacao, faz isso com PUT(para obeter o retorno)
public class Utils {
    // Vamos diser que seja um update parcial. Vamos agora fazer uma validacao para  verificar se os campos estao nulos, se vc esqueceu algum campo, criando assim um metodo que verifique todos os nossos atributos no objeto, vendo o que e nulo o que nao e nulo, passando assim a fazer uma busca no banco de dados 


    // aqui vai recolher tudo que nao for null
    public static void copyNonNullProperties(Object source, Object target) {
      // consegue pega um objeto para outro objeto, entao aqui ele pega tudo que for errado da suas informacoes e faz a convercao, trazendo as informacoes certas
      BeanUtils.copyProperties(source, target, getNullPropertyNames(source)); 
    }


    // aqui vai recolher tudo que for null, alguma informacao errada o usuario estive errando nas suas informacoes, objeto ou id.
    public static String[] getNullPropertyNames(Object source) {
      final BeanWrapper src = new BeanWrapperImpl(source);
      PropertyDescriptor[] pds = src.getPropertyDescriptors();
      Set<String> emptyNames = new HashSet<>();

      for (PropertyDescriptor pd : pds) {
          Object srcValue = src.getPropertyValue(pd.getName());
          if (srcValue == null) {
              emptyNames.add(pd.getName());
          }
      }
      String[] result = new String[emptyNames.size()];
      return emptyNames.toArray(result);
    }
}
