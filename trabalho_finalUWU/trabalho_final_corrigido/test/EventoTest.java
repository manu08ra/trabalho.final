import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class EventoTest {

    private Evento evento;
    private Participante ana;
    private Participante joao;

    @BeforeEach
    void setUp() {
        evento = new Evento("Simpósio", "Presencial", 2);
        ana = new Participante("Ana", "9999-1111", "111.111.111-11", 20);
        joao = new Participante("João", "8888-2222", "222.222.222-22", 22);
    }

    // Simula o que acontece ao gravar e ler um objeto de arquivo: vira uma CÓPIA.
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
    void eventoNovoTemTodasAsVagasLivres() {
        assertEquals(2, evento.getVagasDisponiveis());
        assertTrue(evento.getInscritos().isEmpty());
    }

    @Test
    void adicionarInscritoOcupaUmaVaga() throws VagasEsgotadasException {
        evento.adicionarInscrito(ana);
        assertEquals(1, evento.getInscritos().size());
        assertEquals(1, evento.getVagasDisponiveis());
    }

    @Test
    void eventoLotadoLancaVagasEsgotadasException() throws VagasEsgotadasException {
        evento.adicionarInscrito(ana);
        evento.adicionarInscrito(joao);

        Participante bia = new Participante("Bia", "7777-3333", "333.333.333-33", 25);
        assertThrows(VagasEsgotadasException.class, () -> evento.adicionarInscrito(bia));
        assertEquals(2, evento.getInscritos().size());
    }

    @Test
    void removerInscritoLiberaAVaga() throws VagasEsgotadasException {
        evento.adicionarInscrito(ana);
        assertTrue(evento.removerInscrito(ana));
        assertEquals(2, evento.getVagasDisponiveis());
    }

    @Test
    void removerInscritoQueNaoEstaNoEventoRetornaFalse() {
        assertFalse(evento.removerInscrito(ana));
    }

    @Test
    void removerInscritoFuncionaMesmoComCopiaDoParticipante() throws Exception {
        evento.adicionarInscrito(ana);
        // depois de ler de arquivo, o participante da inscrição é outro objeto (cópia)
        Participante copiaDaAna = copiarComoSeFosseLidoDeArquivo(ana);
        assertNotSame(ana, copiaDaAna);

        assertTrue(evento.removerInscrito(copiaDaAna));
        assertTrue(evento.getInscritos().isEmpty());
    }

    @Test
    void reconectarInscritosTrocaCopiasPelosParticipantesOficiais() throws Exception {
        evento.adicionarInscrito(ana);
        Evento eventoLido = copiarComoSeFosseLidoDeArquivo(evento);

        ArrayList<Participante> lista = new ArrayList<>();
        lista.add(ana);
        eventoLido.reconectarInscritos(lista);

        assertSame(ana, eventoLido.getInscritos().get(0));
    }

    @Test
    void dataEhFormatadaComoDiaMesAno() throws Exception {
        evento.setData(new SimpleDateFormat("dd/MM/yyyy").parse("25/12/2026"));
        assertEquals("25/12/2026", evento.getDataFormatada());
    }

    @Test
    void eventoSemDataInformaQueNaoFoiInformada() {
        assertEquals("não informada", evento.getDataFormatada());
    }

    @Test
    void setLocalEModalidadeAlteramOsDados() {
        evento.setLocal("Arena Central");
        evento.setModalidade("Online");
        assertEquals("Arena Central", evento.getLocal());
        assertEquals("Online", evento.getModalidade());
    }

    @Test
    void idsSaoGeradosDeFormaSequencial() {
        Evento outro = new Evento("Outro", "Online", 5);
        assertEquals(evento.getId() + 1, outro.getId());
    }
}
