package net.qsef1256.dacobot.struct;

import java.util.*;
import java.util.function.BiConsumer;

// TODO: Map 또는 Iterable 구현하기 또는 새로운 인터페이스 만들기

/**
 * {@code Map&lt;T, Map&lt;S, ?&gt;&gt;} 꼴의 중첩된 맵입니다.
 *
 * <p>
 * 제네릭 파라미터는 키의 타입입니다.<br>
 * 두개의 키를 가집니다. 메인 키로 서브 키들을 검색할 수 있습니다.
 * </p>
 *
 * <p>서브 키는 메인 키에 종속됩니다. 메인 키를 삭제하면 메인 키의 서브 키도 전부 삭제됩니다.</p>
 * <p>생성: <pre>{@code
 * NestedMap<String, String> table = new NestedMap<>();
 * }</pre>
 *
 * <p><b>Non Thread-Safe</b>
 *
 * @param <T> Type of main Key
 * @param <S> Type of sub Key
 * @see Map
 * @see com.google.common.collect.Table
 */
@SuppressWarnings("unchecked")
public class NestedMap<T, S> {

    private final Map<T, Map<S, ?>> keyMap = new HashMap<>();

    /**
     * 저장된 메인 키의 숫자를 반환합니다.
     *
     * @return main Key size
     * @see #size(T)
     */
    public int size() {
        return keyMap.size();
    }

    /**
     * 지정된 서브 키의 숫자를 반환합니다.
     *
     * @param key main Key
     * @return sub Key size
     * @see #size()
     */
    public int size(T key) {
        return keyMap.get(key).size();
    }

    /**
     * 해당 메인 키가 있는지 찾습니다.
     *
     * @param key main Key
     * @return true if main key is exist
     * @see #contains(T, S)
     */
    public boolean contains(T key) {
        return keyMap.containsKey(key);
    }

    /**
     * 해당 값이 있는지 찾습니다.
     *
     * @param key    main Key
     * @param subKey sub Key
     * @return true if value is exist
     * @see #contains(T)
     */
    public boolean contains(T key, S subKey) {
        return keyMap.get(key).containsKey(subKey);
    }

    /**
     * 메인 키에 해당하는 서브키 - 값 Map 을 반환합니다.
     * <p>값이 없을 경우 null 을 반환합니다.</p>
     * <p>예시:
     * <pre>{@code
     * table.getValueMap("a").forEach((key, value) -> {
     *     System.out.println(key + " " + value);
     * }
     * }</pre>
     *
     * <p>결과:
     * <pre>{@code
     * user asdf
     * painter net.qsef1256.diabot.game.paint.model.painter.Painter@d83da2e
     * asdf true
     * }</pre>
     *
     * @param key main Key
     * @return main Key's value map
     * @see #contains(T)
     * @see #get(T, S)
     */
    public Map<S, ?> get(T key) {
        return keyMap.get(key);
    }

    /**
     * 값을 가져옵니다. 값이 없을 경우 null 이 반환됩니다.
     * <p><b>가져온 값은 캐스팅 되어야 합니다.</b>
     * <p>예시:
     * <pre>{@code
     * Painter painter = table.get("a","painter");
     * (boolean) table.get("a", "asdf");
     * }</pre>
     *
     * @param key    main Key
     * @param subKey sub Key
     * @param <V>    Type of value
     * @return value
     * @see #get(T)
     */
    public <V> V get(T key, S subKey) {
        return (V) keyMap.get(key).get(subKey);
    }

    /**
     * 값을 넣습니다.
     *
     * <p>예시:
     * <pre>{@code
     * table.put("a", "user", "asdf");
     * table.put("a", "painter", new Painter());
     * table.put("a", "asdf", true);
     * table.put("b", "asdf", true);
     * }</pre>
     *
     * @param <V>    Type of value
     * @param key    main Key
     * @param subKey sub Key
     * @param value  value
     * @return put value
     * @see #putAll(T, Map)
     */
    public <V> V put(T key, S subKey, V value) {
        Map<S, V> subMap = keyMap.containsKey(key) ? (Map<S, V>) keyMap.get(key) : new HashMap<>();
        V replaced = subMap.put(subKey, value);

        if (keyMap.containsKey(key)) keyMap.replace(key, subMap);
        else keyMap.put(key, subMap);
        return replaced;
    }

    /**
     * 여러 개의 값을 넣습니다.
     *
     * @param key    main Key
     * @param subMap subMap with subKey, value
     * @return put subMap
     * @see #put(T, S, Object)
     */
    public Map<S, ?> putAll(T key, Map<S, ?> subMap) {
        return keyMap.put(key, subMap);
    }

    /**
     * 맵을 전부 비웁니다.
     */
    public void clear() {
        keyMap.clear();
    }

