package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CamposParametrizaveisEntity {
    private String identificador;
    private String tipoInformacao;
    private String nomeCampoTela;
    private String campoObrigatorio;
}
