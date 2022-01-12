package model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class DadosLocalizacaoRequest {
    private Integer canal;
    private Integer origem;
    private Integer tipoPontoVenda;
    private Integer numeroPontoVenda;
    private String cpfCnpj;
    private String cep;
    private Integer numero;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String cidade;
    private Integer estado;
}
