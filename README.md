# 📋 Clipboard – Documentação
<img width="902" height="618" alt="image" src="https://github.com/user-attachments/assets/fbc1e16d-153a-43d8-a610-d225944397bd" />


**Versão:** 0.0.1  
**Data de criação:** 01/08/2025  
**Última atualização:** 25/08/2025  
**Autor:** Cassio Nathan  

---

## 1. Introdução
O **Clipboard** é uma aplicação desenvolvida para compartilhar rapidamente a área de transferência (**clipboard**) entre dispositivos **Windows** e **Mac**.  

O objetivo é tornar o processo simples e ágil, permitindo que **textos** e **imagens** e **pequenos arquivos** sejam transferidos de um dispositivo para outro sem complicações.

---

## 2. Problema Inicial
Era necessário compartilhar conteúdos entre **Windows** e **Mac** de forma **rápida e prática**, sem depender de métodos complexos ou softwares pesados.

---

## 3. Solução da Aplicação

- **Área de transferência compartilhada:** possibilita enviar e receber conteúdos entre dispositivos.
- **Interface web:** servidor possui uma página HTML que permite interagir com os itens copiados e enviar manualmente textos.
- **Extensão VS Code:** abre um WebView que direciona o usuário para a página web, facilitando o acesso dentro do editor.
- **Histórico de itens:** últimos **5 itens copiados** (textos ou imagens) ficam salvos e reutilizáveis na interface web.
- **Integração com a barra do Windows:** ícone na aba de itens ocultos do windows para sempre estar disponivel.
- **Atalho rápido:** permite abrir a página web ou webview instantaneamente, alem de disponibilizar um qr code para leitura em dispositivos moveis.
- **Enviar arquivos:** possibilidade de enviar pequenos arquivos (até 100 megas)e.

---

## 4. Tecnologias Utilizadas

| Componente                     | Tecnologia                                  |
|--------------------------------|----------------------------------------------|
| **Backend**                    | Spring Boot                                 |
| **Frontend Web**               | HTML / CSS / JavaScript                     |
| **Extensão VS Code**           | VSC Extension                               |
| **Integração com clipboard**   | Java nativo (verificação a cada 1s)         |

---

## 5. Uso

1. Rodar o servidor local.  
2. Acessar a interface via navegador ou WebView no VS Code.  
3. Copiar e colar textos/imagens e pequenos arquivos entre dispositivos.  

---

## 6. Histórico de Versões

| Versão | Data       | Mudanças principais        | Autor          |
|--------|------------|----------------------------|----------------|
| 0.0.1  | 01/08/2025 | Criação inicial do projeto | Cassio Nathan  |

---
