package com.mini;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FirstFitAllocationGUI extends JFrame {
    private JTextArea memoryBlocksArea, processSizeArea, outputArea;
    private JButton allocateButton, clearButton;

    private ArrayList<MemoryBlock> memoryBlocks;
    private ArrayList<Process> processes;

    public FirstFitAllocationGUI() {
        setTitle("First Fit Memory Allocation Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panels
        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        JPanel memoryPanel = new JPanel(new BorderLayout());
        memoryPanel.setBorder(BorderFactory.createTitledBorder("Memory Block Sizes (e.g., 200 300 400)"));
        memoryBlocksArea = new JTextArea(3, 20);
        memoryPanel.add(new JScrollPane(memoryBlocksArea), BorderLayout.CENTER);

        JPanel processPanel = new JPanel(new BorderLayout());
        processPanel.setBorder(BorderFactory.createTitledBorder("Process Sizes (e.g., 120 250 50)"));
        processSizeArea = new JTextArea(3, 20);
        processPanel.add(new JScrollPane(processSizeArea), BorderLayout.CENTER);

        inputPanel.add(memoryPanel);
        inputPanel.add(processPanel);
        add(inputPanel, BorderLayout.WEST);

        // Output Panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Output"));
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(outputPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        allocateButton = new JButton("Allocate Memory");
        clearButton = new JButton("Clear");
        buttonPanel.add(allocateButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        allocateButton.addActionListener(new AllocateActionListener());
        clearButton.addActionListener(e -> clearAll());
    }

    private class AllocateActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Parse memory blocks
                memoryBlocks = new ArrayList<>();
                String[] memoryInput = memoryBlocksArea.getText().split("\\s+");
                for (String size : memoryInput) {
                    memoryBlocks.add(new MemoryBlock(Integer.parseInt(size)));
                }

                // Parse processes
                processes = new ArrayList<>();
                String[] processInput = processSizeArea.getText().split("\\s+");
                int processCounter = 1;
                for (String size : processInput) {
                    processes.add(new Process("Process " + processCounter++, Integer.parseInt(size)));
                }

                // Simulate First Fit Allocation
                simulateFirstFit();

                // Display results
                displayResults();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid input! Please enter integers only.");
            }
        }
    }

    private void simulateFirstFit() {
        for (Process process : processes) {
            boolean allocated = false;
            for (int i = 0; i < memoryBlocks.size(); i++) {
                MemoryBlock block = memoryBlocks.get(i);
                if (block.isFree && block.size >= process.size) {
                    process.allocatedBlock = i + 1; // Assign block number (1-based index)
                    block.isFree = false;          // Mark block as busy
                    allocated = true;
                    break; // Stop searching for blocks after allocation
                }
            }
            if (!allocated) {
                process.allocationError = "No suitable block found or all blocks are busy.";
            }
        }
    }

    private void displayResults() {
        StringBuilder result = new StringBuilder("Process\tSize\tBlock No.\tStatus\n");
        for (Process process : processes) {
            result.append(process.name).append("\t")
                  .append(process.size).append("\t")
                  .append(process.allocatedBlock == -1 ? "N/A" : "Block " + process.allocatedBlock).append("\t")
                  .append(process.allocationError.isEmpty() ? "Allocated" : process.allocationError)
                  .append("\n");
        }

        result.append("\nFinal Memory State:\n");
        for (int i = 0; i < memoryBlocks.size(); i++) {
            MemoryBlock block = memoryBlocks.get(i);
            result.append("Block ").append(i + 1).append(": ")
                  .append(block.isFree ? "Free" : "Busy")
                  .append("\n");
        }

        outputArea.setText(result.toString());
    }

    private void clearAll() {
        memoryBlocksArea.setText("");
        processSizeArea.setText("");
        outputArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FirstFitAllocationGUI gui = new FirstFitAllocationGUI();
            gui.setVisible(true);
        });
    }
}
