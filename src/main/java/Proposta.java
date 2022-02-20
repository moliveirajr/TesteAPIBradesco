import com.google.gson.Gson;
import exceptions.ErroAberturaPropostaException;
import exceptions.ErroBradescoException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import model.*;
import model.requests.*;
import okhttp3.Response;
import service.BradescoClient;
import util.Foto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
public class Proposta {

    private Ambiente ambiente;
    private String authorization;
    private String xBradAuth;
    private Usuario usuario;
    private Cliente cliente;

    public void executaProposta() throws ErroBradescoException, IOException {
        Gson gson = new Gson();
        Foto foto = new Foto();
        String body;
        BradescoClient client;
        Response response;

        System.out.println("--------------------------------------------------------------------");
        System.out.println("API Abertura de proposta - " + ambiente.getAmbiente());
        System.out.println("--------------------------------------------------------------------");
        PropostaRequest propostaRequest = PropostaRequest.builder()
                .canal(usuario.getCanal())
                .origem(usuario.getOrigem())
                .tipoPontoVenda(usuario.getTipoPontoVenda())
                .numeroPontoVenda(usuario.getNumeroPontoVenda())
                .ddd(cliente.getDdd())
                .celular(cliente.getCelular())
                .codigoAcesso(0)
                .cpfCnpj(cliente.getCpf())
                .build();
        System.out.println("********** Proposta Request  " + propostaRequest.toString());
        body = gson.toJson(propostaRequest);
        System.out.println("********** Body  " + body);

        client = BradescoClient.builder()
                .endpoint("/cartoes/aquisicao/parceiros/v1/proposta")
                .ambiente(ambiente)
                .xBradAuth(xBradAuth)
                .authorization(authorization)
                .body(body)
                .build();
        response = client.getDados();
        if (!response.isSuccessful()) {
            var erroBradesco = gson.fromJson(response.body().charStream(), ErroBradescoEntity.class);
            throw new ErroBradescoException("Erro na abertura de Proposta - " +response.code()+ " - "+response.message(),erroBradesco);
        }
        PropostaEntity proposta = gson.fromJson(response.body().charStream(),PropostaEntity.class);
        String propostaNum = proposta.getNumeroProposta().toString();


        System.out.println ("--------------------------------------------------------------------");
        System.out.println ("API Oferta de produtos - " + ambiente.getAmbiente ( ));
        System.out.println ("--------------------------------------------------------------------");
        ProdutosRequest produtosRequest = ProdutosRequest.builder ( )
                .canal (usuario.getCanal ( ))
                .origem (usuario.getOrigem ( ))
                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
                .cpfCnpj (cliente.getCpf ( ))
                .build ( );
        System.out.println ("********** Oferta de produtos Request  " + produtosRequest.toString ( ));
        body = gson.toJson(produtosRequest);
        System.out.println("********** Body  " + body);
        client = BradescoClient.builder()
                .endpoint("/cartoes/aquisicao/parceiros/v1/proposta/"+ propostaNum +"/oferta-produtos")
                .ambiente(ambiente)
                .xBradAuth(xBradAuth)
                .authorization(authorization)
                .body(body)
                .build();
        response = client.getDados();
        if (!response.isSuccessful()) {
            var erroBradesco = gson.fromJson(response.body().charStream(), ErroBradescoEntity.class);
            throw new ErroBradescoException("Erro na Busca de Produtos - " +response.code()+ " - "+response.message(),erroBradesco);
        }
        ProdutosEntity produtos = gson.fromJson(response.body().charStream(),ProdutosEntity.class);
        System.out.println ("********** Produtos  " + produtos.toString() );
        String produto = produtos.getProdutos().stream().findFirst().get().getCodigoProduto().toString();
        Integer diaVencimento = produtos.getProdutos().stream().findFirst().get().getVencimentos().get(0);
        System.out.println("********** Produto selecionado " +(produto));

        System.out.println ("--------------------------------------------------------------------");
        System.out.println ("API Cartões - " + ambiente.getAmbiente ( ));
        System.out.println ("--------------------------------------------------------------------");
        List<Cartoes> cartoes = new ArrayList<>( );
        cartoes.add (Cartoes.builder ( )
                .diaVencimento (diaVencimento)
                .codigoProduto (produto)
                .nomeImpressao ("João Jose Silva")
                .build ( ));
        CartoesRequest cartoesRequest = CartoesRequest.builder ( )
                .canal (usuario.getCanal ( ))
                .origem (usuario.getOrigem ( ))
                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
                .cpfCnpj (cliente.getCpf ( ))
                .cartoes (cartoes)
                .build ( );
        System.out.println ("********** Cartoes Request  " + cartoesRequest.toString ( ));
        body = gson.toJson(cartoesRequest);
        System.out.println("********** Body  " + body);
        client = BradescoClient.builder()
                .endpoint("/cartoes/aquisicao/parceiros/v1/proposta/"+ propostaNum +"/cartoes")
                .ambiente(ambiente)
                .xBradAuth(xBradAuth)
                .authorization(authorization)
                .body(body)
                .build();
        response = client.getDados();
        if (!response.isSuccessful()) {
            var erroBradesco = gson.fromJson(response.body().charStream(), ErroBradescoEntity.class);
            throw new ErroBradescoException("Erro no preenchimento do Cartão - " +response.code()+ " - "+response.message(),erroBradesco);
        }
        System.out.println ("********** Inserir Cartões -  " + response.body().string());

        System.out.println ("--------------------------------------------------------------------");
        System.out.println ("API Dados cartões adicionais - " + ambiente.getAmbiente ( ));
        System.out.println ("--------------------------------------------------------------------");


        System.out.println ("--------------------------------------------------------------------");
        System.out.println ("API Dados pessoais - " + ambiente.getAmbiente ( ));
        System.out.println ("--------------------------------------------------------------------");
        DadosPessoaisRequest dadosPessoaisRequest = DadosPessoaisRequest.builder ( )
                .canal (usuario.getCanal ( ))
                .origem (usuario.getOrigem ( ))
                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
                .nomeCompleto ("Nadia Scopeta de Oliveira")
                .cpfCnpj (cliente.getCpf ( ))
                .dataNascimento ("18/11/1979")
                .nomeMae ("Adremira Scopeta")
                .nacionalidade (9)
                .sexo ("F")
                .tipoDeficiencia (1)
                .quantidadeDependentes (2)
                .estadoCivil (3)
//                .tipoSegundoDocumento ()
//                .numeroSegundoDocumento ()
                .dddCelular (11)
                .numeroCelular (995597656)
                .dddResidencial (11)
                .numeroResidencial (995597656)
                .email ("marco-ojunior@via.com.br")
                .ppe ("N")
                .agentePublico ("N")
                .build ( );
        System.out.println ("********** Dados Pessoais Request  " +dadosPessoaisRequest);
        body = gson.toJson(dadosPessoaisRequest);

        System.out.println("********** Body  " + body);
        client = BradescoClient.builder()
                .endpoint("/cartoes/aquisicao/parceiros/v1/proposta/"+ propostaNum +"/dados-pessoais")
                .ambiente(ambiente)
                .xBradAuth(xBradAuth)
                .authorization(authorization)
                .body(body)
                .build();
        response = client.getDados();
        if (!response.isSuccessful()) {
            var erroBradesco = gson.fromJson(response.body().charStream(), ErroBradescoEntity.class);
            throw new ErroBradescoException("Erro ao enviar dados pessoais - " +response.code()+ " - "+response.message(),erroBradesco);
        }
        System.out.println ("********** Dados Pessoais -  " + response.body().string());

    }


}
