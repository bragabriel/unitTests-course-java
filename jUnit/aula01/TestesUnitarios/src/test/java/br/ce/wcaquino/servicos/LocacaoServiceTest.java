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
import java.util.Calendar;
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
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

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
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        //verificacao
        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);
    }

}
