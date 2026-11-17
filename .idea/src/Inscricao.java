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
    public void realizarInscricao() {
        this.status = "Confirmada";
        this.categoria.decrementarVaga();
    }

    public double calcularValor() {
        return this.valorFinal;
    }

    public void cancelar() {
        this.status = "Cancelada";
        if (this.pagamento != null) {
            this.pagamento.cancelar();
        }
    }

    public void consultar() {
        System.out.println("Inscrição ID: " + id + " | Status: " + status + " | Valor: R$ " + valorFinal);
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
