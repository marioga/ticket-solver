package com.marioga.ticketsolver;

/**
 * 
 *This class represents the callables used by ClassicTickerSolverMP
 */

import java.util.Stack;
import java.util.concurrent.Callable;

public class TicketSolverCallable implements Callable<Iterable<String>> { 
    private String[] mTickets;
    private int mLength;
    private int mAmount;
    
    public TicketSolverCallable(String[] tickets, int length,
            int amount) {
        mTickets = tickets;
        mLength = length;
        mAmount = amount;
    } 
    
    @Override
    public Iterable<String> call() throws Exception {
        Stack<String> stack = new Stack<>();
        TicketSolver ts = new TicketSolver(mLength);
        int upperLimit = mTickets.length;
        if (mTickets[upperLimit - 1] == "") {
            upperLimit--;
        }
        for (int i = 0; i < upperLimit; i++) {
            String s = ts.solve(mTickets[i], mAmount);
            if (s != "") {
                s = mTickets[i] + " " + s;
            } else {
                s += mTickets[i] + " " + "NO SOLUTION";
            }
            stack.push(s);
        }
        return stack;
    }
}
