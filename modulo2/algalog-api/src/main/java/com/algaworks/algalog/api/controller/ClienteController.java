package com.algaworks.algalog.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.algaworks.algalog.domain.dto.ClienteEmailDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.algaworks.algalog.domain.model.Cliente;
import com.algaworks.algalog.domain.repository.ClienteRepository;
import com.algaworks.algalog.domain.service.CatalogoClienteService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private ClienteRepository clienteRepository;
    private CatalogoClienteService catalogoClienteService;

    @GetMapping
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long clienteId) {
        return clienteRepository.findById(clienteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente adicionar(@Valid @RequestBody Cliente cliente) {
        return catalogoClienteService.salvar(cliente);
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long clienteId,
                                             @Valid @RequestBody Cliente cliente) {
        if (!clienteRepository.existsById(clienteId)) {
            return ResponseEntity.notFound().build();
        }

        cliente.setId(clienteId);
        cliente = catalogoClienteService.salvar(cliente);

        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> remover(@PathVariable Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            return ResponseEntity.notFound().build();
        }

        catalogoClienteService.excluir(clienteId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> buscarPorEmail(@PathVariable String email) {
        Optional<Cliente> clienteOptional = clienteRepository.findByEmail(email);

        if (clienteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clienteOptional.get());
    }

    @PatchMapping("/email")
    public ResponseEntity<Cliente> atualizarEmail(@Valid @RequestBody ClienteEmailDto clienteEmailDto) {
        return clienteRepository.findById(clienteEmailDto.getId())
                .map(cliente -> {
                            cliente.setEmail(clienteEmailDto.getEmail());
                            return ResponseEntity.ok(
                                    clienteRepository.save(cliente));
                        }
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Cliente>> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(clienteRepository.findByNome(nome));
    }

    @GetMapping("/nome/contains/{str}")
    public ResponseEntity<List<Cliente>> buscarPorStringContidaEmNome(@PathVariable String str) {
        return ResponseEntity.ok(clienteRepository.findByNomeContaining(str));
    }

}
