import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoTest {

    @Test
    void pagamentoGuardaOValorInformado() {
        Pagamento p = new Pagamento(90.45);
        assertEquals(90.45, p.calcularValor(), 0.001);
    }

    @Test
    void pagamentoRegistradoNasceComoPago() {
        assertEquals("Pago", new Pagamento(10.0).getStatus());
    }

    @Test
    void cancelarMudaOStatusParaCancelado() {
        Pagamento p = new Pagamento(10.0);
        p.cancelar();
        assertEquals("Cancelado", p.getStatus());
    }

    @Test
    void idsSaoGeradosDeFormaSequencial() {
        Pagamento p1 = new Pagamento(10.0);
        Pagamento p2 = new Pagamento(20.0);
        assertEquals(p1.getId() + 1, p2.getId());
    }
}
