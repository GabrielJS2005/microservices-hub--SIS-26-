package com.github.GabrielJS2005.ms_pedidos.service;

import com.github.GabrielJS2005.ms_pedidos.dto.ItemDoPedidoDTO;
import com.github.GabrielJS2005.ms_pedidos.dto.PedidoDTO;
import com.github.GabrielJS2005.ms_pedidos.entities.ItemDoPedido;
import com.github.GabrielJS2005.ms_pedidos.entities.Pedido;
import com.github.GabrielJS2005.ms_pedidos.entities.Status;
import com.github.GabrielJS2005.ms_pedidos.exceptions.ResourceNotFoundException;
import com.github.GabrielJS2005.ms_pedidos.repositories.ItemDoPedidoRepository;
import com.github.GabrielJS2005.ms_pedidos.repositories.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemDoPedidoRepository itemDoPedidoRepository;

    @Transactional(readOnly = true)
    public List<PedidoDTO> findAllPedidos() {

        return (pedidoRepository.findAll().stream().map(PedidoDTO::new).toList());

    }

    @Transactional(readOnly = true)
    public PedidoDTO findPedidoById (Long id) {

        Pedido pedido = pedidoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado! ID: " + id));

        return (new PedidoDTO(pedido));

    }

    @Transactional
    public PedidoDTO savePedido (PedidoDTO pedidoDTO) {

        Pedido pedido = new Pedido();
        pedido.setData(LocalDate.now());
        pedido.setStatus(Status.CRIADO);
        copyDTOtoPedido(pedidoDTO, pedido);
        pedido.calcularValorTotalDoPedido();
        pedido = pedidoRepository.save(pedido);

        return (new PedidoDTO(pedido));


    }

    @Transactional
    public PedidoDTO updatePedido (PedidoDTO pedidoDTO, Long id) {

        try {

            Pedido pedido = pedidoRepository.getReferenceById(id);
            pedido.getItens().clear();
            pedido.setData(LocalDate.now());
            pedido.setStatus(Status.CRIADO);
            copyDTOtoPedido(pedidoDTO, pedido);
            pedido.calcularValorTotalDoPedido();
            pedido = pedidoRepository.save(pedido);

            return (new PedidoDTO(pedido));

        } catch (EntityNotFoundException e) {

            throw new ResourceNotFoundException("Recurso não encontrado! ID: " + id);

        }

    }

    @Transactional
    public void deletePedidoById (Long id) {

        if (!pedidoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Requisito não encontrado! ID: " + id);

        }

        pedidoRepository.deleteById(id);

    }

    public void copyDTOtoPedido (PedidoDTO pedidoDTO, Pedido pedido) {

        pedido.setNome(pedidoDTO.getNome());
        pedido.setCpf(pedidoDTO.getCpf());

        for (ItemDoPedidoDTO itemDoPedidoDTO : pedidoDTO.getItens()) {

            ItemDoPedido itemDoPedido = new ItemDoPedido();

            itemDoPedido.setQuantidade(itemDoPedidoDTO.getQuantidade());
            itemDoPedido.setDescricao(itemDoPedidoDTO.getDescricao());
            itemDoPedido.setPrecoUnitario(itemDoPedidoDTO.getPrecoUnitario());
            itemDoPedido.setPedido(pedido);
            pedido.getItens().add(itemDoPedido);

        }

    }

}
