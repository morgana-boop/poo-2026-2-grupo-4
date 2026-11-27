package app;

import alunos.*;
import atividades.*;
import core.Academia;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
// import relatorios.Relatorios;
import turmas.AvaliacaoFisica;

public class Menu {

    // contador static: gera o código das atividades
    private static int proxCodAtividade = 1;

    private final Scanner scanner = new Scanner(System.in);
    private final Academia academia = new Academia();
    // private final Relatorios relatorios = new Relatorios(academia);

    public Menu() {
        // avaliações físicas criadas no início do programa
        academia.cadastrarTurma(new AvaliacaoFisica("08:00", 10));
        academia.cadastrarTurma(new AvaliacaoFisica("14:00", 10));
    }

    public void executar() {
        scanner.useLocale(Locale.forLanguageTag("pt-BR"));
        int opcao = -1;

        while (opcao != 0) {
            exibirMenu();
            if (!scanner.hasNextLine())
                break;
            opcao = lerInt("");

            switch (opcao) {
                case 1:
                    menuAtividades();
                    break;
                case 2:
                    menuAlunos();
                    break;
                case 3:
                    matricular();
                    break;
                case 4:
                    inscrever();
                    break;
                case 5:
                    cancelarInscricao();
                    break;
                case 6:
                    trancar();
                    break;
                case 7:
                    menuRelatorios();
                    break;
                case 0:
                    System.out.println("Encerrando sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }

        scanner.close();
    }

    private void exibirMenu() {
        System.out.println("\nMENU ACADEMIA");
        System.out.println("1 - Atividades");
        System.out.println("2 - Alunos");
        System.out.println("3 - Matricular aluno");
        System.out.println("4 - Inscrever em turma");
        System.out.println("5 - Cancelar inscrição");
        System.out.println("6 - Trancar matrícula");
        System.out.println("7 - Relatórios");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    // ---------- leitura (inválido devolve -1; o texto sempre é lido por linha)
    // ----------

    private int lerInt(String msg) {
        System.out.print(msg);
        if (!scanner.hasNextInt()) {
            if (scanner.hasNextLine())
                scanner.nextLine();
            return -1;
        }
        int n = scanner.nextInt();
        if (scanner.hasNextLine() && !scanner.nextLine().isBlank())
            return -1;
        return n;
    }

    private double lerDouble(String msg) {
        System.out.print(msg);
        if (!scanner.hasNextDouble()) {
            if (scanner.hasNextLine())
                scanner.nextLine();
            return -1;
        }
        double d = scanner.nextDouble();
        if (scanner.hasNextLine() && !scanner.nextLine().isBlank())
            return -1;
        return Double.isFinite(d) ? d : -1;
    }

    private String lerTexto(String msg) {
        System.out.print(msg);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "";
    }

    private void erro() {
        System.out.println("Entrada inválida.");
    }

    private int submenu(String titulo) {
        System.out.println("\n" + titulo);
        System.out.println("1 - Cadastrar\n2 - Listar\n3 - Buscar\n4 - Alterar\n5 - Remover\n0 - Voltar");
        return lerInt("Escolha: ");
    }

    // ---------- atividades ----------

    private void menuAtividades() {
        switch (submenu("ATIVIDADES")) {
            case 1:
                cadastrarAtividade();
                break;
            case 2:
                listarAtividades();
                break;
            case 3:
                buscarAtividade();
                break;
            case 4:
                alterarAtividade();
                break;
            case 5:
                removerAtividade();
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private void cadastrarAtividade() {
        int tipo = lerInt("Tipo (1-Musculação 2-Natação 3-Pilates): ");
        if (tipo < 1 || tipo > 3) {
            erro();
            return;
        }
        String nome = lerTexto("Nome: ");
        if (nome.isBlank()) {
            erro();
            return;
        }
        double valor = lerDouble("Valor base: ");
        if (valor <= 0) {
            erro();
            return;
        }
        int cap = 1;
        if (tipo != 1) {
            cap = lerInt("Capacidade da turma: ");
            if (cap < 1) {
                erro();
                return;
            }
        }

        int cod = proxCodAtividade++;
        if (tipo == 1) {
            System.out.println(academia.cadastrarAtividade(new Musculacao(cod, nome, valor)));
        } else if (tipo == 2) {
            Natacao n = new Natacao(cod, nome, valor, cap);
            System.out.println(academia.cadastrarAtividade(n));
            academia.cadastrarTurma(n); // mesmo objeto nas duas listas
        } else {
            Pilates p = new Pilates(cod, nome, valor, cap);
            System.out.println(academia.cadastrarAtividade(p));
            academia.cadastrarTurma(p);
        }
    }

    private void listarAtividades() {
        ArrayList<Atividade> lista = academia.getAtividades();
        if (lista.isEmpty())
            System.out.println("Nenhuma atividade.");
        for (Atividade a : lista)
            System.out.println(a.descrever());
    }

    private void buscarAtividade() {
        int cod = lerInt("Código: ");
        if (cod < 1) {
            erro();
            return;
        }
        Atividade a = academia.buscarAtividade(cod);
        System.out.println(a == null ? "Atividade não encontrada" : a.descrever());
    }

    private void alterarAtividade() {
        int cod = lerInt("Código: ");
        if (cod < 1) {
            erro();
            return;
        }
        if (academia.buscarAtividade(cod) == null) {
            System.out.println("Atividade não encontrada");
            return;
        }
        String nome = lerTexto("Novo nome: ");
        if (nome.isBlank()) {
            erro();
            return;
        }
        double valor = lerDouble("Novo valor base: ");
        if (valor <= 0) {
            erro();
            return;
        }
        System.out.println(academia.alterarAtividade(cod, nome, valor));
    }

    private void removerAtividade() {
        int cod = lerInt("Código: ");
        if (cod < 1) {
            erro();
            return;
        }
        System.out.println(academia.removerAtividade(cod));
    }

    // ---------- alunos ----------

    private void menuAlunos() {
        switch (submenu("ALUNOS")) {
            case 1:
                cadastrarAluno();
                break;
            case 2:
                listarAlunos();
                break;
            case 3:
                buscarAluno();
                break;
            case 4:
                alterarAluno();
                break;
            case 5:
                removerAluno();
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private void cadastrarAluno() {
        int tipo = lerInt("Tipo (1-Regular 2-Estudante 3-Senior): ");
        if (tipo < 1 || tipo > 3) {
            erro();
            return;
        }
        String nome = lerTexto("Nome: ");
        if (nome.isBlank()) {
            erro();
            return;
        }
        String cpf = lerTexto("CPF (11 dígitos): ");
        if (!cpf.matches("[0-9]{11}")) {
            erro();
            return;
        }
        int idade = lerInt("Idade: ");
        if (idade < 7) {
            erro();
            return;
        }

        Aluno a;
        if (tipo == 1)
            a = new AlunoRegular(nome, cpf, idade);
        else if (tipo == 2)
            a = new AlunoEstudante(nome, cpf, idade);
        else
            a = new AlunoSenior(nome, cpf, idade);
        System.out.println(academia.cadastrarAluno(a));
    }

    private void listarAlunos() {
        ArrayList<Aluno> lista = academia.getAlunos();
        if (lista.isEmpty())
            System.out.println("Nenhum aluno.");
        for (Aluno a : lista)
            System.out.println(a.descrever());
    }

    private void buscarAluno() {
        Aluno a = academia.buscarAluno(lerTexto("CPF: "));
        System.out.println(a == null ? "Aluno não encontrado" : a.descrever());
    }

    private void alterarAluno() {
        String cpf = lerTexto("CPF: ");
        if (academia.buscarAluno(cpf) == null) {
            System.out.println("Aluno não encontrado");
            return;
        }
        String nome = lerTexto("Novo nome: ");
        if (nome.isBlank()) {
            erro();
            return;
        }
        int idade = lerInt("Nova idade: ");
        if (idade < 7) {
            erro();
            return;
        }
        System.out.println(academia.alterarAluno(cpf, nome, idade));
    }

    private void removerAluno() {
        System.out.println(academia.removerAluno(lerTexto("CPF: ")));
    }

    // ---------- matrículas e turmas ----------

    private void matricular() {
        String cpf = lerTexto("CPF do aluno: ");
        if (academia.buscarAluno(cpf) == null) {
            System.out.println("Aluno não encontrado");
            return;
        }
        while (true) {
            listarAtividades();
            int cod = lerInt("Código da atividade (0 encerra): ");
            if (cod == 0)
                return;
            if (cod < 0) {
                erro();
                return;
            }
            System.out.println(academia.matricular(cpf, cod));
        }
    }

    private void inscrever() {
        String cpf = lerTexto("CPF do aluno: ");
        // System.out.println(relatorios.turmasComVagas());
        int pos = lerInt("Posição da turma: ");
        if (pos < 1) {
            erro();
            return;
        }
        System.out.println(academia.inscrever(cpf, pos));
    }

    private void cancelarInscricao() {
        String cpf = lerTexto("CPF do aluno: ");
        // System.out.println(relatorios.turmasComVagas());
        int pos = lerInt("Posição da turma: ");
        if (pos < 1) {
            erro();
            return;
        }
        System.out.println(academia.cancelarInscricao(cpf, pos));
    }

    private void trancar() {
        System.out.println(academia.trancarMatricula(lerTexto("CPF do aluno: ")));
    }

    // ---------- relatórios ----------

    private void menuRelatorios() {
        System.out.println("\nRELATÓRIOS");
        System.out.println(
                "1 - Turmas com vagas\n2 - Mensalidade de um aluno\n3 - Alunos por atividade\n4 - Faturamento do mês\n0 - Voltar");
        switch (lerInt("Escolha: ")) {
            // case 1: System.out.println(relatorios.turmasComVagas()); break;
            // case 2: System.out.println(relatorios.mensalidadeDoAluno(lerTexto("CPF: ")));
            // break;
            // case 3: System.out.println(relatorios.alunosPorAtividade()); break;
            // case 4: System.out.println(relatorios.faturamentoDoMes()); break;
            // case 0: break;
            default:
                System.out.println("Opção inválida!");
        }
    }
}
