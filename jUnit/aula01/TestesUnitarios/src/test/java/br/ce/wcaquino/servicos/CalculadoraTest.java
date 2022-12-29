package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CalculadoraTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Calculadora calc;

    @Before
    public void setup(){
        calc = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores(){
        //cenario
        int a = 5;
        int b = 3;
        //Calculadora calc = new Calculadora(); -> Não preciso mais, já está no Before de cada test.

        //acao
        int resultado = calc.somar(a, b);

        //verificacao
        Assert.assertEquals(8, resultado);
    }

    @Test
    public void deveSubtrairDoisValores(){
        //cenario
        int a = 8;
        int b = 5;

        //acao
        int resultado = calc.subtrair(a, b);

        //verificacao
        Assert.assertEquals(3, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
        //cenario
        int a = 10;
        int b = 2;

        //acao
        int resultado = calc.dividir(a, b);

        //verificacao
        Assert.assertEquals(5, resultado);
    }

    @Test
    public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
        //cenario
        int a = 10;
        int b = 0;

        //verificacao
        //Estou esperando a NaoPodeDividirPorZeroException & a mensagem 'Não pode dividir por zero!'
        exception.expect(NaoPodeDividirPorZeroException.class);
        exception.expectMessage("Não pode dividir por zero!");

        //acao
        int resultado = calc.dividir(a, b);
    }

    @Test
    public void deveMultiplicarDoisValores(){
        //cenario
        int a = 10;
        int b = 2;

        //acao
        int resultado = calc.multiplicar(a, b);

        //verificacao
        Assert.assertEquals(20, resultado);
    }
}
