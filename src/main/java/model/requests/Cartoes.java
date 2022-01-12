package model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Cartoes {
    private Integer diaVencimento;
    private String codigoProduto;
    private String nomeImpressao;
    private String codigoCampanha;
    private FormaPagamento formaPagamento;
}
