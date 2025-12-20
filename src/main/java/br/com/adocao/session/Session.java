package br.com.adocao.session;


import br.com.adocao.model.User;


public class Session {
    private static  User usuarioLogado;

    public static  void setUsuarioLogado(User user){
        usuarioLogado = user;
    }

    public static User getUsuarioLogado(){
        return usuarioLogado;
    }

    public static boolean isLogado(){
        return usuarioLogado != null;
    }

    public static void logout(){
        usuarioLogado = null;
    }

}
