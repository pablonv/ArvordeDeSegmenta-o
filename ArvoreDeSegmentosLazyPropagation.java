/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arvoredesegmentos;

/**
 *
 * @author USER
 */
public class ArvoreDeSegmentosLazyPropagation {
    final int MAX = 1000;     // Tamanho máximo da árvore
    int tree[] = new int[MAX]; // Para armazenar a árvore de segmentos
    int lazy[] = new int[MAX]; // Para armazenar atualizações pendentes
  
    /* si -> índice do nó atual na árvore de segmentos
         ss e se -> Índices iniciais e finais de elementos para os quais os nós atuais armazenam soma.
         us e eu -> índices iniciais e finais da consulta de atualização
         ue -> índice final da consulta de atualização
         diff -> que precisamos adicionar no intervalo us para ue */
    void updateRangeUtil(int si, int ss, int se, int us,
                        int ue, int diff)
    {
        /* Se o valor do vetor lazy for diferente de zero para o nó atual da árvore de segmentos, 
        há algumas atualizações pendentes. Portanto, precisamos garantir que as atualizações pendentes 
        sejam feitas antes de fazer novas atualizações. Porque este valor pode ser usado pelo pai após 
        chamadas recursivas (veja a última linha desta função)*/
        if (lazy[si] != 0)
        {
            // Faça atualizações pendentes usando o valor armazenado em nós preguiçosos
            tree[si] = (se - ss + 1) * lazy[si];
  
            // verificando se não é nó folha porque se for nó folha então paramos
            if (ss != se)
            {
                // Podemos adiar a atualização das crianças que não precisamos de seus novos valores agora.
                // Como ainda não estamos atualizando os filhos de si, precisamos definir sinalizadores preguiçosos para os filhos
                lazy[si * 2 + 1] = lazy[si];
                lazy[si * 2 + 2] = lazy[si];
            }
  
            // Defina o valor no vetor lazy para o nó atual como 0, pois foi atualizado
            lazy[si] = 0;
        }
  
        // fora de alcance
        if (ss > se || ss > ue || se < us)
            return;
  
        // O segmento atual está totalmente dentro do alcance
        if (ss >= us && se <= ue)
        {
            // Adicione a diferença ao nó atual
            tree[si] = (se - ss + 1) * diff;
  
            // mesma lógica para verificar o nó folha ou não
            if (ss != se)
            {
                // É aqui que armazenamos valores em nós preguiçosos, em vez de atualizar a própria
                // árvore de segmentos. Como não precisamos desses valores atualizados agora, 
                // adiamos as atualizações armazenando valores em lazy[]
                lazy[si * 2 + 1] = diff;
                lazy[si * 2 + 2] = diff;
            }
            return;
        }
  
        // Se não estiver completamente dentro do alcance, mas se sobrepuser, repetir para os filhos,
        int mid = (ss + se) / 2;
        updateRangeUtil(si * 2 + 1, ss, mid, us, ue, diff);
        updateRangeUtil(si * 2 + 2, mid + 1, se, us, ue, diff);
  
        // E use o resultado das chamadas dos filhos para atualizar este nó
        tree[si] = tree[si * 2 + 1] + tree[si * 2 + 2];
    }
  
