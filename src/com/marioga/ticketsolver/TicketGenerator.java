package com.marioga.ticketsolver;

/**
 * This class generates all the tickets of a given length using digits
 * from a particular alphabet (typically, "123456789").
 */

import java.util.Stack;

public class TicketGenerator {
    private static int sTicketCount = 0;
    private static char[] sAlphabet; 
    
    public static Iterable<String> generateTickets(int length,
            String alphabet, int start, int end) {
        Stack<String> stack = new Stack<>();
        sAlphabet = new char[alphabet.length()];
        for (int i = 0; i < alphabet.length(); i++) {
            sAlphabet[i] = alphabet.charAt(i);
        }
        generateTickets("", length, start, end, stack);
        return stack;
    }
    
    private static void generateTickets(String soFar,
            int unusedLength, int start, int end, 
            Stack<String> stack) {
        if (sTicketCount >= end) return;
        if (unusedLength == 0) {
            sTicketCount++;
            if (sTicketCount >= start) {
                stack.push(soFar);
            }
            return;
        }
        for (int i = 0; i < sAlphabet.length; i++) {
            generateTickets(soFar + sAlphabet[i],
                    unusedLength - 1, start, end, stack);
        }
    }
}
