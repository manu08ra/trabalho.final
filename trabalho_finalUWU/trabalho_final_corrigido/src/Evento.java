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

    // CORREÇÃO: remove pelo ID (e não por equals), pois após ler do arquivo
    // o objeto Participante é uma cópia diferente e remove(p) não encontraria nada.
    public boolean removerInscrito(Participante p) {
        for (java.util.Iterator<Participante> it = inscritos.iterator(); it.hasNext(); ) {
            if (it.next().getId() == p.getId()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }

    public int getVagasDisponiveis() {
        return capacidadeMax - inscritos.size();
    }

    // Data no formato dd/MM/aaaa (ou aviso se ainda não foi informada)
    public String getDataFormatada() {
        if (data == null) {
            return "não informada";
        }
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(data);
    }

    @Override
    public String toString() {
        return "Evento ID: " + id + " | Nome: " + nome + " | Data: " + getDataFormatada()
                + " | Vagas: " + getVagasDisponiveis();
    }

    // Resumo completo, usado na consulta de ocupação
    public String detalhar() {
        return "Evento ID: " + id
                + "\nNome: " + nome
                + "\nModalidade: " + modalidade
                + "\nLocal: " + local
                + "\nData: " + getDataFormatada()
                + "\nCapacidade máxima: " + capacidadeMax
                + "\nInscritos: " + inscritos.size()
                + "\nVagas livres: " + getVagasDisponiveis();
    }

    // Após ler eventos.dat, os inscritos são cópias dos participantes.
    // Troca cada cópia pelo participante "oficial" da lista (mesmo ID).
    public void reconectarInscritos(java.util.List<Participante> participantes) {
        for (int i = 0; i < inscritos.size(); i++) {
            for (Participante p : participantes) {
                if (p.getId() == inscritos.get(i).getId()) {
                    inscritos.set(i, p);
                    break;
                }
            }
        }
    }

    public ArrayList<Participante> getInscritos() {
        return this.inscritos;
    }

    public static void atualizarContador(int maiorIdEncontrado) {
        contadorId = maiorIdEncontrado + 1;
    }
}