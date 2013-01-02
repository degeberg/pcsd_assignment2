package keyValueBaseInterfaces;

import java.io.Serializable;

public class Pair<K, V> implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -4594021541262250026L;
    private K k;
    private V v;
    
    @SuppressWarnings("unused")
    private Pair()
    {
    }
    
    public Pair (K k, V v){
        this.k = k;
        this.v = v;
    }
    
    public K getKey(){
        return k;
    }
    
    public V getValue(){
        return v;
    }
    
    @Override
    public String toString() {
        return "(" + k + "," + v + ")";
    }
}
