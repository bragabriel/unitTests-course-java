package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.SQLOutput;

public class CalculadoraMockTest {

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
