package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Myles Bohrer-Purnell
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V>[] pairs;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> copyArray = new AssociativeArray<K, V>();
    copyArray.size = this.size();
    for(int i = 0; i < this.size; i++){
      copyArray.expand();
      copyArray.pairs[i] = new KVPair<K,V>(this.pairs[i].key, this.pairs[i].val);
    }
    return copyArray;
  } // clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    String resultString = "";
    if(this.size == 0){
      return resultString;
    }
    for(int i = 0; i < this.size; i++){
      resultString = resultString.concat(pairs[i].toString());
    }
    return "{" + resultString + "}";
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value.
   *
   * @param key
   *   The key whose value we are seeting.
   * @param value
   *   The value of that key.
   *
   * @throws NullKeyException
   *   If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {
    if(key == null){
      throw new NullKeyException("ERROR: Null Key Provided");
    } else if(hasKey(key)){
      try {
        this.pairs[find(key)].val = value;
      } catch (KeyNotFoundException e) {
        // should not happen, does nothing
      } // try/catch
    } else {
      this.expand();
      this.pairs[this.size] = new KVPair<K,V>(key, value);
      this.size+=1;
    } // else
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key
   *   A key
   *
   * @return
   *   The corresponding value
   *
   * @throws KeyNotFoundException
   *   when the key is null or does not appear in the associative array.
   */
  public V get(K key) throws KeyNotFoundException {
    if(key == null){
      throw new KeyNotFoundException("ERROR: Null Key Provided");
    } 
    try {
      return this.pairs[find(key)].val;
    } catch (KeyNotFoundException e) {
      throw new KeyNotFoundException("ERROR: Key Not Found");
    }
  } // get(K)

  /**
   * Get a key at a specific index.
   *
   * @param index
   *   An index
   *
   * @return
   *   The corresponding key
   *
   * @throws IndexOutOfBoundsException
   *   when the index is out of the bound of the array
   */
  public K getKey(int index) throws IndexOutOfBoundsException {
    if(index > this.size - 1 || index < 0){
      throw new IndexOutOfBoundsException();
    } 
      return this.pairs[index].key;
  } // get(K)

  /**
   * Determine if key appears in the associative array. Should
   * return false for the null key, since it cannot appear.
   *
   * @param key
   *   The key we're looking for.
   *
   * @return true if the key appears and false otherwise.
   */
  public boolean hasKey(K key) {
    for(int i = 0; i < this.size; i++){
      if(this.pairs[i].key.equals(key)){
        return true;
      } // if
    } // for
    return false;
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls
   * to get(key) will throw an exception. If the key does not appear
   * in the associative array, does nothing.
   *
   * @param key
   *   The key to remove.
   */
  public void remove(K key) {
    if(hasKey(key)){
      try {
        this.pairs[find(key)] = this.pairs[this.size-1].clone();
        this.pairs[this.size-1] = new KVPair<K, V>();
        this.size-=1;
      } catch (KeyNotFoundException e) {
        // does nothing if exception is thrown
      }
    }
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   *
   * @return The number of key/value pairs in the array.
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws an exception.
   *
   * @param key
   *   The key of the entry.
   *
   * @return
   *   The index of the key, if found.
   *
   * @throws KeyNotFoundException
   *   If the key does not appear in the associative array.
   */
  int find(K key) throws KeyNotFoundException {
    for(int i = 0; i < this.size; i++){
      if(this.pairs[i].key.equals(key)){
        return i;
      } // if
    } // for
    throw new KeyNotFoundException("Key not found");
  } // find(K)

} // class AssociativeArray