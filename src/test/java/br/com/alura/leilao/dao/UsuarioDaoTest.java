package br.com.alura.leilao.dao;

import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import br.com.alura.leilao.util.builder.UsuarioBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioDaoTest {

    private EntityManager em;
    private UsuarioDao dao;

    @BeforeEach
    void setUp() {
        this.em = JPAUtil.getEntityManager();
        dao = new UsuarioDao(em);
        em.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        em.getTransaction().rollback();
    }

    @Test
    void deveriaEncontrarUsuarioCadastrado() {
        Usuario usuario = new UsuarioBuilder()
                .comNome("fulano")
                .comEmail("fulando@email.com")
                .comSenha("112345678")
                .criar();

        em.persist(usuario);

        Usuario encontrado = dao.buscarPorUsername(usuario.getNome());
        Assertions.assertNotNull(encontrado);
    }

    @Test
    void naoDeveriaEncontrarUsuarioNaoCadastrado() {
        Usuario usuario = new UsuarioBuilder()
                .comNome("fulano")
                .comEmail("fulando@email.com")
                .comSenha("112345678")
                .criar();

        em.persist(usuario);

        assertThrows(NoResultException.class,
                () -> dao.buscarPorUsername("beltrano"));
    }

    @Test
    void deveriaRemoverUmUsuario() {
        Usuario usuario = new UsuarioBuilder()
                .comNome("fulano")
                .comEmail("fulando@email.com")
                .comSenha("112345678")
                .criar();

        em.persist(usuario);

        dao.deletar(usuario);

        assertThrows(NoResultException.class,
                () -> dao.buscarPorUsername(usuario.getNome()));
    }

}