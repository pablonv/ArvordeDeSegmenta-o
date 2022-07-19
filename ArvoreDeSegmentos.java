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
public class ArvoreDeSegmentos {

  private int[] st, A;
  private int n;
  private int left (int p) { return p << 1; } // Filho esquerdo
  private int right(int p) { return (p << 1) + 1; } // Filho direito

  private void build(int p, int L, int R) {
    if (L == R)                            // como L == R, qualquer um está bem
      st[p] = L;                                         // armazenar o índice
    else {                                // calcular recursivamente os valores
      build(left(p) , L              , (L + R) / 2);
      build(right(p), (L + R) / 2 + 1, R          );
      int p1 = st[left(p)], p2 = st[right(p)];
      //para o mínimo <=
      st[p] = (A[p1] > A[p2]) ? p1 : p2;
  } }

  private int rmq(int p, int L, int R, int i, int j) {          // O(log n)
    if (i >  R || j <  L) return -1; // segmento atual fora do intervalo de consulta
    if (L >= i && R <= j) return st[p]; // dentro do intervalo de consulta

     // calcula o valor da posição máxima/minima na parte esquerda e direita do intervalo
    int p1 = rmq(left(p) , L              , (L+R) / 2, i, j);
    int p2 = rmq(right(p), (L+R) / 2 + 1, R          , i, j);

    if (p1 == -1) return p2;   // se tentarmos acessar o segmento fora da consulta
    if (p2 == -1) return p1;     
   
    //para o mínimo <=
    return (A[p1] > A[p2]) ? p1 : p2; }          // como na rotina de construção

  private int update_point(int p, int L, int R, int idx, int new_value) {
    int i = idx, j = idx;
    //se o intervalo atual não cruzar o intervalo de atualização, retorna o valor do nó de st
    
    if (i > R || j < L)
      return st[p];

    // se o intervalo atual estiver incluído no intervalo de atualização, atualize st[nó] e o vetor A.
    if (L == i && R == j) {
      A[i] = new_value; // Atualiza o vetor
      return st[p] = L; // retorna o index
    }

    // calcula a posição do maximo/minimo na parte esquerda e direita do intervalo
    int p1, p2;
    p1 = update_point(left(p) , L              , (L + R) / 2, idx, new_value);
    p2 = update_point(right(p), (L + R) / 2 + 1, R          , idx, new_value);

   
    //para o mínimo <=
    return st[p] = (A[p1] > A[p2]) ? p1 : p2; //retornar a posição do maximo/minimo geral
  }

  public ArvoreDeSegmentos(int[] _A) {
    A = _A; n = A.length;                   // copiar conteúdo para uso local
    st = new int[4 * n];
    for (int i = 0; i < 4 * n; i++) st[i] = 0; // crie um vetor com comprimento de A e preencha-o com zeros
    build(1, 0, n - 1);                     // construção recursiva
  }

  public int rmq(int i, int j) { return A[rmq(1, 0, n - 1, i, j)]; } // sobrecarregando

  public int update_point(int idx, int new_value) {
    return update_point(1, 0, n - 1, idx, new_value); }
}
//simula as operações da arvore de segmentos. 
class segmenttree_ds {
  public static void main(String[] args) {
    int[] A = new int[] { 18, 17, 13, 19, 15, 11, 20 }; // O vetor original
    ArvoreDeSegmentos st = new ArvoreDeSegmentos(A);

    System.out.printf("              idx    0, 1, 2, 3, 4,  5, 6\n");
    System.out.printf("              A é {18,17,13,19,15, 11,20}\n");
    System.out.printf("RMQ(1, 3) = %d\n", st.rmq(1, 3)); 
    System.out.printf("RMQ(4, 6) = %d\n", st.rmq(4, 6)); 
    System.out.printf("RMQ(3, 4) = %d\n", st.rmq(3, 4)); 
    System.out.printf("RMQ(0, 0) = %d\n", st.rmq(0, 0)); 
    System.out.printf("RMQ(0, 1) = %d\n", st.rmq(0, 1)); 
    System.out.printf("RMQ(0, 6) = %d\n", st.rmq(0, 6)); 

    System.out.printf("              idx    0, 1, 2, 3, 4,  5, 6\n");
    System.out.printf("     Modificando A  {18,17,13,19,15,100,20}\n");
    st.update_point(5, 100);                  // update A[5] from 11 to 100
    System.out.printf("Esses valores das consultas não mudaram\n");
    System.out.printf("RMQ(1, 3) = %d\n", st.rmq(1, 3));               
    System.out.printf("RMQ(3, 4) = %d\n", st.rmq(3, 4));               
    System.out.printf("RMQ(0, 0) = %d\n", st.rmq(0, 0));               
    System.out.printf("RMQ(0, 1) = %d\n", st.rmq(0, 1));               
    System.out.printf("Esses valores das consultas mudaram\n");
    System.out.printf("RMQ(0, 6) = %d\n", st.rmq(0, 6));            
    System.out.printf("RMQ(4, 6) = %d\n", st.rmq(4, 6));            
    System.out.printf("RMQ(4, 5) = %d\n", st.rmq(4, 5));           
  }
    
}
