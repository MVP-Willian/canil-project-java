# canil-project-java


🐶 Sistema de Gerenciamento de Canil

Sistema desenvolvido em Java para gerenciamento de um canil, permitindo o controle de animais, usuários, solicitações de adoção e resgate, utilizando banco de dados SQLite e interface gráfica com Swing.

🚀 Funcionalidades

Cadastro e login de usuários

Controle de permissões (usuário e administrador)

Cadastro e listagem de animais

Solicitações de adoção

Solicitações de resgate

Aprovação e gerenciamento de solicitações

Persistência de dados com SQLite

🧱 Arquitetura do Projeto

O sistema segue uma arquitetura em camadas:

model        → Entidades do sistema
persistence  → Acesso a dados (DAO + SQLite)
services     → Regras de negócio
view         → Interface gráfica (Swing)
session      → Controle de sessão do usuário


Essa separação facilita manutenção, organização e escalabilidade do sistema.

🗄️ Banco de Dados

Banco: SQLite

Arquivo: canil.db

Inicialização automática via DatabaseInitializer

🛠️ Tecnologias Utilizadas

Java

Swing

SQLite

Maven

▶️ Como Executar

Clone o repositório

Abra o projeto em uma IDE Java (IntelliJ, Eclipse)

Execute a classe Main_terminal.java

O banco de dados será criado automaticamente

📌 Observações

Projeto desenvolvido com foco em aprendizado de arquitetura de software, organização em camadas e boas práticas em Java.
