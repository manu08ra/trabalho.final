import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoriaIngressoTest {

    @Test
    void calcularValorSemDescontoMantemOValorBruto() {
        CategoriaIngresso c = new CategoriaIngresso("Pista", 100.0, 0.0, 10);
        assertEquals(100.0, c.calcularValor(), 0.001);
    }

    @Test
    void calcularValorAplicaPercentualDeDesconto() {
        CategoriaIngresso c = new CategoriaIngresso("Estudante", 100.0, 50.0, 10);
        assertEquals(50.0, c.calcularValor(), 0.001);
    }

    @Test
    void descontoDeCemPorCentoDeixaIngressoGratuito() {
        CategoriaIngresso c = new CategoriaIngresso("Cortesia", 80.0, 100.0, 5);
        assertEquals(0.0, c.calcularValor(), 0.001);
    }

    @Test
    void decrementarVagaAteZerarEsgotaACategoria() {
        CategoriaIngresso c = new CategoriaIngresso("VIP", 200.0, 0.0, 2);
        assertTrue(c.consultarDisponibilidade());

        c.decrementarVaga();
        assertTrue(c.consultarDisponibilidade());

        c.decrementarVaga();
        assertFalse(c.consultarDisponibilidade());
    }

    @Test
    void decrementarComCategoriaEsgotadaNaoDeixaQuantidadeNegativa() {
        CategoriaIngresso c = new CategoriaIngresso("VIP", 200.0, 0.0, 1);
        c.decrementarVaga();
        c.decrementarVaga();
        assertEquals(0, c.getQntdDisponivel());
    }

    @Test
    void incrementarVagaDevolveIngresso() {
        CategoriaIngresso c = new CategoriaIngresso("VIP", 200.0, 0.0, 1);
        c.decrementarVaga();
        assertFalse(c.consultarDisponibilidade());

        c.incrementarVaga();
        assertTrue(c.consultarDisponibilidade());
        assertEquals(1, c.getQntdDisponivel());
    }

    @Test
    void idsSaoGeradosDeFormaSequencial() {
        CategoriaIngresso c1 = new CategoriaIngresso("A", 10.0, 0.0, 1);
        CategoriaIngresso c2 = new CategoriaIngresso("B", 10.0, 0.0, 1);
        assertEquals(c1.getId() + 1, c2.getId());
    }
}
