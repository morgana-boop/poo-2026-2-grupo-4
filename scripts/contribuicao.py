import argparse
import re
import subprocess
from collections import Counter
from datetime import datetime, timedelta, timezone
from pathlib import Path

# Mapeamento: Nome -> Lista de e-mails associados ao integrante
# Se alguém usar e-mail @users.noreply.github.com, basta adicionar na lista da pessoa.
MEMBROS = {
    "Gabriel S": ["truegojira@gmail.com"],
    "Anna Júlia": ["j.anna@acad.ifma.edu.br"],
    "Erasmo": ["erasmo.pereira0708@gmail.com"],
    "Marco Antônio": ["marccout2544@gmail.com"],
    "Lucas Fontes": ["lfontes@acad.ifma.edu.br"],
    "Marcos Chagas": ["nuitmorgana@gmail.com"],
}

EMAILS_IGNORADOS = {
    "morgscollab@teste.com",
}

EMAIL_PARA_NOME = {
    email.lower(): nome
    for nome, emails in MEMBROS.items()
    for email in emails
}

MARCADOR_INICIO = "<!-- CONTRIBUICAO:START -->"
MARCADOR_FIM = "<!-- CONTRIBUICAO:END -->"


def git(*args: str) -> bytes:
    return subprocess.check_output(
        ["git", *args],
        stderr=subprocess.DEVNULL,
    )


def linhas_com_codigo(codigo: str) -> set[int]:
    validas = set()
    estado = "normal"

    for numero, linha in enumerate(codigo.splitlines(), 1):
        i = 0
        tem_codigo = False

        while i < len(linha):
            if estado == "block":
                if linha.startswith("*/", i):
                    estado = "normal"
                    i += 2
                else:
                    i += 1

            elif estado == "text":
                if linha.startswith('"""', i):
                    estado = "normal"
                    i += 3
                else:
                    tem_codigo = True
                    i += 1

            elif estado in ("string", "char"):
                tem_codigo = True

                if linha[i] == "\\":
                    i += 2
                elif (estado == "string" and linha[i] == '"') or (
                    estado == "char" and linha[i] == "'"
                ):
                    estado = "normal"
                    i += 1
                else:
                    i += 1

            else:
                if linha.startswith("//", i):
                    break

                elif linha.startswith("/*", i):
                    estado = "block"
                    i += 2

                elif linha.startswith('"""', i):
                    estado = "text"
                    tem_codigo = True
                    i += 3

                elif linha[i] == '"':
                    estado = "string"
                    tem_codigo = True
                    i += 1

                elif linha[i] == "'":
                    estado = "char"
                    tem_codigo = True
                    i += 1

                elif not linha[i].isspace():
                    tem_codigo = True
                    i += 1

                else:
                    i += 1

        if tem_codigo:
            validas.add(numero)

    return validas


def linhas_adicionadas(diff: str) -> set[int]:
    adicionadas = set()
    linha_nova = 0

    for linha in diff.splitlines():
        if linha.startswith("@@"):
            match = re.search(r"\+(\d+)(?:,(\d+))?", linha)
            if match:
                linha_nova = int(match.group(1))

        elif linha.startswith("+++"):
            continue

        elif linha.startswith("+"):
            adicionadas.add(linha_nova)
            linha_nova += 1

        elif linha.startswith("-"):
            continue

        elif linha.startswith(" "):
            linha_nova += 1

    return adicionadas


def calcular_metricas(ref: str = "--all"):
    linhas_por_membro = Counter()
    commits_por_membro = Counter()
    emails_nao_mapeados = Counter()

    commits = (
        git("log", ref, "--no-merges", "--format=%H%x09%ae")
        .decode(errors="replace")
        .splitlines()
    )

    for registro in commits:
        if "\t" not in registro:
            continue

        commit, email = registro.split("\t", 1)
        email = email.strip().lower()

        if email in EMAILS_IGNORADOS:
            continue

        nome = EMAIL_PARA_NOME.get(email)
        if not nome:
            emails_nao_mapeados[email] += 1
            continue

        commits_por_membro[nome] += 1

        arquivos = (
            git(
                "diff-tree",
                "--root",
                "--no-commit-id",
                "--name-only",
                "-r",
                "--diff-filter=AM",
                commit,
            )
            .decode(errors="replace")
            .splitlines()
        )

        for arquivo in arquivos:
            if not arquivo.endswith(".java"):
                continue

            try:
                codigo = git("show", f"{commit}:{arquivo}").decode(
                    "utf-8", errors="replace"
                )
                diff = git(
                    "show",
                    "--format=",
                    "--no-ext-diff",
                    "--unified=0",
                    commit,
                    "--",
                    arquivo,
                ).decode("utf-8", errors="replace")

                validas = linhas_com_codigo(codigo)
                adicionadas = linhas_adicionadas(diff)
                linhas_por_membro[nome] += len(validas & adicionadas)

            except subprocess.CalledProcessError:
                continue

    return linhas_por_membro, commits_por_membro, emails_nao_mapeados


