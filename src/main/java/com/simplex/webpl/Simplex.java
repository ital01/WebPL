// A seguir, resolve um problema de programacao linear
// Em forma padronizada usando o metodo simplex
// Por favor, leia abaixo.
/************************************************USO*************************************************************
 * 1. Crie uma instancia da classe simplex
 * 2. Preencha a tabela com a forma padronizada do problema chamando simplex.fillTable()
 * 3. Crie um loop while e chame o metodo simplex.compute() ate que ele retorne ERROR.IS_OPTIMAL ou ERROR.UNBOUNDED
 * ****************************************************************************************************************/
package com.simplex.webpl;

public class Simplex {
    private int linhas, colunas; // linha e coluna
    private float[][] tabela; // tabela simplex
    private boolean solutionIsUnbounded = false;

    public static enum ERRO{
        NAO_OTIMO,
        OTIMO,
        INDETERMIANDA
    };
    
    public Simplex(int numOfConstraints, int numOfUnknowns){
        linhas = numOfConstraints+1; // numero de linhas + 1 
        colunas = numOfUnknowns+1;   // numero de colunas + 1
        tabela = new float[linhas][]; // criar uma matriz 2D
        
        // inicializa as referencias para as matrizes
        for(int i = 0; i < linhas; i++){
            tabela[i] = new float[colunas];
        }
    }
    
    // imprime a tabela simplex
    public void print(){
        for(int i = 0; i < linhas; i++){
            for(int j = 0; j < colunas; j++){
                String valor = String.format("%.2f", tabela[i][j]);
                System.out.print(valor + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    // preenche a tabela simplex com coeficientes
    public void preencherTabela(float[][] dados){
        for(int i = 0; i < tabela.length; i++){
            System.arraycopy(dados[i], 0, this.tabela[i], 0, dados[i].length);
        }
    }
    
    // calcula os valores da tabela simplex
    // deve ser usado em um loop para calcular continuamente ate
    // uma solucao otima ser encontrada
    public ERRO calcular(){
        // passo 1
        if(verificarOptimalidade()){
            return ERRO.OTIMO; // solucao e otima
        }
        
        // passo 2
        // encontrar a coluna de entrada
        int colunaPivot = encontrarColunaEntrada();
        System.out.println("Coluna Pivot: "+colunaPivot);
        
        // passo 3
        // encontrar valor partindo
        float[] razoes = calcularRazoes(colunaPivot);
        if(solutionIsUnbounded == true)
            return ERRO.INDETERMIANDA;
        int linhaPivot = encontrarMenorValor(razoes);
        //System.out.println("Linha Pivot: "+ linhaPivot);
        
        // passo 4
        // forma a proxima tabela
        formarProximaTabela(linhaPivot, colunaPivot);
        
        // como formamos uma nova tabela, entao retornamos NAO_OTIMO
        return ERRO.NAO_OTIMO;
    }
    
    // Forma uma nova tabela a partir de valores pre-calculados.
    private void formarProximaTabela(int linhaPivot, int colunaPivot){
        float valorPivot = tabela[linhaPivot][colunaPivot];
        float[] valoresLinhaPivot = new float[colunas];
        float[] valoresColunaPivot = new float[colunas];
        float[] linhaNova = new float[colunas];
        
        // divide todas as entradas na linha pivot pela entrada na coluna pivot
        // obtenha a entrada na linha pivot
        System.arraycopy(tabela[linhaPivot], 0, valoresLinhaPivot, 0, colunas);
        
        // obtenha a entrada na coluna pivot
        for(int i = 0; i < linhas; i++)
            valoresColunaPivot[i] = tabela[i][colunaPivot];
        
        // divide os valores na linha pivot pelo valor pivot
        for(int  i = 0; i < colunas; i++)
            linhaNova[i] =  valoresLinhaPivot[i] / valorPivot;
        
        // subtrair de cada uma das outras linhas
        for(int i = 0; i < linhas; i++){
            if(i != linhaPivot){
                for(int j = 0; j < colunas; j++){
                    float c = valoresColunaPivot[i];
                    tabela[i][j] = tabela[i][j] - (c * linhaNova[j]);
                }
            }
        }
        
        // substituir a linha
        System.arraycopy(linhaNova, 0, tabela[linhaPivot], 0, linhaNova.length);
    }
    
    // calcula as razoes da linha pivot
    private float[] calcularRazoes(int coluna){
        float[] entradasPositivas = new float[linhas];
        float[] res = new float[linhas];
        int todosNegativosContagem = 0;
        for(int i = 0; i < linhas; i++){
            if(tabela[i][coluna] > 0){
                entradasPositivas[i] = tabela[i][coluna];
            }
            else{
                entradasPositivas[i] = 0;
                todosNegativosContagem++;
            }
            //System.out.println(entradasPositivas[i]);
        }
        
        if(todosNegativosContagem == linhas)
            this.solutionIsUnbounded = true;
        else{
            for(int i = 0;  i < linhas; i++){
                float val = entradasPositivas[i];
                if(val > 0){
                    res[i] = tabela[i][colunas -1] / val;
                }
            }
        }
        
        return res;
    }
    
    // encontra a proxima coluna de entrada
    private int encontrarColunaEntrada(){
        float[] valores = new float[colunas];
        int localizacao = 0;
        
        int pos, contagem = 0; 
        for(pos = 0; pos < colunas-1; pos++){
            if(tabela[linhas-1][pos] < 0){
                //System.out.println("valor negativo encontrado");
                contagem++;
            }
        }
        
        if(contagem > 1){
            for(int i = 0; i < colunas-1; i++)
                valores[i] = Math.abs(tabela[linhas-1][i]);
            localizacao = encontrarMaiorValor(valores);
        } else localizacao = contagem - 1;
        
        return localizacao;
    }
    
    
    // encontra o menor valor em uma matriz
    private int encontrarMenorValor(float[] dados){
        float minimo ;
        int c, localizacao = 0;
        minimo = dados[0];
        
        for(c = 1; c < dados.length; c++){
            if(dados[c] > 0){
                if(Float.compare(dados[c], minimo) < 0){
                    minimo = dados[c];
                    localizacao  = c;
                }
            }
        }
        
        return localizacao;
    }
    
    // encontra o maior valor em uma matriz
    private int encontrarMaiorValor(float[] dados){
        float maximo = 0;
        int c, localizacao = 0;
        maximo = dados[0];
        
        for(c = 1; c < dados.length; c++){
            if(Float.compare(dados[c], maximo) > 0){
                maximo = dados[c];
                localizacao  = c;
            }
        }
        
        return localizacao;
    }
    
    // verifica se a tabela e otima
    public boolean verificarOptimalidade(){
        boolean eOtimo = false;
        int vContagem = 0;
        
        for(int i = 0; i < colunas-1; i++){
            float val = tabela[linhas-1][i];
            if(val >= 0){
                vContagem++;
            }
        }
        
        if(vContagem == colunas-1){
            eOtimo = true;
        }
        
        return eOtimo;
    }

    // retorna a tabela simplex
    public float[][] getTabela() {
        return tabela;
    }
}