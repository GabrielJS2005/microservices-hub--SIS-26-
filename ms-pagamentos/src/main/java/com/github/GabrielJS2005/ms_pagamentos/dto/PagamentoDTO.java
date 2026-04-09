package com.github.GabrielJS2005.ms_pagamentos.dto;

import com.github.GabrielJS2005.ms_pagamentos.entities.Pagamento;
import com.github.GabrielJS2005.ms_pagamentos.entities.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PagamentoDTO {

    private Long id;

    @NotNull(message = "O campo valor é obrigatório")
    @Positive(message = "O valor do pagamento dever ser um número positivo")
    private BigDecimal valor;

    @NotBlank(message = "O campo nome é obrigatório")
    @Size(min = 3, max = 50, message = "O nome deve entre 3 e 50 caracteres")
    private String nome;

    @NotBlank(message = "O campo número do cartão é obrigatório")
    @Size(min = 16, max = 16, message = "Número do cartão deve ter 16 caracteres")
    private String numeroCartao; // XXXX XXXX XXXX XXXX

    @NotBlank(message = "O campo validade é obrigatório")
    @Size(min = 5, max = 5, message = "Validade do cartão deve ter 5 caracteres")
    private String validade; // MM/AA

    @NotBlank(message = "O código de segurança é obrigatório")
    @Size(min = 3, max = 3, message = "Código de segurança deve ter 3 caracteres")
    private String codigoSeguranca; // XXX

    private Status status;

    @NotNull(message = "O campo ID do pedido é obrigatório")
    private Long pedidoId;

    public PagamentoDTO(Pagamento pagamento) {

        id = pagamento.getId();
        valor = pagamento.getValor();
        nome = pagamento.getNome();
        numeroCartao = pagamento.getNumeroCartao();
        validade = pagamento.getValidade();
        codigoSeguranca = pagamento.getCodigoSeguranca();
        status = pagamento.getStatus();
        pedidoId = pagamento.getPedidoId();

    }

}
