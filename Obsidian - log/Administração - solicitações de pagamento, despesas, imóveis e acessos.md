Autor: Melqui

Novas funções no menu do Administrador, cada uma em um arquivo .java próprio para não gerar conflito de merge com o que já existe.

## Menu do Administrador

```
1 - Gerar novos dados (Usuários/Imóveis)
2 - Solicitações de pagamento (gerar, filtrar e receber)   <- substitui o antigo "Registrar pagamentos"
3 - Ver todas as receitas                                   <- mantido como estava (financeiro.json)
4 - Despesas da Fundação
5 - Imóveis
6 - Acessos (funcionários e inquilinos)
0 - Sair
```

Único arquivo existente alterado: `admin.java` (opções do menu e remoção do método `registrarPagamento`, substituído pelo `MenuCobrancas`).

## 1. Solicitações de pagamento (`MenuCobrancas`, `Cobranca`)

- O administrador escolhe o inquilino e o imóvel **da lista**; o valor vem do cadastro do imóvel (não é digitado).
- Mês de referência (mm/aaaa) e dia do vencimento (padrão: dia 10).
- Não deixa criar duas solicitações do mesmo mês para o mesmo inquilino e imóvel.
- Filtros: **Em dia** (pagas), **A vencer** e **Atrasadas**.
- Atrasadas mostram dias de atraso, multa, juros e o total do dia.
- Registrar pagamento: escolhe a solicitação, mostra o cálculo, pede a forma (Dinheiro, Pix, Transferência, Depósito), confirma e imprime o **recibo numerado**.
- Gravado em `cobrancas.json`.

Cálculo (`EncargosContratuais`): multa de 10% + juros de 1% ao mês proporcionais aos dias.
Ex.: aluguel de R$ 1.000,00 com 30 dias de atraso = R$ 1.000,00 + R$ 100,00 + R$ 10,00 = **R$ 1.110,00**.

## 2. Despesas da Fundação (`MenuDespesas`, `Despesa`)

- Lançar: descrição, categoria (Água, Energia, Internet, Telefone, Combustível, Manutenção, Limpeza, Folha, Contabilidade, Vigilância, Outras), valor e vencimento.
- Toda despesa nasce **Aguardando aprovação** → aprovada vira **A pagar** → paga vira **Pago**. Não dá para pagar sem aprovar.
- Filtros: Pago, A pagar e Aguardando aprovação. Despesa a pagar vencida aparece como "A pagar (vencida)".
- Guarda quem lançou e quem aprovou. Gravado em `despesas.json`.

## 3. Lista de imóveis (`MenuImoveis`)

- Situação de cada imóvel: **Alugado** (já tem solicitação de pagamento) ou **Disponível**.
- Filtros: todos, só disponíveis, só alugados; busca por nome ou localização; ordenação pelo valor do aluguel.
- Resumo: quantidade, alugados, disponíveis, soma e média dos aluguéis.

## 4. Acessos (`MenuAcessos`)

- Lista de **funcionários** (contas que não são de inquilino; hoje, os administradores) e de **inquilinos**, com CPF mascarado (***.456.789-**).
- Busca por nome, e-mail ou CPF.
- **Cadastrar novo acesso** chama o `GerarDados.executar(scanner)` que já existe, sem alterar o `GerarDados.java` (não dupliquei a regra de criação de usuário).
- Remover acesso com confirmação; não deixa remover o próprio acesso nem o último administrador.

## Apoio

- `Moeda`: mostra valores como "R$ 1.500,50" e aceita digitar 1500,50 / 1500.50 / 1.500,50.
- `Datas`: datas dd/mm/aaaa e meses mm/aaaa, recusando datas inexistentes (31/02).
- `Lancamento` (classe abstrata): base comum de `Cobranca` e `Despesa` (valor, vencimento, pagamento, atraso).
- `RegraDeEncargos` (interface) + `EncargosContratuais`: a regra de multa e juros pode ser trocada sem mexer nas cobranças.
- `EntradaTexto`: perguntas, escolha em lista e confirmação S/N usadas pelos menus novos.

## Os 4 pilares

- **Abstração:** `Lancamento` e `RegraDeEncargos`.
- **Encapsulamento:** atributos privados com validação; status só muda por métodos (`aprovar`, `registrarPagamento`).
- **Herança:** `Cobranca` e `Despesa` estendem `Lancamento`.
- **Polimorfismo:** `getSituacao()` e `getDescricao()` têm uma versão em cada lançamento; `RegraDeEncargos` aceita outras implementações.

## Testes

- `MoedaEDatasTest`, `CobrancaEDespesaTest` e `MenusDeAdministracaoTest`: **20 testes, todos passando**.
- A bateria antiga tem **26 testes que já falhavam antes destas mudanças** (eles ainda chamam os construtores sem CPF e sem aluguel). Nenhuma falha nova.

## Pontos para o grupo

- `imoveis.json` do repositório continua com os acentos quebrados. Agora os menus mostram "o arquivo precisa estar em UTF-8" em vez de fechar o programa, mas o arquivo precisa ser corrigido ou recriado.
- O antigo "Ver todas as receitas" (financeiro.json) foi mantido. As novas receitas ficam nas Solicitações de pagamento (filtro Em dia).
- Quando existir o perfil Secretaria, só o Administrador deve aprovar despesas (hoje o próprio administrador lança e aprova).
- "CPF vinculado" na lista de imóveis é o campo `cpfProprietario`; vale confirmar o que ele representa.
