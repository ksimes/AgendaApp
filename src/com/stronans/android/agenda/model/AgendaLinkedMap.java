/**
 *
 */
package com.stronans.android.agenda.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Very simple immutable extension to the Linked list hash map to allow the selection of an item from a map by index.
 * box.
 *
 * @author SimonKing
 */
public final class AgendaLinkedMap<K, V> extends LinkedHashMap<K, V> {

    public V getValue(int i) {

        Map.Entry<K, V> entry = this.getEntry(i);
        if (entry == null) return null;

        return entry.getValue();
    }

    public Map.Entry<K, V> getEntry(int i) {
        // check if negetive index provided

        Set<Entry<K, V>> entries = entrySet();
        int j = 0;

        for (Map.Entry<K, V> entry : entries)
            if (j++ == i) return entry;

        return null;

    }
}