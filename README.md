# Trabalho 4 — Arquitetura MVVM — PogVideo

Continuação do Trabalho 3. A reorganização mantém as telas, as tabelas `canais` e `videos`, o nome do banco `pogvideo_database` e o fluxo de CRUD já existente.

## O que mudou em relação ao Trabalho 3?

Antes os Composables consultavam DAOs e executavam corrotinas para buscar novamente a lista depois de cada alteração. Agora os DAOs expõem `Flow<List<...>>`; os Repositories são os únicos que consultam os DAOs; os ViewModels coletam os fluxos e expõem `StateFlow` dos UiStates; e as telas chamam ações do ViewModel.

## Pacotes

- `ui`: telas Compose, formulários e componentes visuais.
- `data`: duas Entities Room (`Class_Canal` e `Class_video`), `CanalDAO`, `VideoDAO` e `AppDatabase`, além do modelo de playlist.
- `data.repository`: `CanalRepository`, `VideoRepository`, `DadosIniciaisRepository`.
- `viewmodel`: `CanalViewModel`, `VideoViewModel`, seus UiStates e suas Factories.

## Por que Repository?

Centraliza as consultas e alterações em uma camada de acesso aos dados, evitando que cada tela conheça o DAO. Uma mudança futura na origem dos dados exigiria alteração centralizada.

## Por que esses campos de UiState?

- `CanalUiState`: lista reativa de canais, campos nome/identificador/descrição, visibilidade do formulário, loading e mensagem de erro.
- `VideoUiState`: lista reativa de vídeos, campos de cadastro e edição (título, visualizações, curtidas, descrição, comentário), modo de edição, visibilidade do formulário, loading e mensagem de erro.

Esses valores são modificados pelos ViewModels com `MutableStateFlow` privado e observados pelas telas por `StateFlow` público. Estados puramente visuais sem regra de negócio, como expandir texto, continuam locais na UI.

## Funcionalidades conservadas

Listar, cadastrar, alterar inscrição e excluir canal; listar, cadastrar, editar e excluir vídeo; navegar entre início, inscrições, canal, vídeo, você e playlist; persistência Room. Ao excluir um canal, o ViewModel solicita primeiro a remoção dos seus vídeos aos Repositories.

## Problemas e decisões durante a reorganização

No código anterior, formulários e lógica de cadastro ainda estavam em Composables. Foi necessário mover os campos, validação e montagem dos objetos para UiState/ViewModel. A exclusão em cascata foi retirada do Composable de inscrições e centralizada no CanalViewModel. Optou-se por manter o uso de `collectAsState()`, permitido no enunciado, para não adicionar exigência de dependência de lifecycle nas telas.

## Execução e validação — pendente

Este pacote contém arquivos Kotlin e uma configuração Gradle/Manifest montada para entrega, **mas não houve compilação do aplicativo ou execução em emulador** neste ambiente. O ZIP original do Trabalho 3 não continha Gradle ou AndroidManifest; portanto esta configuração foi reconstruída, não copiada do projeto original. É necessário abrir e sincronizar em um ambiente com Android SDK para confirmar funcionamento. Não há vídeo de teste, capturas de tela ou depoimentos verificados do trio: anexar evidências reais após testar, se o professor exigir.

## Checklist real para demonstração

- [ ] Abrir e sincronizar o projeto no Android Studio.
- [ ] Inserir, listar, editar e excluir vídeo; criar, alternar inscrição e excluir canal.
- [ ] Fechar e reabrir para validar persistência.
- [ ] Gravar o vídeo curto do CRUD e prints da organização de pacotes.
- [ ] Documentar dificuldades de cada integrante do trio e soluções reais.

## Entrega

O professor pediu o **mesmo repositório do Trabalho 3**, com os arquivos reorganizados, e apresentação em sala. Não criar um repositório novo para substituir o original. Os arquivos deste ZIP devem ser integrados ao repositório existente para cumprir esse requisito.

## Link do vídeo de teste (Youtube)

https://youtu.be/uigRcrZPzKE
