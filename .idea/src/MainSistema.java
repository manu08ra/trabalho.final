import javax.swing.JOptionPane;
import java.util.ArrayList;

public class MainSistema {
    // 1. Listas globais carregadas dos arquivos
    private static ArrayList<Evento> eventos = GerenciadorArquivos.carregarDados("eventos.dat");
    private static ArrayList<Participante> participantes = GerenciadorArquivos.carregarDados("participantes.dat");
    private static ArrayList<Inscricao> inscricoes = GerenciadorArquivos.carregarDados("inscricoes.dat");
    private static ArrayList<CategoriaIngresso> categorias = GerenciadorArquivos.carregarDados("categorias.dat");

    public static void main(String[] args) {
        inicializarContadores();

        boolean executando = true;
        String[] perfis = {"Organizador", "Participante", "Sair"};

        while (executando) {
            int perfil = JOptionPane.showOptionDialog(null, "Selecione o seu perfil de acesso:",
                    "Sistema de Gerenciamento de Eventos", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, perfis, perfis[0]);

            switch (perfil) {
                case 0:
                    menuOrganizador();
                    break;
                case 1:
                    menuParticipante();
                    break;
                default:
                    executando = false;
                    break;
            }
        }
    }

    // ==========================================
    // MENU DO ORGANIZADOR
    // ==========================================
    private static void menuOrganizador() {
        String[] opcoes = {
                "1. Cadastrar/Alterar Eventos",
                "2. Cadastrar/Alterar Participantes",
                "3. Cadastrar Categoria de Ingresso",
                "4. Consultar Ocupação dos Eventos",
                "5. Listar Participantes Inscritos",
                "6. Consultar Inscrições Globais",
                "Voltar"
        };

        boolean noMenu = true;
        while (noMenu) {
            int escolha = JOptionPane.showOptionDialog(null, "Menu do Organizador",
                    "Área do Organizador", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);

            switch (escolha) {
                case 0: // Cadastrar e alterar eventos
                    String acaoEv = JOptionPane.showInputDialog("Digite 'C' para Cadastrar ou 'A' para Alterar um evento:");
                    if (acaoEv != null && acaoEv.equalsIgnoreCase("C")) {
                        String nomeEv = JOptionPane.showInputDialog("Nome do Evento:");
                        String modEv = JOptionPane.showInputDialog("Modalidade:");
                        int capEv = Integer.parseInt(JOptionPane.showInputDialog("Capacidade Máxima:"));
                        Evento ev = new Evento(nomeEv, modEv, capEv);
                        eventos.add(ev);
                        salvar("eventos");
                        JOptionPane.showMessageDialog(null, "Evento cadastrado! ID: " + ev.getId());
                    } else if (acaoEv != null && acaoEv.equalsIgnoreCase("A")) {
                        int idEv = Integer.parseInt(JOptionPane.showInputDialog("ID do Evento para alterar:"));
                        Evento evAlt = buscarEvento(idEv);
                        if (evAlt != null) {
                            JOptionPane.showMessageDialog(null, "Para alterar, implemente os setters na classe Evento e altere os atributos desejados aqui.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Evento não encontrado.");
                        }
                    }
                    break;

                case 1: // Cadastrar e alterar participantes
                    String acaoP = JOptionPane.showInputDialog("Digite 'C' para Cadastrar ou 'A' para Alterar participante:");
                    if (acaoP != null && acaoP.equalsIgnoreCase("C")) {
                        String nomeP = JOptionPane.showInputDialog("Nome:");
                        String telP = JOptionPane.showInputDialog("Telefone:");
                        String cpfP = JOptionPane.showInputDialog("CPF:");
                        int idadeP = Integer.parseInt(JOptionPane.showInputDialog("Idade:"));
                        Participante p = new Participante(nomeP, telP, cpfP, idadeP);
                        participantes.add(p);
                        salvar("participantes");
                        JOptionPane.showMessageDialog(null, "Participante cadastrado! ID: " + p.getId());
                    } else if (acaoP != null && acaoP.equalsIgnoreCase("A")) {
                        int idP = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do Participante para buscar seus dados:"));
                        Participante pAlt = buscarParticipante(idP);
                        if (pAlt != null) {
                            String novoTel = JOptionPane.showInputDialog("Novo Telefone (Atual: " + pAlt.telefone + "):");
                            pAlt.setTelefone(novoTel);
                            salvar("participantes");
                            JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Participante não encontrado.");
                        }
                    }
                    break;

                case 2: // Cadastrar categorias de ingressos
                    String nomeCat = JOptionPane.showInputDialog("Nome da Categoria (Ex: VIP, Pista):");
                    double valor = Double.parseDouble(JOptionPane.showInputDialog("Valor Bruto (R$):"));
                    double desconto = Double.parseDouble(JOptionPane.showInputDialog("Percentual de Desconto (%):"));
                    int qtd = Integer.parseInt(JOptionPane.showInputDialog("Quantidade Disponível:"));
                    categorias.add(new CategoriaIngresso(nomeCat, valor, desconto, qtd));
                    salvar("categorias");
                    JOptionPane.showMessageDialog(null, "Categoria cadastrada!");
                    break;

                case 3: // Consultar a ocupação dos eventos
                    StringBuilder sbOcup = new StringBuilder("EVENTOS DISPONÍVEIS:\n");
                    for (Evento e : eventos) {
                        sbOcup.append("ID: ").append(e.getId()).append(" | ").append(e.getNome()).append("\n");
                    }
                    int idEvOcup = Integer.parseInt(JOptionPane.showInputDialog(sbOcup.toString() + "\nDigite o ID do Evento:"));
                    Evento evOcup = buscarEvento(idEvOcup);
                    if (evOcup != null) {
                        JOptionPane.showMessageDialog(null, evOcup.toString());
                    } else {
                        JOptionPane.showMessageDialog(null, "Evento não encontrado.");
                    }
                    break;

                case 4: // Listar participantes inscritos
                    StringBuilder sbListEv = new StringBuilder("EVENTOS DISPONÍVEIS:\n");
                    for (Evento e : eventos) {
                        sbListEv.append("ID: ").append(e.getId()).append(" | ").append(e.getNome()).append("\n");
                    }
                    int idEvList = Integer.parseInt(JOptionPane.showInputDialog(sbListEv.toString() + "\nDigite o ID do Evento:"));
                    Evento evList = buscarEvento(idEvList);

                    if (evList != null) {
                        StringBuilder sb = new StringBuilder("Inscritos no evento " + evList.getNome() + ":\n");
                        for (Participante p : evList.getInscritos()) {
                            sb.append("- ").append(p.getNome()).append(" (ID: ").append(p.getId()).append(")\n");
                        }
                        JOptionPane.showMessageDialog(null, sb.toString());
                    } else {
                        JOptionPane.showMessageDialog(null, "Evento não encontrado.");
                    }
                    break;

                case 5: // Consultar inscrições globais
                    StringBuilder strInsc = new StringBuilder("Todas as Inscrições:\n");
                    for (Inscricao i : inscricoes) {
                        strInsc.append("Inscrição ID: ").append(i.getId())
                                .append(" | Participante ID: ").append(i.getParticipante().getId())
                                .append(" | Valor: R$ ").append(i.calcularValor())
                                .append(" | Status: ").append(i.getStatus()).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, strInsc.toString());
                    break;

                default: // Voltar
                    noMenu = false;
                    break;
            }
        }
    }

