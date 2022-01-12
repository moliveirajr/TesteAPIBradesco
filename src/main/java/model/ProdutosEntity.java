package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ProdutosEntity {
  private List<Produto> produtos;
  private Double valorLimiteAprovadoCompra;
  private Double valorLimiteAprovadoSaque;
  private Double valorLimiteAprovadoParcela;
  private Integer quantidadeMaximaCartoesAdicionaisProposta;
  private List<Optins> optins;
}

