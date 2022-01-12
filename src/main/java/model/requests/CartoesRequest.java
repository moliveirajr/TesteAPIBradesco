package model.requests;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartoesRequest {
    private Integer canal;
    private Integer origem;
    private Integer tipoPontoVenda;
    private Integer numeroPontoVenda;
    private String cpfCnpj;
    private List<Cartoes> cartoes;
}

