import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import model.Ambiente;
import model.BradSignature;
import model.Dominio;
import model.NaturezaOcupacao;
import service.NaturezaService;
import service.ProfissaoService;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

@Builder
@AllArgsConstructor
public class Dominios {

    private Ambiente ambiente;
    private String authorization;
    private String xBradAuth;


    public void listaProfissoes() {
        var gson = new Gson();
        System.out.println ("--------------------------------------------------------------------");
        System.out.println ("Listar Profissões e sua Natureza -  " + ambiente.getAmbiente ( ));
        System.out.println ("--------------------------------------------------------------------");

        var naturezaService = NaturezaService.builder()
                .endpoint("/cartoes/aquisicao/parceiros/v1/dominios/natureza-ocupacao")
                .ambiente(ambiente)
                .authorization(authorization)
                .xBradAuth(xBradAuth)
                .build();

        Dominio[] naturezas = gson.fromJson(naturezaService.getDados(),Dominio[].class);
        for (Dominio natureza : naturezas) {
            var profissaoService = ProfissaoService.builder()
                    .endpoint("/cartoes/aquisicao/parceiros/v1/dominios/profissao")
                    .ambiente(ambiente)
                    .authorization(authorization)
                    .xBradAuth(xBradAuth)
                    .natureza(natureza.getDominio())
                    .build();
            Dominio[] profissoes  = gson.fromJson(profissaoService.getDados(),Dominio[].class);
            for (Dominio profissao: profissoes){
                System.out.println(natureza.getDominio()+";"+natureza.getDescricao()+";"+profissao.getDominio()+";"+profissao.getDescricao());
            }
        }
        System.out.println ("----\n");
    }
}
