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

            insc1.realizarInscricao(); // já adiciona o inscrito no evento
            insc1.registrarPagamento(pag1);

            System.out.println("[OK] Inscrição realizada com sucesso para: " + p1.getNome());
            System.out.println("[OK] Status da Inscrição: " + insc1.getStatus());

        } catch (VagasEsgotadasException e) {
            System.out.println("[FALHA] Não deveria dar erro aqui.");
        }

        // Teste 3: Tratamento de Exceção (Lotação Máxima)
        System.out.println("\nTESTE 3: Disparo da Exceção VagasEsgotadasException");
        try {
            Inscricao insc2 = new Inscricao(p2, eventoTeste, catEstudante);
            // Ao tentar inscrever no evento que só tinha 1 vaga, a exceção deve estourar
            insc2.realizarInscricao();

            System.out.println("[FALHA] A exceção não foi disparada! João entrou no evento lotado.");
        } catch (VagasEsgotadasException e) {
            System.out.println("[OK] Exceção capturada com sucesso: " + e.getMessage());
        }

        // Teste 4: cancelamento devolve a vaga ao evento e à categoria
        System.out.println("\nTESTE 4: Cancelamento devolve a vaga");
        try {
            Evento ev4 = new Evento("Workshop", "Online", 1);
            CategoriaIngresso cat4 = new CategoriaIngresso("Geral", 80.0, 0.0, 1);
            Inscricao i4 = new Inscricao(p1, ev4, cat4);
            i4.realizarInscricao();
            boolean lotado = ev4.getInscritos().size() == 1 && !cat4.consultarDisponibilidade();

            boolean cancelou = i4.cancelar();
            boolean vagaVoltou = ev4.getInscritos().isEmpty() && cat4.consultarDisponibilidade();
            boolean segundoCancel = i4.cancelar(); // não pode devolver vaga 2x

            if (lotado && cancelou && vagaVoltou && !segundoCancel) {
                System.out.println("[OK] Vaga devolvida ao evento e à categoria; 2º cancelamento ignorado.");
            } else {
                System.out.println("[FALHA] lotado=" + lotado + " cancelou=" + cancelou
                        + " vagaVoltou=" + vagaVoltou + " segundoCancel=" + segundoCancel);
            }

            // Outro participante agora consegue se inscrever na vaga liberada
            new Inscricao(p2, ev4, cat4).realizarInscricao();
            System.out.println("[OK] Outro participante conseguiu ocupar a vaga liberada.");
        } catch (VagasEsgotadasException e) {
            System.out.println("[FALHA] A vaga não foi devolvida: " + e.getMessage());
        }

        System.out.println("\n=== FIM DOS TESTES ===");
    }
}