    // ==========================================
    // MENU DO PARTICIPANTE
    // ==========================================
    private static void menuParticipante() {
        String[] opcoes = {
                "1. Cadastrar/Alterar Meus Dados",
                "2. Consultar Categorias e Vagas",
                "3. Realizar Inscrição",
                "4. Consultar Valor da Inscrição",
                "5. Registrar Pagamento",
                "6. Consultar Inscrição",
                "7. Solicitar Cancelamento",
                "Voltar"
        };

        boolean noMenu = true;
        while (noMenu) {
            int escolha = JOptionPane.showOptionDialog(null, "Menu do Participante",
                    "Área do Participante", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);

            switch (escolha) {
                case 0: // Cadastrar e alterar dados
                    String acaoP = JOptionPane.showInputDialog("Digite 'C' para Cadastrar ou 'A' para Alterar seus dados:");
                    if (acaoP != null && acaoP.equalsIgnoreCase("C")) {
                        String nomeP = JOptionPane.showInputDialog("Seu Nome:");
                        String telP = JOptionPane.showInputDialog("Seu Telefone:");
                        String cpfP = JOptionPane.showInputDialog("Seu CPF:");
                        int idadeP = Integer.parseInt(JOptionPane.showInputDialog("Sua Idade:"));

                        Participante p = new Participante(nomeP, telP, cpfP, idadeP);
                        participantes.add(p);
                        salvar("participantes");
                        JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!\nGUARDE SEU ID: " + p.getId());

                    } else if (acaoP != null && acaoP.equalsIgnoreCase("A")) {
                        int idP = Integer.parseInt(JOptionPane.showInputDialog("Digite seu ID de Participante para buscar seus dados:"));
                        Participante pAlt = buscarParticipante(idP);

                        if (pAlt != null) {
                            String novoTel = JOptionPane.showInputDialog("Novo Telefone (Atual: " + pAlt.telefone + "):");
                            pAlt.setTelefone(novoTel);
                            salvar("participantes");
                            JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Participante não encontrado.");
                        }
                    }
                    break;

                case 1: // Consultar categorias e vagas
                    StringBuilder sbCat = new StringBuilder("Categorias Disponíveis:\n");
                    for (int i = 0; i < categorias.size(); i++) {
                        CategoriaIngresso c = categorias.get(i);
                        sbCat.append("Índice ").append(i).append(" - ").append(c.getNome())
                                .append(" | Valor C/ Desconto: R$").append(c.calcularValor())
                                .append(" | Vagas: ").append(c.consultarDisponibilidade() ? "Sim" : "Não").append("\n");
                    }
                    JOptionPane.showMessageDialog(null, sbCat.toString());
                    break;

                case 2: // Realizar Inscrição
                    int idP = Integer.parseInt(JOptionPane.showInputDialog("Seu ID de Participante:"));

                    StringBuilder sbEv = new StringBuilder("EVENTOS DISPONÍVEIS:\n");
                    for (Evento e : eventos) {
                        sbEv.append("ID: ").append(e.getId()).append(" | ").append(e.getNome()).append("\n");
                    }
                    int idEv = Integer.parseInt(JOptionPane.showInputDialog(sbEv.toString() + "\nDigite o ID do Evento escolhido:"));

                    StringBuilder sbCatInsc = new StringBuilder("CATEGORIAS DISPONÍVEIS:\n");
                    for (int i = 0; i < categorias.size(); i++) {
                        CategoriaIngresso c = categorias.get(i);
                        sbCatInsc.append("Índice ").append(i).append(" - ").append(c.getNome())
                                .append(" (R$ ").append(c.calcularValor()).append(")\n");
                    }
                    int indexCat = Integer.parseInt(JOptionPane.showInputDialog(sbCatInsc.toString() + "\nDigite o Índice da Categoria:"));

                    try {
                        Participante p = buscarParticipante(idP);
                        Evento ev = buscarEvento(idEv);
                        CategoriaIngresso cat = categorias.get(indexCat);

                        if (p != null && ev != null && cat != null) {
                            Inscricao nova = new Inscricao(p, ev, cat);
                            nova.realizarInscricao();
                            ev.adicionarInscrito(p);
                            inscricoes.add(nova);
                            salvar("tudo");
                            JOptionPane.showMessageDialog(null, "Pré-inscrição realizada!\nID da sua Inscrição: " + nova.getId() + "\nRealize o pagamento para confirmar.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Dados inválidos. Verifique os IDs informados.");
                        }
                    } catch (VagasEsgotadasException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (IndexOutOfBoundsException e) {
                        JOptionPane.showMessageDialog(null, "Índice de categoria inválido.");
                    }
                    break;

                case 3: // Consultar valor
                case 5: // Consultar Inscrição
                case 4: // Registrar pagamento
                case 6: // Solicitar cancelamento
                    int idBuscaP = Integer.parseInt(JOptionPane.showInputDialog("Digite seu ID de Participante para buscar suas inscrições:"));

                    StringBuilder sbInscricoes = new StringBuilder("SUAS INSCRIÇÕES:\n");
                    boolean temInscricao = false;

                    for (Inscricao i : inscricoes) {
                        if (i.getParticipante().getId() == idBuscaP) {
                            sbInscricoes.append("ID da Inscrição: ").append(i.getId())
                                    .append(" | Status: ").append(i.getStatus())
                                    .append(" | Valor: R$ ").append(i.calcularValor()).append("\n");
                            temInscricao = true;
                        }
                    }

                    if (!temInscricao) {
                        JOptionPane.showMessageDialog(null, "Nenhuma inscrição encontrada para o seu ID.");
                        break;
                    }

                    int idInscAcao = Integer.parseInt(JOptionPane.showInputDialog(sbInscricoes.toString() + "\nDigite o ID da Inscrição que deseja gerenciar:"));
                    Inscricao inscSelecionada = buscarInscricao(idInscAcao);

                    if (inscSelecionada != null && inscSelecionada.getParticipante().getId() == idBuscaP) {
                        if (escolha == 3 || escolha == 5) {
                            JOptionPane.showMessageDialog(null, "Inscrição ID: " + inscSelecionada.getId() + "\nValor: R$ " + inscSelecionada.calcularValor() + "\nStatus: " + inscSelecionada.getStatus());
                        }
                        else if (escolha == 4) { // Pagamento
                            if (inscSelecionada.getStatus().equals("Pago")) {
                                JOptionPane.showMessageDialog(null, "Esta inscrição já está paga!");
                            } else {
                                Pagamento pag = new Pagamento(inscSelecionada.calcularValor());
                                inscSelecionada.registrarPagamento(pag);
                                salvar("inscricoes");
                                JOptionPane.showMessageDialog(null, "Pagamento de R$ " + pag.calcularValor() + " registrado com sucesso!");
                            }
                        }
                        else if (escolha == 6) { // Cancelamento
                            inscSelecionada.cancelar();
                            salvar("inscricoes");
                            JOptionPane.showMessageDialog(null, "Inscrição cancelada com sucesso!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Inscrição inválida ou não pertence a você.");
                    }
                    break;

                default: // Voltar
                    noMenu = false;
                    break;
            }
        }
    }

    // ==========================================
    // MÉTODOS AUXILIARES DE BUSCA E SALVAMENTO
    // ==========================================
    private static Evento buscarEvento(int id) {
        for (Evento e : eventos) { if (e.getId() == id) return e; }
        return null;
    }

    private static Participante buscarParticipante(int id) {
        for (Participante p : participantes) { if (p.getId() == id) return p; }
        return null;
    }

    private static Inscricao buscarInscricao(int id) {
        for (Inscricao i : inscricoes) { if (i.getId() == id) return i; }
        return null;
    }

    private static void salvar(String tipo) {
        if (tipo.equals("eventos") || tipo.equals("tudo")) GerenciadorArquivos.salvarDados(eventos, "eventos.dat");
        if (tipo.equals("participantes") || tipo.equals("tudo")) GerenciadorArquivos.salvarDados(participantes, "participantes.dat");
        if (tipo.equals("inscricoes") || tipo.equals("tudo")) GerenciadorArquivos.salvarDados(inscricoes, "inscricoes.dat");
        if (tipo.equals("categorias") || tipo.equals("tudo")) GerenciadorArquivos.salvarDados(categorias, "categorias.dat");
    }



    private static void inicializarContadores() {
        int maxEv = 0;
        for (Evento e : eventos) { if (e.getId() > maxEv) maxEv = e.getId(); }
        Evento.atualizarContador(maxEv);

        int maxP = 0;
        for (Participante p : participantes) { if (p.getId() > maxP) maxP = p.getId(); }
        Participante.atualizarContador(maxP);

        int maxI = 0;
        for (Inscricao i : inscricoes) { if (i.getId() > maxI) maxI = i.getId(); }
        Inscricao.atualizarContador(maxI);
    }
}