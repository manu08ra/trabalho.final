import java.io.Serializable;

public class CategoriaIngresso implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private double valor;
    private double percentualDesconto;
    private int qntdDisponivel;

    public CategoriaIngresso(String nome, double valor, double percentualDesconto, int qntdDisponivel) {
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

    public String getNome() {
        return this.nome;
    }
}