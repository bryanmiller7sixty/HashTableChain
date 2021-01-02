package edu.miracosta.cs113;


import java.util.*;
/** Hash table implementation using chaining. */
class HashTableChain<K, V> implements Map<K,V>{
    // Insert inner class Entry<K, V> here.
    /** The table */
    private LinkedList<Entry<K, V>>[] table;
    /** The number of keys */
    private int numKeys;
    /** The capacity */
    private static final int CAPACITY = 101;
    /** The maximum load factor */
    private static final double LOAD_THRESHOLD = 3.0;
    // Constructor
    public HashTableChain() {
        table = new LinkedList[CAPACITY];
    }
    /** Contains key-value pairs for a hash table. */
    private static class Entry<K, V> implements Map.Entry<K, V> {

        /** The key */
        private K key;
        /** The value */
        private V value;

        /**
         * Creates a new key-value pair.
         * @param key The key
         * @param value The value
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Retrieves the key.
         * @return The key
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * Retrieves the value.
         * @return The value
         */
        @Override
        public V getValue() {
            return value;
        }

        /**
         * Sets the value.
         * @param val The new value
         * @return The old value
         */
        @Override
        public V setValue(V val) {
            V oldVal = value;
            value = val;
            return oldVal;
        }
        public String toString(){
            return key + "=" + value;
        }
// Insert solution to programming exercise 3, section 4, chapter 7 here
    }
    @Override
    public int size() {
       return numKeys;
    }
    @Override
    public boolean isEmpty() {
        return numKeys == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int index = key.hashCode() % table.length;
        if (index < 0){
            index += table.length;
        }
        if (table[index] == null) {
            return false;
        }
        // Search the list at table[index] to find the key.
        LinkedList temp = table[index];
        for(int i = 0; i  < temp.size(); i++ ){
            Entry<K,V> temp2 = (Entry<K, V>) temp.get(i);
            if(temp2.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        // Search the list at table[index] to find the key.

        for(int i = 0; i  < table.length; i++){
            if(table[i] != null){
                LinkedList temp = table[i];
                for(int x = 0; x < table[i].size();x++ ){
                    Entry<K,V> temp2 = (Entry<K, V>) temp.get(x);
                    if(temp2.getValue().equals(value)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public V get(Object key) {
        int index = key.hashCode() % table.length;
        if (index < 0)
            index += table.length;
        if (table[index] == null)
            return null; // key is not in the table.
        // Search the list at table[index] to find the key.
        for (Entry<K, V> nextItem : table[index]) {
            if (nextItem.getKey().equals(key))
                return nextItem.getValue();
        }
        // assert: key is not in the table.
        return null;
    }

    @Override
    public V put(K key, V value) {
        int index = key.hashCode() % table.length;
        if (index < 0)
            index += table.length;
        if (table[index] == null) {
            // Create a new linked list at table[index].
            table[index] = new LinkedList<Entry<K, V>>();
            //table[index] = new LinkedList<>();
        }

        // Search the list at table[index] to find the key.
        for (Entry<K, V> nextItem : table[index]) {
            // If the search is successful, replace the old value.
            if (nextItem.getKey().equals(key)) {
                // Replace value for this key.
                V oldVal = nextItem.getValue();
                nextItem.setValue(value);
                return oldVal;
            }
        }
        // assert: key is not in the table, add new item.
        table[index].addFirst(new Entry<>(key, value));
        numKeys++;
        if (numKeys > (LOAD_THRESHOLD * table.length)){
            rehash();
        }
        return null;
    }
    public void rehash(){
        LinkedList<Entry<K, V>>[] temp = table;
        numKeys = 0;
        table = new LinkedList[temp.length * 2 + 1];
        for(int i = 0; i < temp.length; i++){
            for(int x = 0; temp[i]!= null && x < temp[i].size(); x++){
                put(temp[i].get(x).getKey(), temp[i].get(x).getValue());
            }
        }
    }
    @Override
    public V remove(Object key) {
        int index = key.hashCode() % table.length;
        if(table[index] == null){
            return null;
        }else{
            for(int i = 0; i < table[index].size(); i++){
                if(table[index].get(i).getKey().equals(key)){
                    V temp = table[index].get(i).getValue();
                    table[index].remove(i);
                    return temp;
                }
            }
        }
        return null;
    }
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        //no put all
    }

    public boolean equals(Object two){
        Map<K,V> twoHash = (Map<K, V>)two;
        Set<K> tempSet1 = this.keySet();
        TreeSet<K> tempTree = new TreeSet<>(tempSet1);
        Set<K> tempSet2 = twoHash.keySet();
        TreeSet<K> tempTree2 = new TreeSet<>(tempSet2);

        return tempTree.equals(tempTree2);
    }
    @Override
    public void clear() {
        numKeys = 0;
        table = new LinkedList[table.length];
    }
    public int hashCode(){
        Map<V, K> other = new Hashtable<>();
        for(int i = 0; i < table.length; i++){
            if(table[i] != null){
                for(int x = 0;  x < table[i].size(); x++){
                    other.put(table[i].get(x).getValue(), table[i].get(x).getKey());
                }
            }
        }
        int sum = other.hashCode();
        return sum;
    }
    @Override
    public Set<K> keySet() {
        Set<K> tempSet = new HashSet<>();
        for(int i = 0; i < table.length; i++){
           for(int x = 0; table[i] != null && x < table[i].size(); x++){
               tempSet.add(table[i].get(x).getKey());
            }
        }
        return tempSet;
    }

    @Override
    public Collection<V> values() {
        return null;
        //no values
    }
    @Override
    //Iterator<Map.Entry<K, V>> iter = myMap.entrySet().iterator();
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }
    private class SetIterator implements Iterator<Map.Entry<K, V>>{
        int index;
        Iterator<Entry<K, V>> iter = null;
        Entry<K, V> lastitemReturned = null;
        public SetIterator(){
            this.index = 0;
            for(int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    this.index = i;
                    break;
                }
            }
        }
        @Override
        public boolean hasNext() {
            if(iter != null && iter.hasNext()){
                return true;
            }
            for(int i = index + 1; i < table.length; i++){
                if(table[i] != null){
                    return true;
                }
            }
            return false;
        }
        @Override
        public Entry<K, V> next() {
            if(iter == null){
                for (index = 0; index < table.length; index++) {
                    if (table[index] != null) {
                        iter = table[index].iterator();
                        lastitemReturned = iter.next();
                        return lastitemReturned;
                    }
                }
                throw new NoSuchElementException();
            }else if(iter.hasNext()){
                lastitemReturned = iter.next();
                return lastitemReturned;
            }else if(!iter.hasNext()){
                for(index = index + 1; index < table.length; index++){
                    if(table[index] != null){
                        iter = table[index].iterator();
                        lastitemReturned = iter.next();
                        return lastitemReturned;
                    }
                }
                throw new NoSuchElementException();
            }
            else{
                throw new NoSuchElementException();
            }
        }
        @Override
        public void remove(){
            if(lastitemReturned == null){
                throw new IllegalStateException();
            }else{
                HashTableChain.this.remove(lastitemReturned.key);
                lastitemReturned = null;
            }
        }
    }
    /** Inner class to implement the set view. */
    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        /** Return the size of the set. */
        @Override
        public int size() {
            return numKeys;
        }
        /** Return an iterator over the set. */
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new SetIterator();
        }
    }
    public static void main(String args[]){
        HashTableChain<String, Integer> newHash = new HashTableChain<>();
        Map<String, Integer> other = new Hashtable<String, Integer>();
        newHash.put("one", 1);
        other.put("one", 1);
        System.out.println(newHash.hashCode());

        //System.out.println(other.hashCode());
    }
}