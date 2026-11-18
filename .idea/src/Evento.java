import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Evento implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Participante> inscritos;

    private static int contadorId = 1;
    private int id;
    private String nome;
    private String modalidade;
    private Date data; 
    private String local; 
    private int capacidadeMax;

    //MÉTODOS SETTERS (atualizar)

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setModalidade(String modalidade){
        this.modalidade = modalidade;
    }

    public void setData(Date data){
        this.data = data;
    }

    public void setLocal(String local){
        this.local = local;
    }

    public void setCapacidadeMax(int capacidadeMax){
        this.capacidadeMax = capacidadeMax;
    }

    ////MÉTODOS GETTERS
    
    public String getModalidade() {
        return modalidade;
    }

    public Date getData() {
        return data;
    }

    public String getLocal() {
        return local;
    }

    public int getCapacidadeMax() {
        return capacidadeMax;
    }

    public Evento(String nome, String modalidade, int capacidadeMax) {
        this.id = contadorId++;
        this.nome = nome;
        this.modalidade = modalidade;
        this.capacidadeMax = capacidadeMax;
        this.inscritos = new ArrayList<>();
    }

    public void adicionarInscrito(Participante p) throws VagasEsgotadasException {
        if (inscritos.size() >= capacidadeMax) {
            throw new VagasEsgotadasException("Capacidade máxima do evento '" + nome + "' atingida!");
        }
        inscritos.add(p);
    }

    public int getId() { return id; }
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return "Evento ID: " + id + " | Nome: " + nome + " | Vagas: " + (capacidadeMax - inscritos.size());
    }

    public ArrayList<Participante> getInscritos() {
        return this.inscritos;
    }

    public static void atualizarContador(int maiorIdEncontrado) {
        contadorId = maiorIdEncontrado + 1;
    }
}