package com.github.GabrielJS2005.ms_pagamentos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.GabrielJS2005.ms_pagamentos.dto.PagamentoDTO;
import com.github.GabrielJS2005.ms_pagamentos.entities.Pagamento;
import com.github.GabrielJS2005.ms_pagamentos.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PagamentoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Pagamento pagamento;
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = Long.MAX_VALUE;

    }

    @Test
    void createPagamentoShouldReturn422WhenInvalid() throws Exception {

        Pagamento pagamentoInvalido = Factory.createPagamentoSemId();
        pagamentoInvalido.setValor(BigDecimal.valueOf(0));
        pagamentoInvalido.setNome(null);
        PagamentoDTO requestDTO = new PagamentoDTO(pagamentoInvalido);
        String jsonRequestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("Dados inválidos"))
                .andExpect(jsonPath("$.errors").isArray());

    }

    @Test
    void updatePagamentoShouldReturn200WhenIdExists() throws Exception{

        pagamento = Factory.createPagamento();
        PagamentoDTO requestDTO = new PagamentoDTO(pagamento);
        String jsonRequestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(put("/pagamentos/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.nome").value(pagamento.getNome()));

    }

//    void updatePagamentoShouldReturn404WhenIdDoesNotExists() throws Exception{
//
//        pagamento = Factory.createPagamento();
//        PagamentoDTO requestDTO = new PagamentoDTO(pagamento);
//        String jsonRequestBody = objectMapper.writeValueAsString(requestDTO);
//
//        mockMvc.perform(put("/pagamentos/{id}", nonExistingId)
//                .contentType(MediaType.APPLICATION_JSON))
//
//    }

}
