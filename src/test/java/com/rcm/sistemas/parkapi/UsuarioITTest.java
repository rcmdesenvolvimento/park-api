/*
    Data : 22/11/2023
 */
package com.rcm.sistemas.parkapi;

import com.rcm.sistemas.parkapi.web.dto.UsuarioCreateDto;
import com.rcm.sistemas.parkapi.web.dto.UsuarioResponseDto;
import com.rcm.sistemas.parkapi.web.exception.ErrorMessage;
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

    @Test
    public void createUsuario_ComUserNameInvalido_RetornarErrorMessageStatus422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("", "vascao"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("rcmtt@", "vascao"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("rcmsoftware@gmail", "vascao"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_ComPasswordInvalido_RetornarErrorMessageStatus422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("ricardo@gmail.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("ricardo@gmail.com", "vas"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("rcmsoftware@gmail.com", "vascaoxxxx"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_ComUserNAmenRepetido_RetornaErrorMessageComStatus409() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void buscarUsuario_ComIdExistente_RetornaUsuarioCriadoComStatus200() {
        UsuarioResponseDto usuarioResponseDto = webTestClient
                .get()
                .uri("/api/v1/usuarios/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(usuarioResponseDto).isNotNull();
        Assertions.assertThat(usuarioResponseDto.getId()).isEqualTo(100);
        Assertions.assertThat(usuarioResponseDto.getUserName()).isEqualTo("ana@gmail.com");
        Assertions.assertThat(usuarioResponseDto.getRole()).isEqualTo("ADMIN");
    }

    @Test
    public void buscarUsuario_ComIdInexistente_RetornaErrorMessageComStatus404() {
        ErrorMessage usuarioResponseDto = webTestClient
                .get()
                .uri("/api/v1/usuarios/1000")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(usuarioResponseDto).isNotNull();
        Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(404);
    }
}
