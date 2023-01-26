import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*Trabalho Desenvolvido para a disciplina de Algoritmos e Técnicas de Programação pelos seguintes alunos: 
 * GABRIEL LUÍS FERREIRA FIGUEIREDO
 * GUSTAVO HENRIQUE FERREIRA
 * VINICIUS ANDREATA DUMONT
 */
public class Labirinto {
	
	
	private static char[][] matriz = new char[][] {};
	
	public static int tamanhoY, tamanhoX;
	
	public static String caminhoPercorrido = "";
	
	public static Boolean temChave = false, mapaDesafio = false, abriuPorta = false;
	
	/* LEITURA DO ARQUIVO labirinto.txt, QUE CONTÉM O MAPA QUE SERÁ EECUTADO */
	public static void lerArquivoLabirinto() {
		try {
			File myObj = new File("labirinto/mapa2desafio1.txt"); //ARQUIVO TEM DE ESTAR NO MESMO DIRETÓRIO DO CÓDIGO
			
		    Scanner myReader = new Scanner(myObj);
		    
		    tamanhoY = 0; //TAMANHO DAS LINHAS DA MATRIZ QUE CONTERÁ O MAPA
		    
		    while (myReader.hasNextLine()) {
		        String data = myReader.nextLine(); //LEITURA DA LINHA DO ARQUIVO
		        
		        tamanhoX = data.length(); //TAMANHO DAS COLUNAS DA MATRIZ QUE CONTERÁ O MAPA
		        
		        tamanhoY++; //INCREMENTO DO TAMANHO DA LINHA
		    }
		    
		    char [][] mAux = new char[tamanhoY][tamanhoX]; //MATRIZ AUXILIAR QUE AJUDARÁ A CRIAR O MATRIZ DINÂMICA GLOBAL
		    
		    myReader.close(); //FECHANDO ARQUIVO
		    
		    myReader = new Scanner(myObj); //ABRINDO NOVAMENTE O ARQUIVO PARA UMA RELEITURA
		    
		    int aux = 0; //AUXILIAR QUE AJUDA NA CONTAGEM DAS LINHAS
		    
		    while (myReader.hasNextLine()) {
		        String data = myReader.nextLine(); //LEITURA DA LINHA DO ARQUIVO
		        
		        /*ITERATIVO QUE PEGA O TAMANHO DA LINHA E ESCREVE CHAR POR CHAR NA MATRIZ AUXILIAR*/
		        for(int i = 0; i < data.length(); i++) {
		        	mAux[aux][i] = data.charAt(i); //ATRIBUIÇÃO DE VALOR NA MATRIZ AUXILIAR
		        	
		        	if(data.charAt(i) == 'D' || data.charAt(i) == 'K') { //CONDICIONAL QUE VERIFICA SE O MAPA TEM DESAFIO DE CHAVE OU NÃO
		        		mapaDesafio = true;
		        	}
		        }
		        
		        aux++; //INCREMENTO DO AUXILIAR PARA PREENCHER NA PROXIMA LINHA DA MATRIZ AUXILIAR
		    }
		    
		    matriz = mAux; //ATRIBUIÇÃO DE VALORES NA MATRIZ GLOBAL ATRAVÉS DA MATRIZ AUXILIAR
		    
		    myReader.close(); //FECHANDO ARQUIVO
		
		/* TRATAMENTO DE EXCEÇÕES NA LEITURA DO ARQUIVO */
		} catch (FileNotFoundException e) {
			System.out.println("Ocorreu um erro ao ler o arquivo labirinto.txt");
			
		    e.printStackTrace();
		}
	}
	
