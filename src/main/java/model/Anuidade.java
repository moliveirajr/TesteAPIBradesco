package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Anuidade {
    private Double valorAnuidade;
    private Integer quantidadeParcelas;
}
