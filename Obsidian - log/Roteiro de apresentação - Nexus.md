# Roteiro de apresentação — Nexus (5 min)

**Duração:** 5 minutos · **Formato:** Login (main) → Inquilino → Admin
**Foco:** mostrar, grupo a grupo, quais conceitos de POO foram respeitados
**Stack:** Java 21, Maven, Gson, JUnit · **Autor:** Melqui

> [!tip] Regra de ouro
> O código é o slide. Em cada grupo, mostre **1 arquivo** e diga **1 conceito respeitado**. Não explique o menu inteiro.

## Cronômetro

| Tempo | Grupo | O que mostrar |
|---|---|---|
| 0:00–0:15 | Abertura | 1 frase sobre o sistema |
| 0:15–1:25 | **Login (main) + Usuario** | `main.java`, `Usuario.java` |
| 1:25–2:25 | **Inquilino** | `inquilino.java`, `Cobranca.getSituacao()` |
| 2:25–4:30 | **Admin** | `admin.java`, `Lancamento`/`Cobranca`/`Despesa`, `RegraDeEncargos` |
| 4:30–4:45 | Extras | tabela rápida |
| 4:45–5:00 | Fechamento | resumo dos 4 pilares |

---

## 0:00–0:15 — Abertura

> [!tip] Fala
> "O **Nexus** é um sistema de gestão de aluguéis em **Java 21 + Maven + Gson**. Vou percorrer o fluxo **Login → Inquilino → Admin** e, em cada etapa, mostrar quais conceitos de POO foram respeitados."

---

## 0:15–1:25 — Grupo 1: Login (`main.java`) + `Usuario.java`

**Abrir:** `main.java` e `Usuario.java`.

> [!tip] Fala
> "O `main` faz o login e decide o que abrir. Repare que ele **não** mexe em banco nem em menu: ele só conversa com `Usuario`."

**O que foi respeitado aqui:**

| Conceito | Onde aparece |
|---|---|
| **Abstração** | O login trabalha com `Usuario`, o tipo comum — sem saber ainda se é admin ou inquilino. |
| **Encapsulamento** | Em `Usuario`: campos `private`; setters validam nome, e-mail (regex), senha (6–12) e CPF (11 dígitos); `equals`/`hashCode` por CPF. |
| **Polimorfismo** | `main` decide em execução com `instanceof admin admin` / `instanceof inquilino inquilino`; o `JsonUtil.UsuarioAdapter` reconstrói a subclasse pelo campo `tipo`. |
| **Herança (base)** | `admin` e `inquilino` **estendem** `Usuario` — a hierarquia que os próximos grupos usam. |

> [!important] Ponto para destacar
> `Usuario` já entrega de graça o **encapsulamento** (ninguém deixa o objeto inválido) e é a **base do polimorfismo** (o `main` e o `JsonUtil` tratam tipos diferentes pelo mesmo tipo `Usuario`).

---

## 1:25–2:25 — Grupo 2: Inquilino (`inquilino.java`)

**Abrir:** `inquilino.java` e `Cobranca.getSituacao()`.

> [!tip] Fala
> "O inquilino não reimplementa nada de usuário: ele **herda** nome, e-mail, CPF e `autenticar`. E só enxerga o que é dele."

**O que foi respeitado aqui:**

| Conceito | Onde aparece |
|---|---|
| **Herança** | `inquilino extends Usuario` — reaproveita identidade e `autenticar`. |
| **Encapsulamento** | As consultas filtram por `getCpf()`; o menu só lê pelos getters. |
| **Abstração** | Usa `Cobranca`/`ProcessoLocacao`/`EtapaLocacao` sem conhecer os detalhes internos. |
| **Polimorfismo** | `Cobranca.getSituacao()` é a sobrescrita de `Lancamento.getSituacao()` — reage ao estado (Em dia / A vencer / Atrasado). |

---

## 2:25–4:30 — Grupo 3: Admin (`admin.java` + menus + domínio)

**Abrir:** `admin.java`, `Lancamento.java`, `Cobranca.java`/`Despesa.java`, `RegraDeEncargos.java`/`EncargosContratuais.java`.

> [!tip] Fala
> "O admin herda de Usuario e delega cada área para um menu. No domínio, a POO aparece com força: `Lancamento` abstrata, `Cobranca`/`Despesa` especializadas e a regra de encargos plugável."

**O que foi respeitado aqui:**

| Conceito | Onde aparece |
|---|---|
| **Herança** | `admin extends Usuario`; `Cobranca`/`Despesa extends Lancamento` (herdam id, valor, vencimento, pagamento). |
| **Polimorfismo** | `getDescricao()`/`getSituacao()` sobrescritos; `RegraDeEncargos` injetada em `Cobranca.getMulta(...)` (Strategy). |
| **Abstração** | `Lancamento` abstrata e `RegraDeEncargos` interface. |
| **Encapsulamento** | `setValor` rejeita ≤ 0; `marcarComoPago` é `protected`. |
| **Composição** | `MenuLocacoes` tem `LocacaoService`; `MenuCobrancas` tem `RegraDeEncargos`. |

> [!important] Frase de efeito
> "Trocar a multa é criar outra `RegraDeEncargos` e injetar — `Cobranca` não muda."

---

## 4:30–4:45 — Extras (só se sobrar tempo)

| Conceito | Onde |
|---|---|
| Construtor padrão/parametrizado | `Usuario()`, `Cobranca(...)` |
| `this` e `super` | `admin`/`inquilino`, `Cobranca`/`Despesa` |
| `static`/`final` | `JsonUtil.ARQUIVO_USUARIOS`, `EncargosContratuais.PERCENTUAL_MULTA` |
| Interface + `default` | `RegraDeEncargos.totalComEncargos` |
| Enum com métodos | `EtapaLocacao` (`proxima`, `anterior`, `pendenciaPadrao`) |
| Generics / streams | `<T> List<T> ler(...)`, `stream().mapToInt().max()` |
| Exceções | `IllegalArgumentException`, `IllegalStateException` |

---

## 4:45–5:00 — Fechamento

> [!tip] Fala
> "Resumindo: **Login** mostrou encapsulamento e a base do polimorfismo em `Usuario`; **Inquilino** mostrou herança e abstração no uso; **Admin** fechou com herança, polimorfismo e abstração no domínio. Obrigado — perguntas?"

---

## Cola de demo (opcional, 30 s)

- Admin: `admin@admin.com` / `admin123` → **6 – Processos de Locação** → **2 – Ver painel**.
- Inquilino: `jorgealves@hotmail.com` / `jorge2004` → **3 – Minhas faturas** (cobrança 12/2026 "Em dia").

> [!warning] Não crie dados novos durante a apresentação
> Se for demonstrar criação, use uma cópia dos `.json`.

## Como rodar

```shell
mvn -f sistema-usuarios clean package
java -cp sistema-usuarios/target/classes com.nexusmaven.main
```

## Perguntas prováveis

1. **Onde está o polimorfismo?** → `getDescricao/getSituacao` + `RegraDeEncargos` injetada + `instanceof` no `main`.
2. **Por que herança em admin/inquilino?** → ambos são usuários e compartilham `autenticar`.
3. **Por que JSON?** → simplicidade; persistência isolada em `JsonUtil`.
4. **Senha protegida?** → não (didático); evolução: hash + salt.

---

#nexus #java #poo #apresentacao
