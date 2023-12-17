package com.epam.rd.autocode.hashtableopen816;

public class HashtableOpen816Impl implements HashtableOpen8to16 {
    private static final int INITIAL_CAPACITY = 8;
    private static final int MAX_CAPACITY = 16;
    private Node[] table;
    private int size;

    public HashtableOpen816Impl() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void insert(int key, Object value) {
        int index = indexByKey(key);
        if (index >= 0) {
            table[index].value = value;
            return;
        }
        checkCapacity();
        if (size == table.length) {
            resize(table.length << 1);
        }
        index = Math.abs(key % table.length);
        Node node = new Node(key, value);
        if (table[index] == null) {
            table[index] = node;
        } else {
            insertLinear(index, node);
        }
        size++;
    }

    @Override
    public Object search(int key) {
        int nodeIndex = indexByKey(key);
        return nodeIndex > -1 ? table[nodeIndex].value :
                null;
    }

    @Override
    public void remove(int key) {
        int nodeIndex = indexByKey(key);
        if (nodeIndex != -1) {
            table[nodeIndex] = null;
            size--;
            if (needToDecrease()) {
                resize(table.length >> 1);
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int[] keys() {
        int[] keysArr = new int[table.length];
        for (int i = 0; i < table.length; i++) {
            keysArr[i] = table[i] == null ? 0 :
                    table[i].key;
        }
        return keysArr;
    }

    private void resize(int newLength) {
        Node[] temp = table;
        table = new Node[newLength];
        size = 0;
        for (Node node : temp) {
            if (node != null) {
                insert(node.key, node.value);
            }
        }
    }

    private void checkCapacity() {
        if (size == MAX_CAPACITY) {
            throw new IllegalStateException("There is no free space in table");
        }
    }

    private void insertLinear(int index, Node node) {
        int tempIndex = index;
        while ((tempIndex = (++tempIndex) % table.length) != index) {
            if (table[tempIndex] == null) {
                table[tempIndex] = node;
                return;
            }
        }
    }

    private int indexByKey(int key) {
        int startIndex = Math.abs(key % table.length);
        int counter = startIndex;
        do {
            if (table[counter] != null && table[counter].key == key) {
                return counter;
            }
        } while ((counter = (++counter) % table.length) != startIndex);
        return -1;
    }

    private boolean needToDecrease() {
        int sizeToCapacityRatio = table.length / 4;
        return sizeToCapacityRatio > 0
                && size <= sizeToCapacityRatio;
    }

    private static class Node {
        private final int key;
        private Object value;

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}
