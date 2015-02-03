package com.marioga.ticketsolver;

/**
 * This class solves the "classical" ticket problems
 * (i.e., 6 digits and N = 100) from START to END, in lexicographical
 * order. In total, there are 9^6 = 531441 classical ticket problems.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ClassicTicketSolver {
    private static final int TICKET_LENGTH = 6;
    private static final int AMOUNT = 100;
    private static final String ALPHABET = "123456789";
    private static final int START = 1;
    private static final int END = 5;
    
    public static void main(String[] args) {
        TicketSolver ts = new TicketSolver(TICKET_LENGTH);
        try {
            OutputStream os = new FileOutputStream(
                    "tickets6_100/tickets"
                    + Integer.toString(TICKET_LENGTH) + "_" 
                    + Integer.toString(START) + "_"
                    + Integer.toString(END)
                    + ".txt");
            OutputStreamWriter osw = 
                    new OutputStreamWriter(os, "UTF-8");
            PrintWriter out = new PrintWriter(osw);
            
            int cont = START - 1;
            for (String ticket : 
                TicketGenerator.generateTickets(
                        TICKET_LENGTH, ALPHABET, START, END)) {
                cont++;
                out.write(Integer.toString(cont) + " " + ticket + " ");
                String s = ts.solve(ticket, AMOUNT);
                if (s != "") {
                    out.write(s);
                } else {
                    out.write("NO SOLUTION");
                }
                
                out.write(System.lineSeparator());
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
