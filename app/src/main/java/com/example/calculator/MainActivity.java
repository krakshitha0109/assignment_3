package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    TextView tvInput;
    String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInput = findViewById(R.id.tvInput);

        // Number buttons
        int[] numberIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
                R.id.btn8, R.id.btn9, R.id.btn00
        };

        View.OnClickListener numberListener = v -> {
            Button b = (Button) v;
            input += b.getText().toString();
            tvInput.setText(input);
        };

        for (int id : numberIds) {
            findViewById(id).setOnClickListener(numberListener);
        }

        // Dot
        findViewById(R.id.btnDot).setOnClickListener(v -> {
            if (input.isEmpty() || endsWithOperator()) {
                input += "0.";
            } else if (!getLastNumber().contains(".")) {
                input += ".";
            }
            tvInput.setText(input);
        });

        // Operators
        setOperator(R.id.btnPlus, "+");
        setOperator(R.id.btnMinus, "-");
        setOperator(R.id.btnMul, "*");
        setOperator(R.id.btnDiv, "/");
        setOperator(R.id.btnPercent, "%");

        // Clear
        findViewById(R.id.btnAC).setOnClickListener(v -> {
            input = "";
            tvInput.setText("0");
        });

        // Delete
        findViewById(R.id.btnDel).setOnClickListener(v -> {
            if (input.length() > 0) {
                input = input.substring(0, input.length() - 1);
                tvInput.setText(input.isEmpty() ? "0" : input);
            }
        });

        // Equal (=)
        findViewById(R.id.btnEqual).setOnClickListener(v -> {
            try {
                double result = evaluateExpression(input);
                tvInput.setText(String.valueOf(result));
                input = String.valueOf(result);
            } catch (Exception e) {
                tvInput.setText("Error");
                input = "";
            }
        });
    }

    private void setOperator(int btnId, String op) {
        findViewById(btnId).setOnClickListener(v -> {
            if (!input.isEmpty() && !endsWithOperator()) {
                input += op;
                tvInput.setText(input);
            }
        });
    }

    private boolean endsWithOperator() {
        return input.endsWith("+") || input.endsWith("-") ||
                input.endsWith("*") || input.endsWith("/") ||
                input.endsWith("%");
    }

    private String getLastNumber() {
        String[] parts = input.split("[+\\-*/%]");
        return parts[parts.length - 1];
    }

    // ✅ Expression Evaluator (no external libs)
    private double evaluateExpression(String expr) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expr.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else if (eat('%')) x %= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = pos;
                if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expr.substring(startPos, pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }
}