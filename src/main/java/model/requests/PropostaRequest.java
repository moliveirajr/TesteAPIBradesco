package model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PropostaRequest {
    private Integer canal;
    private Integer origem;
    private Integer tipoPontoVenda;
    private Integer numeroPontoVenda;
    private Long celular;
    private Integer ddd;
    private Integer codigoAcesso;
    private String cpfCnpj;
}
