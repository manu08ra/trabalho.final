import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class InscricaoTest {

    private Evento evento;
    private CategoriaIngresso categoria;
    private Participante ana;
    private Participante joao;

    @BeforeEach
    void setUp() {
        evento = new Evento("Show", "Presencial", 1);
        categoria = new CategoriaIngresso("VIP", 100.0, 20.0, 1);
        ana = new Participante("Ana", "9999", "111", 20);
        joao = new Participante("João", "8888", "222", 22);
    }

    @SuppressWarnings("unchecked")
    private static <T> T copiarComoSeFosseLidoDeArquivo(T objeto) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(objeto);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            return (T) in.readObject();
        }
    }

    @Test
    void inscricaoNovaFicaPendenteComValorJaComDesconto() {
        Inscricao i = new Inscricao(ana, evento, categoria);
        assertEquals("Pendente", i.getStatus());
        assertEquals(80.0, i.calcularValor(), 0.001);
    }

    @Test
    void realizarInscricaoOcupaVagaNoEventoENaCategoria() throws VagasEsgotadasException {
        Inscricao i = new Inscricao(ana, evento, categoria);
        i.realizarInscricao();

        assertEquals(1, evento.getInscritos().size());
        assertFalse(categoria.consultarDisponibilidade());
    }

    @Test
    void registrarPagamentoMarcaInscricaoComoPaga() throws VagasEsgotadasException {
        Inscricao i = new Inscricao(ana, evento, categoria);
        i.realizarInscricao();
        i.registrarPagamento(new Pagamento(i.calcularValor()));

        assertEquals("Pago", i.getStatus());
    }

    @Test
    void cancelarDevolveVagaAoEventoEACategoria() throws VagasEsgotadasException {
        Inscricao i = new Inscricao(ana, evento, categoria);
        i.realizarInscricao();

        assertTrue(i.cancelar());

        assertEquals("Cancelada", i.getStatus());
        assertTrue(evento.getInscritos().isEmpty());
        assertEquals(1, evento.getVagasDisponiveis());
        assertTrue(categoria.consultarDisponibilidade());
    }

    @Test
    void cancelarInscricaoPagaTambemCancelaOPagamento() throws VagasEsgotadasException {
        Inscricao i = new Inscricao(ana, evento, categoria);
        i.realizarInscricao();
        Pagamento pag = new Pagamento(i.calcularValor());
        i.registrarPagamento(pag);

        i.cancelar();

        assertEquals("Cancelado", pag.getStatus());
    }

    @Test
    void cancelarDuasVezesNaoDevolveAVagaDuasVezes() throws VagasEsgotadasException {
        Inscricao i = new Inscricao(ana, evento, categoria);
        i.realizarInscricao();

        assertTrue(i.cancelar());
        assertFalse(i.cancelar());
        assertEquals(1, categoria.getQntdDisponivel()); // e não 2
    }

    @Test
    void vagaDevolvidaPodeSerOcupadaPorOutroParticipante() throws VagasEsgotadasException {
        Inscricao primeira = new Inscricao(ana, evento, categoria);
        primeira.realizarInscricao();
        primeira.cancelar();

        Inscricao segunda = new Inscricao(joao, evento, categoria);
        segunda.realizarInscricao();

        assertEquals(1, evento.getInscritos().size());
        assertEquals("João", evento.getInscritos().get(0).getNome());
    }

    @Test
    void eventoLotadoLancaExcecaoENaoConsomeIngressoDaCategoria() throws VagasEsgotadasException {
        new Inscricao(ana, evento, categoria).realizarInscricao();
        CategoriaIngresso outra = new CategoriaIngresso("Pista", 50.0, 0.0, 5);

        assertThrows(VagasEsgotadasException.class,
                () -> new Inscricao(joao, evento, outra).realizarInscricao());
        assertEquals(5, outra.getQntdDisponivel());
    }

    @Test
    void categoriaEsgotadaLancaExcecaoENaoAdicionaInscritoNoEvento() {
        Evento grande = new Evento("Festival", "Presencial", 10);
        CategoriaIngresso esgotada = new CategoriaIngresso("Camarote", 300.0, 0.0, 0);

        assertThrows(VagasEsgotadasException.class,
                () -> new Inscricao(ana, grande, esgotada).realizarInscricao());
        assertTrue(grande.getInscritos().isEmpty());
    }

    @Test
    void cancelarFuncionaDepoisDeLerDoArquivoQuandoReconectado() throws Exception {
        // Estado "salvo": evento, participante e categoria em listas; inscrição já feita
        Inscricao original = new Inscricao(ana, evento, categoria);
        original.realizarInscricao();

        // Cada lista é gravada em um arquivo próprio => ao ler, tudo vira cópia independente
        Evento eventoLido = copiarComoSeFosseLidoDeArquivo(evento);
        Participante anaLida = copiarComoSeFosseLidoDeArquivo(ana);
        CategoriaIngresso categoriaLida = copiarComoSeFosseLidoDeArquivo(categoria);
        Inscricao inscricaoLida = copiarComoSeFosseLidoDeArquivo(original);

        ArrayList<Evento> eventos = new ArrayList<>();
        eventos.add(eventoLido);
        ArrayList<Participante> participantes = new ArrayList<>();
        participantes.add(anaLida);
        ArrayList<CategoriaIngresso> categorias = new ArrayList<>();
        categorias.add(categoriaLida);

        inscricaoLida.reconectar(eventos, participantes, categorias);
        assertSame(eventoLido, inscricaoLida.getEvento());
        assertSame(categoriaLida, inscricaoLida.getCategoria());

        inscricaoLida.cancelar();

        // a vaga voltou nos objetos que o sistema realmente usa (os das listas)
        assertTrue(eventoLido.getInscritos().isEmpty());
        assertTrue(categoriaLida.consultarDisponibilidade());
    }
}
