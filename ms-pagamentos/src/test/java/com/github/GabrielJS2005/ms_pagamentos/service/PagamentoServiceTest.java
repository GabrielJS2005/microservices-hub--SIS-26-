package com.github.GabrielJS2005.ms_pagamentos.service;

import com.github.GabrielJS2005.ms_pagamentos.exceptions.ResourceNotFoundException;
import com.github.GabrielJS2005.ms_pagamentos.repositories.PagamentoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Long existingId;
    private long nonExistingid;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingid = Long.MAX_VALUE;

    }

    @Test
    void deletePagamentoByIdShouldDeleteWhenIdExists() {

        Mockito.when(pagamentoRepository.existsById(existingId)).thenReturn(true);

        pagamentoService.deletePagamentoById(existingId);

        Mockito.verify(pagamentoRepository).existsById(existingId);
        Mockito.verify(pagamentoRepository, Mockito.times(1)).deleteById(existingId);

    }

    @Test
    @DisplayName("deletePagamento deveria lançar ResourceNotFound quando o ID não existir")
    void deletePagamentoByIdShouldThrowResourceNotFoundExceptionWhenIddoesNotExist() {

        Mockito.when(pagamentoRepository.existsById(nonExistingid)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> {
                    pagamentoService.deletePagamentoById(nonExistingid);
                });

        Mockito.verify(pagamentoRepository).existsById(nonExistingid);
        Mockito.verify(pagamentoRepository, Mockito.never()).deleteById(Mockito.anyLong());

    }

}
