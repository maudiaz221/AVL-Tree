package Arboles;

public class AVL <T extends Comparable<T>>{

    Nodo<T> raiz;
    int cont;


    AVL(){
        cont = 0;
        raiz = null;

    }
    AVL(T elem){
        cont = 1;
        raiz = new Nodo<T>(elem);
    }



    public int altura(Nodo<T> nodo) {
        if (nodo == null)
            return 0;
        else {
            int m1 = altura(nodo.getDer()) + 1;
            int m2 = altura(nodo.getIzq()) + 1;
            if (m1 > m2)
                return m1;
            else
                return m2;
        }
    }

    public void actualizaFes(Nodo<T> nodo) {

        if(nodo!= null) {

            if (nodo.getIzq() != null && nodo.getDer() != null) {// tiene ambos hijos

                nodo.setFE(altura(nodo.getDer()) - altura(nodo.getIzq()));
            }
            else if (nodo.getIzq() != null) { // tiene solo hijo izquierdo


                nodo.setFE(-altura(nodo.getIzq()));
            }
            else if(nodo.getDer()!= null) { // tiene solo hijo derecho


                nodo.setFE(altura(nodo.getDer()));
            }
            else // no tiene ningun hijo
                nodo.setFE(0);
        }

    }

    public void actualizacionFesForzada(Nodo<T> nodo) {
        if(nodo == null)
            return;
        actualizaFes(nodo);
        actualizacionFesForzada(nodo.getIzq());
        actualizacionFesForzada(nodo.getDer());

    }

    public void inserta(T elem){
        Nodo<T> nuevo = new Nodo<>(elem);
        Nodo<T> actual = raiz;
        if(raiz == null){
            raiz = nuevo;
            cont++;
            return;
        }
        Nodo<T> papa = actual;
        boolean encontro = false;
        while(actual!=null && !encontro){
            papa = actual;
            if(actual.getElem().compareTo(elem) > 0)
                actual = actual.getIzq();
            else if(actual.getElem().compareTo(elem) == 0)
                encontro = true;
            else
                actual = actual.getDer();

        }
        if(!encontro){
            actual = papa;
            actual.cuelga(nuevo);
            cont++;
        }

        actual = nuevo;
        boolean termine = false;
        while(!termine){
            if(actual==raiz){
                termine = true;
            }
            else{
                papa = actual.getPapa();
                if(actual == papa.getIzq())
                    papa.setFE(papa.getFE() - 1);
                else
                    papa.setFE(papa.getFE() + 1);

                if(Math.abs(papa.getFE())==2) { //valor absoluto de papa.getFE() == 2
                    rota(papa);
                    termine = true;
                }
                if(papa.getFE()==0)
                    termine = true;

                actual = papa;
            }

        }





    }


    public boolean find(T elem) {
        Nodo<T> actual = find(raiz,elem);
        return actual!=null;
    }

    private Nodo<T> find(Nodo<T> actual, T dato){
        if(actual == null)
            return actual;
        if(actual.getElem().equals(dato))
            return actual;

        Nodo<T> temp = find(actual.getIzq(), dato);
        if(temp == null)
            temp = find(actual.getDer(), dato);
        return temp;
    }

    private Nodo<T> eliminaPorCasos(Nodo<T> nodo){
        if(nodo.getIzq() ==null && nodo.getDer()==null) {//caaso1
            if (nodo == raiz)
                raiz = null;
            else if(nodo == nodo.papa.getDer())//soy tu hijo izquierdo o derecho
                nodo.papa.setDer(null);
            else
                nodo.papa.setIzq(null);

            return nodo;

        }
        if(nodo.getIzq() == null || nodo.getDer() == null){
            if(nodo == raiz) {
                if (nodo.getIzq() != null)
                    raiz = nodo.getIzq();
                else
                    raiz = nodo.getDer();
            }
            else {
                if (nodo.getIzq() != null) {
                    nodo.papa.cuelga(nodo.getIzq());
                    return nodo.papa;
                }
                else
                    nodo.papa.cuelga(nodo.getDer());
                return nodo.papa;


            }
        }

        Nodo<T> actual = nodo.getDer();// caso 3
        while(actual.getIzq()!=null)
            actual = actual.getIzq();
        nodo.setElem(actual.getElem());
        if(actual!=nodo.getDer()) {
            actual.papa.setIzq(actual.getDer());
            if(actual.getDer()!=null)
                actual.getDer().setPapa(actual.papa);
        }
        else
            nodo.setDer(actual.getDer());


    return actual.papa;
    }

