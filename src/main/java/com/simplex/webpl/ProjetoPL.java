package com.simplex.webpl;

public class ProjetoPL 
{

    public static void main(String[] args) 
    {
        
        boolean sair = false;
        
        // Exemplo de problema:
        // maximizar 3x + 5y 
        // sujeito a x + y = 4 e
        //            x + 3y = 6
        float[][] padronizado =  
        {
                { 1,   1,    1,  0,   4},
                { 1,   3,    0,  1,   6},
                {-3,  -5,    0,  0,   0}
        };
        
        // linha e coluna não incluem
        // os valores do lado direito
        // e a linha objetivo
        Simplex simplex = new Simplex(2, 4);
        
        simplex.preencherTabela(padronizado);

        // imprime a tabela
        System.out.println("---Conjunto inicial---");
        simplex.print();
        
        // se a tabela não for ótima, reitere
        while(!sair)
        {
            Simplex.ERRO err = simplex.calcular();

            if(err == Simplex.ERRO.OTIMO)
            {
                simplex.print();
                sair = true;
            }
            else if(err == Simplex.ERRO.INDETERMIANDA)
            {
                System.out.println("---Solução é indeterminada---");
                sair = true;
            }
        }
    } 
    
}