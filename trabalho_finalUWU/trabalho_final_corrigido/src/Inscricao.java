import java.io.Serializable;
import java.util.Date;

public class Inscricao implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int contadorId = 1;
    private int id;
    private Date dataInscricao;
    private double valorFinal;
    private String status; // Pendente, Pago, Cancelado

    // Relacionamentos do diagrama
    private Participante participante;
    private Evento evento;
    private CategoriaIngresso categoria;
    private Pagamento pagamento;

    public Inscricao(Participante participante, Evento evento, CategoriaIngresso categoria) {
        this.id = contadorId++;
        this.dataInscricao = new Date();
        this.participante = participante;
        this.evento = evento;
        this.categoria = categoria;
        this.valorFinal = categoria.calcularValor();
        this.status = "Pendente";
    }

    // Associação com o pagamento
    public void registrarPagamento(Pagamento pag) {
        this.pagamento = pag;
        this.status = pag.getStatus();
    }

    // Métodos conforme o diagrama de classes
    // CORREÇÃO: agora a inscrição só ocupa vaga se TUDO der certo (evento e categoria),
    // evitando "vaga perdida" quando o evento está lotado.
    // Confere evento E categoria antes de qualquer alteração.
    // É estático para poder ser chamado antes de criar a Inscricao (não "gasta" ID à toa).
    public static void verificarDisponibilidade(Evento evento, CategoriaIngresso categoria)
            throws VagasEsgotadasException {
        if (evento.getVagasDisponiveis() <= 0) {
            throw new VagasEsgotadasException(
                "Capacidade máxima do evento '" + evento.getNome() + "' atingida!");
        }
        if (!categoria.consultarDisponibilidade()) {
            throw new VagasEsgotadasException(
                "Não há mais ingressos da categoria '" + categoria.getNome() + "'!");
        }
    }

    public void realizarInscricao() throws VagasEsgotadasException {
        verificarDisponibilidade(evento, categoria);
        evento.adicionarInscrito(participante); // lança exceção se o evento estiver lotado
        categoria.decrementarVaga();
        this.status = "Pendente"; // vira "Pago" quando o pagamento é registrado
    }

    public double calcularValor() {
        return this.valorFinal;
    }

    // CORREÇÃO: devolve a vaga ao evento e ao ingresso. Retorna false se já estava cancelada
    // (evita devolver a mesma vaga duas vezes).
    public boolean cancelar() {
        if ("Cancelada".equals(this.status)) {
            return false;
        }
        this.status = "Cancelada";
        if (this.pagamento != null) {
            this.pagamento.cancelar();
        }
        this.evento.removerInscrito(this.participante);
        this.categoria.incrementarVaga();
        return true;
    }

    // CORREÇÃO: após ler os arquivos, cada lista vira objetos separados (cópias).
    // Aqui religamos a inscrição aos objetos "oficiais" das listas, pelo ID.
    public void reconectar(java.util.List<Evento> eventos,
                           java.util.List<Participante> participantes,
                           java.util.List<CategoriaIngresso> categorias) {
        for (Evento e : eventos) {
            if (e.getId() == this.evento.getId()) { this.evento = e; break; }
        }
        for (Participante p : participantes) {
            if (p.getId() == this.participante.getId()) { this.participante = p; break; }
        }
        for (CategoriaIngresso c : categorias) {
            if (c.getId() == this.categoria.getId()) { this.categoria = c; break; }
        }
    }

    public Evento getEvento() {
        return this.evento;
    }

    public CategoriaIngresso getCategoria() {
        return this.categoria;
    }

    // Texto da inscrição para exibir na interface gráfica
    public String resumo() {
        return "Inscrição ID: " + id
                + " | Participante: " + participante.getNome()
                + " | Evento: " + evento.getNome()
                + " | Categoria: " + categoria.getNome()
                + " | Valor: R$ " + String.format("%.2f", valorFinal)
                + " | Status: " + status;
    }

    public void consultar() {
        System.out.println(resumo());
    }

    public Pagamento getPagamento() {
        return this.pagamento;
    }

    public int getId() {
        return this.id;
    }

    public String getStatus() {
        return this.status;
    }

    public static void atualizarContador(int maiorIdEncontrado) {
        contadorId = maiorIdEncontrado + 1;
    }

    public Participante getParticipante() {
        return this.participante;
    }



}
