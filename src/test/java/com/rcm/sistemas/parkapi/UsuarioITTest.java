package com.rcm.sistemas.parkapi;

import com.rcm.sistemas.parkapi.web.dto.UsuarioCreateDto;
import com.rcm.sistemas.parkapi.web.dto.UsuarioResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioITTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createUsuario_ComUserNameEPasswordValidos_RetornaUsuarioCriadoComStatus201() {
        UsuarioResponseDto usuarioResponseDto = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("vasco@gmail.com", "vascao"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(usuarioResponseDto).isNotNull();
        Assertions.assertThat(usuarioResponseDto.getId()).isNotNull();
        Assertions.assertThat(usuarioResponseDto.getUserName()).isEqualTo("vasco@gmail.com");
        Assertions.assertThat(usuarioResponseDto.getRole()).isEqualTo("CLIENTE");
    }
}
