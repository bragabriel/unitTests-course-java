package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //Executar os testes em ordem alfabetica
public class OrdemDependeTest {

    public static int contador = 0;

    @Test
    public void inicia(){
        contador = 1;
    }
    @Test
    public void verifica(){
        Assert.assertEquals(1, contador);
    }
}