    public void borrar(T elem){
        Nodo<T> nodo = find(raiz, elem);
        if(nodo == null)
            return;

        nodo = eliminaPorCasos(nodo); //el nodo es el eliminado que sigue colgando
        //debes actualizaaar factor de equilibrio de papa del nodo eliminado antes dee entrar aal while

        nodo.setFE(altura(nodo.der)- altura(nodo.izq));
/*
        Nodo<T> aux = nodo.papa;
        if(aux.getIzq()== null)
            nodo.papa.setFE(nodo.papa.getFE() + 1);
        else if (aux.getDer()==null)
            nodo.papa.setFE(nodo.papa.getFE() - 1);

 */

        boolean termine;
        if(nodo.getFE()==1 || nodo.getFE()==-1)
             termine = true;
        else
           termine= false;
        while(!termine){
            Nodo<T> papa = nodo.getPapa();
            if(nodo == raiz)
                termine = true;
            if(termine !=true) {
                if (papa.getIzq() == nodo)
                    papa.setFE(papa.getFE() + 1);
                else
                    papa.setFE(papa.getFE() - 1);
                if (papa.getFE() == 2 || papa.getFE() == -2)
                    papa = rota(papa);
                if (papa.getFE() == 1 || papa.getFE() == -1)
                    termine = true;
            }

            nodo = papa;
        }






    }

    public void actualizaFE(Nodo<T> alfa, Nodo<T> beta, Nodo<T> gamma){
        int nuevoFe = altura(alfa.der)- altura(alfa.izq);
        alfa.setFE(nuevoFe);
        int nuevoFe1 = altura(beta.der)- altura(beta.izq);
        beta.setFE(nuevoFe1);
        int nuevoFe2 = altura(gamma.der) - altura(gamma.izq);
        gamma.setFE(nuevoFe2);
    }

