package model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FormaPagamento {
    private Integer id;
    private Integer agencia;
    private Integer conta;
    private Integer digitoConta;
}
