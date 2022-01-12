package model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DadosPessoaisRequest {
    private Integer canal;
    private Integer origem;
    private Integer tipoPontoVenda;
    private Integer numeroPontoVenda;
    private String nomeCompleto;
    private String nomeSocial;
    private String cpfCnpj;
    private String dataNascimento;
    private String nomeMae;
    private Integer nacionalidade;
    private String sexo;
    private Integer tipoDeficiencia;
    private Integer quantidadeDependentes;
    private Integer estadoCivil;
    private Integer tipoSegundoDocumento;
    private String numeroSegundoDocumento;
    private Integer dddCelular;
    private Integer numeroCelular;
    private Integer dddResidencial;
    private Integer numeroResidencial;
    private String email;
    private String ppe;
    private String agentePublico;
}
