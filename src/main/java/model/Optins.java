package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Optins {
    private Integer codigo;
    private String descricaoCompleta;
    private String descricaoResumida;
    private String obrigatorio;
}
