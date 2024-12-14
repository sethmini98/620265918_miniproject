package com.mini;

public class Process {
    String name;
    int size;
    int allocatedBlock;
    String allocationError; // Field to store allocation error messages

    public Process(String name, int size) {
        this.name = name;
        this.size = size;
        this.allocatedBlock = -1; // -1 indicates not allocated
        this.allocationError = ""; // Initialize with no error
    }
}
