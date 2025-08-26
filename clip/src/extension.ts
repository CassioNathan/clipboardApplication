import * as http from "http";
import * as vscode from "vscode";

export function activate(context: vscode.ExtensionContext) {
  const provider = new ClipViewProvider();

  context.subscriptions.push(
    vscode.window.registerWebviewViewProvider("clipView", provider)
  );
}

class ClipViewProvider implements vscode.WebviewViewProvider {
  private _view?: vscode.WebviewView;

  resolveWebviewView(
    webviewView: vscode.WebviewView,
    _context: vscode.WebviewViewResolveContext,
    _token: vscode.CancellationToken
  ) {
    this._view = webviewView;

    webviewView.webview.options = {
      enableScripts: true,
    };

    // Faz a requisição HTTP e renderiza o HTML // adicione seu ip na requisição
    http.get("http://localhost:4002/clipboardArea", (res) => {
        let data = "";

        res.on("data", (chunk) => (data += chunk));
        res.on("end", () => {
          webviewView.webview.html = data;
        });
      })
      .on("error", (err) => {
        webviewView.webview.html = `   <html><body>
            <h2>Erro ao carregar HTML:</h2>
            <pre>${err.message}</pre>
        </body></html>`;
      });
  }
}
