import java.io.*;
import java.util.*;

class Vertice {
    int id;
    String rotulo;
    int peso;

    Vertice(int id, String rotulo, int peso) {
        this.id = id;
        this.rotulo = rotulo;
        this.peso = peso;
    }
}

public class GrafoVeiculosApp {
    static int tipoGrafo;
    static Map<Integer, Vertice> vertices = new HashMap<>();
    static Map<Integer, Map<Integer, Integer>> adjacencias = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n===== APLICACAO - ANALISE DE VEICULOS POR GRAFO =====");
            System.out.println("1. Ler dados do arquivo grafo.txt");
            System.out.println("2. Gravar dados no arquivo grafo.txt");
            System.out.println("3. Inserir vertice");
            System.out.println("4. Inserir aresta");
            System.out.println("5. Remover vertice");
            System.out.println("6. Remover aresta");
            System.out.println("7. Mostrar conteudo do arquivo");
            System.out.println("8. Mostrar grafo (lista de adjacencia)");
            System.out.println("9. Verificar conexidade");
            System.out.println("10. Encerrar");
            System.out.print("Escolha uma opcao: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> lerArquivo("grafo.txt");
                case 2 -> gravarArquivo("grafo.txt");
                case 3 -> inserirVertice(scanner);
                case 4 -> inserirAresta(scanner);
                case 5 -> removerVertice(scanner);
                case 6 -> removerAresta(scanner);
                case 7 -> mostrarConteudo();
                case 8 -> mostrarListaAdjacencia();
                case 9 -> verificarConexidade();
                case 10 -> System.out.println("Encerrando...");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 10);

        scanner.close();
    }

    static void lerArquivo(String nomeArquivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
        tipoGrafo = Integer.parseInt(br.readLine());
        int n = Integer.parseInt(br.readLine());

        vertices.clear();
        adjacencias.clear();

        for (int i = 0; i < n; i++) {
            String linha = br.readLine();
            String[] partes = linha.split(" \"");
            int id = Integer.parseInt(partes[0]);
            String rotulo = partes[1].replaceAll("\"", "");
            int peso = Integer.parseInt(partes[2].replaceAll("\"", ""));
            vertices.put(id, new Vertice(id, rotulo, peso));
            adjacencias.put(id, new HashMap<>());
        }

        int m = Integer.parseInt(br.readLine());
        for (int i = 0; i < m; i++) {
            String[] partes = br.readLine().split(" ");
            int origem = Integer.parseInt(partes[0]);
            int destino = Integer.parseInt(partes[1]);
            int peso = Integer.parseInt(partes[2]);
            adjacencias.get(origem).put(destino, peso);
        }

        br.close();
        System.out.println("Arquivo lido com sucesso!");
    }

    static void gravarArquivo(String nomeArquivo) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo));
        bw.write(tipoGrafo + "\n");
        bw.write(vertices.size() + "\n");

        for (Vertice v : vertices.values()) {
            bw.write(v.id + " \"" + v.rotulo + "\" \"" + v.peso + "\"\n");
        }

        int count = 0;
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : adjacencias.entrySet()) {
            count += entry.getValue().size();
        }
        bw.write(count + "\n");

        for (Map.Entry<Integer, Map<Integer, Integer>> entry : adjacencias.entrySet()) {
            for (Map.Entry<Integer, Integer> destino : entry.getValue().entrySet()) {
                bw.write(entry.getKey() + " " + destino.getKey() + " " + destino.getValue() + "\n");
            }
        }

        bw.close();
        System.out.println("Arquivo gravado com sucesso!");
    }

    static void inserirVertice(Scanner scanner) {
        System.out.print("ID do novo vertice: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Rotulo: ");
        String rotulo = scanner.nextLine();
        System.out.print("Peso: ");
        int peso = scanner.nextInt();

        vertices.put(id, new Vertice(id, rotulo, peso));
        adjacencias.put(id, new HashMap<>());
        System.out.println("Vertice inserido.");
    }

    static void inserirAresta(Scanner scanner) {
        System.out.print("Origem: ");
        int origem = scanner.nextInt();
        System.out.print("Destino: ");
        int destino = scanner.nextInt();
        System.out.print("Peso: ");
        int peso = scanner.nextInt();
        adjacencias.get(origem).put(destino, peso);
        System.out.println("Aresta inserida.");
    }

    static void removerVertice(Scanner scanner) {
        System.out.print("ID do vertice a remover: ");
        int id = scanner.nextInt();
        vertices.remove(id);
        adjacencias.remove(id);
        for (Map<Integer, Integer> map : adjacencias.values()) {
            map.remove(id);
        }
        System.out.println("Vertice removido.");
    }

    static void removerAresta(Scanner scanner) {
        System.out.print("Origem: ");
        int origem = scanner.nextInt();
        System.out.print("Destino: ");
        int destino = scanner.nextInt();
        adjacencias.get(origem).remove(destino);
        System.out.println("Aresta removida.");
    }

    static void mostrarConteudo() {
        System.out.println("\nTipo do Grafo: " + tipoGrafo);
        System.out.println("Vertices:");
        for (Vertice v : vertices.values()) {
            System.out.println(v.id + " -> " + v.rotulo + " (" + v.peso + ")");
        }
        System.out.println("Arestas:");
        for (var entry : adjacencias.entrySet()) {
            for (var destino : entry.getValue().entrySet()) {
                System.out.println(entry.getKey() + " -> " + destino.getKey() + " [" + destino.getValue() + "]");
            }
        }
    }

    static void mostrarListaAdjacencia() {
        System.out.println("\nLista de Adjacencia:");
        for (var entry : adjacencias.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            for (var destino : entry.getValue().entrySet()) {
                System.out.print(destino.getKey() + "(" + destino.getValue() + ") ");
            }
            System.out.println();
        }
    }

    static void verificarConexidade() {
        Set<Integer> visitado = new HashSet<>();
        dfs(vertices.keySet().iterator().next(), visitado);
        if (visitado.size() == vertices.size()) {
            System.out.println("O grafo eh conexo.");
        } else {
            System.out.println("O grafo eh desconexo.");
        }
    }

    static void dfs(int atual, Set<Integer> visitado) {
        visitado.add(atual);
        for (int vizinho : adjacencias.get(atual).keySet()) {
            if (!visitado.contains(vizinho)) {
                dfs(vizinho, visitado);
            }
        }
    }
}
