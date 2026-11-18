import java.io.Serializable;

public class CategoriaIngresso implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int contadorId = 1;
    private int id;
    private String nome;
    private double valor;
    private double percentualDesconto;
    private int qntdDisponivel;

    public CategoriaIngresso(String nome, double valor, double percentualDesconto, int qntdDisponivel) {
        this.id = contadorId++;
        this.nome = nome;
        this.valor = valor;
        this.percentualDesconto = percentualDesconto;
        this.qntdDisponivel = qntdDisponivel;
    }

    // Métodos conforme o diagrama de classes
    public double calcularValor() {
        return valor - (valor * (percentualDesconto / 100.0));
    }

    public boolean consultarDisponibilidade() {
        return qntdDisponivel > 0;
    }

    public void decrementarVaga() {
        if (qntdDisponivel > 0) qntdDisponivel--;
    }

    // CORREÇÃO: devolve a vaga da categoria (usado no cancelamento)
    public void incrementarVaga() {
        qntdDisponivel++;
    }

    public int getId() {
        return this.id;
    }

    public double getValor() {
        return this.valor;
    }

    public double getPercentualDesconto() {
        return this.percentualDesconto;
    }

    public int getQntdDisponivel() {
        return this.qntdDisponivel;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setPercentualDesconto(double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public void setQntdDisponivel(int qntdDisponivel) {
        this.qntdDisponivel = qntdDisponivel;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | Valor: R$ %.2f | Desconto: %.0f%% | Final: R$ %.2f | Disponíveis: %d",
                id, nome, valor, percentualDesconto, calcularValor(), qntdDisponivel);
    }

    public String getNome() {
        return this.nome;
    }

    public static void atualizarContador(int maiorIdEncontrado) {
        contadorId = maiorIdEncontrado + 1;
    }
}