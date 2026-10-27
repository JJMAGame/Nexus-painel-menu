Agora que eu parei pra pensar, eu preciso fazer que o programa fique funcionando em enquanto ta em funcionamento então terei que aplicar um loop e so finalizar quando o cliente quiser que finaliza![[Pasted image 20261026194400.png]]

![[Recording 20261026212631.m4a]]

## Correção de bug

Vou percebido que o GerarDados que ele não estava respeitando as linhas de cada conta, então eu fiz alteração no código para ele ler todo o json para depois aplicar

```java fold:Fragmento-da-correção
 List<Usuario> usuarios = JsonUtil.lerUsuarios();
```