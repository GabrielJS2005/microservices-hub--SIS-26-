package com.github.GabrielJS2005.ms_pedidos.service;

import com.github.GabrielJS2005.ms_pedidos.dto.PedidoDTO;
import com.github.GabrielJS2005.ms_pedidos.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional(readOnly = true)
    public List<PedidoDTO> findAllPedidos() {

        return (pedidoRepository.findAll().stream().map(PedidoDTO::new).toList());

    }

}
