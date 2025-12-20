package br.com.adocao.model;

/**
 * Enum que define os possíveis status de uma {@link Solicitacao}.
 */
public enum StatusSolicitacao {
    /** A solicitação foi criada e aguarda análise. */
    PENDENTE,
    /** A solicitação (ex: resgate) está sendo atendida. */
    EM_ANDAMENTO,
    /** A solicitação foi aprovada por um administrador. */
    APROVADO,
    /** A solicitação foi cancelada pelo próprio usuário. */
    CANCELADA,
    /** A solicitação foi reprovada por um administrador. */
    REPROVADO;
}