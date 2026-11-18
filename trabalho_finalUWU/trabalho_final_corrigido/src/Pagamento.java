import java.io.Serializable;
import java.util.Date;

public class Pagamento implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int contadorId = 1;
    private int id;
    private double valor;
    private Date dataPagamento;
    private String status;

    public Pagamento(double valor) {
        this.id = contadorId++;
        this.valor = valor;
        this.dataPagamento = new Date();
        this.status = "Pago"; // Status inicial
    }

    // Métodos listados no diagrama de classes para Pagamento
    public void realizarInscricao() { /* Lógica de processamento */ }
    public double calcularValor() { return this.valor; }
    public void cancelar() { this.status = "Cancelado"; }
    public void consultar() { System.out.println("Status financeiro: " + status); }

    public String getStatus() { return status; }

    public int getId() { return id; }

    public static void atualizarContador(int maiorIdEncontrado) {
        contadorId = maiorIdEncontrado + 1;
    }
}
