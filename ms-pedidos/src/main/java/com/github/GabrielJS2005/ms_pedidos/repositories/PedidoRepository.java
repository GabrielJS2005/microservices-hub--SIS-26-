package com.github.GabrielJS2005.ms_pedidos.repositories;

import com.github.GabrielJS2005.ms_pedidos.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
