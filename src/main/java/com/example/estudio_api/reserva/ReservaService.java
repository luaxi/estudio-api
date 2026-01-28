package com.example.estudio_api.reserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.estudio_api.cliente.Cliente;
import com.example.estudio_api.cliente.ClienteService;
import com.example.estudio_api.reserva.dto.ReservaRequestDTO;
import com.example.estudio_api.sala.Sala;
import com.example.estudio_api.sala.SalaService;
import com.example.estudio_api.shared.errors.HorarioInvalidoException;
import com.example.estudio_api.shared.errors.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservaService {
    
    private final ReservaRepository repository;
    private final ClienteService clienteService;
    private final SalaService salaService;

    public Reserva criar(ReservaRequestDTO dto){
        // Busca e verifica se o cliente existe
        Cliente cliente = clienteService.buscarPorId(dto.clienteId());

        // Busca e verifica se a sala existe
        Sala sala = salaService.buscarPorId(dto.salaId());

        // Valida os horários da reserva
        validarHorarios(dto);

        // Verifica conflitos de horário
        verificaConflitos(dto);

        // Calcula a quantidade de horas reservadas
        var horasReservadas = dto.dataFim().getHour() - dto.dataInicio().getHour();
        
        // Calcula o valor total da reserva
        BigDecimal valorTotal = sala.getPrecoPorHora()
            .multiply(BigDecimal.valueOf(horasReservadas));

        // Cria a reserva
        Reserva reserva = Reserva.builder()
            .cliente(cliente)
            .sala(sala)
            .dataInicio(dto.dataInicio())
            .dataFim(dto.dataFim())
            .valorTotal(valorTotal)
            .build();

        return repository.save(reserva);
    }

    public Reserva buscarPorId(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Reserva não encontrada!"));
    }

    public Reserva atualizar(Long id, ReservaRequestDTO dto){

        // Busca e verifica se a reserva existe
        Reserva reserva = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Reserva não encontrada!"));

        // Busca e verifica se o cliente existe
        Cliente cliente = clienteService.buscarPorId(dto.clienteId());

        // Busca e verifica se a sala existe
        Sala sala = salaService.buscarPorId(dto.salaId());

        // Valida os horários da reserva
        validarHorarios(dto);

        // Verifica conflitos de horário
        verificaConflitos(dto);

        // Calcula a quantidade de horas reservadas
        var horasReservadas = dto.dataFim().getHour() - dto.dataInicio().getHour();
        
        // Calcula o valor total da reserva
        BigDecimal valorTotal = sala.getPrecoPorHora()
            .multiply(BigDecimal.valueOf(horasReservadas));

        // Atualiza a reserva
        reserva.setCliente(cliente);
        reserva.setSala(sala);
        reserva.setDataInicio(dto.dataInicio());
        reserva.setDataFim(dto.dataFim());
        reserva.setValorTotal(valorTotal);

        return repository.save(reserva);
    }

    public List<Reserva> listar(){
        return repository.findAll();
    }

    public List<Reserva> listarPorSala(Long salaId){
        
        // Verifica se a sala existe
        // if(!salaService.existeSala(salaId)){
        //     throw new NotFoundException("Sala não encontrada!");
        // }

        return repository.findBySalaId(salaId);
    }

    public List<Reserva> listarPorCliente(Long clienteId){
        return repository.findByClienteId(clienteId);
    }

    public List<Reserva> listarAtivasPorCliente(Long clienteId){
        return repository.findAtivasByClienteId(clienteId);
    }

    public List<Reserva> listarPassadasPorCliente(Long clienteId){
        return repository.findPassadasByClienteId(clienteId);
    }

    public void deletar(Long id){

        // Verifica se a reserva existe
        if(!repository.existsById(id)){
            throw new NotFoundException("Reserva não encontrada!");
        }

        repository.deleteById(id);
    }

    private void verificaConflitos(ReservaRequestDTO dto) {
        var conflitos = repository.findConflitos(
            dto.salaId(),
            dto.dataInicio(),
            dto.dataFim()
        );

        if(!conflitos.isEmpty()) {
            throw new HorarioInvalidoException("Já existe uma reserva para o período informado.");
        }
    }

    private void validarHorarios(ReservaRequestDTO dto) {
        
        // Verifica se são horários redondos
        if(!isHorarioRedondo(dto.dataInicio()) || !isHorarioRedondo(dto.dataFim())) {
            throw new HorarioInvalidoException("Os horários de início e fim devem ser horas redondas.");
        }

        // Verifica se dataFim é depois de dataInicio
        if(dto.dataFim().isBefore(dto.dataInicio()) || dto.dataFim().isEqual(dto.dataInicio())) {
            throw new HorarioInvalidoException("O horário de fim deve ser depois do horário de início.");
        }

        // Verifica se a reserva começa e termina no mesmo dia
        if(dto.dataInicio().toLocalDate().isBefore(dto.dataFim().toLocalDate()) ||
           dto.dataInicio().toLocalDate().isAfter(dto.dataFim().toLocalDate())) {
            throw new HorarioInvalidoException("A reserva deve começar e terminar no mesmo dia.");
        }

        // Verifica se os horários estão dentro do permitido (9h às 23h)
        if(dto.dataInicio().toLocalTime().isBefore(LocalTime.of(9, 0)) || 
           dto.dataFim().toLocalTime().isAfter(LocalTime.of(23, 0))) {
            throw new HorarioInvalidoException("Os horários de reserva devem estar entre 09:00 e 23:00.");
        }
    }

    private boolean isHorarioRedondo(LocalDateTime data) {
        return data.getMinute() == 0 && data.getSecond() == 0;
    }

}
