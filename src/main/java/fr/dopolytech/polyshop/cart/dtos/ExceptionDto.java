package fr.dopolytech.polyshop.cart.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor(staticName = "create")
public class ExceptionDto {
  
  public String code;
  public String message;

}
