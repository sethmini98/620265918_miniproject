package com.mini;


public class MemoryBlock {
    int size;
    boolean isFree;

    public MemoryBlock(int size) {
        this.size = size;
        this.isFree = true;
    }
}
