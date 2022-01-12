package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropostaEntity {
    private String codigoAcessoObrigatorio;
    private String capturaFotoObrigatoria;
    private String pacDigitalObrigatoria;
    private String documentosObrigatorios;
    private String emissaoInstantaneaHabilitada;
    private String cadastroSenhaEmbossing;
    private Long numeroProposta;
    private List<CamposParametrizaveisEntity> camposParametrizaveis;
}
