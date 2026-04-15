package com.github.GabrielJS2005.ms_pagamentos.tests;

import com.github.GabrielJS2005.ms_pagamentos.entities.Pagamento;
import com.github.GabrielJS2005.ms_pagamentos.entities.Status;

import java.math.BigDecimal;

public class Factory {

    public static Pagamento createPagamento() {

        Pagamento pagamento = new Pagamento(1L, BigDecimal.valueOf(32.25),
                "Brienne de Tarth", "3651233561261627", "07/15",
                "354", Status.CRIADO, 1L);

        return (pagamento);

    }

    public static Pagamento createPagamentoSemId() {

        Pagamento pagamento = createPagamento();
        pagamento.setId(null);

        return (pagamento);

    }

}// class
