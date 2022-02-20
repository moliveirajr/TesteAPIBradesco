package model;

public enum SituacaoProposta {
    APROVADA("Aprovada"),
    EMANALISE("Em Análise"),
    NEGADA("Negada"),
    CANCELADA("Cancelada"),
    AGUARDANDOACEITE("Aguardando Retorno do Aceite");

    private String descricao;

    SituacaoProposta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
