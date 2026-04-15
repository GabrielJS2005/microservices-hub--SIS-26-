package com.github.GabrielJS2005.ms_pagamentos.service;

import com.github.GabrielJS2005.ms_pagamentos.dto.PagamentoDTO;
import com.github.GabrielJS2005.ms_pagamentos.entities.Pagamento;
import com.github.GabrielJS2005.ms_pagamentos.exceptions.ResourceNotFoundException;
import com.github.GabrielJS2005.ms_pagamentos.repositories.PagamentoRepository;
import com.github.GabrielJS2005.ms_pagamentos.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Long existingId;
    private long nonExistingid;

    private Pagamento pagamento;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingid = Long.MAX_VALUE;

        pagamento = Factory.createPagamento();

    }

    @Test
    void deletePagamentoByIdShouldDeleteWhenIdExists() {

        Mockito.when(pagamentoRepository.existsById(existingId)).thenReturn(true);

        pagamentoService.deletePagamentoById(existingId);

        Mockito.verify(pagamentoRepository).existsById(existingId);
        Mockito.verify(pagamentoRepository, Mockito.times(1))
                .deleteById(existingId);

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

    @Test
    void findPagamentoByIdShouldReturnPagamentoDTOWhenIdExists() {

        Mockito.when(pagamentoRepository.findById(existingId))
                .thenReturn(Optional.of(pagamento));

        PagamentoDTO result = pagamentoService.findPagamentoById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());
        Assertions.assertEquals(pagamento.getValor(), result.getValor());

        Mockito.verify(pagamentoRepository).findById(existingId);
        Mockito.verifyNoMoreInteractions(pagamentoRepository);

    }

    @Test
    void findPagamentoByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        Mockito.when(pagamentoRepository.findById(nonExistingid))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pagamentoService.findPagamentoById(nonExistingid));

        Mockito.verify(pagamentoRepository).findById(nonExistingid);
        Mockito.verifyNoMoreInteractions(pagamentoRepository);

    }

    @Test
    @DisplayName("Dado parâmetros válidos e ID nulo " +
            "quando chamar Salvar Pagamento " +
            "então deve gerar ID e persistir um Pagamento")
    void givenValidParamsAndIdIsNull_whenSave_thenShouldPersistPagamento() {

        //Arrange
        Mockito.when(pagamentoRepository.save(any(Pagamento.class)))
                .thenReturn(pagamento);
        PagamentoDTO inputDTO = new PagamentoDTO(pagamento);

        //Act
        PagamentoDTO result = pagamentoService.savePagamento(inputDTO);

        //Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());

        //Verify
        Mockito.verify(pagamentoRepository).save(any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);

    }

    @Test
    void updatePagametoShouldReturnPagamentoDTOWhenPagamentoExists() {

        //Arrange
        Long id = pagamento.getId();
        Mockito.when(pagamentoRepository.getReferenceById(id)).thenReturn(pagamento);
        Mockito.when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        //Act
        PagamentoDTO result = pagamentoService.updatePagamento(id, new PagamentoDTO(pagamento));

        //Assert e Verify
        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(pagamento.getValor(), result.getValor());
        Mockito.verify(pagamentoRepository).getReferenceById(id);
        Mockito.verify(pagamentoRepository).save(Mockito.any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);

    }

    @Test
    void updatePagamentoShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        Mockito.when(pagamentoRepository.getReferenceById(nonExistingid))
                .thenThrow(EntityNotFoundException.class);
        PagamentoDTO inputDTO = new PagamentoDTO(pagamento);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pagamentoService.updatePagamento(nonExistingid, inputDTO));

        Mockito.verify(pagamentoRepository).getReferenceById(nonExistingid);
        Mockito.verify(pagamentoRepository, Mockito.never())
                .save(Mockito.any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);

    }

}// class