	/* FUNÇÃO QUE IMPRIME O LABIRINTO COM UM DELAY DE 150MS E APAGANDO O CONSOLE PARA NÃO POLUIÇÃO DO MESMO */
	public static void imprimeLabirinto() {
		/* DELAY DE 150 MS */
		try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
		for(int i = 0; i< 50; i++) System.out.println(); //ITERAÇÃO QUE APAGA O CONSOLE
		
		for(int y = 0; y < matriz.length; y++) { //ITERAÇÃO QUE INCREMENTA AS LINHAS
			for(int x = 0; x < matriz[y].length; x++) { // ITERAÇÃO QUE INCREMENTA AS COLUNAS
				System.out.print(matriz[y][x] + " "); //IMPRESSÃO DAS COLUNAS
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/* PROCEDIMENTO QUE VERIFICA SE A CÉLULA É EXISTENTE E NÃO VIOLA A MEMÓRIA */
	public  static boolean localValido(int linha, int coluna, int nlinhas, int ncolunas) {
        return linha < nlinhas && coluna < ncolunas && linha >= 0 && coluna >= 0; 
    }
	
	/* PROCEDIMENTO QUE RETORNA TRUE OU FALSE E FAZ TODO O TRAVERSE DO MAPA E FAZ TODAS
	 * AS VERIFICAÇÕES DE CÉLULAS
	 */
	public static Boolean resolverLabirintoTraverse(final char[][] labirinto, final int linha, final int coluna) {
		imprimeLabirinto(); //IMPRESSÃO DO LABIRINTO
		
		if(localValido(linha, coluna, labirinto.length, labirinto[0].length)) {
			if(labirinto[linha][coluna] == 'D' && !temChave) { //VERIFICA SE A CÉLULA É UMA PORTA E SE O NPC JÁ TEM A CHAVE
				return false;
			}
			if(labirinto[linha][coluna] == 'X' || labirinto[linha][coluna] == '.' || (labirinto[linha][coluna] == '*')) { //VERIFICA SE A CÉLULA NÃO É ALGO QUE 
				//SEJA UMA CÉLULA BLOQUEADORA QUE IMPEÇA O NPC DE PASSAR
				return false;
			}
			if(labirinto[linha][coluna] == 'E') { //VERIFICA SE O NPC CHEGOU AO FINAL DO MAPA
	            return true;
	        }
			if(labirinto[linha][coluna] == 'K') { //VERIFICA SE O NPC CHEGOU EM UMA CHAVE
				temChave = true;
			}
			if(labirinto[linha][coluna] == 'D' && temChave) { //VERIFICA SE O NPC CHEGOU NA PORTA
				abriuPorta = true;
			}
			if(labirinto[linha][coluna] == 'S') { //VERIFICA SE O NPC CHEGOU NO START AO REALIZAR BACKTRACKING
				labirinto[linha][coluna] = 'S';
			} else {
		        labirinto[linha][coluna] = '*';
			}

	        //baixo
	        if(resolverLabirintoTraverse(labirinto, linha + 1, coluna)) { //AVANÇA O NPC PARA UMA CÉLULA ABAIXO
	        	caminhoPercorrido += 'B';
	        	return true;
	        }
	        //esquerda
	        if(resolverLabirintoTraverse(labirinto, linha, coluna - 1)) { //AVANÇA O NPC PARA UMA CÉLULA A ESQUERDA
	        	caminhoPercorrido += 'E';
	        	return true;
	        }
	        //cima
	        if(resolverLabirintoTraverse(labirinto, linha - 1, coluna)) { //AVANÇA O NPC PARA UMA CÉLULA ACIMA
	            caminhoPercorrido += 'C';
	            return true;
	        }
	        //direita
	        if(resolverLabirintoTraverse(labirinto, linha, coluna + 1)) { //AVANÇA O NPC PARA UMA CÉLULA A DIREITA
	        	caminhoPercorrido += 'D';
	            return true;
	        }
	        
		    //backtrack
	        if(labirinto[linha][coluna] != 'S') //CONDICIONAL QUE REALIZA O BACKTRACKING, RETORNANDO O NPC UMA CASA QUE ELE AVANÇOU POR ÚLTIMO
	        	labirinto[linha][coluna] = mapaDesafio ? ' ' : '.';
		    
		    return false;
		}
		return false;
	}
	
	/* FUNÇÃO PRINCIPAL QUE EXECUTA TODO O CÓDIGO DE MANEIRA ORDENADA E IMPRIME OS DADOS FINAIS */
	public static void main(String[] args) {
		lerArquivoLabirinto(); //CHAMADA DA FUNÇÃO QUE FAZ LEITURA DO ARQUIVO
		
		imprimeLabirinto(); //FUNÇÃO QUE IMPRIME A PRIMEIRA VEZ O MAPA (O MAPA ORIGINAL)
		
		Boolean caminhoExiste = false;
		
		/* ITERATIVOS QUE PERCORREM A MATRIZ E ACHAM O START PARA INICIAR O TRAVERSE DO MAPA */
        for(int i = 0 ;i < matriz.length; i++)
            for(int j = 0; j <matriz[i].length; j++)
                if(matriz[i][j] == 'S') {
                    caminhoExiste = resolverLabirintoTraverse(matriz, i, j);
                }
        
		imprimeLabirinto(); //FUNÇÃO QUE IMPRIME A VERSÃO FINAL DO MAPA
		
		/* BLOCO QUE FAZ A VERIFICAÇÃO DE PROBLEMAS NO BACKTRACKING NOS CASOS DOS MAPAS DE DESAFIOS COM MAIS DE UM CAMINHO */
		if(!caminhoExiste)
			for(int i = 0 ;i < matriz.length; i++)
	            for(int j = 0; j <matriz[i].length; j++)
	                if(matriz[i][j] == 'S') {
	                    caminhoExiste = resolverLabirintoTraverse(matriz, i, j);
	                }
        
		String resp = caminhoExiste == true ? "Sim" : "Não"; //CONDICIONAL QUE TROCA TRUE E FALSE POR SIM E NÃO COM O RETORNO DO PERCURSO DO MAPA
		
		System.out.println("Labirinto Resolvido: " + resp);
		
		System.out.println("Caminho Percorrido do ponto End até o ponto Start: " + caminhoPercorrido);
	}
}