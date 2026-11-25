# Sistema Academia - Grupo 4
Projeto Programação Orientada a Objetos
IFMA - Ciência da Computação - 2026.2

## Como executar

**Pré-requisito:** JDK 17 ou superior.

Na raiz do repositório, compile todos os arquivos Java:

```bash
javac -d out $(find src -name "*.java")
```

Depois, execute a classe principal:

```bash
java -cp out app.Main
```

> Os arquivos devem declarar o pacote correspondente à sua pasta (`package app;`, `package alunos;` etc.). O comando de execução pressupõe que `Main.java` declara `package app;` e contém o método `public static void main(String[] args)`.

## Divisão de responsabilidades

| Integrante | GitHub | Pacote(s) |
|---|---|---|
| Gabriel S | [@putrefascia](https://github.com/putrefascia) | `atividades/` |
| Anna Júlia | [@janna-juu](https://github.com/janna-juu) | `alunos/` |
| Erasmo | [@ErasmoPereira0708](https://github.com/ErasmoPereira0708) | `turmas/` |
| Marco Antônio | [@Kratonz-21](https://github.com/Kratonz-21) | `matriculas/` |
| Lucas Fontes | [@lfonteskayn](https://github.com/lfonteskayn) | `core/` |
| Marcos Chagas | [@morgana-boop](https://github.com/morgana-boop) | `app/` e `relatorios/` |

## Diagrama de classes (UML)

Representação da arquitetura orientada a objetos do
Sistema Academia e dos relacionamentos entre suas classes.

<p align="center">
  <img src="https://i.imgur.com/yH3kPXp.png"
       alt="Diagrama de classes UML"
       width="100%">
</p>

## Declaração de uso de IA

Gemini foi usado para delegar o trabalho de cada integrante sobre o que foi exigido pelo professor.
ChatGPT foi utilizado para auxiliar na elaboração do diagrama de classes do sistema.
