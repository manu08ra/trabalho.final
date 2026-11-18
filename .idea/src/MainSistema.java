
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class MainSistema {

    // Listas Globais
    private static ArrayList<Evento> eventos = GerenciadorArquivos.carregarDados("eventos.dat");
    private static ArrayList<Participante> participantes = GerenciadorArquivos.carregarDados("participantes.dat");
    private static ArrayList<Inscricao> inscricoes = GerenciadorArquivos.carregarDados("inscricoes.dat");
    private static ArrayList<CategoriaIngresso> categorias = GerenciadorArquivos.carregarDados("categorias.dat");

    // Menu Principal
    public static void main(String[] args) {

        inicializarContadores();
        boolean executando = true;

        String[] perfis = {
                "Organizador",
                "Participante",
                "Sair"
        };

        while (executando) {

            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Bem-vindo ao Sistema de Gerenciamento de Eventos!",
                    "Menu Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    perfis,
                    perfis[0]
            );

            switch (escolha) {

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

        salvar("tudo");

        JOptionPane.showMessageDialog(null, "Sistema encerrado!");
    }

    // Menu do Organizador
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
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Menu do Organizador",
                    "Área do Organizador",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            switch (escolha) {
                // Cadastrar/alterar eventos
                case 0: {
                    String acaoEv = JOptionPane.showInputDialog(
                        "Digite 'C' para Cadastrar ou 'A' para Alterar um evento:"
                    );
                    if (acaoEv != null && acaoEv.equalsIgnoreCase("C")) {

                        String nomeEv = JOptionPane.showInputDialog("Nome do Evento:");
                        String modEv = JOptionPane.showInputDialog("Modalidade:");
                        String localEv = JOptionPane.showInputDialog("Local:");
                        String capStr = JOptionPane.showInputDialog("Capacidade Máxima:");

                        if (nomeEv == null || modEv == null ||
                            localEv == null || capStr == null) {
                            break;
                        }

                        try {
                            int capEv = Integer.parseInt(capStr);
                            if (nomeEv.trim().isEmpty() ||
                                    modEv.trim().isEmpty() ||
                                    localEv.trim().isEmpty() ||
                                    capEv <= 0) {
                                JOptionPane.showMessageDialog(null,
                                    "Preencha todos os campos corretamente.");
                                break;
                            }

                            Evento ev = new Evento(nomeEv, modEv, capEv);
                            ev.setLocal(localEv);

                            eventos.add(ev);

                            salvar("eventos");

                            JOptionPane.showMessageDialog(null,
                                    "Evento cadastrado! ID: " + ev.getId());

                        } catch (NumberFormatException e) {

                            JOptionPane.showMessageDialog(null,
                                    "Digite uma capacidade válida.");
                        }

                    } else if (acaoEv != null &&
                            acaoEv.equalsIgnoreCase("A")) {

                        String idStr = JOptionPane.showInputDialog(
                                "ID do Evento para alterar:"
                        );

                        if (idStr == null) {
                            break;
                        }

                        try {

                            int idEv = Integer.parseInt(idStr);
                            Evento evAlt = buscarEvento(idEv);

                            if (evAlt != null) {

                                String novoNome = JOptionPane.showInputDialog(
                                        "Novo Nome (Atual: " + evAlt.getNome() + "):"
                                );

                                String novaModalidade = JOptionPane.showInputDialog(
                                        "Nova Modalidade (Atual: " +
                                                evAlt.getModalidade() + "):"
                                );

                                String novoLocal = JOptionPane.showInputDialog(
                                        "Novo Local (Atual: " + evAlt.getLocal() + "):"
                                );

                                String novaCapStr = JOptionPane.showInputDialog(
                                        "Nova Capacidade Máxima (Atual: " +
                                                evAlt.getCapacidadeMax() + "):"
                                );

                                if (novoNome != null &&
                                        !novoNome.trim().isEmpty()) {
                                    evAlt.setNome(novoNome);
                                }

                                if (novaModalidade != null &&
                                        !novaModalidade.trim().isEmpty()) {
                                    evAlt.setModalidade(novaModalidade);
                                }

                                if (novoLocal != null &&
                                        !novoLocal.trim().isEmpty()) {
                                    evAlt.setLocal(novoLocal);
                                }

                                boolean capacidadeValida = true;

                                if (novaCapStr != null &&
                                        !novaCapStr.trim().isEmpty()) {

                                    try {

                                        int novaCap = Integer.parseInt(novaCapStr);

                                        if (novaCap > 0 &&
                                                novaCap >= evAlt.getInscritos().size()) {

                                            evAlt.setCapacidadeMax(novaCap);

                                        } else {

                                            capacidadeValida = false;

                                            JOptionPane.showMessageDialog(null,
                                                    "A capacidade não pode ser menor " +
                                                            "que a quantidade de inscritos.");
                                        }

                                    } catch (NumberFormatException e) {

                                        capacidadeValida = false;

                                        JOptionPane.showMessageDialog(null,
                                                "Digite uma capacidade válida.");
                                    }
                                }

                                salvar("eventos");

                                if (capacidadeValida) {
                                    JOptionPane.showMessageDialog(null,
                                            "Evento alterado com sucesso!");
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "Os outros dados foram atualizados, " +
                                                    "mas a capacidade não foi alterada.");
                                }

                            } else {

                                JOptionPane.showMessageDialog(null,
                                        "Evento não encontrado.");
                            }

                        } catch (NumberFormatException e) {

                            JOptionPane.showMessageDialog(null,
                                    "Digite um ID válido.");
                        }
                    }

                    break;
                }


                //Cadastrar/ alterar participante
                case 1: {

                    String acaoP = JOptionPane.showInputDialog(
                            "Digite 'C' para Cadastrar ou 'A' para Alterar participante:"
                    );

                    if (acaoP != null && acaoP.equalsIgnoreCase("C")) {

                        String nomeP = JOptionPane.showInputDialog("Nome:");
                        String telP = JOptionPane.showInputDialog("Telefone:");
                        String cpfP = JOptionPane.showInputDialog("CPF:");
                        String idadeStr = JOptionPane.showInputDialog("Idade:");

                        if (nomeP == null || telP == null ||
                                cpfP == null || idadeStr == null) {
                            break;
                        }

                        try {

                            int idadeP = Integer.parseInt(idadeStr);

                            if (nomeP.trim().isEmpty() ||
                                    telP.trim().isEmpty() ||
                                    cpfP.trim().isEmpty() ||
                                    idadeP <= 0) {

                                JOptionPane.showMessageDialog(null,
                                        "Preencha todos os campos corretamente.");
                                break;
                            }

                            Participante p = new Participante(
                                    nomeP, telP, cpfP, idadeP
                            );

                            participantes.add(p);

                            salvar("participantes");

                            JOptionPane.showMessageDialog(null,
                                    "Participante cadastrado! ID: " + p.getId());

                        } catch (NumberFormatException e) {

                            JOptionPane.showMessageDialog(null,
                                    "Digite uma idade válida.");
                        }

                    } else if (acaoP != null &&
                            acaoP.equalsIgnoreCase("A")) {

                        String idStr = JOptionPane.showInputDialog(
                                "Digite o ID do Participante:"
                        );

                        if (idStr == null) {
                            break;
                        }

                        try {

                            int idP = Integer.parseInt(idStr);
                            Participante pAlt = buscarParticipante(idP);

                            if (pAlt != null) {

                                String novoTel = JOptionPane.showInputDialog(
                                        "Novo Telefone (Atual: " +
                                                pAlt.getTelefone() + "):"
                                );

                                if (novoTel != null &&
                                        !novoTel.trim().isEmpty()) {

                                    pAlt.setTelefone(novoTel);

                                    salvar("participantes");

                                    JOptionPane.showMessageDialog(null,
                                            "Dados atualizados com sucesso!");
                                }

                            } else {

                                JOptionPane.showMessageDialog(null,
                                        "Participante não encontrado.");
                            }

                        } catch (NumberFormatException e) {

                            JOptionPane.showMessageDialog(null,
                                    "Digite um ID válido.");
                        }
                    }

                    break;
                }


                // Cadastrar categoria de ingresso
                case 2: {

                    String nomeCat = JOptionPane.showInputDialog(
                            "Nome da Categoria (Ex: VIP, Pista):"
                    );

                    String valorStr = JOptionPane.showInputDialog(
                            "Valor Bruto (R$):"
                    );

                    String descontoStr = JOptionPane.showInputDialog(
                            "Percentual de Desconto (%):"
                    );

                    String qtdStr = JOptionPane.showInputDialog(
                            "Quantidade Disponível:"
                    );

                    if (nomeCat == null || valorStr == null ||
                            descontoStr == null || qtdStr == null) {
                        break;
                    }

                    try {

                        double valor = Double.parseDouble(valorStr);
                        double desconto = Double.parseDouble(descontoStr);
                        int qtd = Integer.parseInt(qtdStr);

                        if (nomeCat.trim().isEmpty() ||
                                valor < 0 ||
                                desconto < 0 ||
                                desconto > 100 ||
                                qtd < 0) {

                            JOptionPane.showMessageDialog(null,
                                    "Verifique os valores informados.");
                            break;
                        }

                        categorias.add(new CategoriaIngresso(
                                nomeCat, valor, desconto, qtd
                        ));

                        salvar("categorias");

                        JOptionPane.showMessageDialog(null,
                                "Categoria cadastrada!");

                    } catch (NumberFormatException e) {

                        JOptionPane.showMessageDialog(null,
                                "Digite valores numéricos válidos.");
                    }

                    break;
                }


                //ocupação dos eventos
                case 3: {

                    StringBuilder sbOcup = new StringBuilder(
                            "EVENTOS DISPONÍVEIS:\n"
                    );

                    for (Evento e : eventos) {

                        sbOcup.append("ID: ")
                                .append(e.getId())
                                .append(" | ")
                                .append(e.getNome())
                                .append("\n");
                    }

                    String idOcupStr = JOptionPane.showInputDialog(
                            sbOcup.toString() + "\nDigite o ID do Evento:"
                    );

                    if (idOcupStr == null) {
                        break;
                    }

                    try {

                        int idEvOcup = Integer.parseInt(idOcupStr);
                        Evento evOcup = buscarEvento(idEvOcup);

                        if (evOcup != null) {

                            JOptionPane.showMessageDialog(null,
                                    evOcup.toString());

                        } else {

                            JOptionPane.showMessageDialog(null,
                                    "Evento não encontrado.");
                        }

                    } catch (NumberFormatException e) {

                        JOptionPane.showMessageDialog(null,
                                "Digite um ID válido.");
                    }

                    break;
                }


                // participantes inscritos
                case 4: {

                    StringBuilder sbListEv = new StringBuilder(
                            "EVENTOS DISPONÍVEIS:\n"
                    );

                    for (Evento e : eventos) {

                        sbListEv.append("ID: ")
                                .append(e.getId())
                                .append(" | ")
                                .append(e.getNome())
                                .append("\n");
                    }

                    String idListStr = JOptionPane.showInputDialog(
                            sbListEv.toString() + "\nDigite o ID do Evento:"
                    );

                    if (idListStr == null) {
                        break;
                    }

                    try {

                        int idEvList = Integer.parseInt(idListStr);
                        Evento evList = buscarEvento(idEvList);

                        if (evList != null) {

                            StringBuilder sb = new StringBuilder(
                                    "Inscritos no evento " +
                                            evList.getNome() + ":\n"
                            );

                            for (Participante p : evList.getInscritos()) {

                                sb.append("- ")
                                        .append(p.getNome())
                                        .append(" (ID: ")
                                        .append(p.getId())
                                        .append(")\n");
                            }

                            JOptionPane.showMessageDialog(null,
                                    sb.toString());

                        } else {

                            JOptionPane.showMessageDialog(null,
                                    "Evento não encontrado.");
                        }

                    } catch (NumberFormatException e) {

                        JOptionPane.showMessageDialog(null,
                                "Digite um ID válido.");
                    }

                    break;
                }


                //inscrições globais
                case 5: {

                    StringBuilder strInsc = new StringBuilder(
                            "Todas as Inscrições:\n"
                    );

                    for (Inscricao i : inscricoes) {

                        strInsc.append("Inscrição ID: ")
                                .append(i.getId())
                                .append(" | Participante ID: ")
                                .append(i.getParticipante().getId())
                                .append(" | Valor: R$ ")
                                .append(i.calcularValor())
                                .append(" | Status: ")
                                .append(i.getStatus())
                                .append("\n");
                    }

                    JOptionPane.showMessageDialog(null,
                            strInsc.toString());

                    break;
                }


                default:
                    noMenu = false;
                    break;
            }
        }
    }

    //MENU DO PARTICIPANTE
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

            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Menu do Participante",
                    "Área do Participante",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            switch (escolha) {

                //cadastrar/alterar dados
                case 0: {

                    String acaoP = JOptionPane.showInputDialog(
                            "Digite 'C' para Cadastrar ou 'A' para Alterar seus dados:"
                    );

                    if (acaoP != null && acaoP.equalsIgnoreCase("C")) {

                        String nomeP = JOptionPane.showInputDialog("Seu Nome:");
                        String telP = JOptionPane.showInputDialog("Seu Telefone:");
                        String cpfP = JOptionPane.showInputDialog("Seu CPF:");
                        String idadeStr = JOptionPane.showInputDialog("Sua Idade:");

                        if (nomeP == null || telP == null ||
                                cpfP == null || idadeStr == null) {
                            break;
                        }

                        try {

                            int idadeP = Integer.parseInt(idadeStr);

                            if (nomeP.trim().isEmpty() ||
                                    telP.trim().isEmpty() ||
                                    cpfP.trim().isEmpty() ||
                                    idadeP <= 0) {

                                JOptionPane.showMessageDialog(null,
                                        "Preencha todos os campos corretamente.");
                                break;
                            }

                            Participante p = new Participante(
                                    nomeP, telP, cpfP, idadeP
                            );

                            participantes.add(p);

                            salvar("participantes");

                            JOptionPane.showMessageDialog(null,
                                    "Cadastro realizado com sucesso!\nGUARDE SEU ID: " +
                                            p.getId());

                        } catch (NumberFormatException e) {

                            JOptionPane.showMessageDialog(null,
                                    "Digite uma idade válida.");
                        }

                    } else if (acaoP != null &&
                            acaoP.equalsIgnoreCase("A")) {

                        String idStr = JOptionPane.showInputDialog(
                                "Digite seu ID de Participante:"
                        );

                        if (idStr == null) {
                            break;
                        }

                        try {

                            int idP = Integer.parseInt(idStr);
                            Participante pAlt = buscarParticipante(idP);

                            if (pAlt != null) {

                                String novoTel = JOptionPane.showInputDialog(
                                        "Novo Telefone (Atual: " +
                                                pAlt.getTelefone() + "):"
                                );

                                if (novoTel != null &&
                                        !novoTel.trim().isEmpty()) {

                                    pAlt.setTelefone(novoTel);

                                    salvar("participantes");

                                    JOptionPane.showMessageDialog(null,
                                            "Dados atualizados com sucesso!");
                                }

                            } else {

                                JOptionPane.showMessageDialog(null,
                                        "Participante não encontrado.");
                            }

                        } catch (NumberFormatException e) {

                            JOptionPane.showMessageDialog(null,
                                    "Digite um ID válido.");
                        }
                    }

                    break;
                }


                //categorias e vagas
                case 1: {

                    StringBuilder sbCat = new StringBuilder(
                            "Categorias Disponíveis:\n"
                    );

                    for (int i = 0; i < categorias.size(); i++) {

                        CategoriaIngresso c = categorias.get(i);

                        sbCat.append("Índice ")
                                .append(i)
                                .append(" - ")
                                .append(c.getNome())
                                .append(" | Valor C/ Desconto: R$ ")
                                .append(c.calcularValor())
                                .append(" | Vagas: ")
                                .append(c.consultarDisponibilidade()
                                        ? "Sim" : "Não")
                                .append("\n");
                    }

                    JOptionPane.showMessageDialog(null,
                            sbCat.toString());

                    break;
                }


                //inscrição
                case 2: {

                    String idParticipanteStr = JOptionPane.showInputDialog(
                            "Seu ID de Participante:"
                    );

                    if (idParticipanteStr == null) {
                        break;
                    }

                    StringBuilder sbEv = new StringBuilder(
                            "EVENTOS DISPONÍVEIS:\n"
                    );

                    for (Evento e : eventos) {

                        sbEv.append("ID: ")
                                .append(e.getId())
                                .append(" | ")
                                .append(e.getNome())
                                .append("\n");
                    }

                    String idEventoStr = JOptionPane.showInputDialog(
                            sbEv.toString() +
                                    "\nDigite o ID do Evento escolhido:"
                    );

                    if (idEventoStr == null) {
                        break;
                    }

                    StringBuilder sbCatInsc = new StringBuilder(
                            "CATEGORIAS DISPONÍVEIS:\n"
                    );

                    for (int i = 0; i < categorias.size(); i++) {

                        CategoriaIngresso c = categorias.get(i);

                        sbCatInsc.append("Índice ")
                                .append(i)
                                .append(" - ")
                                .append(c.getNome())
                                .append(" (R$ ")
                                .append(c.calcularValor())
                                .append(")\n");
                    }

                    String indexCatStr = JOptionPane.showInputDialog(
                            sbCatInsc.toString() +
                                    "\nDigite o Índice da Categoria:"
                    );

                    if (indexCatStr == null) {
                        break;
                    }

                    try {

                        int idP = Integer.parseInt(idParticipanteStr);
                        int idEv = Integer.parseInt(idEventoStr);
                        int indexCat = Integer.parseInt(indexCatStr);

                        Participante p = buscarParticipante(idP);
                        Evento ev = buscarEvento(idEv);

                        if (p == null || ev == null) {
                            JOptionPane.showMessageDialog(null,
                                "Participante ou evento não encontrado.");
                            break;
                        }

                        if (indexCat < 0 || indexCat >= categorias.size()) {
                            JOptionPane.showMessageDialog(null,
                                "Índice de categoria inválido.");
                            break;
                        }

                        CategoriaIngresso cat = categorias.get(indexCat);
                        Inscricao nova = new Inscricao(p, ev, cat);

                        nova.realizarInscricao();
                        ev.adicionarInscrito(p);
                        inscricoes.add(nova);
                        salvar("tudo");

                        JOptionPane.showMessageDialog(null,
                            "Pré-inscrição realizada!\n" +
                            "ID da sua Inscrição: " + nova.getId() +
                            "\nRealize o pagamento para confirmar.");
                    } catch (VagasEsgotadasException e) {
                        JOptionPane.showMessageDialog(null,
                             e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null,
                            "Digite números válidos.");
                    }
                    break;
                }

                //CONSULTAR VALOR DA INSCRIÇÃO
                //CONSULTAR INSCRIÇÃO
                // REGISTRAR PAGAMENTO
                //SOLICITAR CANCELAMENTO

                case 3:
                case 4:
                case 5:
                case 6: {

                    String idBuscaStr = JOptionPane.showInputDialog(
                        "Digite seu ID de Participante para buscar suas inscrições:"
                    );

                    if (idBuscaStr == null) {
                        break;
                    }

                    try {
                        int idBuscaP = Integer.parseInt(idBuscaStr);
                        StringBuilder sbInscricoes = new StringBuilder(
                            "SUAS INSCRIÇÕES:\n"
                        );

                        boolean temInscricao = false;

                        for (Inscricao i : inscricoes) {
                            if (i.getParticipante().getId() == idBuscaP) {
                                sbInscricoes.append("ID da Inscrição: ")
                                        .append(i.getId())
                                        .append(" | Status: ")
                                        .append(i.getStatus())
                                        .append(" | Valor: R$ ")
                                        .append(i.calcularValor())
                                        .append("\n");
                                temInscricao = true;
                            }
                        }

                        if (!temInscricao) {
                            JOptionPane.showMessageDialog(null,
                                "Nenhuma inscrição encontrada para o seu ID.");
                            break;
                        }

                        String idInscStr = JOptionPane.showInputDialog(
                                sbInscricoes.toString() +
                                "\nDigite o ID da Inscrição que deseja gerenciar:"
                        );

                        if (idInscStr == null) {
                            break;
                        }

                        int idInscAcao = Integer.parseInt(idInscStr);
                        Inscricao inscSelecionada = buscarInscricao(idInscAcao);

                        if (inscSelecionada != null &&
                                inscSelecionada.getParticipante().getId() == idBuscaP) {
                            if (escolha == 3 || escolha == 5) {
                                JOptionPane.showMessageDialog(null,
                                        "Inscrição ID: " + inscSelecionada.getId() + "\nValor: R$ " + inscSelecionada.calcularValor() +
                                        "\nStatus: " +
                                        inscSelecionada.getStatus());

                            } else if (escolha == 4) {
                                if (inscSelecionada.getStatus().equals("Pago")) {
                                    JOptionPane.showMessageDialog(null,
                                        "Esta inscrição já está paga!");

                                } else {
                                    Pagamento pag = new Pagamento(
                                        inscSelecionada.calcularValor()
                                    );

                                    inscSelecionada.registrarPagamento(pag);

                                    salvar("inscricoes");

                                    JOptionPane.showMessageDialog(null,
                                        "Pagamento de R$ " +
                                        pag.calcularValor() +
                                        " registrado com sucesso!");
                                }
                            } else if (escolha == 6) {
                                inscSelecionada.cancelar();
                                salvar("inscricoes");
                                JOptionPane.showMessageDialog(null,
                                    "Inscrição cancelada com sucesso!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,
                                "Inscrição inválida ou não pertence a você.");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null,
                            "Digite um ID válido.");
                    }
                    break;
                }
                default:
                    noMenu = false;
                    break;
            }
        }
    }

    //Metodos aux de busca
    private static Evento buscarEvento(int id) {
        for (Evento e : eventos) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    private static Participante buscarParticipante(int id) {
        for (Participante p : participantes) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private static Inscricao buscarInscricao(int id) {
        for (Inscricao i : inscricoes) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    // Salvando dados
    private static void salvar(String tipo) {
        if (tipo.equals("eventos") || tipo.equals("tudo")) {
            GerenciadorArquivos.salvarDados(
                eventos, "eventos.dat"
            );
        }

        if (tipo.equals("participantes") || tipo.equals("tudo")) {
            GerenciadorArquivos.salvarDados(
                participantes, "participantes.dat"
            );
        }

        if (tipo.equals("inscricoes") || tipo.equals("tudo")) {
            GerenciadorArquivos.salvarDados(
                inscricoes, "inscricoes.dat"
            );
        }

        if (tipo.equals("categorias") || tipo.equals("tudo")) {
            GerenciadorArquivos.salvarDados(
                categorias, "categorias.dat"
            );
        }
    }

    //Inicializando os contadores
    private static void inicializarContadores() {
        int maxEv = 0;

        for (Evento e : eventos) {
            if (e.getId() > maxEv) {
                maxEv = e.getId();
            }
        }

        Evento.atualizarContador(maxEv);
        int maxP = 0;

        for (Participante p : participantes) {
            if (p.getId() > maxP) {
                maxP = p.getId();
            }
        }

        Participante.atualizarContador(maxP);
        int maxI = 0;
        
        for (Inscricao i : inscricoes) {

            if (i.getId() > maxI) {
                maxI = i.getId();
            }
        }
        Inscricao.atualizarContador(maxI);
    }
}
