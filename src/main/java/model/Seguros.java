package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Seguros {
    private String descricao;
    private Double valorSeguro;
    private List<String> codigosExcludentes;
    private Integer idadeMinima;
    private Integer idadeMaxima;
    private String sexo;
    private String grupo;
    private String subGrupo;
    private Double valorMensal;
    private Boolean disponivelTerceiros;
    private String codigo;
    private String tipoSeguro;
}
