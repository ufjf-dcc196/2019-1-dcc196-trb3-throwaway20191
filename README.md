Felipe Inhan Matos

201335007

fmatos@ice.ufjf.br

A estrutura de banco de dados segue exatamente a imagem do enunciado do trabalho, criando chaves estrangeiras quando necessário e mantendo os nomes dos campos.

Todos os campos são TEXT, execeto as chaves primárias e estrangeiras e o campo *horas* da tabela Atividade.

Todos os modelos apresentam a função estática *model::fromCursor*, responsável por transformar os dados da posição atual do cursor em um objeto do modelo.

O modelo reflete a estrutura do banco de dados, porém não há referências a outros modelos, por não haver interações complicadas entre os modelos, e também pela apresentação dos dados ser independente na maioria dos casos.

O caso especial, que é o ranking de candidatos, é feito esclusivamente via SQL e não há modelo específico para a operação (exceto uma classe interna para getter/setter). Ver *RankingActivity.queryRanking*.