    // Função para atualizar um intervalo de valores na árvore de segmentos
      /* us e eu -> índices iniciais e finais da consulta de atualização
         ue -> índice final da consulta de atualização
         diff -> que precisamos adicionar no intervalo us para ue */
    void updateRange(int n, int us, int ue, int diff) {
        updateRangeUtil(0, 0, n - 1, us, ue, diff);
    }

    
    /* Uma função recursiva para obter a soma dos valores em determinado intervalo da matriz. 
         A seguir estão os parâmetros para esta função:
         si --> Índice do nó atual na árvore de segmentos. 
                Inicialmente 0 é passado como raiz e está sempre no índice 0.
         ss & se --> Índices inicial e final do segmento representado pelo nó atual, ou seja, tree[si]
         qs & qe --> Índices iniciais e finais do intervalo de consulta */
    int getSumUtil(int ss, int se, int qs, int qe, int si)
    {
        // Se o sinalizador do vetor lazy estiver definido para o nó atual da árvore de segmentos, 
        // haverá algumas atualizações pendentes. Portanto, precisamos garantir que 
        // as atualizações pendentes sejam feitas antes de processar a consulta de subsoma
        if (lazy[si] != 0)
        {
            // Faça atualizações pendentes neste nó. Observe que este nó representa 
            // a soma dos elementos em vetor[ss..se] e todos esses elementos devem 
            // ser modificados pelo vetor lazy[si]
            tree[si] += (se - ss + 1) * lazy[si];
  
            // verificando se não é nó folha porque se for nó folha então paramos
            if (ss != se)
            {
                // Como ainda não estamos atualizando os filhos do nó si, 
                // precisamos definir valores preguiçosos para os filhos
                lazy[si * 2 + 1] += lazy[si];
                lazy[si * 2 + 2] += lazy[si];
            }
  
            // desative o valor preguiçoso para o nó atual, pois foi atualizado
            lazy[si] = 0;
        }
  
        // Fora de alcance
        if (ss > se || ss > qe || se < qs)
            return 0;
  
        // Neste ponto, com certeza, as atualizações lentas pendentes são feitas para o nó atual.
        // Assim podemos retornar valor do nó.
        
        //Se este segmento estiver no intervalo
        if (ss >= qs && se <= qe)
            return tree[si];
  
        // Se uma parte deste segmento se sobrepuser ao intervalo fornecido
        int mid = (ss + se) / 2;
        return getSumUtil(ss, mid, qs, qe, 2 * si + 1) +
            getSumUtil(mid + 1, se, qs, qe, 2 * si + 2);
    }
  
    // Retorna a soma dos elementos no intervalo do índice qs (início da consulta) a qe (final da consulta). 
    // Ele usa principalmente getSumUtil()
    int getSum(int n, int qs, int qe)
    {
        // Verifique se há valores de entrada incorretos
        if (qs < 0 || qe > n - 1 || qs > qe)
        {
            System.out.println("Entrada Inválida");
            return -1;
        }
  
        return getSumUtil(0, n - 1, qs, qe, 0);
    }
  
    /* Uma função recursiva que constrói a Árvore de Segmentos para vetor[ss..se]. 
       si é o índice do nó atual na árvore de segmentos st. */
    void constructSTUtil(int arr[], int ss, int se, int si)
    {
        // fora do alcance, pois ss nunca pode ser maior que se
        if (ss > se)
            return;
  
        /* Se houver um elemento no vetor, armazene-o no nó atual da árvore de segmentos e retorne */
        if (ss == se)
        {
            tree[si] = arr[ss];
            return;
        }
  
        /* Se houver mais de um elemento, recupere as subárvores esquerda e direita 
        // e armazene a soma dos valores neste nó */
        int mid = (ss + se) / 2;
        constructSTUtil(arr, ss, mid, si * 2 + 1);
        constructSTUtil(arr, mid + 1, se, si * 2 + 2);
  
        tree[si] = tree[si * 2 + 1] + tree[si * 2 + 2];
    }
  
    /* Função para construir a árvore de segmentos de um determinado vetor. 
       Esta função aloca memória para a árvore de segmentos e chama constructSTUtil() 
       para preencher a memória alocada */
    void constructST(int arr[], int n)
    {
        // Preencha a memória alocada st
        constructSTUtil(arr, 0, n - 1, 0);
    }
  
  
    // Instruções para testar as funções acima
    public static void main(String args[])
    {
        int vetor[] = {3, 12, 5, 7, 9, 11};
        int n = vetor.length;
        ArvoreDeSegmentosLazyPropagation arvore = new ArvoreDeSegmentosLazyPropagation();
  
        // Construir árvore de segmentos a partir de determinado vetor
        arvore.constructST(vetor, n);
  
        // Imprima a soma dos valores na matriz do índice 0 a 3
        System.out.println("Soma de valores em determinado intervalo = " +
                        arvore.getSum(n, 0, 3));
  
        // Altera para 5 todos os nós nos índices de 2 a 5.
        arvore.updateRange(n, 2, 5, 5);
  
        // Encontre a soma depois que o valor for atualizado
        System.out.println("Soma atualizada de valores em determinado intervalo = " +
                        arvore.getSum(n, 1, 5));
    }
}

