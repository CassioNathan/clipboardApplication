# 📋 Clipboard – Documentação
<img width="898" height="752" alt="image" src="https://github.com/user-attachments/assets/4b16268d-959f-48a3-b241-38747a8e34be" />

**Versão:** 0.0.1  
**Data de criação:** 01/08/2025  
**Última atualização:** 08/09/2025  
**Autor:** Cassio Nathan  

---

## 1. Introdução
O **Clipboard** é uma aplicação desenvolvida para compartilhar rapidamente a área de transferência (**clipboard**) entre dispositivos.  

O objetivo é tornar o processo simples e ágil, permitindo que **textos**, **imagens** ou **pequenos arquivos** sejam transferidos de um dispositivo para outro sem complicações e sem cabos.

---

## 2. Solução da Aplicação

- **Área de transferência compartilhada:** possibilita enviar e receber conteúdos entre dispositivos  (até 100 megas cada arquivo).
- **Interface web:** servidor possui uma página HTML que permite interagir com os itens copiados e enviar manualmente textos e pequenos arquivos.
- **Interface para outros usuario:** possibilida compartilhar itens especificos com outros usuarios, assim, eles podem acessar o conteudo de forma isolada e mantendo sua privacidade
- **Extensão VS Code:** abre um WebView que direciona o usuário para a página web, facilitando o acesso dentro do editor.
- **Histórico de itens:** últimos **5 itens copiados** (textos, imagens ou arquivos) ficam salvos e reutilizáveis na interface web.
- **Integração com a barra do Windows:** ícone na aba de itens ocultos do windows para sempre estar disponivel.
- **Atalhos rápidos:** permite abrir a página web ou webview instantaneamente e limpar a lista de itens.

---

## 3. Tecnologias Utilizadas

| Componente                     | Tecnologia                                  |
|--------------------------------|----------------------------------------------|
| **Backend**                    | Spring Boot                                 |
| **Frontend Web**               | HTML / CSS / JavaScript                     |
| **Extensão VS Code**           | VSC Extension                               |
| **Integração com clipboard**   | Java nativo (verificação a cada 1s)         |

---

## 4. Uso

1. Rodar o servidor local.  
2. Acessar a interface via navegador ou WebView.
   
OBS: 
Tudo o que for manipulado será salvo em memoria no servidor
Sempre que uma imagem ou texto for copiado no server, automaticamente será adicionado ao clipboard
Sempre que algo for enviado para o clipboard pela rela interface web ou webview será adicionado automaticamente ao clipboard do server, possibilitando
um Control + V rapido 

---

## 5. Histórico de Versões

| Versão | Data       | Mudanças principais        | Autor          |
|--------|------------|----------------------------|----------------|
| 0.0.1  | 01/08/2025 | Criação inicial do projeto | Cassio Nathan  |

---
