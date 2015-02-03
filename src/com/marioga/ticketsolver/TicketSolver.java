package com.marioga.ticketsolver;

/**
 * 
 * This class solves the generalized ticket problem: Given a sequence
 * d_1, ..., d_T of digits and a number N, find a mathematical expression
 * involving the digits in the same order as they are given, that evaluates
 * to N. The operations allowed are +, -, *, /, ^ and grouping of digits.
 * The user can choose between computing one solution, if it exists,
 * or all solutions (slower).
 */

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class TicketSolver {
    private static final String[] OPS = {"+", "-", "*", "/", "^"};
    private int[][] mNumberBlends;
    private String[][] mOperators;
    private String[][] mValidRPNs;
    private int mLength;
    
    public TicketSolver(int length) {
        mLength = length;
        computeOperatorStrings();
        computeNumberBlends();
        computeValidRPNs();
    }
    
    private void computeOperatorStrings() {
        mOperators = new String[mLength][]; // 0 is a sentinel
        mOperators[0] = new String[]{ "" };
        int cont;
        for (int i = 1; i < mLength; i++) {
            mOperators[i] = 
                    new String[OPS.length * mOperators[i - 1].length];
            cont = 0;
            for (String o : OPS) {
                for (int j = 0; j < mOperators[i - 1].length; j++) {
                    mOperators[i][cont++] = mOperators[i - 1][j] + o;
                }
            }
        }
    }
    
    private void computeNumberBlends() {
        mNumberBlends = 
                new int[(int) Math.pow(2, mLength - 1)][mLength];
        int temp;
        int idx;
        int cont = 0;
        for (int i = 0; i < Math.pow(2, mLength - 1); i++) {
            temp = i;
            idx = 0;
            cont = 1;
            for (int total = 0; total < mLength; total++) {
                if (temp % 2 == 0) {
                    mNumberBlends[i][idx++] = cont;
                    cont = 1;
                } else {
                    cont++;
                }
                temp /= 2;
            }
        }
    }
    
    private void findValidRPNs(int onesLeft, int zerosLeft,
            int diff, String soFar, Stack<String> stack) {
        if (onesLeft + zerosLeft == 0) {
            stack.push(soFar);
            return;
        }
        if (onesLeft > 0 && diff >= 2) {
            findValidRPNs(onesLeft - 1, zerosLeft,
                    diff - 1, soFar + "1", stack);
        }
        if (zerosLeft > 0) {
            findValidRPNs(onesLeft, zerosLeft - 1,
                    diff + 1, soFar + "0", stack);
        }
    }
    
    private void computeValidRPNs() {
        mValidRPNs = new String[mLength][]; // 0 is a sentinel
        mValidRPNs[0] = new String[]{ "0" };
        Stack<String> validRPNs;
        for (int i = 1; i < mLength; i++) {
            validRPNs = new Stack<String>();
            findValidRPNs(i, i + 1, 0, "", validRPNs);
            mValidRPNs[i] = new String[validRPNs.size()];
            for (int cont = 0; !validRPNs.isEmpty(); cont++) {
                mValidRPNs[i][cont] = validRPNs.pop();
            }
        }
    }
    
    private Iterable<String> solGenerator(String ticket, 
            double amount, boolean singleSolution) {
        Set<String> iter = new HashSet<>();
        String[] safeNums;
        String[] nums;
        int size;
        for (int i = 0; i < Math.pow(2, mLength - 1); i++) {
            size = 0;
            while (size < mLength && mNumberBlends[i][size] != 0) {
                size++;
            }
            safeNums = new String[size];
            int idx = 0;
            for (int j = 0; j < size; j++) {
                safeNums[j] = ticket.substring(idx, idx + mNumberBlends[i][j]);
                idx += mNumberBlends[i][j];
            }
            for (int j = 0; j < Math.pow(2, size); j++) {
                nums = safeNums.clone();
                int temp = j;
                // Sign changes
                for (int cont = 0; cont < size; cont++) {
                    if (temp % 2 == 1) {
                        nums[cont] = "-" + nums[cont];
                    }
                    temp /= 2;
                }
                for (int k = 0; k < mOperators[size - 1].length; k++) {
                    for (String s : mValidRPNs[size - 1]) {
                        int cont1 = 0;
                        int cont2 = 0;
                        StringBuilder sb = new StringBuilder();
                        for (int l = 0; l < 2 * size - 1; l++) {
                            if (s.charAt(l) == '1') {
                                sb.append(mOperators[size - 1][k].charAt(cont1++));
                            } else {
                                sb.append(nums[cont2++]);
                            }
                            if (l < 2 * size - 1) {
                                sb.append(" ");
                            }
                        }
                        if (Double.compare(MathExpressionParser.
                                evaluateRPN(sb.toString()), amount) == 0) {
                            iter.add(MathExpressionParser.convertToInfixNotation(sb.toString()));
                            if (singleSolution) {
                                return iter;
                            }
                        }
                    }
                }
            }
        }
        return iter;
    }
    
    public Iterable<String> solveAll(String ticket, double amount) {
        return solGenerator(ticket, amount, false);
    }
    
    public String solve(String ticket, double amount) {
        for (String s : solGenerator(ticket, amount, true)) {
            return s; 
        }
        return "";
    }
    
    public boolean isSolvable(String ticket, double amount) {
        return solGenerator(ticket, amount, true).iterator().hasNext();
    }
    
    public void printSolution(String ticket, double amount) {
        System.out.println(solve(ticket, amount));
    }
    
    public void printAllSolutions(String ticket, double amount) {
        for (String s : solveAll(ticket, amount)) {
            System.out.println(s);
        }
    }
    
    public static void main(String[] args) {
        TicketSolver ts = new TicketSolver(6);
        ts.printSolution("111111", 100);
    }
}
