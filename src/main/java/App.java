import com.google.gson.Gson;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import exceptions.ErroBradescoException;
import model.*;
import org.bson.Document;
import service.AccessTokenService;
import service.AuthorizationTokenService;

import java.io.IOException;
import java.util.Iterator;

public class App {
      public static void main(String[] args) throws IOException {

        Gson gson = new Gson();

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Configura Usuário");
        System.out.println("--------------------------------------------------------------------");
        Usuario usuario = Usuario.builder()
                .usuario("C05066794")
                .canal(536)
                .origem(145)
                .tipoPontoVenda(1)
//               .numeroPontoVenda(12783)
                .numeroPontoVenda(12101)
//                .numeroPontoVenda(12273)
                .build();
        System.out.println("********** Usuário" + usuario);

        System.out.println("----\n");
          System.out.println("--------------------------------------------------------------------");
          System.out.println("Conexão mongodb");
          System.out.println("--------------------------------------------------------------------");
          MongoClient mongoClient = new MongoClient();
          System.out.println("********** Conexão mongodb" + usuario);
          System.out.println("----\n");

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Indentificação Cliente");
        System.out.println("--------------------------------------------------------------------");
        Cliente cliente = Cliente.builder()
//                .cpf("43459612894") Leo
//                .cpf("16899971844")
                .cpf("12956844890")
                .ddd(11)
                .celular(999999999L)
                .build();
        System.out.println("********** Cliente  " + cliente.toString());
        System.out.println("----\n");

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Configuração do ambiente");
        System.out.println("--------------------------------------------------------------------");
        Ambiente ambiente = new Ambiente();
        ambiente.configuraProducao();
        System.out.println(ambiente);
        System.out.println("----\n");

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Token de Autorização - Token server - " + ambiente.getAmbiente());
        System.out.println("--------------------------------------------------------------------");
        AuthorizationTokenService authorizationTokenService = new AuthorizationTokenService(ambiente);
        authorizationTokenService.setAuthorizationToken();
        AuthorizationToken authorizationToken = authorizationTokenService.getAuthorizationToken();
        System.out.println(authorizationToken);
        System.out.println("----\n");

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Login origem - " + ambiente.getAmbiente());
        System.out.println("--------------------------------------------------------------------");
        String json = gson.toJson(usuario);
        System.out.println("********** Json " + json);
        var accessTokenService = AccessTokenService.builder()
                .ambiente(ambiente)
                .authorization(authorizationToken.getAccessToken())
                .body(json)
                .build();
        accessTokenService.setBradSignature();
        BradSignature bradAuth = accessTokenService.getBradSignature();
        String xBradAuth = bradAuth.getToken().getAccessToken();
        System.out.println("********** AccessToken  " + bradAuth);
        System.out.println("********** X-Brad-Auth " + xBradAuth);
        System.out.println("----\n");

        switch (args[0]) {
            case "consulta":
                Consulta consulta = null;
                if (args.length == 2) {
                    consulta = Consulta.builder()
                            .usuario(usuario)
                            .ambiente(ambiente)
                            .authorization(authorizationToken.getAccessToken())
                            .xBradAuth(xBradAuth)
                            .cpf(args[1])
                            .build();
                } else if (args.length == 3) {
                    consulta = Consulta.builder()
                            .usuario(usuario)
                            .ambiente(ambiente)
                            .authorization(authorizationToken.getAccessToken())
                            .xBradAuth(xBradAuth)
                            .cpf(args[1])
                            .proposta(Long.valueOf(args[2]))
                            .build();
                }
                if (consulta != null) consulta.consultaProposta();
                break;
            case "proposta":
                var proposta = Proposta.builder()
                        .usuario(usuario)
                        .ambiente(ambiente)
                        .authorization(authorizationToken.getAccessToken())
                        .xBradAuth(xBradAuth)
                        .cliente(cliente)
                        .build();
                try {
                    proposta.executaProposta();
                } catch ( ErroBradescoException e) {
//                    e.printStackTrace();
                }
                break;
            case "dominios":
                var dominios = Dominios.builder()
                        .ambiente(ambiente)
                        .authorization(authorizationToken.getAccessToken())
                        .xBradAuth(xBradAuth)
                        .build();
                dominios.listaProfissoes();
                break;
            case "confere":
                var confere = ConfereStatusProposta.builder()
                        .usuario(usuario)
                        .ambiente(ambiente)
                        .authorization(authorizationToken.getAccessToken())
                        .xBradAuth(xBradAuth)
                        .mongoClient(mongoClient)
                        .build();
                confere.listaSituacao();
        }

//        Foto foto = new Foto ();

//        System.out.println ("--------------------------------------------------------------------");
//        System.out.println ("API Abertura de proposta - " + ambiente.getAmbiente ( ));
//        System.out.println ("--------------------------------------------------------------------");
//        PropostaRequest propostaRequest = PropostaRequest.builder ( )
//                .canal (usuario.getCanal ( ))
//                .origem (usuario.getOrigem ( ))
//                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
//                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
//                .ddd (cliente.getDdd ( ))
//                .celular (cliente.getCelular ( ))
//                .codigoAcesso (0)
//                .cpfCnpj (cliente.getCpf ( ))
//                .build ( );
//        System.out.println ("********** Proposta Request  " + propostaRequest.toString ( ));
//        String jsonProposta = gson.toJson (propostaRequest);
//        System.out.println ("********** Json " + jsonProposta);
//        PropostaService propostaService = PropostaService.builder ( )
//                .ambiente (ambiente)
//                .authorization (authorizationToken.getAccessToken ( ))
//                .body (jsonProposta)
//                .xBradAuth (xBradSignature)
//                .build ( );
//        PropostaEntity propostaEntity = gson.fromJson (propostaService.abrirProposta ( ), PropostaEntity.class);
//        System.out.println (propostaEntity.toString ( ));
//        System.out.println ("----\n");
//
//
//        System.out.println ("--------------------------------------------------------------------");
//        System.out.println ("API Oferta de produtos - " + ambiente.getAmbiente ( ));
//        System.out.println ("--------------------------------------------------------------------");
//        ProdutosRequest produtosRequest = ProdutosRequest.builder ( )
//                .canal (usuario.getCanal ( ))
//                .origem (usuario.getOrigem ( ))
//                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
//                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
//                .cpfCnpj (cliente.getCpf ( ))
//                .build ( );
//        System.out.println ("********** Oferta de produtos Request  " + produtosRequest.toString ( ));
//        String jsonProdutos = gson.toJson (produtosRequest);
//        System.out.println ("********** Json " + jsonProdutos);
//        ProdutosService produtosService = ProdutosService.builder ( )
//                .ambiente (ambiente)
//                .authorization (authorizationToken.getAccessToken ( ))
//                .body (jsonProdutos)
//                .xBradAuth (xBradSignature)
//                .propostaNum (String.valueOf (propostaEntity.getNumeroProposta ( )))
//                .build ( );
//        ProdutosEntity produtosEntity = gson.fromJson (produtosService.getProdutos ( ), ProdutosEntity.class);
//        System.out.println ("********** Produtos " + produtosEntity.toString () );
//        System.out.println ("----\n");
//
//        System.out.println ("--------------------------------------------------------------------");
//        System.out.println ("API Cartões - " + ambiente.getAmbiente ( ));
//        System.out.println ("--------------------------------------------------------------------");
//        List<Cartoes> cartoes = new ArrayList<> ( );
//        cartoes.add (Cartoes.builder ( )
//                .diaVencimento (29)
//                .codigoProduto ("106048")
//                .nomeImpressao ("Nadia S Oliveira")
//                .build ( ));
//        CartoesRequest cartoesRequest = CartoesRequest.builder ( )
//                .canal (usuario.getCanal ( ))
//                .origem (usuario.getOrigem ( ))
//                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
//                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
//                .cpfCnpj (cliente.getCpf ( ))
//                .cartoes (cartoes)
//                .build ( );
//        System.out.println ("********** Cartoes Request  " + cartoesRequest.toString ( ));
//        String jsonCatao = gson.toJson (cartoesRequest);
//        System.out.println ("********** Json " + jsonCatao);
//        CartaoService cartaoService = CartaoService.builder ( )
//                .ambiente (ambiente)
//                .authorization (authorizationToken.getAccessToken ( ))
//                .body (jsonCatao)
//                .xBradAuth (xBradSignature)
//                .propostaNum (String.valueOf (propostaEntity.getNumeroProposta ( )))
//                .build ( );
//        cartaoService.setCartao ( );
//        System.out.println ("----\n");
//
//        System.out.println ("--------------------------------------------------------------------");
//        System.out.println ("API Dados cartões adicionais - " + ambiente.getAmbiente ( ));
//        System.out.println ("--------------------------------------------------------------------");
//        List<DadosCartoesAdicionais> dadosCartoesAdicionais = new ArrayList<> ( );
//        dadosCartoesAdicionais.add (DadosCartoesAdicionais.builder ( )
//                .build ( ));
//        CartoesAdicionaisRequest cartoesAdicionaisRequest = CartoesAdicionaisRequest.builder ( )
//                .canal (usuario.getCanal ( ))
//                .origem (usuario.getOrigem ( ))
//                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
//                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
//                .cpfCnpj (cliente.getCpf ( ))
//                .lista (dadosCartoesAdicionais)
//                .build ( );
//        System.out.println ("********** Cartoes Request  " + cartoesAdicionaisRequest.toString ( ));
//        String jsonCartoesAdicionais = gson.toJson (cartoesAdicionaisRequest);
//        System.out.println ("********** Json " + jsonCartoesAdicionais);
//        System.out.println ("----\n");
//
//        System.out.println ("--------------------------------------------------------------------");
//        System.out.println ("API Dados pessoais - " + ambiente.getAmbiente ( ));
//        System.out.println ("--------------------------------------------------------------------");
//        DadosPessoaisRequest dadosPessoaisRequest = DadosPessoaisRequest.builder ( )
//                .canal (usuario.getCanal ( ))
//                .origem (usuario.getOrigem ( ))
//                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
//                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
//                .nomeCompleto ("Nadia Scopeta de Oliveira")
//                .cpfCnpj (cliente.getCpf ( ))
//                .dataNascimento ("18/11/1979")
//                .nomeMae ("Adremira Scopeta")
//                .nacionalidade (9)
//                .sexo ("F")
//                .tipoDeficiencia (1)
//                .quantidadeDependentes (2)
//                .estadoCivil (3)
////                .tipoSegundoDocumento ()
////                .numeroSegundoDocumento ()
//                .dddCelular (11)
//                .numeroCelular (995597656)
////                .dddResidencial ()
////                .numeroResidencial ()
//                .email ("marco-ojunior@via.com.br")
//                .ppe ("N")
//                .agentePublico ("N")
//                .build ( );
//        System.out.println ("********** Dados Pessoais Request  " + cartoesAdicionaisRequest);
//        String jsonDadosPessoais = gson.toJson (dadosPessoaisRequest);
//        System.out.println ("********** Json " + jsonDadosPessoais);
//        DadosPessoaisService dadosPessoaisService = DadosPessoaisService.builder ( )
//                .ambiente (ambiente)
//                .authorization (authorizationToken.getAccessToken ( ))
//                .body (jsonDadosPessoais)
//                .xBradAuth (xBradSignature)
//                .propostaNum (String.valueOf (propostaEntity.getNumeroProposta ( )))
//                .build ( );
//        dadosPessoaisService.setDadosPessoais ( );
//        System.out.println ("----\n");
//
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("API Dados localização - "+ambiente.getAmbiente());
//        System.out.println("--------------------------------------------------------------------");
//        DadosLocalizacaoRequest dadosLocalizacaoRequest = DadosLocalizacaoRequest.builder ( )
//                .canal (usuario.getCanal ( ))
//                .origem (usuario.getOrigem ( ))
//                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
//                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
//                .cpfCnpj (cliente.getCpf ( ))
//                .cep ("03142-030")
//                .logradouro ("Rua Barão de Juparana")
//                .numero (469)
//                .complemento ("")
//                .bairro ("Vila Zelina")
//                .cidade ("São Paulo")
//                .estado (25)
//                .build ( );
//        System.out.println ("********** Dados localização Request  " + dadosLocalizacaoRequest);
//        String jsonDadosLocalizacao = gson.toJson (dadosLocalizacaoRequest);
//        System.out.println ("********** Json " + jsonDadosLocalizacao);
//        DadosLocalizacaoService dadosLocalizacaoService = DadosLocalizacaoService.builder ( )
//                .ambiente (ambiente)
//                .authorization (authorizationToken.getAccessToken ( ))
//                .body (jsonDadosLocalizacao)
//                .xBradAuth (xBradSignature)
//                .propostaNum (String.valueOf (propostaEntity.getNumeroProposta ( )))
//                .build ( );
//        dadosLocalizacaoService.setDadosLocalizacao ();
//        System.out.println ("----\n");
//
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("API Dados profissionais - "+ambiente.getAmbiente());
//        System.out.println("--------------------------------------------------------------------");
//        DadosProfissionaisRequest dadosProfissionaisRequest = DadosProfissionaisRequest.builder ( )
//                .canal (usuario.getCanal ( ))
//                .origem (usuario.getOrigem ( ))
//                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
//                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
//                .cpfCnpj (cliente.getCpf ( ))
//                .naturezaOcupacao (1)
//                .profissao (1)
//                .ddd (11)
//                .numero (995597656)
//                .renda (0.00)
//                .build ( );
//        System.out.println ("********** Dados profissionais Request  " + dadosProfissionaisRequest);
//        String jsonDadosProfissionais = gson.toJson (dadosProfissionaisRequest);
//        System.out.println ("********** Json " + jsonDadosProfissionais);
//        DadosProfissionaisService dadosProfissionaisService = DadosProfissionaisService.builder ( )
//                .ambiente (ambiente)
//                .authorization (authorizationToken.getAccessToken ( ))
//                .body (jsonDadosProfissionais)
//                .xBradAuth (xBradSignature)
//                .propostaNum (String.valueOf (propostaEntity.getNumeroProposta ( )))
//                .build ( );
//        dadosProfissionaisService.setDados ();
//        System.out.println ("----\n");
//
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("API Foto - "+ambiente.getAmbiente());
//        System.out.println("--------------------------------------------------------------------");
//        FotoRequest fotoRequest = FotoRequest.builder ( )
//                .canal (usuario.getCanal ( ))
//                .origem (usuario.getOrigem ( ))
//                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
//                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
//                .cpfCnpj (cliente.getCpf ( ))
//                .conteudoDocumento (foto.getFoto ())
//                .tipoDocumento (920)
//                .build ( );
//        System.out.println ("********** Foto Request  " + dadosProfissionaisRequest);
//        String jsonFoto = gson.toJson (fotoRequest);
//        System.out.println ("********** Json " + jsonFoto);
//       DadosFotoService dadosFotoService = DadosFotoService.builder ( )
//                .ambiente (ambiente)
//                .authorization (authorizationToken.getAccessToken ( ))
//                .body (jsonFoto)
//                .xBradAuth (xBradSignature)
//                .propostaNum (String.valueOf (propostaEntity.getNumeroProposta ( )))
//                .build ( );
//        dadosFotoService.setDados ();
//        System.out.println ("----\n");
//
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("API Documento - "+ambiente.getAmbiente());
//        System.out.println("--------------------------------------------------------------------");
//        FotoRequest documentoRequest = FotoRequest.builder ( )
//                .canal (usuario.getCanal ( ))
//                .origem (usuario.getOrigem ( ))
//                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
//                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
//                .cpfCnpj (cliente.getCpf ( ))
//                .conteudoDocumento (foto.getDocumento ().toString ())
//                .tipoDocumento (8)
//                .build ( );
//        System.out.println ("********** Foto Request  " + documentoRequest);
//        String jsonDocumento = gson.toJson (documentoRequest);
//        System.out.println ("********** Json " + jsonDocumento);
//        DadosFotoService dadosDocService = DadosFotoService.builder ( )
//                .ambiente (ambiente)
//                .authorization (authorizationToken.getAccessToken ( ))
//                .body (jsonDocumento)
//                .xBradAuth (xBradSignature)
//                .propostaNum (String.valueOf (propostaEntity.getNumeroProposta ( )))
//                .build ( );
//        dadosDocService.setDados ();
//        System.out.println ("----\n");
//
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("API Optins - "+ambiente.getAmbiente());
//        System.out.println("--------------------------------------------------------------------");
//        var optinList = produtosEntity.getOptins();
//        var optinArrayList = new ArrayList<Optin>();
//        for (Optins optins:optinList) {
//            System.out.println(optins.toString());
//            optinArrayList.add( Optin.builder()
//                            .codigoOptin(optins.getCodigo())
//                            .valorOptin("S")
//                    .build());
//        }
//        System.out.println(optinArrayList);
//        OptinsRequest optinsRequest = OptinsRequest.builder()
//                .canal (usuario.getCanal ( ))
//                .origem (usuario.getOrigem ( ))
//                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
//                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
//                .cpfCnpj (cliente.getCpf ( ))
//                .lista (optinArrayList)
//                .build();
//        System.out.println ("********** Optins Request  " + optinsRequest);
//        String jsonOptins = gson.toJson (optinsRequest);
//        System.out.println ("********** Json " + jsonOptins);
//        OptinsService optinsService = OptinsService.builder()
//                .ambiente (ambiente)
//                .authorization (authorizationToken.getAccessToken ( ))
//                .body (jsonOptins)
//                .xBradAuth (xBradSignature)
//                .propostaNum (String.valueOf (propostaEntity.getNumeroProposta ( )))
//                .build();
//        optinsService.setDados();
//        System.out.println ("----\n");
//
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("API Gerar PAC - "+ambiente.getAmbiente());
//        System.out.println("--------------------------------------------------------------------");
//        GerarPACRequest gerarPACRequest = GerarPACRequest.builder()
//                .canal (usuario.getCanal ( ))
//                .origem (usuario.getOrigem ( ))
//                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
//                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
//                .cpfCnpj (cliente.getCpf ( ))
//                .build();
//        System.out.println ("********** Gerar PAC Request  " + gerarPACRequest);
//        String jsonGerarPAC = gson.toJson (gerarPACRequest);
//        System.out.println ("********** Json " + jsonGerarPAC);
//        GerarPACService gerarPACService = GerarPACService.builder()
//                .ambiente (ambiente)
//                .authorization (authorizationToken.getAccessToken ( ))
//                .body (jsonGerarPAC)
//                .xBradAuth (xBradSignature)
//                .propostaNum (String.valueOf (propostaEntity.getNumeroProposta ( )))
//                .build();
//        gerarPACService.setDados();
//        System.out.println ("----\n");
//
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("API Finalizar Proposta - "+ambiente.getAmbiente());
//        System.out.println("--------------------------------------------------------------------");
//        FinalizarPropostaRequest finalizarPropostaRequest = FinalizarPropostaRequest.builder()
//                .canal (usuario.getCanal ( ))
//                .origem (usuario.getOrigem ( ))
//                .numeroPontoVenda (usuario.getNumeroPontoVenda ( ))
//                .tipoPontoVenda (usuario.getTipoPontoVenda ( ))
//                .cpfCnpj (cliente.getCpf ( ))
//                .analiseEspecial("N")
//                .nomeIndicadorVenda("Marco Junior")
//                .build();
//        System.out.println ("********** Finalizar Proposta Request  " + finalizarPropostaRequest);
//        String jsonFinalizar = gson.toJson (finalizarPropostaRequest);
//        System.out.println ("********** Json " + jsonFinalizar);
//        FinalizarService finalizarService = FinalizarService.builder()
//                .ambiente (ambiente)
//                .authorization (authorizationToken.getAccessToken ( ))
//                .body (jsonFinalizar)
//                .xBradAuth (xBradSignature)
//                .propostaNum (String.valueOf (propostaEntity.getNumeroProposta ( )))
//                .build();
//        finalizarService.setDados();
//        System.out.println ("----\n");
    }
}
