package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

    @Test
    public void test(){

        //Verificar se a expressão é verdadeira
        Assert.assertTrue(true);
        Assert.assertFalse(false);

        Assert.assertEquals("Erro de comparação: ",1, 1);
        Assert.assertEquals(0.515, 0.512, 0.01);

        int i = 5;
        Integer i2 = 5;
        Assert.assertEquals(Integer.valueOf(i), i2);
        Assert.assertEquals(i, i2.intValue());

        //Comparando Strings
        Assert.assertEquals("bola", "bola");
        Assert.assertNotEquals("bola", "casa");
        Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
        Assert.assertTrue("bola".startsWith("bo"));

        //Comparando dois objetos
        Usuario u1 = new Usuario("Usuario 1");
        Usuario u2 = new Usuario("Usuario 1");
        Assert.assertEquals(u1, u2);
        Usuario u3 = u2;
        Usuario u4 = null;

        //Comparando a nível de instância (que os dois objetos são da mesma instância)
        //Assert.assertSame(u1, u2); -> erro
        Assert.assertSame(u3, u2);
        Assert.assertNotSame(u1, u2);

        //Verificando se o objeto esta null
        Assert.assertTrue(u4 == null);
        Assert.assertNull(u4);
        Assert.assertNotNull(u2);
    }
}
