package model;

import lombok.Data;

@Data
public class StatusProposta {
    private Long numeroproposta;
    private Integer situacao;
    private Integer origem;
    private Integer tipopontovenda;
    private Integer numeropontovenda;
}
