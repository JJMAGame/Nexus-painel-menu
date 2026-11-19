# Roteiro de apresentação — Nexus (5 min)

**Duração:** 5 minutos · **Foco:** os 4 pilares da POO · **Stack:** Java 21, Maven, Gson, JUnit
**Autor:** Melqui

> [!tip] Regra de ouro
> O código é o slide. Cada pilar tem **1 frase + 1 arquivo para abrir**. Não explicar o menu inteiro.

## Mapa rápido: pilar → onde mostrar

| Pilar | Abrir no código |
|---|---|
| Abstração | `Lancamento.java` (classe `abstract`) e `RegraDeEncargos.java` (interface) |
| Encapsulamento | `Usuario.java` (campos `private` + setters validando) |
| Herança | `admin.java`/`inquilino.java` e `Cobranca.java`/`Despesa.java` |
| Polimorfismo | `Cobranca.getSituacao()` / `Despesa.getSituacao()` e `EncargosContratuais` |

## Cronômetro

| Tempo | Bloco | Ação |
|---|---|---|
| 0:00–0:20 | Abertura | 1 frase sobre o sistema |
| 0:20–0:50 | Arquitetura | slide de camadas (30 s) |
| 0:50–3:50 | **4 pilares** | ~45 s por pilar |
| 3:50–4:30 | Outros conceitos | tabela rápida |
| 4:30–5:00 | Encerramento | frase de efeito + perguntas |

---

## 0:00–0:20 — Abertura

> [!tip] Fala
> "O **Nexus** é um sistema de gestão de aluguéis em **Java 21 + Maven + Gson**, com dois perfis: administrador e inquilino. Hoje eu vou mostrar como os **4 pilares da POO** aparecem no código."

---

## 0:20–0:50 — Arquitetura (1 slide)

| Camada | Classes |
|---|---|
| Entrada/Saída | `EntradaConsole`, `EntradaTexto`, `Moeda`, `Datas` |
| Persistência | `JsonUtil` |
| Domínio | `Usuario`, `admin`, `inquilino`, `Imovel`, `Lancamento`, `Cobranca`, `Despesa`, `ProcessoLocacao`, `EtapaLocacao` |
| Regras | `RegraDeEncargos`, `EncargosContratuais`, `LocacaoService` |
| Menus | `MenuCobrancas`, `MenuDespesas`, `MenuImoveis`, `MenuAcessos`, `MenuLocacoes` |

> [!tip] Fala
> "Cada camada tem uma responsabilidade. A POO aparece principalmente no **domínio** e nas **regras**."

---

## 0:50–3:50 — Os 4 pilares (o coração)

### 1) Abstração (~45 s)

> [!tip] Fala
> "Abstração é representar só o essencial. `Lancamento` é abstrata e representa 'tudo que tem valor, vencimento e pagamento' — sem saber se é receita ou despesa."

**Abrir:**
- `Lancamento.java` → `public abstract class Lancamento`; métodos abstratos `getDescricao()` e `getSituacao()`; comportamento comum `estaPago`, `estaVencido`, `diasDeAtraso`.
- `RegraDeEncargos.java` → interface que abstrai "como calcular multa e juros", sem fixar percentuais.

**Por que importa:** o menu trabalha com `Lancamento`/`Usuario` sem `if` por subtipo.

### 2) Encapsulamento (~45 s)

> [!tip] Fala
> "Encapsulamento é o objeto ser o único dono do seu estado. Ninguém de fora consegue deixá-lo inválido."

**Abrir:**
- `Usuario.java` → campos `private`; setters validam nome, e-mail (regex), senha (6–12) e CPF (11 dígitos).
- `Lancamento.java` → `setValor` rejeita ≤ 0; `marcarComoPago` é `protected`.
- `Moeda`, `Datas`, `EntradaConsole` → construtor privado.

**Frase:** "O objeto protege suas invariantes na borda."

### 3) Herança (~45 s)

> [!tip] Fala
> "Herança é reutilizar o comum e especializar o que muda."

**Abrir:**
- `admin extends Usuario` e `inquilino extends Usuario` → herdam nome, e-mail, senha, CPF e `autenticar`.
- `Cobranca extends Lancamento` e `Despesa extends Lancamento` → herdam `id`, `valor`, `vencimento`, `dataPagamento`.
- `JsonUtil.UsuarioAdapter` reconstrói a subclasse pelo campo `tipo`.

