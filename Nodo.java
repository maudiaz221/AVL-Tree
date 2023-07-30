package Arboles;

public class Nodo<T extends Comparable<T>>{

    T elem;
    Nodo<T> izq, der, papa;
    int FE;

    public Nodo(T elem) {
        this.elem = elem;
    }

    public Nodo<T> getPapa() {
        return papa;
    }

    public int getFE() {
        return FE;
    }

    public void setFE(int FE) {
        this.FE = FE;
    }

    public void setPapa(Nodo<T> papa) {
        this.papa = papa;
    }

    public T getElem() {
        return elem;
    }

    public void setElem(T elem) {
        this.elem = elem;
    }

    public Nodo<T> getIzq() {
        return izq;
    }

    public void setIzq(Nodo<T> izq) {
        this.izq = izq;
    }

    public Nodo<T> getDer() {
        return der;
    }

    public void setDer(Nodo<T> der) {
        this.der = der;
    }

    public int hijos(){
        int cont = 0;
        if(izq!=null)
            cont++;
        if(der!=null)
            cont++;
        return cont;
    }

    public int numDesc(){
        int cont = 0;
        if(izq!=null)
            cont = izq.numDesc() +1;
        if(der!=null)
            cont+=der.numDesc() + 1;
        return cont;
    }

    public void cuelga(Nodo<T> nuevo){
        if(nuevo == null)
            return;

        if(nuevo.getElem().compareTo(elem)<=0)
            izq = nuevo;
        else
            der = nuevo;
        nuevo.setPapa(this);
    }

    @Override
    public String toString() {
        return "\nNodo" +
                "\nelem=" + elem +
                " FE=" + FE
                ;
    }
}
