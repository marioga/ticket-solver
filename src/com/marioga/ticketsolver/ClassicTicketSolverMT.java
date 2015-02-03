package com.marioga.ticketsolver;

/**
 * This class solves the "classical" ticket problems
 * (i.e., 6 digits and N = 100) from START to END, in lexicographical
 * order. It creates a pool of workers using up to NTHREAD threads.
 * In total, there are 9^6 = 531441 classical ticket problems.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClassicTicketSolverMT {
    private static final int TICKET_LENGTH = 6;
    private static final int AMOUNT = 100;
    private static final String ALPHABET = "123456789";
    
    private static final int START = 1;
    private static final int END = 100; 
    
    private static final int NTHREADS = 16;

    private static void writeToFile(String[] lines) {
        OutputStream os;
        try {
            os = new FileOutputStream("tickets"
                    + Integer.toString(TICKET_LENGTH) + "_" 
                    + Integer.toString(START) + "_"
                    + Integer.toString(END)
                    + ".txt");
            OutputStreamWriter osw = 
                    new OutputStreamWriter(os, "UTF-8");
            PrintWriter out = new PrintWriter(osw);
            for (int i = 0; i < lines.length; i++) {
                out.write(Integer.toString(i + START)
                        + " " + lines[i] + System.lineSeparator());
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
        List<Future<Iterable<String>>> list = new ArrayList<>();
        int numTasks = (END - START + 1) / NTHREADS;
        int remainder = END - START + 1 - numTasks * NTHREADS;
        String[] ticketsForCurrentTask = new String[numTasks + 1];
        int cont = 0;
        for (String ticket : 
            TicketGenerator.generateTickets(
                    TICKET_LENGTH, ALPHABET, START, END)) {
            ticketsForCurrentTask[cont] = ticket;
            cont++;
            if ((cont == numTasks + 1 && remainder > 0)
                    || (cont == numTasks && remainder == 0)) {
                if (remainder > 0) { 
                    remainder--;
                } else {
                    ticketsForCurrentTask[numTasks] = "";
                }
                Callable<Iterable<String>> worker = 
                        new TicketSolverCallable(
                                ticketsForCurrentTask.clone(),
                                TICKET_LENGTH,
                                AMOUNT);
                Future<Iterable<String>> submit = executor.submit(worker);
                list.add(submit);
                cont = 0;
            }
        }
        String[] lines = new String[END - START + 1];
        int line = 0;
        // Now retrieve the result
        for (Future<Iterable<String>> future : list) {
            try {
                for (String s : future.get()) {
                    lines[line++] = s;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        Arrays.sort(lines);
        writeToFile(lines);
    }
}
