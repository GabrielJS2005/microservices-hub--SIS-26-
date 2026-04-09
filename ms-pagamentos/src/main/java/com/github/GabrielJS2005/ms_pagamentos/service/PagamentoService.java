package com.github.GabrielJS2005.ms_pagamentos.service;

import com.github.GabrielJS2005.ms_pagamentos.dto.PagamentoDTO;
import com.github.GabrielJS2005.ms_pagamentos.entities.Pagamento;
import com.github.GabrielJS2005.ms_pagamentos.entities.Status;
import com.github.GabrielJS2005.ms_pagamentos.exceptions.ResourceNotFoundException;
import com.github.GabrielJS2005.ms_pagamentos.repositories.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Transactional(readOnly = true)
    public List<PagamentoDTO> findAllPagamentos() {

        return (pagamentoRepository.findAll().stream().map(PagamentoDTO::new).toList());

    }

    @Transactional(readOnly = true)
    public PagamentoDTO findPagamentoById(Long id) {

        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id));

        return (new PagamentoDTO(pagamento));

    }

    @Transactional
    public PagamentoDTO savePagamento(PagamentoDTO pagamentoDTO) {

        Pagamento pagamento = new Pagamento();
        copyDTOtoPagamento(pagamentoDTO, pagamento);
        pagamento.setStatus(Status.CRIADO);
        pagamento = pagamentoRepository.save(pagamento);

        return (new PagamentoDTO(pagamento));

    }

    @Transactional
    public PagamentoDTO updatePagamento(Long id, PagamentoDTO pagamentoDTO){

        try {
            Pagamento pagamento = pagamentoRepository.getReferenceById(id);
            copyDTOtoPagamento(pagamentoDTO, pagamento);
            pagamento.setStatus(pagamentoDTO.getStatus());
            pagamento = pagamentoRepository.save(pagamento);
            return new PagamentoDTO(pagamento);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    @Transactional
    public void deletePagamentoById(Long id) {

        if (!pagamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recu");

        }

        pagamentoRepository.deleteById(id);

    }

    private void copyDTOtoPagamento(PagamentoDTO pagamentoDTO, Pagamento pagamento) {

        pagamento.setValor(pagamentoDTO.getValor());
        pagamento.setNome(pagamento.getNome());
        pagamento.setNumeroCartao(pagamentoDTO.getNumeroCartao());
        pagamento.setValidade(pagamentoDTO.getValidade());
        pagamento.setCodigoSeguranca(pagamentoDTO.getCodigoSeguranca());
        pagamento.setPedidoId(pagamento.getPedidoId());

    }

}
