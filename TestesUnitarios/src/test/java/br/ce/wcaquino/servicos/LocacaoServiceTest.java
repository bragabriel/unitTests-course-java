package br.ce.wcaquino.servicos;

import br.ce.wcaquino.builder.LocacaoBuilder;
import br.ce.wcaquino.builder.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {

    @InjectMocks
    private LocacaoService service;
    @Mock
    private SPCService spc;
    @Mock
    private LocacaoDAO dao;
    @Mock
    private EmailService email;

    //private static int contadorQtdTestesExecutados = 0;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before //Antes de cada teste
    public void setupBefore(){

        MockitoAnnotations.initMocks(this);

        service = new LocacaoService();

//        System.out.println("Before");
//        contadorQtdTestesExecutados++;
//        System.out.println(contadorQtdTestesExecutados);
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
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(locacao.getDataLocacao(), ehHoje());
        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
    }

    /* Exemplo de testes para: Filme Sem Estoque */
    //Dizendo que estamos esperando uma exceção neste teste
    //Forma Elegante: funciona bem quando apenas a exceção importa (quando a mensagem não importa)
    @Test(expected = FilmeSemEstoqueException.class)
    public void deveLancarExcecao_quandoAlugarFilmeSemEstoque() throws Exception {
        //Forma Elegante

        //cenario
        Usuario usuario = new Usuario("Gabriel");
        List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

        //acao
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveLancarExcecao_quandoAlugarFilmeSemEstoque2() {
        //Forma Robusta

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

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
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

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
        List<Filme> filmes = Arrays.asList(umFilme().agora());

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
        //Usuario usuario = new Usuario("Gabriel");
        Usuario usuario = umUsuario().agora();

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
        List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        //verificacao
//        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
//        Assert.assertTrue(ehSegunda);
//        assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
          assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
          assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda());

    }

//    public static void main(String[] args) {
//        new BuilderMaster().gerarCodigoClasse(Locacao.class);
//    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //Quando spc.PossuiNegativacao(usuario) for chamado, retorne true
		Mockito.when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

        //acao
        try {
            service.alugarFilme(usuario, filmes);
        //verificacao
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuário Negativado"));
        }

        Mockito.verify(spc).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacaoesAtrasadas(){
        //cenario
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
        Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();
        List<Locacao> locacoes =
                Arrays.asList(
                        LocacaoBuilder
                                .umLocacao().comUsuario(usuario).atrasado().agora(),
                        LocacaoBuilder
                                .umLocacao().comUsuario(usuario2).agora(),
                        LocacaoBuilder
                                .umLocacao().comUsuario(usuario3).atrasado().agora());

        Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

        //acao
        service.notificarAtrasos();

        //verificacao
        Mockito.verify(email).notificarAtraso(usuario);
        Mockito.verify(email).notificarAtraso(usuario3);
    }

    @Test
    public void deveTratarErroNoSPC() throws Exception {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        Mockito.when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrófica"));

        //verificacao
        exception.expect(LocadoraException.class);
        exception.expectMessage("Problemas com SPC, tente novamente");

        //acao
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveProrrogarUmaLocacao(){
        //cenario
        Locacao locacao = LocacaoBuilder.umLocacao().agora();

        //acao
        service.prorrogarLocacao(locacao, 3);

        //verificação
        ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
        Mockito.verify(dao).salvar(argCapt.capture());
        Locacao locacaoRetornada = argCapt.getValue();

        Assert.assertThat(locacaoRetornada.getValor(), is(12.0));
        Assert.assertThat(locacaoRetornada.getDataLocacao(), is(ehHoje()));
        Assert.assertThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
    }
}
