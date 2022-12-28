package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testeLocacao() throws Exception {

        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Gabriel");
        Filme filme = new Filme("Filme 1", 2, 5.0);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filme);

        //verificacao
        error.checkThat(locacao.getValor(), is(5.0));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
    }

    /* Exemplo de testes para: Filme Sem Estoque */

    //Dizendo que estamos esperando uma exceção neste teste
    //Forma Elegante: funciona bem quando apenas a exceção importa (quando a mensagem não importa)
    @Test(expected = FilmeSemEstoqueException.class)
    public void testeLocacao_filmeSemEstoque() throws Exception {

        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Gabriel");
        Filme filme = new Filme("Filme 1", 0, 5.0);

        //acao
        service.alugarFilme(usuario, filme);
    }

    @Test
    public void testeLocacao_filmeSemEstoque2() {

        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Gabriel");
        Filme filme = new Filme("Filme 1", 0, 5.0);

        //acao
        try {
            service.alugarFilme(usuario, filme);

            //Se voltar uma exceção vai para o catch, se não, dá fail.
            Assert.fail("Deveria ter lançado uma exceção!");
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
        }
    }

    @Test
    public void testeLocacao_filmeSemEstoque3() throws Exception {

        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Gabriel");
        Filme filme = new Filme("Filme 1", 0, 5.0);

        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        //acao
        service.alugarFilme(usuario, filme);
    }

    /* Fim dos Exemplos de testes para: Filme Sem Estoque */


    //Utilizaremos a forma: Robusta
    @Test
    public void testeLocacao_usuarioVazio() throws FilmeSemEstoqueException {
        //cenário
        LocacaoService service = new LocacaoService();
        Filme filme = new Filme("Filme Z", 1, 4.0);
        Usuario usuario = new Usuario("Gabriel");

        //acao
        try {
            service.alugarFilme(null, filme);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuário vazio"));
        }
    }

    //Utilizaremos a forma: Nova
    @Test
    public void testeLocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException {
        //cenário
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Gabriel");

        //Estou esperando a locadoraException & a mensagem 'Filme vazio'
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //acao
        service.alugarFilme(usuario, null);

    }

}
