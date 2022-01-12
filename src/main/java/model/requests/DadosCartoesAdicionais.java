package model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DadosCartoesAdicionais {
    private String cpf;
    private String nomeImpressao;
    private String dataNascimento;
    private Integer dddCelular;
    private Integer numeroCelular;
    private String sexo;
    private String nomeMae;
    private Integer codigoEstadoCivil;
    private Integer codigoParentesco;
    private String ppe;
    private String agentePublico;
    private Integer naturezaOcupacao;
    private Integer profissao;
}
