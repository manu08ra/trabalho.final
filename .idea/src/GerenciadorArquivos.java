import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivos {

    // Gravação de objetos em arquivo (Critério g)
    public static void salvarDados(List<?> lista, String nomeArquivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    // Leitura de objetos do arquivo (Critério g)
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> carregarDados(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        if (!arquivo.exists()) return new ArrayList<T>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (ArrayList<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar arquivo: " + e.getMessage());
            return new ArrayList<T>();
        }
    }
}