def barra_progresso(porcentagem: float, largura: int = 16) -> str:
    preenchido = round((porcentagem / 100) * largura)
    return "█" * preenchido + "░" * (largura - preenchido)


def gerar_markdown(
    linhas_por_membro: Counter,
    commits_por_membro: Counter,
    emails_nao_mapeados: Counter,
    ref: str,
) -> str:
    fuso_brt = timezone(timedelta(hours=-3))
    agora = datetime.now(fuso_brt).strftime("%d/%m/%Y às %H:%M (BRT)")
    total_linhas = sum(linhas_por_membro.values())
    escopo = "todas as branches (`--all`)" if ref == "--all" else f"branch `{ref}`"

    ordenados = sorted(
        MEMBROS.keys(),
        key=lambda nome: (linhas_por_membro[nome], commits_por_membro[nome]),
        reverse=True,
    )

    linhas_md = [
        MARCADOR_INICIO,
        "## 📊 Métricas de Contribuição (Java)",
        "",
        f"- **Escopo analisado:** {escopo} (desde o commit inicial)",
        f"- **Total de linhas válidas de código:** `{total_linhas}`",
        f"- **Última atualização:** {agora}",
        "",
        "| Integrante | Commits | Linhas Válidas | Participação | Progresso |",
        "| :--- | :---: | :---: | :---: | :--- |",
    ]

    for nome in ordenados:
        qtd = linhas_por_membro[nome]
        qtd_commits = commits_por_membro[nome]
        pct = (qtd / total_linhas * 100) if total_linhas else 0.0
        barra = barra_progresso(pct)
        linhas_md.append(
            f"| **{nome}** | {qtd_commits} | `{qtd}` | **{pct:.2f}%** | `{barra}` |"
        )

    if emails_nao_mapeados:
        ignorados = ", ".join(
            f"`{email}` ({qtd} commits)"
            for email, qtd in emails_nao_mapeados.most_common()
        )
        linhas_md.extend(
            [
                "",
                f"> ⚠️ **E-mails fora do mapeamento detectados no histórico:** {ignorados}. "
                "Adicione-os em `MEMBROS` no arquivo `scripts/contribuicao.py` caso pertençam à equipe.",
            ]
        )

    linhas_md.append(MARCADOR_FIM)
    return "\n".join(linhas_md)


def atualizar_readme(caminho_readme: Path, bloco_markdown: str) -> None:
    if caminho_readme.exists():
        conteudo = caminho_readme.read_text(encoding="utf-8")
    else:
        conteudo = "# POO 2026.2 — Grupo 4\n"

    padrao = re.compile(
        rf"{re.escape(MARCADOR_INICIO)}.*?{re.escape(MARCADOR_FIM)}",
        flags=re.DOTALL,
    )

    if padrao.search(conteudo):
        novo_conteudo = padrao.sub(lambda _: bloco_markdown, conteudo)
    else:
        separador = "\n\n" if not conteudo.endswith("\n\n") else ""
        novo_conteudo = f"{conteudo.rstrip()}{separador}{bloco_markdown}\n"

    caminho_readme.write_text(novo_conteudo, encoding="utf-8")


def main() -> None:
    parser = argparse.ArgumentParser(
        description="Calcula métricas de contribuição em arquivos .java."
    )
    parser.add_argument(
        "--ref",
        default="--all",
        help="Referência Git a analisar (padrão: --all; use 'main' para apenas a branch principal).",
    )
    parser.add_argument(
        "--readme",
        type=Path,
        default=None,
        help="Caminho do arquivo Markdown a atualizar (ex: README.md).",
    )
    args = parser.parse_args()

    linhas, commits, nao_mapeados = calcular_metricas(ref=args.ref)
    relatorio_md = gerar_markdown(linhas, commits, nao_mapeados, ref=args.ref)

    if args.readme:
        atualizar_readme(args.readme, relatorio_md)

    print(relatorio_md)


if __name__ == "__main__":
    main()