    private Nodo<T> rota(Nodo<T> nodo){

            if (nodo.getFE() == -2 && nodo.getIzq().getFE() == 1) { //izquierda derecha
                Nodo<T> papa = nodo.papa;
                Nodo<T> alfa = nodo;
                Nodo<T> beta = alfa.izq;
                Nodo<T> D = alfa.der;
                Nodo<T> A = beta.izq;
                Nodo<T> gamma = beta.der;
                Nodo<T> B = gamma.izq;
                Nodo<T> C = gamma.der;

                beta.setIzq(A);
                if(A!=null)
                    A.setPapa(beta);

                beta.setDer(B);
                if(B!=null)
                    B.setPapa(beta);

                alfa.setIzq(C);
                if(C!=null)
                    C.setPapa(alfa);

                alfa.setDer(D);
                if(D!=null)
                    D.setPapa(alfa);

                gamma.cuelga(alfa);
                gamma.cuelga(beta);
                //actualiza fe

                if(alfa!=raiz) {
                    papa.cuelga(gamma);
                }
                else {
                    raiz = gamma;
                    gamma.setPapa(null);
                }
                gamma.setFE(0);
                actualizaFes(alfa);
                actualizaFE(alfa,beta,gamma);


                return gamma;
            }// faltaan los otros 3 casos

        if(nodo.getFE() == 2 && nodo.getDer().getFE() == -1){ //derecha izquierda
            Nodo<T> papa = nodo.papa;
            Nodo<T> alfa = nodo;
            Nodo<T> beta = alfa.der;
            Nodo<T> A = alfa.izq;
            Nodo<T> D = beta.der;
            Nodo<T> gamma = beta.izq;
            Nodo<T> B = gamma.izq;
            Nodo<T> C = gamma.der;

            alfa.setIzq(A);
            if(A!=null)
                A.setPapa(alfa);

            alfa.setDer(B);
            if(B!=null)
                B.setPapa(alfa);

            beta.setIzq(C);
            if(C!=null)
                C.setPapa(beta);

            beta.setDer(D);
            if(D!=null)
                D.setPapa(beta);

            gamma.cuelga(alfa);
            gamma.cuelga(beta);
            //actualiza fe
            if(alfa!=raiz) {
                papa.cuelga(gamma);
            }
            else {
                raiz = gamma;
                gamma.setPapa(null);
            }
            gamma.setFE(0);
            actualizaFes(alfa);
            actualizaFE(alfa,beta,gamma);

            return gamma;
        }//preguntar si izquierda ess nulo
        if(nodo.getFE() == 2 && ((nodo.getDer().getFE() == 1) || (nodo.getDer().getFE() == 0))){// derecha-derecha
            Nodo<T> papa = nodo.papa;
            Nodo<T> alfa = nodo;
            Nodo<T> beta = alfa.der;
            Nodo<T> A = alfa.izq;
            Nodo<T> B = beta.izq;
            Nodo<T> gamma = beta.der;
            Nodo<T> C = gamma.izq;
            Nodo<T> D = gamma.der;
            alfa.setIzq(A);
                if(A!=null)
                    A.setPapa(alfa);
            alfa.setDer(B);
                if(B!=null)
                    B.setPapa(alfa);
            gamma.setIzq(C);
                if(C!=null)
                    C.setPapa(gamma);
            gamma.setDer(D);
                if(D!=null)
                    D.setPapa(gamma);
            beta.cuelga(alfa);
            beta.cuelga(gamma);


            if(alfa!=raiz) {
                papa.cuelga(beta);
            }
            else {
                raiz = beta;
                beta.setPapa(null);
            }
            beta.setFE(0);
            actualizaFes(alfa);
            actualizaFE(alfa,beta,gamma);
            return beta;
        }
        if(nodo.getFE() == -2 && ((nodo.getIzq().getFE() == -1) || (nodo.getIzq().getFE() == 0))){//izquierda-izquierda
            Nodo<T> papa = nodo.papa;
            Nodo<T> alfa = nodo;
            Nodo<T> beta = alfa.izq;
            Nodo<T> D = alfa.der;
            Nodo<T> C = beta.der;
            Nodo<T> gamma = beta.izq;
            Nodo<T> A = gamma.izq;
            Nodo<T> B = gamma.der;

            gamma.setIzq(A);
            if(A!=null)
                A.setPapa(gamma);
            gamma.setDer(B);
            if(B!=null)
                B.setPapa(gamma);
            alfa.setIzq(C);
            if(C!=null)
                C.setPapa(alfa);
            alfa.setDer(D);
            if(D!=null)
                D.setPapa(alfa);

            if(alfa!=raiz) {
                papa.cuelga(beta);
            }
            else {
                raiz = beta;
                beta.setPapa(null);
            }
            beta.setFE(0);
            actualizaFes(alfa);
            actualizaFE(alfa,beta,gamma);

            return beta;
        }


        return nodo;
    }

    //imprimir nodos en un nivel dado
    public void imprimirNodosDeNivel(Nodo<T> nodo, int level, StringBuilder sB) {
        if (nodo == null)
            return;
        if (level == 1) {
            sB.append(nodo.toString() + " ");
        }
        else if (level > 1) {
            imprimirNodosDeNivel(nodo.izq, level - 1, sB);
            imprimirNodosDeNivel(nodo.der, level - 1, sB);
        }
    }

    /* function to print level order traversal of tree*/
    public String imprimeAVL() {
        StringBuilder sB = new StringBuilder();
        int h = altura(raiz);

        for (int i = 1; i <= h; i++) {
            imprimirNodosDeNivel(raiz, i,sB);
            System.out.print("\n");
        }
        return sB.toString();
    }




}
