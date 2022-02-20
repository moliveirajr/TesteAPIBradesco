import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import model.Ambiente;
import model.SituacaoProposta;
import model.StatusProposta;
import model.Usuario;
import model.requests.ConsultaRequest;
import service.ConsultaService;

import java.lang.reflect.Array;

@Data
@Builder
@AllArgsConstructor
public class Consulta {

    private Ambiente ambiente;
    private String authorization;
    private String xBradAuth;
    private Usuario usuario;

    private String cpf;
    private Long proposta;

    public void consultaProposta() {
        var gson = new Gson();
        var consultaRequest = ConsultaRequest.builder()
                .canal(usuario.getCanal())
                .origem(usuario.getOrigem())
                .tipoPontoVenda(usuario.getTipoPontoVenda())
                .numeroPontoVenda(usuario.getNumeroPontoVenda())
                .cpf(this.cpf)
                .numeroProposta(this.proposta)
                .build();
        String body = gson.toJson(consultaRequest);

        var consultaService = ConsultaService.builder()
                .endpoint("/cartoes/aquisicao/parceiros/v1/proposta")
                .ambiente(ambiente)
                .authorization(authorization)
                .xBradAuth(xBradAuth)
                .body(body)
                .build();
        var consulta = consultaService.consulta();

        StatusProposta[] propostas = gson.fromJson(consulta,StatusProposta[].class);
        String[] statusString = {"Aprovada","Em análise","Negada","Cancelada","Aguardando Retorno Aceite"};
        for (StatusProposta proposta: propostas) {
            System.out.println("\n--------------");
            System.out.println("     "+proposta.getNumeroproposta());
            System.out.println("     "+proposta.getSituacao()+" - "+statusString[proposta.getSituacao()-1]);
            System.out.println("-------------");
        }

    }
}