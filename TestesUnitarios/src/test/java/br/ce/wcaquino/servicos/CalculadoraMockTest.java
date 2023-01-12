package br.ce.wcaquino.servicos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.sql.SQLOutput;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;

    @Spy
    private Calculadora calcSpy;

    //Não podemos utilizar SPY em interfaces
    //Podemos utilizar MOCK em interfaces

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        //inicializando os atributos calcMock e calcSpy automaticamente pelo mockito
    }
    @Test
    public void devoMostrarDiferencaEntreMockSpy(){

        Mockito.when(calcMock.somar(1, 2)).thenReturn(8);
        Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);

        //Ele não vai chamar executar a função somar de fato
        //Mockito.doReturn(5).when(calcSpy).somar(1, 2);

        //Quando o mock não sabe o que retornar, ele retorna 0
        System.out.println("Mock: " + calcMock.somar(1, 5));

        //Quando o spy não sabe o que retornar, ele faz a execução real do método
        System.out.println("Spy: " + calcSpy.somar(1, 5));

        //Nao vai fazer nada quando calcSpy.imprime();
        //Mockito.doNothing().when(calcSpy).imprime();

        System.out.println("\n");
        System.out.println("Mock: ");
        calcMock.imprime();
        System.out.println("Spy: ");
        calcSpy.imprime();
    }
    @Test
    public void teste(){
        Calculadora calc = Mockito.mock(Calculadora.class);

        ArgumentCaptor<Integer>argCapt = ArgumentCaptor.forClass(Integer.class);
        //Mockito.when(calc.somar(Mockito.eq(1), 2)).thenReturn(5);
        Mockito.when(calc.somar(argCapt.capture(), Mockito.anyInt())).thenReturn(5);


        //Mockito.when(calc.somar(Mockito.anyInt(), Mockito.anyInt())).thenReturn(5);
        //Agora neste caso, funcionaria com qualquer valor

        //Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
        //Neste caso, é quando fizermos a soma entre 1 e qualquer inteiro

        System.out.println(calc.somar(1, 2)); //Retorna 5
        System.out.println(calc.somar(1, 8)); //Retorna 0 -> comportamento default

        System.out.println(argCapt.getAllValues());
    }
}
