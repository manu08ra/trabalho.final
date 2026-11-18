import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Testa a classe abstrata Pessoa através das subclasses (herança e polimorfismo)
class PessoaTest {

    @Test
    void participanteInformaSeuTipo() {
        Pessoa p = new Participante("Ana", "9999", "111.111.111-11", 20);
        assertEquals("Participante", p.getTipo());
    }

    @Test
    void organizadorInformaSeuTipo() {
        Pessoa o = new Organizador("Carlos", "8888", "carlos@empresa.com");
        assertEquals("Organizador", o.getTipo());
    }

    @Test
    void participanteGuardaOsDadosInformados() {
        Participante p = new Participante("Ana", "9999", "111.111.111-11", 20);
        assertEquals("Ana", p.getNome());
        assertEquals("9999", p.getTelefone());
        assertEquals("111.111.111-11", p.getCpf());
        assertEquals(20, p.getIdade());
    }

    @Test
    void settersAlteramNomeTelefoneEIdade() {
        Participante p = new Participante("Ana", "9999", "111", 20);
        p.setNome("Ana Paula");
        p.setTelefone("1234");
        p.setIdade(21);
        assertEquals("Ana Paula", p.getNome());
        assertEquals("1234", p.getTelefone());
        assertEquals(21, p.getIdade());
    }

    @Test
    void idsDeParticipantesSaoSequenciais() {
        Participante p1 = new Participante("A", "1", "1", 20);
        Participante p2 = new Participante("B", "2", "2", 21);
        assertEquals(p1.getId() + 1, p2.getId());
    }
}
