import java.io.Serializable;
import java.util.Date;

public class Participante extends Pessoa {
    private static final long serialVersionUID = 1L;

    private static int contadorId = 1;
    private String cpf;
    private int idade;

    public Participante(String nome, String telefone, String cpf, int idade) {
        super(nome, telefone);
        this.id = contadorId++;
        this.cpf = cpf;
        this.idade = idade;
    }

    @Override
    public String getTipo() { return "Participante"; }

    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | CPF: " + cpf
                + " | Telefone: " + telefone + " | Idade: " + idade;
    }

    public String getCpf() {
        return this.cpf;
    }

    public int getIdade() {
        return this.idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public static void atualizarContador(int maiorIdEncontrado) {
        contadorId = maiorIdEncontrado + 1;
    }


}
