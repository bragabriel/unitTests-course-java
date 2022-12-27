package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;

public class LocacaoServiceTest {

    @Test
    public void teste() {

        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario();
        Filme filme = new Filme("Filme 1", 2, 5.0);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filme);

        //verificacao
        Assert.assertThat(locacao.getValor(), is(5.0));
        Assert.assertThat(locacao.getValor(), CoreMatchers.equalTo(5.0));
        Assert.assertThat(locacao.getValor(), CoreMatchers.not(4.0));

        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));

        Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
        Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        //CoreMatchers.is  ou  apenas is()
    }
}
