package com.github.GabrielJS2005.ms_pagamentos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.GabrielJS2005.ms_pagamentos.dto.PagamentoDTO;
import com.github.GabrielJS2005.ms_pagamentos.entities.Pagamento;
import com.github.GabrielJS2005.ms_pagamentos.exceptions.ResourceNotFoundException;
import com.github.GabrielJS2005.ms_pagamentos.service.PagamentoService;
import com.github.GabrielJS2005.ms_pagamentos.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // IMPORTANTE: Agrupa get, post, put, delete
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagamentoController.class)
public class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PagamentoService pagamentoService;
    private Pagamento pagamento;
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = Long.MAX_VALUE;
        pagamento = Factory.createPagamento();

    }

    @Test
    void findAllPagamentosShouldReturnListPagamentoDTO() throws Exception {
        // Arrange
        PagamentoDTO inputDto = new PagamentoDTO(pagamento);
        List<PagamentoDTO> list = List.of(inputDto);
        Mockito.when(pagamentoService.findAllPagamentos()).thenReturn(list);

        // Act + Assert
        ResultActions result = mockMvc.perform(get("/pagamentos")
                .accept(MediaType.APPLICATION_JSON) ); // request: Accept

        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$[0].id").value(pagamento.getId()));
        result.andExpect(jsonPath("$[0].valor").value(pagamento.getValor().doubleValue()));

        // Verify (comportamento)
        Mockito.verify(pagamentoService).findAllPagamentos();
        Mockito.verifyNoMoreInteractions(pagamentoService);
    }

    @Test
    void findPagamentoByIdShouldReturnPagamentoDTOWhenIdExists() throws Exception {

        PagamentoDTO responseDTO = new PagamentoDTO(pagamento);
        Mockito.when(pagamentoService.findPagamentoById(existingId)).thenReturn(responseDTO);

        mockMvc.perform(get("/pagamentos/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.valor").value(pagamento.getValor().doubleValue()))
                .andExpect(jsonPath("$.status").value(pagamento.getStatus().name()))
                .andExpect(jsonPath("$.pedidoId").value(pagamento.getPedidoId()));

        Mockito.verify(pagamentoService).findPagamentoById(existingId);
        Mockito.verifyNoMoreInteractions(pagamentoService);
    }

    @Test
    void findPagamentoByIdShouldReturn404whenIdDoesNotExist() throws Exception{

        Mockito.when(pagamentoService.findPagamentoById(nonExistingId))
                .thenThrow(new ResourceNotFoundException("Recurso não encontrado. ID: "
                        + nonExistingId));

        mockMvc.perform(get("/pagamentos/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

        Mockito.verify(pagamentoService).findPagamentoById(nonExistingId);
        Mockito.verifyNoMoreInteractions(pagamentoService);

    }

    @Test
    void createPagamentoShouldReturn201WhenValid() throws Exception {

        PagamentoDTO requestDTO = new PagamentoDTO(Factory.createPagamentoSemId());
        // Bean objectMapper para converter JAVA para JSON
        String jsonRequestBody = objectMapper.writeValueAsString(requestDTO);
        PagamentoDTO responseDTO = new PagamentoDTO(pagamento);

        Mockito.when(pagamentoService.savePagamento(any(PagamentoDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON) // request Content-Type
                        .accept(MediaType.APPLICATION_JSON)      // request Accept
                        .content(jsonRequestBody))               // request body
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) // response
                .andExpect(jsonPath("$.id").value(pagamento.getId()))
                .andExpect(jsonPath("$.status").value(pagamento.getStatus().name()))
                .andExpect(jsonPath("$.valor").value(pagamento.getValor().doubleValue()))
                .andExpect(jsonPath("$.pedidoId").value(pagamento.getPedidoId()));

        Mockito.verify(pagamentoService).savePagamento(any(PagamentoDTO.class));
        Mockito.verifyNoMoreInteractions(pagamentoService);
    }

} //class
