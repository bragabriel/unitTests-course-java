package br.ce.wcaquino.builder;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder(){
    //método privado para que ninguem possa criar instancias do Builder fora dele
    }

    public static UsuarioBuilder umUsuario(){
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario();
        builder.usuario.setNome("Usuario 1");
        return builder;

        /*
         * Este método está:
         * Criando a instancia do Builder
         * Inicializando a construção do usuário
         * Povoando o usuário
        */
    }

    public Usuario agora(){
        return usuario;
    }

    public UsuarioBuilder comNome(String nome) {
        usuario.setNome(nome);
        return this;
    }
}
