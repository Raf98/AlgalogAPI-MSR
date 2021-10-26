package com.algaworks.algalog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class ClienteEmailDto {
    @NotNull(message = "Por favor, digite o id")
    private Long id;

    @NotBlank(message = "Por favor, digite o email")
    @Email
    private String email;
}