    /**
     * 메인 키에 해당 하는 값들을 모두 지웁니다. <b>하위 값들도 모두 지워집니다.</b>
     *
     * @param key main Key
     * @return Removed subMap
     * @see #remove(T, S)
     */
    public Map<S, ?> remove(T key) {
        return keyMap.remove(key);
    }

    /**
     * 해당 값을 지웁니다.
     *
     * @param key    main Key
     * @param subKey sub Key
     * @param <V>    Type of value
     * @return Removed value
     * @see #remove(T)
     */
    public <V> V remove(T key, S subKey) {
        Map<S, V> subMap = (Map<S, V>) keyMap.get(key);
        V removed = subMap.remove(subKey);
        keyMap.replace(key, subMap);

        return removed;
    }

    /**
     * 해당 메인 키의 값들을 바꿉니다.
     *
     * @param key    main Key
     * @param subMap sub Key-value Map
     * @return replaced subMap
     * @see #replace(T, S, Object)
     */
    public Map<S, ?> replace(T key, Map<S, ?> subMap) {
        return keyMap.replace(key, subMap);
    }

    /**
     * 해당 값을 바꿉니다.
     *
     * @param key    main Key
     * @param subKey sub Key
     * @param value  value
     * @param <V>    Type of value
     * @return replaced Value
     * @see #replace(T, Map)
     */
    public <V> V replace(T key, S subKey, V value) {
        Map<S, V> subMap = keyMap.containsKey(key) ? (Map<S, V>) keyMap.get(key) : new HashMap<>();
        V replaced = subMap.replace(subKey, value);

        if (keyMap.containsKey(key)) keyMap.replace(key, subMap);
        else keyMap.put(key, subMap);
        return replaced;
    }

    /**
     * 메인 키들을 가져옵니다.
     * <p>예시:
     * <pre>{@code
     * table.keySet(); // result: a, b
     * }</pre>
     *
     * @return Set of main keys
     */
    public Set<T> keySet() {
        return keyMap.keySet();
    }

    /**
     * 서브 키들을 가져옵니다.
     * <p>예시:
     * <pre>{@code
     * table.subKeySet("a"); // result: user, painter, asdf
     * }</pre>
     *
     * @param key main Key
     * @return Set of sub keys
     */
    public Set<S> keySet(T key) {
        return keyMap.get(key).keySet();
    }

    /**
     * 메인 키의 서브 맵들을 가져옵니다.
     *
     * @return List of subMaps
     */
    public Collection<?> values() {
        return keyMap.values();
    }

    /**
     * 서브 키의 값들을 가져옵니다.
     *
     * @param key main Key
     * @return subMap's values
     */
    public Collection<?> values(T key) {
        return keyMap.get(key).values();
    }

    /**
     * {@code Entry&lt;T, Map&lt;S, ?&gt;&gt;} 를 담고 있는 iterator 를 구합니다.
     *
     * @return iterator
     * @see #forEach(BiConsumer)
     */
    public Iterator<Map.Entry<T, Map<S, ?>>> iterator() {
        return keyMap.entrySet().iterator();
    }

    /**
     * mainKey 와 subMap 을 대상으로 반복합니다.
     *
     * <p><b>주의!</b> 순서를 보장하지 않습니다.
     *
     * <p>예시:
     * <pre>{@code
     * table.forEach(mainKey, subMap ->
     *     subMap.forEach((subKey, value) ->
     *         System.out.println(mainKey + " " + subKey + " " + value)));
     * }</pre>
     *
     * <p>결과:
     * <pre>{@code
     * a painter net.qsef1256.diabot.game.paint.model.painter.Painter@d83da2e
     * a asdf true
     * a user asdf
     * b asdf true
     * }</pre>
     */
    public void forEach(BiConsumer<T, ? super Map<S, ?>> action) {
        keyMap.forEach(action);
    }

    /**
     * {@code Entry&lt;T, Map&lt;S, ?&gt;&gt;} 를 담고 있는 spliterator 를 구합니다.
     *
     * @return spliterator
     * @see #forEach(BiConsumer)
     */
    public Spliterator<Map.Entry<T, Map<S, ?>>> spliterator() {
        return keyMap.entrySet().spliterator();
    }

    /**
     * keyMap 의 hashCode 를 반환합니다.
     *
     * @return haseCode
     * @see #equals(Object)
     */
    @Override
    public int hashCode() {
        return keyMap.hashCode();
    }

    /**
     * 동일성을 비교합니다.
     * <p><b>주의:</b> 기본적인 {@link #hashCode()} 를 사용하기 때문에, {@link Object#equals(Object)} 를 재정의 하지 않는 객체가 들어온 경우
     * * (ex: 배열) false 가 반환 될 수 있습니다.</p>
     *
     * @param o object to compare
     * @return true if equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (this.getClass() != o.getClass()) return false;

        if (o.hashCode() == this.hashCode()) return true;
        return false;
    }

}
