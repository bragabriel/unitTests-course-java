package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {

    private LocacaoService service;

    private static int contadorQtdTestesExecutados = 0;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before //Antes de cada teste
    public void setupBefore(){
        service = new LocacaoService();
        System.out.println("Before");
        contadorQtdTestesExecutados++;
        System.out.println(contadorQtdTestesExecutados);
    }
    @After //Depois de cada teste
    public void setupAfter(){
        System.out.println("After");
    }
    @BeforeClass //Antes da classe LocacaoService
    public static void setupBeforeClass(){
    //necessário ser static para o jUnit ter acesso à eles antes da classe ser criada
        System.out.println("BeforeClass");
    }
    @AfterClass //Depois da classe LocacaoService
    public static void setupAfterClass(){
    //necessário ser static para o jUnit ter acesso à eles antes da classe ser criada
        System.out.println("AfterClass");
    }

    @Test
    public void deveAlugarFilmeComSucesso() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        System.out.println("Teste!");

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), is(5.0));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
    }

    /* Exemplo de testes para: Filme Sem Estoque */
    //Dizendo que estamos esperando uma exceção neste teste
    //Forma Elegante: funciona bem quando apenas a exceção importa (quando a mensagem não importa)
    @Test(expected = FilmeSemEstoqueException.class)
    public void deveLancarExcecao_quandoAlugarFilmeSemEstoque() throws Exception {
        //Forma Elegante

        //cenario
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0));

        //acao
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveLancarExcecao_quandoAlugarFilmeSemEstoque2() {
        //Forma Robusta

        //cenario
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        //acao
        try {
            service.alugarFilme(usuario, filmes);

            //Se voltar uma exceção vai para o catch, se não, dá fail.
            Assert.fail("Deveria ter lançado uma exceção!");
        } catch (Exception e) {
            assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
        }
    }

    @Test
    public void deveLancarExcecao_quandoAlugarFilmeSemEstoque3() throws Exception {
        //Forma Nova

        //cenario
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        //acao
        service.alugarFilme(usuario, filmes);
    }

    /* Fim dos Exemplos de testes para: Filme Sem Estoque */


    @Test
    public void naoDeveAlugarFilme_quandoUsuarioVazio() throws FilmeSemEstoqueException {
    //Utilizaremos a forma: Robusta
        //cenário
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0));
        Usuario usuario = new Usuario("Gabriel");

        //acao
        try {
            service.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário vazio"));
        }
    }

    //Utilizaremos a forma: Nova
    @Test
    public void naoDeveAlugarFilme_quandoFilmeVazio() throws FilmeSemEstoqueException, LocadoraException {
        //cenário
        Usuario usuario = new Usuario("Gabriel");

        //Estou esperando a locadoraException & a mensagem 'Filme vazio'
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //acao
        service.alugarFilme(usuario, null);
    }

    @Test
    public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        //Desconto de 25%

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 3, 4.0),
                new Filme("Filme 3", 1, 4.0)
        );

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        //4+4+3 = 11 (25% de desconto no 3° filme)
        assertThat(resultado.getValor(), CoreMatchers.is(11.0));
    }

    @Test
    public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        //Desconto de 50%

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 3, 4.0),
                new Filme("Filme 3", 1, 4.0),
                new Filme("Filme 4", 2, 4.0)
        );

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        //4+4+3+2 = 13 (25% de desconto no 3° filme, 50% de desc no 4°)
        assertThat(resultado.getValor(), CoreMatchers.is(13.0));
    }

    @Test
    public void devePagar75PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        //Desconto de 75%

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 3, 4.0),
                new Filme("Filme 3", 1, 4.0),
                new Filme("Filme 4", 2, 4.0),
                new Filme("Filme 5", 2, 4.0)
        );

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        //4+4+3+2+1 = 14 (25% de desc no 3°, 50% de desc no 4°, 75% de desc no 5°)
        assertThat(resultado.getValor(), CoreMatchers.is(14.0));
    }
    @Test
    public void devePagar100PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        //Desconto de 100%

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 3, 4.0),
                new Filme("Filme 3", 1, 4.0),
                new Filme("Filme 4", 2, 4.0),
                new Filme("Filme 5", 2, 4.0),
                new Filme("Filme 6", 3, 4.0)
        );

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        //4+4+3+2+1+0 = 14 (25% de desc no 3°, 50% de desc no 4°, 75% de desc no 5°, 100% de desc no 6°)
        assertThat(resultado.getValor(), CoreMatchers.is(14.0));
    }

}