**Por que importa:** uma correção em `Lancamento` vale para receita e despesa ao mesmo tempo.

### 4) Polimorfismo (~45 s)

> [!tip] Fala
> "Polimorfismo é a mesma mensagem com comportamentos diferentes: `getSituacao()` faz coisas diferentes em Cobrança e em Despesa."

**Abrir:**
- `Cobranca.getDescricao()` / `getSituacao()` e `Despesa.getDescricao()` / `getSituacao()`.
- `RegraDeEncargos` + `EncargosContratuais` injetada em `Cobranca.getMulta(hoje, regra)` → padrão **Strategy**.
- `main.java` → `instanceof admin admin` / `instanceof inquilino inquilino` (despacho em execução).

**Frase de efeito:** "Trocar a multa é criar outra `RegraDeEncargos` e injetar — `Cobranca` não muda."

---

## 3:50–4:30 — Outros conceitos de POO (tabela)

| Conceito | Onde |
|---|---|
| Classe e objeto | `new admin(...)`, `new Cobranca(...)` |
| Construtor padrão e parametrizado | `Usuario()`, `Cobranca(...)` |
| `this` e `super` | `admin`/`inquilino`, `Cobranca`/`Despesa` |
| `static` e `final` | `JsonUtil.ARQUIVO_USUARIOS`, `EncargosContratuais.PERCENTUAL_MULTA` |
| Interface + `default` | `RegraDeEncargos.totalComEncargos` |
| Enum com métodos | `EtapaLocacao` (`proxima`, `anterior`, `pendenciaPadrao`) |
| Composição | `MenuLocacoes` tem `LocacaoService` |
| Sobrescrita × sobrecarga | `getSituacao` × `Datas.formatar(LocalDate/YearMonth)` |
| Exceções | `IllegalArgumentException`, `IllegalStateException` |
| Generics | `<T> List<T> ler(...)` |
| Streams/lambdas | `stream().mapToInt().max()`, `removeIf`, `Comparator.comparing` |
| `equals`/`hashCode` | `Usuario` por CPF |

---

## 4:30–5:00 — Encerramento

> [!tip] Fala
> "O Nexus mostra os quatro pilares na prática: **abstração** no `Lancamento` e na `RegraDeEncargos`; **encapsulamento** nos setters e campos privados; **herança** entre `Usuario`/`admin`/`inquilino` e `Lancamento`/`Cobranca`/`Despesa`; e **polimorfismo** em `getSituacao()` e na regra de encargos injetada. Obrigado — perguntas?"

---

## Cola para a demo (30 s, opcional)

**Login admin:** `admin@admin.com` / `admin123` → opção **6 – Processos de Locação** → **2 – Ver painel**.

**Login inquilino:** `jorgealves@hotmail.com` / `jorge2004` → **3 – Minhas faturas** (mostra a cobrança 12/2026 "Em dia").

> [!warning] Não criar dados novos durante a apresentação
> Se precisar demonstrar criação, use uma cópia dos arquivos JSON.

## Como rodar

```shell
# na raiz do repositório (onde ficam os .json)
mvn -f sistema-usuarios clean package
java -cp sistema-usuarios/target/classes com.nexusmaven.main

# testes
cd sistema-usuarios && mvn test
```

## Perguntas prováveis

1. **Onde está o polimorfismo?** → `getDescricao/getSituacao` sobrescritos + `RegraDeEncargos` injetada.
2. **Por que herança em admin/inquilino?** → ambos são usuários e compartilham `autenticar`.
3. **Por que JSON?** → simplicidade; toda a persistência está isolada em `JsonUtil`.
4. **Senha protegida?** → hoje não (didático); evolução: hash + salt.
5. **Como testar sem digitar?** → `ApoioDeTestes` simula teclado; `TemporaryFolder` isola JSON.

## Limitações (se perguntarem)

- Classes `main`, `admin`, `inquilino` fora do padrão PascalCase.
- `abrirMenu` não é polimórfico (há `instanceof` no `main`).
- Campo `tipo` duplicado nas subclasses.
- Senhas em texto puro.

---

#nexus #java #poo #apresentacao
