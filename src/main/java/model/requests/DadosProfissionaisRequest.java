package model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DadosProfissionaisRequest {
    private Integer canal;
    private Integer origem;
    private Integer tipoPontoVenda;
    private Integer numeroPontoVenda;
    private String cpfCnpj;
    private Integer naturezaOcupacao;
    private Integer profissao;
    private Integer ddd;
    private Integer numero;
    private String ramal;
    private Double renda;
}
