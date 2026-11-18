import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainSistema {

    // Listas Globais (carregadas dos arquivos ao iniciar)
    private static ArrayList<Evento> eventos = GerenciadorArquivos.carregarDados("eventos.dat");
    private static ArrayList<Participante> participantes = GerenciadorArquivos.carregarDados("participantes.dat");
    private static ArrayList<Inscricao> inscricoes = GerenciadorArquivos.carregarDados("inscricoes.dat");
    private static ArrayList<CategoriaIngresso> categorias = GerenciadorArquivos.carregarDados("categorias.dat");

    // =====================================================================
    //  MENU PRINCIPAL
    // =====================================================================
    public static void main(String[] args) {

        inicializarContadores();
        boolean executando = true;

        String[] perfis = {"Organizador", "Participante", "Sair"};

        while (executando) {

            int escolha = menuBotoes("Menu Principal",
                    "Bem-vindo ao Sistema de Gerenciamento de Eventos!", perfis);

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
        aviso("Sistema encerrado!");
    }

    // =====================================================================
    //  MENU DO ORGANIZADOR
    // =====================================================================
    private static void menuOrganizador() {

        String[] opcoes = {
                "1. Cadastrar/Alterar/Excluir Eventos",
                "2. Cadastrar/Alterar/Excluir Participantes",
                "3. Cadastrar/Alterar/Excluir Categorias de Ingresso",
                "4. Consultar Ocupação dos Eventos",
                "5. Listar Participantes Inscritos",
                "6. Consultar Inscrições Globais",
                "7. Listar/Consultar Cadastros",
                "Voltar"
        };

        boolean noMenu = true;

        while (noMenu) {
            int escolha = menu("Área do Organizador", "Menu do Organizador", opcoes);

            switch (escolha) {
                case 0:
                    gerenciarEventos();
                    break;
                case 1:
                    gerenciarParticipantes(true);
                    break;
                case 2:
                    gerenciarCategorias();
                    break;
                case 3:
                    consultarOcupacao();
                    break;
                case 4:
                    listarInscritosDoEvento();
                    break;
                case 5:
                    listarInscricoesGlobais();
                    break;
                case 6:
                    listarOuConsultarCadastros();
                    break;
                default:
                    noMenu = false;
                    break;
            }
        }
    }

    // =====================================================================
    //  MENU DO PARTICIPANTE
    // =====================================================================
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
            int escolha = menu("Área do Participante", "Menu do Participante", opcoes);

            switch (escolha) {
                case 0:
                    gerenciarParticipantes(false);
                    break;
                case 1:
                    aviso("CATEGORIAS DE INGRESSO:\n" + listaCategorias());
                    break;
                case 2:
                    realizarInscricao();
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                    gerenciarMinhaInscricao(escolha);
                    break;
                default:
                    noMenu = false;
                    break;
            }
        }
    }

    // =====================================================================
    //  EVENTOS (Incluir / Alterar / Excluir)
    // =====================================================================
    private static void gerenciarEventos() {
        String acao = entrada(
                "Digite 'C' para Cadastrar, 'A' para Alterar ou 'E' para Excluir um evento:");
        if (acao == null) {
            return;
        }
        acao = acao.trim();

        if (acao.equalsIgnoreCase("C")) {
            cadastrarEvento();
        } else if (acao.equalsIgnoreCase("A")) {
            alterarEvento();
        } else if (acao.equalsIgnoreCase("E")) {
            excluirEvento();
        } else {
            aviso("Opção inválida. Use C, A ou E.");
        }
    }

    private static void cadastrarEvento() {
        String nome = entrada("Nome do Evento:");
        String modalidade = entrada("Modalidade (Ex: Presencial, Online, Híbrido):");
        String local = entrada("Local:");
        String dataStr = entrada("Data do evento (dd/MM/aaaa):");
        String capStr = entrada("Capacidade Máxima:");

        if (nome == null || modalidade == null || local == null
                || dataStr == null || capStr == null) {
            return;
        }

        try {
            int capacidade = Integer.parseInt(capStr.trim());

            if (nome.trim().isEmpty() || modalidade.trim().isEmpty()
                    || local.trim().isEmpty() || capacidade <= 0) {
                aviso("Preencha todos os campos corretamente.");
                return;
            }

            Date data = lerData(dataStr);

            Evento ev = new Evento(nome.trim(), modalidade.trim(), capacidade);
            ev.setLocal(local.trim());
            ev.setData(data);

            eventos.add(ev);
            salvar("eventos");

            aviso("Evento cadastrado! ID: " + ev.getId());

        } catch (NumberFormatException e) {
            aviso("Digite uma capacidade válida.");
        } catch (ParseException e) {
            aviso("Data inválida. Use o formato dd/MM/aaaa (ex: 25/12/2026).");
        }
    }

    private static void alterarEvento() {
        String idStr = entrada("ID do Evento para alterar:\n" + listaEventos());
        if (idStr == null) {
            return;
        }

        try {
            Evento ev = buscarEvento(Integer.parseInt(idStr.trim()));

            if (ev == null) {
                aviso("Evento não encontrado.");
                return;
            }

            String aviso = "(deixe em branco para manter)";
            String novoNome = entrada("Novo Nome (Atual: " + ev.getNome() + ")\n" + aviso);
            String novaMod = entrada("Nova Modalidade (Atual: " + ev.getModalidade() + ")\n" + aviso);
            String novoLocal = entrada("Novo Local (Atual: " + ev.getLocal() + ")\n" + aviso);
            String novaData = entrada("Nova Data dd/MM/aaaa (Atual: " + ev.getDataFormatada() + ")\n" + aviso);
            String novaCap = entrada("Nova Capacidade Máxima (Atual: " + ev.getCapacidadeMax() + ")\n" + aviso);

            StringBuilder problemas = new StringBuilder();

            if (preenchido(novoNome)) {
                ev.setNome(novoNome.trim());
            }
            if (preenchido(novaMod)) {
                ev.setModalidade(novaMod.trim());
            }
            if (preenchido(novoLocal)) {
                ev.setLocal(novoLocal.trim());
            }

            if (preenchido(novaData)) {
                try {
                    ev.setData(lerData(novaData));
                } catch (ParseException e) {
                    problemas.append("- Data inválida (use dd/MM/aaaa); a data não foi alterada.\n");
                }
            }

            if (preenchido(novaCap)) {
                try {
                    int cap = Integer.parseInt(novaCap.trim());
                    if (cap > 0 && cap >= ev.getInscritos().size()) {
                        ev.setCapacidadeMax(cap);
                    } else {
                        problemas.append("- A capacidade deve ser maior que zero e não pode ser menor "
                                + "que a quantidade de inscritos (" + ev.getInscritos().size() + ").\n");
                    }
                } catch (NumberFormatException e) {
                    problemas.append("- Capacidade inválida; não foi alterada.\n");
                }
            }

            salvar("eventos");

            if (problemas.length() == 0) {
                aviso("Evento alterado com sucesso!");
            } else {
                aviso("Os outros dados foram atualizados, mas:\n" + problemas);
            }

        } catch (NumberFormatException e) {
            aviso("Digite um ID válido.");
        }
    }

    private static void excluirEvento() {
        String idStr = entrada("ID do Evento para excluir:\n" + listaEventos());
        if (idStr == null) {
            return;
        }

        try {
            Evento ev = buscarEvento(Integer.parseInt(idStr.trim()));

            if (ev == null) {
                aviso("Evento não encontrado.");
                return;
            }

            if (eventoTemInscricaoAtiva(ev)) {
                aviso("Não é possível excluir: o evento possui inscrições ativas.\n"
                        + "Cancele as inscrições antes de excluir.");
                return;
            }

            if (!confirmar("Excluir o evento '" + ev.getNome() + "'?")) {
                return;
            }

            final int idEv = ev.getId();
            inscricoes.removeIf(i -> i.getEvento().getId() == idEv); // só restam as canceladas
            eventos.remove(ev);
            salvar("tudo");

            aviso("Evento excluído com sucesso!");

        } catch (NumberFormatException e) {
            aviso("Digite um ID válido.");
        }
    }

    // =====================================================================
    //  PARTICIPANTES (Incluir / Alterar / Excluir)
    // =====================================================================
    // organizador = true  -> pode cadastrar, alterar e excluir
    // organizador = false -> o próprio participante cadastra/altera seus dados
    private static void gerenciarParticipantes(boolean organizador) {
        String acao;
        if (organizador) {
            acao = entrada("Digite 'C' para Cadastrar, 'A' para Alterar ou 'E' para Excluir um participante:");
        } else {
            acao = entrada("Digite 'C' para Cadastrar ou 'A' para Alterar seus dados:");
        }
        if (acao == null) {
            return;
        }
        acao = acao.trim();

        if (acao.equalsIgnoreCase("C")) {
            cadastrarParticipante(organizador);
        } else if (acao.equalsIgnoreCase("A")) {
            alterarParticipante(organizador);
        } else if (acao.equalsIgnoreCase("E") && organizador) {
            excluirParticipante();
        } else {
            aviso(organizador ? "Opção inválida. Use C, A ou E." : "Opção inválida. Use C ou A.");
        }
    }

    private static void cadastrarParticipante(boolean organizador) {
        String nome = entrada(organizador ? "Nome:" : "Seu Nome:");
        String tel = entrada(organizador ? "Telefone:" : "Seu Telefone:");
        String cpf = entrada(organizador ? "CPF (11 dígitos):" : "Seu CPF (11 dígitos):");
        String idadeStr = entrada(organizador ? "Idade:" : "Sua Idade:");

        if (nome == null || tel == null || cpf == null || idadeStr == null) {
            return;
        }

        try {
            int idade = Integer.parseInt(idadeStr.trim());

            if (nome.trim().isEmpty() || tel.trim().isEmpty() || idade <= 0 || idade > 120) {
                aviso("Preencha todos os campos corretamente.");
                return;
            }

            String cpfNumeros = cpf.replaceAll("\\D", "");
            if (cpfNumeros.length() != 11) {
                aviso("CPF inválido. Digite os 11 dígitos.");
                return;
            }

            for (Participante existente : participantes) {
                if (existente.getCpf().replaceAll("\\D", "").equals(cpfNumeros)) {
                    aviso("Já existe um participante cadastrado com este CPF (ID: "
                            + existente.getId() + ").");
                    return;
                }
            }

            Participante p = new Participante(nome.trim(), tel.trim(), cpf.trim(), idade);
            participantes.add(p);
            salvar("participantes");

            if (organizador) {
                aviso("Participante cadastrado! ID: " + p.getId());
            } else {
                aviso("Cadastro realizado com sucesso!\nGUARDE SEU ID: " + p.getId());
            }

        } catch (NumberFormatException e) {
            aviso("Digite uma idade válida.");
        }
    }

    private static void alterarParticipante(boolean organizador) {
        String idStr = entrada(organizador
                ? "ID do Participante:\n" + listaParticipantes()
                : "Digite seu ID de Participante:");
        if (idStr == null) {
            return;
        }

        try {
            Participante p = buscarParticipante(Integer.parseInt(idStr.trim()));

            if (p == null) {
                aviso("Participante não encontrado.");
                return;
            }

            String manter = "(deixe em branco para manter)";
            String novoNome = entrada("Novo Nome (Atual: " + p.getNome() + ")\n" + manter);
            String novoTel = entrada("Novo Telefone (Atual: " + p.getTelefone() + ")\n" + manter);
            String novaIdade = entrada("Nova Idade (Atual: " + p.getIdade() + ")\n" + manter);

            StringBuilder problemas = new StringBuilder();

            if (preenchido(novoNome)) {
                p.setNome(novoNome.trim());
            }
            if (preenchido(novoTel)) {
                p.setTelefone(novoTel.trim());
            }
            if (preenchido(novaIdade)) {
                try {
                    int idade = Integer.parseInt(novaIdade.trim());
                    if (idade > 0 && idade <= 120) {
                        p.setIdade(idade);
                    } else {
                        problemas.append("- Idade fora do intervalo permitido; não foi alterada.\n");
                    }
                } catch (NumberFormatException e) {
                    problemas.append("- Idade inválida; não foi alterada.\n");
                }
            }

            salvar("participantes");

            if (problemas.length() == 0) {
                aviso("Dados atualizados com sucesso!");
            } else {
                aviso("Os outros dados foram atualizados, mas:\n" + problemas);
            }

        } catch (NumberFormatException e) {
            aviso("Digite um ID válido.");
        }
    }

    private static void excluirParticipante() {
        String idStr = entrada("ID do Participante para excluir:\n" + listaParticipantes());
        if (idStr == null) {
            return;
        }

        try {
            Participante p = buscarParticipante(Integer.parseInt(idStr.trim()));

            if (p == null) {
                aviso("Participante não encontrado.");
                return;
            }

            if (participanteTemInscricaoAtiva(p)) {
                aviso("Não é possível excluir: o participante possui inscrições ativas.\n"
                        + "Cancele as inscrições antes de excluir.");
                return;
            }

            if (!confirmar("Excluir o participante '" + p.getNome() + "'?")) {
                return;
            }

            final int idP = p.getId();
            inscricoes.removeIf(i -> i.getParticipante().getId() == idP); // só restam as canceladas
            participantes.remove(p);
            salvar("tudo");

            aviso("Participante excluído com sucesso!");

        } catch (NumberFormatException e) {
            aviso("Digite um ID válido.");
        }
    }

    // =====================================================================
    //  CATEGORIAS DE INGRESSO (Incluir / Alterar / Excluir)
    // =====================================================================
    private static void gerenciarCategorias() {
        String acao = entrada(
                "Digite 'C' para Cadastrar, 'A' para Alterar ou 'E' para Excluir uma categoria:");
        if (acao == null) {
            return;
        }
        acao = acao.trim();

        if (acao.equalsIgnoreCase("C")) {
            cadastrarCategoria();
        } else if (acao.equalsIgnoreCase("A")) {
            alterarCategoria();
        } else if (acao.equalsIgnoreCase("E")) {
            excluirCategoria();
        } else {
            aviso("Opção inválida. Use C, A ou E.");
        }
    }

    private static void cadastrarCategoria() {
        String nome = entrada("Nome da Categoria (Ex: VIP, Pista):");
        String valorStr = entrada("Valor Bruto (R$):");
        String descontoStr = entrada("Percentual de Desconto (%):");
        String qtdStr = entrada("Quantidade Disponível:");

        if (nome == null || valorStr == null || descontoStr == null || qtdStr == null) {
            return;
        }

        try {
            double valor = lerDouble(valorStr);
            double desconto = lerDouble(descontoStr);
            int qtd = Integer.parseInt(qtdStr.trim());

            if (nome.trim().isEmpty() || valor < 0 || desconto < 0 || desconto > 100 || qtd < 0) {
                aviso("Verifique os valores informados.");
                return;
            }

            CategoriaIngresso cat = new CategoriaIngresso(nome.trim(), valor, desconto, qtd);
            categorias.add(cat);
            salvar("categorias");

            aviso("Categoria cadastrada! ID: " + cat.getId());

        } catch (NumberFormatException e) {
            aviso("Digite valores numéricos válidos.");
        }
    }

    private static void alterarCategoria() {
        String idStr = entrada("ID da Categoria para alterar:\n" + listaCategorias());
        if (idStr == null) {
            return;
        }

        try {
            CategoriaIngresso cat = buscarCategoria(Integer.parseInt(idStr.trim()));

            if (cat == null) {
                aviso("Categoria não encontrada.");
                return;
            }

            String manter = "(deixe em branco para manter)";
            String novoNome = entrada("Novo Nome (Atual: " + cat.getNome() + ")\n" + manter);
            String novoValor = entrada("Novo Valor Bruto (Atual: " + cat.getValor() + ")\n" + manter);
            String novoDesc = entrada("Novo Desconto % (Atual: " + cat.getPercentualDesconto() + ")\n" + manter);
            String novaQtd = entrada("Nova Quantidade Disponível (Atual: " + cat.getQntdDisponivel() + ")\n" + manter);

            StringBuilder problemas = new StringBuilder();

            if (preenchido(novoNome)) {
                cat.setNome(novoNome.trim());
            }

            if (preenchido(novoValor)) {
                try {
                    double v = lerDouble(novoValor);
                    if (v >= 0) {
                        cat.setValor(v);
                    } else {
                        problemas.append("- O valor não pode ser negativo.\n");
                    }
                } catch (NumberFormatException e) {
                    problemas.append("- Valor inválido; não foi alterado.\n");
                }
            }

            if (preenchido(novoDesc)) {
                try {
                    double d = lerDouble(novoDesc);
                    if (d >= 0 && d <= 100) {
                        cat.setPercentualDesconto(d);
                    } else {
                        problemas.append("- O desconto deve estar entre 0 e 100.\n");
                    }
                } catch (NumberFormatException e) {
                    problemas.append("- Desconto inválido; não foi alterado.\n");
                }
            }

            if (preenchido(novaQtd)) {
                try {
                    int q = Integer.parseInt(novaQtd.trim());
                    if (q >= 0) {
                        cat.setQntdDisponivel(q);
                    } else {
                        problemas.append("- A quantidade não pode ser negativa.\n");
                    }
                } catch (NumberFormatException e) {
                    problemas.append("- Quantidade inválida; não foi alterada.\n");
                }
            }

            salvar("categorias");

            if (problemas.length() == 0) {
                aviso("Categoria alterada com sucesso!\n(Inscrições já feitas mantêm o valor original.)");
            } else {
                aviso("Os outros dados foram atualizados, mas:\n" + problemas);
            }

        } catch (NumberFormatException e) {
            aviso("Digite um ID válido.");
        }
    }

    private static void excluirCategoria() {
        String idStr = entrada("ID da Categoria para excluir:\n" + listaCategorias());
        if (idStr == null) {
            return;
        }

        try {
            CategoriaIngresso cat = buscarCategoria(Integer.parseInt(idStr.trim()));

            if (cat == null) {
                aviso("Categoria não encontrada.");
                return;
            }

            if (categoriaTemInscricaoAtiva(cat)) {
                aviso("Não é possível excluir: existem inscrições ativas nesta categoria.");
                return;
            }

            if (!confirmar("Excluir a categoria '" + cat.getNome() + "'?")) {
                return;
            }

            final int idCat = cat.getId();
            inscricoes.removeIf(i -> i.getCategoria().getId() == idCat); // só restam as canceladas
            categorias.remove(cat);
            salvar("tudo");

            aviso("Categoria excluída com sucesso!");

        } catch (NumberFormatException e) {
            aviso("Digite um ID válido.");
        }
    }

    // =====================================================================
    //  CONSULTAS E LISTAGENS DO ORGANIZADOR
    // =====================================================================
    private static void consultarOcupacao() {
        if (eventos.isEmpty()) {
            aviso("Nenhum evento cadastrado.");
            return;
        }

        String idStr = entrada("EVENTOS:\n" + listaEventos() + "\nDigite o ID do Evento:");
        if (idStr == null) {
            return;
        }

        try {
            Evento ev = buscarEvento(Integer.parseInt(idStr.trim()));

            if (ev != null) {
                aviso(ev.detalhar());
            } else {
                aviso("Evento não encontrado.");
            }

        } catch (NumberFormatException e) {
            aviso("Digite um ID válido.");
        }
    }

    private static void listarInscritosDoEvento() {
        if (eventos.isEmpty()) {
            aviso("Nenhum evento cadastrado.");
            return;
        }

        String idStr = entrada("EVENTOS:\n" + listaEventos() + "\nDigite o ID do Evento:");
        if (idStr == null) {
            return;
        }

        try {
            Evento ev = buscarEvento(Integer.parseInt(idStr.trim()));

            if (ev == null) {
                aviso("Evento não encontrado.");
                return;
            }

            StringBuilder sb = new StringBuilder("Inscritos no evento " + ev.getNome() + ":\n");

            if (ev.getInscritos().isEmpty()) {
                sb.append("(nenhum inscrito)");
            }

            for (Participante p : ev.getInscritos()) {
                sb.append("- ").append(p.getNome())
                        .append(" (ID: ").append(p.getId()).append(")\n");
            }

            aviso(sb.toString());

        } catch (NumberFormatException e) {
            aviso("Digite um ID válido.");
        }
    }

    private static void listarInscricoesGlobais() {
        if (inscricoes.isEmpty()) {
            aviso("Nenhuma inscrição registrada.");
            return;
        }

        StringBuilder sb = new StringBuilder("Todas as Inscrições:\n");
        for (Inscricao i : inscricoes) {
            sb.append(i.resumo()).append("\n");
        }
        aviso(sb.toString());
    }

    // Lista todos ou consulta um cadastro específico pelo ID
    private static void listarOuConsultarCadastros() {
        String tipo = entrada("Listar/Consultar:\n"
                + "E - Eventos\nP - Participantes\nC - Categorias de ingresso");
        if (tipo == null) {
            return;
        }
        tipo = tipo.trim().toUpperCase();

        if (!tipo.equals("E") && !tipo.equals("P") && !tipo.equals("C")) {
            aviso("Opção inválida. Use E, P ou C.");
            return;
        }

        String idStr = entrada("Digite o ID para consultar um cadastro específico\n"
                + "(ou deixe em branco para listar todos):");
        if (idStr == null) {
            return;
        }

        if (idStr.trim().isEmpty()) {
            if (tipo.equals("E")) {
                aviso("EVENTOS:\n" + listaEventos());
            } else if (tipo.equals("P")) {
                aviso("PARTICIPANTES:\n" + listaParticipantes());
            } else {
                aviso("CATEGORIAS:\n" + listaCategorias());
            }
            return;
        }

        try {
            int id = Integer.parseInt(idStr.trim());

            if (tipo.equals("E")) {
                Evento ev = buscarEvento(id);
                aviso(ev != null ? ev.detalhar() : "Evento não encontrado.");
            } else if (tipo.equals("P")) {
                Participante p = buscarParticipante(id);
                aviso(p != null ? p.toString() : "Participante não encontrado.");
            } else {
                CategoriaIngresso c = buscarCategoria(id);
                aviso(c != null ? c.toString() : "Categoria não encontrada.");
            }

        } catch (NumberFormatException e) {
            aviso("Digite um ID válido.");
        }
    }

    // =====================================================================
    //  INSCRIÇÃO, PAGAMENTO E CANCELAMENTO (participante)
    // =====================================================================
    private static void realizarInscricao() {
        String idPStr = entrada("Seu ID de Participante:");
        if (idPStr == null) {
            return;
        }

        if (eventos.isEmpty()) {
            aviso("Não há eventos cadastrados.");
            return;
        }
        if (categorias.isEmpty()) {
            aviso("Não há categorias de ingresso cadastradas.");
            return;
        }

        String idEvStr = entrada("EVENTOS DISPONÍVEIS:\n" + listaEventos()
                + "\nDigite o ID do Evento escolhido:");
        if (idEvStr == null) {
            return;
        }

        String idCatStr = entrada("CATEGORIAS DISPONÍVEIS:\n" + listaCategorias()
                + "\nDigite o ID da Categoria:");
        if (idCatStr == null) {
            return;
        }

        try {
            int idP = Integer.parseInt(idPStr.trim());
            int idEv = Integer.parseInt(idEvStr.trim());
            int idCat = Integer.parseInt(idCatStr.trim());

            Participante p = buscarParticipante(idP);
            Evento ev = buscarEvento(idEv);
            CategoriaIngresso cat = buscarCategoria(idCat);

            if (p == null || ev == null) {
                aviso("Participante ou evento não encontrado.");
                return;
            }

            if (cat == null) {
                aviso("Categoria não encontrada.");
                return;
            }

            if (temInscricaoAtiva(p, ev)) {
                aviso("Você já possui uma inscrição ativa neste evento.");
                return;
            }

            // Confere vagas ANTES de criar a inscrição (assim nenhum ID é "gasto" à toa)
            Inscricao.verificarDisponibilidade(ev, cat);

            Inscricao nova = new Inscricao(p, ev, cat);
            nova.realizarInscricao(); // ocupa a vaga no evento e na categoria
            inscricoes.add(nova);
            salvar("tudo");

            aviso("Inscrição realizada!\n"
                    + "ID da sua Inscrição: " + nova.getId()
                    + "\nValor: R$ " + String.format("%.2f", nova.calcularValor())
                    + "\nRealize o pagamento para confirmar.");

        } catch (VagasEsgotadasException e) {
            erro(e.getMessage());
        } catch (NumberFormatException e) {
            aviso("Digite números válidos.");
        }
    }

    // escolha: 3 = valor, 4 = pagamento, 5 = consultar, 6 = cancelar
    private static void gerenciarMinhaInscricao(int escolha) {
        String idBuscaStr = entrada("Digite seu ID de Participante para buscar suas inscrições:");
        if (idBuscaStr == null) {
            return;
        }

        try {
            int idBuscaP = Integer.parseInt(idBuscaStr.trim());
            StringBuilder sb = new StringBuilder("SUAS INSCRIÇÕES:\n");
            boolean temInscricao = false;

            for (Inscricao i : inscricoes) {
                if (i.getParticipante().getId() == idBuscaP) {
                    sb.append(i.resumo()).append("\n");
                    temInscricao = true;
                }
            }

            if (!temInscricao) {
                aviso("Nenhuma inscrição encontrada para o seu ID.");
                return;
            }

            String idInscStr = entrada(sb + "\nDigite o ID da Inscrição que deseja gerenciar:");
            if (idInscStr == null) {
                return;
            }

            Inscricao insc = buscarInscricao(Integer.parseInt(idInscStr.trim()));

            if (insc == null || insc.getParticipante().getId() != idBuscaP) {
                aviso("Inscrição inválida ou não pertence a você.");
                return;
            }

            if (escolha == 3 || escolha == 5) {
                aviso(insc.resumo());

            } else if (escolha == 4) {
                if (insc.getStatus().equals("Cancelada")) {
                    aviso("Esta inscrição foi cancelada e não pode ser paga.");
                } else if (insc.getStatus().equals("Pago")) {
                    aviso("Esta inscrição já está paga!");
                } else {
                    Pagamento pag = new Pagamento(insc.calcularValor());
                    insc.registrarPagamento(pag);
                    salvar("inscricoes");
                    aviso("Pagamento de R$ " + String.format("%.2f", pag.calcularValor())
                            + " registrado com sucesso!");
                }

            } else if (escolha == 6) {
                if (insc.getStatus().equals("Cancelada")) {
                    aviso("Esta inscrição já estava cancelada.");
                } else if (confirmar("Deseja realmente cancelar a inscrição " + insc.getId() + "?")) {
                    insc.cancelar(); // devolve a vaga ao evento e à categoria
                    salvar("tudo");  // eventos e categorias também mudaram
                    aviso("Inscrição cancelada com sucesso! A vaga foi devolvida.");
                }
            }

        } catch (NumberFormatException e) {
            aviso("Digite um ID válido.");
        }
    }

    // =====================================================================
    //  LISTAS DE TEXTO (usadas nos diálogos)
    // =====================================================================
    private static String listaEventos() {
        if (eventos.isEmpty()) {
            return "(nenhum evento cadastrado)\n";
        }
        StringBuilder sb = new StringBuilder();
        for (Evento e : eventos) {
            sb.append(e).append("\n");
        }
        return sb.toString();
    }

    private static String listaParticipantes() {
        if (participantes.isEmpty()) {
            return "(nenhum participante cadastrado)\n";
        }
        StringBuilder sb = new StringBuilder();
        for (Participante p : participantes) {
            sb.append(p).append("\n");
        }
        return sb.toString();
    }

    private static String listaCategorias() {
        if (categorias.isEmpty()) {
            return "(nenhuma categoria cadastrada)\n";
        }
        StringBuilder sb = new StringBuilder();
        for (CategoriaIngresso c : categorias) {
            sb.append(c).append("\n");
        }
        return sb.toString();
    }

    // =====================================================================
    //  BUSCAS
    // =====================================================================
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

    private static CategoriaIngresso buscarCategoria(int id) {
        for (CategoriaIngresso c : categorias) {
            if (c.getId() == id) {
                return c;
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

    // Inscrição "ativa" = qualquer uma que não esteja cancelada
    private static boolean temInscricaoAtiva(Participante p, Evento ev) {
        for (Inscricao i : inscricoes) {
            if (i.getParticipante().getId() == p.getId()
                    && i.getEvento().getId() == ev.getId()
                    && !i.getStatus().equals("Cancelada")) {
                return true;
            }
        }
        return false;
    }

    private static boolean eventoTemInscricaoAtiva(Evento ev) {
        for (Inscricao i : inscricoes) {
            if (i.getEvento().getId() == ev.getId() && !i.getStatus().equals("Cancelada")) {
                return true;
            }
        }
        return false;
    }

    private static boolean participanteTemInscricaoAtiva(Participante p) {
        for (Inscricao i : inscricoes) {
            if (i.getParticipante().getId() == p.getId() && !i.getStatus().equals("Cancelada")) {
                return true;
            }
        }
        return false;
    }

    private static boolean categoriaTemInscricaoAtiva(CategoriaIngresso c) {
        for (Inscricao i : inscricoes) {
            if (i.getCategoria().getId() == c.getId() && !i.getStatus().equals("Cancelada")) {
                return true;
            }
        }
        return false;
    }

    // =====================================================================
    //  UTILITÁRIOS
    // =====================================================================
    private static boolean preenchido(String s) {
        return s != null && !s.trim().isEmpty();
    }

    // Aceita "10,5" e "10.5"
    private static double lerDouble(String texto) {
        return Double.parseDouble(texto.trim().replace(',', '.'));
    }

    // Aceita somente dd/MM/aaaa e datas que existem de verdade (rejeita 31/02/2026)
    private static Date lerData(String texto) throws ParseException {
        String t = texto.trim();
        if (!t.matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new ParseException("formato inválido", 0);
        }
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        formato.setLenient(false);
        return formato.parse(t);
    }

    // =====================================================================
    //  PERSISTÊNCIA
    // =====================================================================
    private static void salvar(String tipo) {
        if (tipo.equals("eventos") || tipo.equals("tudo")) {
            GerenciadorArquivos.salvarDados(eventos, "eventos.dat");
        }
        if (tipo.equals("participantes") || tipo.equals("tudo")) {
            GerenciadorArquivos.salvarDados(participantes, "participantes.dat");
        }
        if (tipo.equals("inscricoes") || tipo.equals("tudo")) {
            GerenciadorArquivos.salvarDados(inscricoes, "inscricoes.dat");
        }
        if (tipo.equals("categorias") || tipo.equals("tudo")) {
            GerenciadorArquivos.salvarDados(categorias, "categorias.dat");
        }
    }

    // Ajusta os contadores estáticos de ID e religa os objetos lidos dos arquivos
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

        int maxC = 0;
        for (CategoriaIngresso c : categorias) {
            if (c.getId() > maxC) {
                maxC = c.getId();
            }
        }
        CategoriaIngresso.atualizarContador(maxC);

        int maxI = 0;
        int maxPag = 0;
        for (Inscricao i : inscricoes) {
            if (i.getId() > maxI) {
                maxI = i.getId();
            }
            if (i.getPagamento() != null && i.getPagamento().getId() > maxPag) {
                maxPag = i.getPagamento().getId();
            }
        }
        Inscricao.atualizarContador(maxI);
        Pagamento.atualizarContador(maxPag);

        // Cada arquivo foi lido separadamente, então os objetos são cópias.
        // Aqui tudo é religado aos objetos oficiais das listas (pelo ID).
        for (Evento e : eventos) {
            e.reconectarInscritos(participantes);
        }
        for (Inscricao i : inscricoes) {
            i.reconectar(eventos, participantes, categorias);
        }
    }

    // =====================================================================
    //  CAMADA DE DIÁLOGOS (JOptionPane) - único lugar que abre janelas
    // =====================================================================
    // <<DIALOGOS>>
    private static String entrada(String mensagem) {
        return JOptionPane.showInputDialog(mensagem);
    }

    private static void aviso(String mensagem) {
        if (mensagem.split("\n").length > 18) {
            // textos longos ganham barra de rolagem
            JTextArea area = new JTextArea(mensagem, 18, 60);
            area.setEditable(false);
            JOptionPane.showMessageDialog(null, new JScrollPane(area));
        } else {
            JOptionPane.showMessageDialog(null, mensagem);
        }
    }

    private static void erro(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private static boolean confirmar(String mensagem) {
        return JOptionPane.showConfirmDialog(null, mensagem, "Confirmação",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    // Menu em lista suspensa (cabe qualquer quantidade de opções). Retorna o índice ou -1.
    private static int menu(String titulo, String mensagem, String[] opcoes) {
        Object sel = JOptionPane.showInputDialog(null, mensagem, titulo,
                JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);
        for (int i = 0; i < opcoes.length; i++) {
            if (opcoes[i].equals(sel)) {
                return i;
            }
        }
        return -1;
    }

    // Menu com botões (poucas opções). Retorna o índice ou -1.
    private static int menuBotoes(String titulo, String mensagem, String[] opcoes) {
        return JOptionPane.showOptionDialog(null, mensagem, titulo,
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, opcoes, opcoes[0]);
    }
    // <<FIM-DIALOGOS>>
}
