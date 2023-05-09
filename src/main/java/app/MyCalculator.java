package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MyCalculator {

    public void createGUI() {
        JFrame frame = new JFrame("Калькулятор");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(createMainPanel());
        frame.setVisible(true);
    }

    //метод передающий шрифт для калькулятора
    public Font loadDigitalFont(float size) {
        try {
            InputStream fontStream = getClass().getResourceAsStream("/fonts/digital-7.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            return baseFont.deriveFont(size);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, (int)size);
        }
    }


    public JPanel createMainPanel() {
        //создаем общую панель
        JPanel panel = new JPanel(new BorderLayout());

        //создаем текстовое поле
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(350, 80));
        Font digitalFont = loadDigitalFont(50);
        textField.setFont(digitalFont);
        textField.setBackground(new Color(197, 235, 156));
        panel.add(textField, BorderLayout.NORTH);

        //создаем панель с кнопками
        JPanel buttonsPanel = new JPanel(new GridLayout(4, 4));
        String[] buttons = {"7", "8", "9", "+", "4", "5", "6", "-", "1", "2", "3", "*", "0", ".", "C", "/"};

        //добавляем кнопки на панель с кнопками, а затем на общую панель
        for (String text : buttons){
            JButton button = new JButton(text);
            button.addActionListener(createButtonListener(textField, text));
            buttonsPanel.add(button);
        }
        panel.add(buttonsPanel, BorderLayout.CENTER);

        //Добавляем кнопку "=" вниз
        JButton equal = new JButton("=");
        equal.setPreferredSize(new Dimension(350, 70));
        equal.addActionListener(e -> textField.setText(evaluateExpression(textField.getText())));
        panel.add(equal, BorderLayout.SOUTH);

        return panel;
    }

    private ActionListener createButtonListener(JTextField textField, String text) {
        return e -> {
            if (text.matches("[0-9]")) {
                textField.setText(textField.getText() + text);
            } else if (text.equals("C")) {
                textField.setText("");
            } else if (text.equals("=")) {
                textField.setText(String.valueOf(evaluateExpression(textField.getText())));
            } else {
                textField.setText(textField.getText() + " " + text + " ");
            }
        };
    }

    private String evaluateExpression(String expression) { //expression - строка принемающая математическое выражение
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            Object result = engine.eval(expression);
            return result.toString();
        } catch (ScriptException e) {
            e.printStackTrace();
            return "Ошибка";
        }
    }


}


