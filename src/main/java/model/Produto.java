package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Produto {
    private Integer codigoProduto;
    private String nomeProduto;
    private List<Integer> vencimentos;
    private List<Seguros> seguros;
    private Anuidade anuidadeTitular;
    private Anuidade anuidadeAdicional;
}
