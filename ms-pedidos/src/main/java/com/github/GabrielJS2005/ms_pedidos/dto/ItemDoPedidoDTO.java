package com.github.GabrielJS2005.ms_pedidos.dto;

import com.github.GabrielJS2005.ms_pedidos.entities.ItemDoPedido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemDoPedidoDTO {

    private Long id;

    @NotNull(message = "Quantidade requerida!")
    @Positive(message = "A quantidade de ser um número positivo")
    private Integer quantidade;

    @NotBlank(message = "Descrição requerida")
    private String descricao;


    @NotNull(message = "Preço Unitário é requerido")
    @Positive(message = "O Preço Unitário deve ser um valor positivo")
    private BigDecimal precoUnitario;

    public ItemDoPedidoDTO(ItemDoPedido itemDoPedido) {

        id = itemDoPedido.getId();
        quantidade = itemDoPedido.getQuantidade();
        descricao = itemDoPedido.getDescricao();
        precoUnitario = itemDoPedido.getPrecoUnitario();

    }

}// class
