public class TesteFuncional {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO CASOS DE TESTE ===\n");

        // 1. Cenário Base (Setup)
        // Capacidade máxima é apenas 1 para forçarmos o erro no Teste 3
        Evento eventoTeste = new Evento("Simpósio de TI", "Presencial", 1);
        Participante p1 = new Participante("Ana", "9999-1111", "000.000.000-01", 20);
        Participante p2 = new Participante("João", "8888-2222", "111.111.111-02", 22);

        // Categoria que custa R$ 100 com 50% de desconto
        CategoriaIngresso catEstudante = new CategoriaIngresso("Estudante", 100.0, 50.0, 50);

        // Teste 1: Regra de cálculo de desconto da Categoria
        System.out.println("TESTE 1: Cálculo de valor com desconto");
        double valorEsperado = 50.0;
        double valorCalculado = catEstudante.calcularValor();
        if (valorCalculado == valorEsperado) {
            System.out.println("[OK] Desconto aplicado corretamente: R$ " + valorCalculado);
        } else {
            System.out.println("[FALHA] Valor incorreto: " + valorCalculado);
        }

        // Teste 2: Fluxo completo de Inscrição e Pagamento
        System.out.println("\nTESTE 2: Inscrição e registro de pagamento");
        try {
            Inscricao insc1 = new Inscricao(p1, eventoTeste, catEstudante);
            Pagamento pag1 = new Pagamento(insc1.calcularValor());

            insc1.registrarPagamento(pag1);
            insc1.realizarInscricao();
            eventoTeste.adicionarInscrito(p1);

            System.out.println("[OK] Inscrição realizada com sucesso para: " + p1.getNome());
            System.out.println("[OK] Status da Inscrição: " + insc1.getStatus());

        } catch (VagasEsgotadasException e) {
            System.out.println("[FALHA] Não deveria dar erro aqui.");
        }

        // Teste 3: Tratamento de Exceção (Lotação Máxima)
        System.out.println("\nTESTE 3: Disparo da Exceção VagasEsgotadasException");
        try {
            Inscricao insc2 = new Inscricao(p2, eventoTeste, catEstudante);
            // Ao tentar adicionar no evento que só tinha 1 vaga, a exceção deve estourar
            eventoTeste.adicionarInscrito(p2);

            System.out.println("[FALHA] A exceção não foi disparada! João entrou no evento lotado.");
        } catch (VagasEsgotadasException e) {
            System.out.println("[OK] Exceção capturada com sucesso: " + e.getMessage());
        }

        System.out.println("\n=== FIM DOS TESTES ===");
    }
}
