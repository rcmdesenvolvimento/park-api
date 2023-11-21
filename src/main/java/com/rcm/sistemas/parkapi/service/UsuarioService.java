package com.rcm.sistemas.parkapi.service;

import com.rcm.sistemas.parkapi.entity.Usuario;
import com.rcm.sistemas.parkapi.exception.EmptyNotFoundException;
import com.rcm.sistemas.parkapi.exception.UserNameUniqueViolationException;
import com.rcm.sistemas.parkapi.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        try {
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException ex) {
            throw new UserNameUniqueViolationException(String.format("Username '%s' já cadastrado", usuario.getUserName()));
        }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EmptyNotFoundException(String.format("Usuário com id = %s não encontrado", id)));
    }

    @Transactional
    public Usuario editarSenha(long id, String senhaAtual, String novaSenha, String confirmaSenha) {

        if (!novaSenha.equals(confirmaSenha)) {
            throw new EmptyNotFoundException(String.format("Nova senha %s, não confere com confirmação de senha %s", novaSenha, confirmaSenha));
        }
        Usuario user = buscarPorId(id);

        if (!user.getPassword().equals(senhaAtual)) {
            throw new EmptyNotFoundException(String.format("Sua senha %s não confere.", senhaAtual));
        }
        user.setPassword(novaSenha);
        usuarioRepository.save(user);
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

}
