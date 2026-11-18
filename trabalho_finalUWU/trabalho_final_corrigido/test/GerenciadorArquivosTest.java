import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class GerenciadorArquivosTest {

    private File arquivoTemporario() throws Exception {
        File f = File.createTempFile("teste_eventos_", ".dat");
        f.deleteOnExit();
        return f;
    }

    @Test
    void salvarELerPreservaOsObjetos() throws Exception {
        File arq = arquivoTemporario();
        ArrayList<CategoriaIngresso> original = new ArrayList<>();
        original.add(new CategoriaIngresso("VIP", 200.0, 10.0, 5));
        original.add(new CategoriaIngresso("Pista", 100.0, 0.0, 50));

        GerenciadorArquivos.salvarDados(original, arq.getPath());
        ArrayList<CategoriaIngresso> lida = GerenciadorArquivos.carregarDados(arq.getPath());

        assertEquals(2, lida.size());
        assertEquals("VIP", lida.get(0).getNome());
        assertEquals(180.0, lida.get(0).calcularValor(), 0.001);
        assertEquals("Pista", lida.get(1).getNome());
        assertEquals(50, lida.get(1).getQntdDisponivel());
    }

    @Test
    void lerArquivoInexistenteDevolveListaVazia() {
        String caminho = "arquivo_que_nao_existe_" + System.nanoTime() + ".dat";
        ArrayList<Evento> lida = GerenciadorArquivos.carregarDados(caminho);
        assertNotNull(lida);
        assertTrue(lida.isEmpty());
    }

    @Test
    void eventoComInscritosSobreviveAoSalvarELer() throws Exception {
        File arq = arquivoTemporario();
        Evento ev = new Evento("Show", "Presencial", 3);
        ev.adicionarInscrito(new Participante("Ana", "9999", "111", 20));
        ArrayList<Evento> lista = new ArrayList<>();
        lista.add(ev);

        GerenciadorArquivos.salvarDados(lista, arq.getPath());
        ArrayList<Evento> lida = GerenciadorArquivos.carregarDados(arq.getPath());

        assertEquals(1, lida.size());
        assertEquals(1, lida.get(0).getInscritos().size());
        assertEquals(2, lida.get(0).getVagasDisponiveis());
        assertEquals("Ana", lida.get(0).getInscritos().get(0).getNome());
    }

    @Test
    void inscricaoSalvaPreservaStatusEValor() throws Exception {
        File arq = arquivoTemporario();
        Evento ev = new Evento("Show", "Presencial", 3);
        CategoriaIngresso cat = new CategoriaIngresso("VIP", 100.0, 50.0, 3);
        Participante p = new Participante("Ana", "9999", "111", 20);
        Inscricao insc = new Inscricao(p, ev, cat);
        insc.realizarInscricao();
        insc.registrarPagamento(new Pagamento(insc.calcularValor()));
        ArrayList<Inscricao> lista = new ArrayList<>();
        lista.add(insc);

        GerenciadorArquivos.salvarDados(lista, arq.getPath());
        ArrayList<Inscricao> lida = GerenciadorArquivos.carregarDados(arq.getPath());

        assertEquals("Pago", lida.get(0).getStatus());
        assertEquals(50.0, lida.get(0).calcularValor(), 0.001);
        assertEquals("Ana", lida.get(0).getParticipante().getNome());
    }
}
