package model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.UsuarioEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdutosRequest {
    private Integer canal;
    private Integer origem;
    private Integer tipoPontoVenda;
    private Integer numeroPontoVenda;
    private String cpfCnpj;
}
