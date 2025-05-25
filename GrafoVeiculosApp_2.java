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
            opcao = 0;
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
            System.out.println("10. Comparar potências");
            System.out.println("11. Mostrar grau por carro");
            System.out.println("12. Verificar se pode ser hamiltoniano");
            System.out.println("13. Verificar se é planar");
            System.out.println("14. Encerrar");
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
                case 10 -> ordenarPorPotenciaBFS();
                case 11 -> graus();
                case 12 -> verificaHamiltoniano();
                case 13 -> verificarPlanaridade();
                case 14 -> System.out.println("Encerrando...");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 14);

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
        autoAresta(id);
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

    static void autoAresta(int idOrigem) {
        Vertice vOrigem = vertices.get(idOrigem);

        for (Vertice v : vertices.values()) {
            if (v.id != idOrigem) {
                int peso = 0;
                if (vOrigem.peso >= v.peso) {
                    peso = vOrigem.peso - v.peso;
                    adjacencias.get(idOrigem).put(v.id, peso);
                } else {
                    peso = v.peso - vOrigem.peso;
                    adjacencias.get(v.id).put(idOrigem, peso);
                }
            }
        }

        System.out.println("Arestas inseridas com sucesso");
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

    static void ordenarPorPotenciaBFS() {
        System.out.print("Digite o ID do carro que deseja comparar: ");
        Scanner scanner = new Scanner(System.in);
        int carroInicial = scanner.nextInt();
        scanner.nextLine();

        if (!vertices.containsKey(carroInicial)) {
            System.out.println("ID do carro não encontrado.");
            return;
        }

        Map<Integer, Integer> potenciaRelativa = new HashMap<>();
        Set<Integer> visitado = new HashSet<>();
        Queue<Integer> fila = new LinkedList<>();

        potenciaRelativa.put(carroInicial, 0);
        fila.add(carroInicial);

        while (!fila.isEmpty()) { // BFS (busca menos potência)
            int atual = fila.poll();
            visitado.add(atual);
            int potenciaAtual = potenciaRelativa.get(atual);

            for (Map.Entry<Integer, Integer> vizinho : adjacencias.get(atual).entrySet()) {
                int idVizinho = vizinho.getKey();
                int diferenca = vizinho.getValue();

                if (!potenciaRelativa.containsKey(idVizinho)) {
                    potenciaRelativa.put(idVizinho, potenciaAtual + diferenca);
                    fila.add(idVizinho);
                }
            }
        }

        // Busca simples para carro com mais potência
        System.out.println("\nCarros com MAIS potência:");
        List<Map.Entry<Integer, Integer>> maisPotentes = new ArrayList<>();
        for (Map.Entry<Integer, Map<Integer, Integer>> entrada : adjacencias.entrySet()) {
            int origem = entrada.getKey();
            Map<Integer, Integer> destinos = entrada.getValue();

            if (destinos.containsKey(carroInicial)) {
                int diferenca = destinos.get(carroInicial);
                maisPotentes.add(Map.entry(origem, diferenca));
            }
        }

        // Ordena potências maiores (decrescente)
        maisPotentes.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        for (Map.Entry<Integer, Integer> entry : maisPotentes) {
            Vertice v = vertices.get(entry.getKey());
            int diferenca = entry.getValue();
            System.out.println("ID: " + v.id + ", Rótulo: " + v.rotulo + ", Diferença: +" + diferenca + " cv");
        }

        List<Map.Entry<Integer, Integer>> ordenado = new ArrayList<>(potenciaRelativa.entrySet());
        ordenado.sort(Comparator.comparingInt(Map.Entry::getValue));

        System.out.println("\nCarros com MENOS potência:");
        for (Map.Entry<Integer, Integer> entry : ordenado) {
            Vertice v = vertices.get(entry.getKey());
            System.out.println("ID: " + v.id + ", Rótulo: " + v.rotulo + ", Diferença de potência: " + entry.getValue());
        }
    }

    static void graus() {
        Map<Integer, Integer> grauEntrada = new HashMap<>();
        Map<Integer, Integer> grauSaida = new HashMap<>();

        // Inicializa graus
        for (int id : vertices.keySet()) {
            grauEntrada.put(id, 0);
            grauSaida.put(id, 0);
        }

        // Calcula graus de entrada e saída
        for (Map.Entry<Integer, Map<Integer, Integer>> entrada : adjacencias.entrySet()) {
            int origem = entrada.getKey();
            Map<Integer, Integer> destinos = entrada.getValue();

            grauSaida.put(origem, grauSaida.get(origem) + destinos.size());

            for (int destino : destinos.keySet()) {
                grauEntrada.put(destino, grauEntrada.get(destino) + 1);
            }
        }

        // Exibe grau de entrada e saída por carro
        System.out.println("\nGraus de cada carro:");
        for (int id : vertices.keySet()) {
            Vertice v = vertices.get(id);
            System.out.println("ID: " + v.id + ", Rótulo: " + v.rotulo + ", Grau de entrada: " + grauEntrada.get(id) + ", Grau de saída: " + grauSaida.get(id));
        }
    }

    static void verificaHamiltoniano() {
        int n = vertices.size();
        Map<Integer, Integer> grau = new HashMap<>();

        // Conta grau total (entrada + saída, para ignorar direção)
        for (int id : vertices.keySet()) {
            grau.put(id, 0);
        }

        for (Map.Entry<Integer, Map<Integer, Integer>> entrada : adjacencias.entrySet()) {
            int origem = entrada.getKey();
            for (int destino : entrada.getValue().keySet()) {
                grau.put(origem, grau.get(origem) + 1);
                grau.put(destino, grau.get(destino) + 1);
            }
        }

        // Verifica condição de Dirac
        boolean diracOk = true;
        for (int id : grau.keySet()) {
            if (grau.get(id) < n / 2) {
                diracOk = false;
                break;
            }
        }

        System.out.println("\nVerificação teórica (ignora direção):");
        if (diracOk) {
            System.out.println("O grafo ADMITE ciclo Hamiltoniano pelo Teorema de Dirac.");
        } else {
            System.out.println("O grafo NÃO satisfaz a condição de Dirac. Isso NÃO significa que não tem ciclo Hamiltoniano, apenas que não é garantido.");
        }
    }

    static void verificarPlanaridade() {
        int v = vertices.size();
        int e = 0;

        Set<String> arestasContadas = new HashSet<>();

        for (Map.Entry<Integer, Map<Integer, Integer>> entrada : adjacencias.entrySet()) {
            int origem = entrada.getKey();
            for (int destino : entrada.getValue().keySet()) {
                // Evita contar duas vezes (tratando como não-direcionado)
                String chave = origem < destino ? origem + "-" + destino : destino + "-" + origem;
                if (!arestasContadas.contains(chave)) {
                    arestasContadas.add(chave);
                    e++;
                }
            }
        }

        System.out.println("\nVerificação de planaridade:");
        System.out.println("Vértices: " + v);
        System.out.println("Arestas: " + e);
        System.out.println("Limite superior para planaridade (3v - 6): " + (3 * v - 6));

        if (v >= 3 && e > (3 * v - 6)) {
            System.out.println("O grafo EXCEDE o limite de Euler. NÃO é planar.");
        } else {
            System.out.println("O grafo RESPEITA o limite. PODE ser planar.");
        }
    }

}
