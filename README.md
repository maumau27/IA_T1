# IA_T1
Trabalho 1 de IA PUC 2016-2, chapeuzinho vermelho

# Principais classes e métodos

## Classe: Celula
Atua como uma estrutura de dados.

### Construtor:
**Celula( int x , int y , int terreno , float custo )**
### Variaveis Publicas:
int x = posicao X no mapa
int y = posicao Y no mapa
int terreno = tipo de terreno ( floresta, clareira, cidade, etc )
float custo = custo em tempo para entrar na celula

## Classe: Mapa
Carrega o arquivo de mapa, armazena funções e operações sobre o mapa.

### Construtor:
**new Mapa()**
Usa o arquivo "mapa.txt" na raiz do projeto como base

**new Mapa(String)**
Usa o arquivo com o caminho String como base do mapa

### Metodos:
**int ObterTerreno( x , y )**
Retorna o tipo de terreno em X , Y
Sendo:
-1) x e y fora do mapa
1) trilha limpa
2) trilha com galhos
3) floresta densa
4) clareira
5) vilareijo
6) casa da vovo

**float ObterTempo( x , y )**
Retorna o tempo para entrar em x,y vindo de uma casa adjacente
Ou -1 para X e Y inexistente

**Celula ObterInicio()**
Retorna uma classe Celula que é a celula inicial do mapa ( vila )
Obs.: A classe celula esta descrita acima, tem x,y,terreno,custo

**Celula ObterFim()**
Retorna uma classe Celula que é a celula final do mapa ( casa da vovo )
Obs.: A classe celula esta descrita acima, tem x,y,terreno,custo

**int[2] ObterTamanho()** 
Retona um vetor onde
[0] = tamanho X do mapa ( em celulas )
[1] = tamanho Y do mapa ( em celulas )

**ArrayList<Celula> ObterVizinhos( Celula cel )**
Retorna uma lista de celulas vizinhas a celula CEL

**int ObterQuantidadeClareiras()**
Retorna quantas clareiras existem no mapa

**boolean PertenceAoMapa( Celula cel )**
**Celula ObterCelula( int x , int y )**
Checa se uma celula ou coordenada pertence ao mapa

## Classe: Encontros
Carrega o arquivo de cestas e encontros, calcula a melhor distribuição para uma quantidade definida de encontros.

### Construtor:
**public Encontros()**
Usa o arquivo "encontros.txt" e "cesta.txt" na raiz do projeto como base


### Metodos:
**void CalcularEncontros( int qtd )**
Calcula a melhor distribuição de doces para uma quantidade especifica de encontros.
Nao retorna nada, para obter o custo de um encontro, use o metodo **ObterCustoEncontro**

**double ObterCustoEncontro( int numeroDoEncontro )**
Retorna o custo de um encontro especifico.

**void ImprimirEncontros()**
Imprime como ficou a distribuição de doces por encontro, os doces são exibidos por um ID ( ordem de leitura no arquivo ).



**
