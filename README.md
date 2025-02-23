# Matching
Foi feita a execuçaõ do caso base e todos os casos bônus.

## Tecnologias e como executar
Este projeto foi desenvolvido em Java utilizando apenas bibliotecas padrão. Para executar a aplicação, siga os passos abaixo:
```
git clone https://github.com/Giovanni-Rossi/Exercicio_programa-MorganStanley

cd src

javac App.java

java App
```

## Divisão de classes

A aplicação segue o princípio de separação de responsabilidades:

 - App: Classe principal responsável pela interação com o usuário, leitura de comandos e delegação das operações para o MatchingEngine.
 - MatchingEngine: Centraliza a lógica de processamento, armazenando e manipulando as ordens.
 - Limit: Classe base que modela as ordens limit.
 - Peg: Herdeira de Limit, especializando o comportamento para ordens do tipo peg.
 - Market: Representa as ordens de mercado e contém a lógica para execução imediata com base nas ordens disponíveis.
 - PriceWrapper: Utilizada como atributo em outras classes (como em MatchingEngine e nas ordens peg) para encapsular e atualizar dinamicamente os melhores preços (bid e offer).
   
## Complexidade

As operações de matching, cancelamento e alteração de ordens percorrem as listas de ordens, resultando em uma complexidade linear (O(n)) em relação ao número de ordens.

## Execução:
Segue o link para execução dos casos específicados.

### - Caso base:
[![Caso base](https://img.youtube.com/vi/I0ate1AAwlM/0.jpg)](https://youtu.be/I0ate1AAwlM)


### - Bônus 1 e 2:
Para verificar o livro:
```
book
```
[![Bonus 1 e 2](https://img.youtube.com/vi/JlMvkEO5BJA/0.jpg)](https://youtu.be/JlMvkEO5BJA)

### - Bônus 3:
[![Bonus 3](https://img.youtube.com/vi/WoMR5AKrCWs/0.jpg)](https://youtu.be/WoMR5AKrCWs)

### - Bônus 4:
[![bonus 4](https://img.youtube.com/vi/y2e8XbSfWEg/0.jpg)](https://youtu.be/y2e8XbSfWEg)

### - Bônus 5:
[![Bonus 5](https://img.youtube.com/vi/z1njqRs25Gc/0.jpg)](https://youtu.be/z1njqRs25Gc)

