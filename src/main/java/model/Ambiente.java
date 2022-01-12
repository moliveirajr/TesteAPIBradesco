package model;

import lombok.Data;

@Data
public class Ambiente {
    private String ambiente;
    private String baseURL;
    private String clientId;
    private String algoritmo;
    private boolean producao;

    public Ambiente() {
        configuraHomolgacao ();
    }

    public void configuraProducao() {
        this.ambiente = "Produção";
        this.baseURL = "https://openapi.bradesco.com.br";
        this.clientId = "1be56c3e-d6fb-43ef-9bf6-be619b66220d";
        this.algoritmo = "SHA256";
        this.producao = true;
    }

    public void configuraHomolgacao() {
        this.ambiente = "Homologação";
        this.baseURL = "https://proxy.api.prebanco.com.br";
        this.clientId = "8ff166a4-2547-4398-84ed-83a52aa5e676";
        this.algoritmo = "SHA256";
        this.producao = false;
    }
}
