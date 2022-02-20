package model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ConsultaRequest {
    private Integer canal;
    private Integer origem;
    private Integer tipoPontoVenda;
    private Integer numeroPontoVenda;
    private String cpf;
    private Long numeroProposta;
}
