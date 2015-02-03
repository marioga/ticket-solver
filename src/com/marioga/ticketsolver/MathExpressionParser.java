package com.marioga.ticketsolver;

/**
 * 
 * This helper class contains methods to evaluate an expression in 
 * Reverse Polish notation (RPN) and convert it to infix notation.
 */
import java.util.Stack;

public class MathExpressionParser {
    public static double evaluateRPN(String rpn) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = rpn.trim().split("\\s+");
        
        double o1;
        double o2;
        for (int i = 0; i < tokens.length; i++) {
            switch (tokens[i]) {
            case "+":
                stack.push(stack.pop() + stack.pop());
                break;
            case "-":
                o1 = stack.pop();
                o2 = stack.pop();
                stack.push(o2 - o1);
                break;
            case "*":
                stack.push(stack.pop() * stack.pop());
                break;
            case "/":
                o1 = stack.pop();
                o2 = stack.pop();
                stack.push(o2 / o1);
                break;
            case "^":
                o1 = stack.pop();
                o2 = stack.pop();
                stack.push(Math.pow(o2, o1));
                break;
            default:
                try {
                    stack.push(Double.parseDouble(tokens[i]));
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }     
        }
        return stack.pop();
    }
    
    public static String convertToInfixNotation(String rpn) {
        final String OPS = "-+/*^";
 
        class Expression {
            String repr;
            int precedence = 3;
 
            Expression(String e) {
                repr = e;
            }
 
            Expression(String e, int prec) {
                this(e);
                precedence = prec;
            }
        }
 
        Stack<Expression> expr = new Stack<>();
 
        for (String token : rpn.split("\\s+")) {
            char c = token.charAt(0);
            int idx = OPS.indexOf(c);
            if (idx != -1 && token.length() == 1) {
 
                Expression r = expr.pop();
                Expression l = expr.pop();
 
                int opPrec = idx / 2;
 
                if (l.precedence < opPrec
                        || (l.precedence == opPrec && idx == 4)) {
                    l.repr = '(' + l.repr + ')';
                }
 
                if (r.precedence < opPrec
                        || (r.precedence == opPrec && (idx == 0 || idx == 2))) {
                    r.repr = '(' + r.repr + ')';
                }
                
                if (idx == 4) {
                    expr.push(new Expression(l.repr + '^' + r.repr, opPrec));
                } else {
                    expr.push(new Expression(l.repr + " " + token + " " + r.repr, opPrec));
                }
 
                
            } else if (idx == 0){
                expr.push(new Expression('(' + token + ')'));
            } else {
                expr.push(new Expression(token));
            }
        }
        return expr.peek().repr;
    }
    
    public static void main(String[] args) {
        String expr = "2 3 4 * /";
        System.out.println(evaluateRPN(expr));
        System.out.println(convertToInfixNotation(expr));
    }
}
