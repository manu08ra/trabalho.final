import java.io.Serializable;

public class Organizador extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int contadorId = 1;
    private String email; // Atributo exclusivo do Organizador

    public Organizador(String nome, String telefone, String email) {
        super(nome, telefone);
        this.id = contadorId++;
        this.email = email;
    }

    @Override
    public String getTipo() {
        return "Organizador";
    }

    // Métodos conforme o diagrama de classes
    public void cadastrarEvento() {
        System.out.println("Lógica para cadastrar evento...");
    }

    public void alterarEvento() {
        System.out.println("Lógica para alterar evento...");
    }

    public void consultarOcupacao() {
        System.out.println("Lógica para consultar ocupação...");
    }

    public void listarInscritos() {
        System.out.println("Lógica para listar inscritos...");
    }

    public String getEmail() { return email; }
}
