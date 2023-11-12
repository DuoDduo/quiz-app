package QuizApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class QuizApp extends JFrame implements ActionListener {
    private final List<Question> questions;
    private int currentQuestionIndex;

    private final JLabel questionLabel;
    private final List<JRadioButton> answerRadioButtons;
    private final JButton nextBtn;
    private final JButton prevBtn;
    private final JButton submitBtn;

    private ButtonGroup radioButtonGroup;

    public QuizApp() {
        setTitle("Artificial Intelligence Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        questions = new ArrayList<>();
        questions.add(new Question("Who invented C#?",
                new String[]{"Mark Zuckerberg", "Bill Gates", "Anders Hejlsberg", "Steve Jobs"}, 2));
        questions.add(new Question("Who invented GitHub?",
                new String[]{"Linus Torvalds", "Chris Wanstrath", "Elon Musk", "Jeff Bezos"}, 1));
        questions.add(new Question("Who invented Java?",
                new String[]{"Larry Page", "James Gosling", "Bill Gates", "Ada Lovelace"}, 1));
        questions.add(new Question("Who invented JavaScript?",
                new String[]{"Brendan Eich", "Tim Berners-Lee", "Jeff Bezos", "Grace Hopper"}, 0));


        currentQuestionIndex = 0;

        questionLabel = new JLabel();
        answerRadioButtons = new ArrayList<>();
        nextBtn = new JButton("Next");
        prevBtn = new JButton("Previous");
        submitBtn = new JButton("Submit");

        setLayout(new BorderLayout());

        radioButtonGroup = new ButtonGroup();

        JPanel questionPanel = new JPanel(new FlowLayout());
        questionPanel.add(questionLabel);
        add(questionPanel, BorderLayout.NORTH);

        JPanel answerPanel = new JPanel(new GridLayout(0, 1, 0, 10)); // Adjusted spacing
        for (int i = 0; i < 4; i++) {
            JRadioButton radioButton = new JRadioButton();
            answerRadioButtons.add(radioButton);
            answerPanel.add(radioButton);
            radioButtonGroup.add(radioButton); // Add radio button to the group
        }

        // Adjust button dimensions
        nextBtn.setPreferredSize(new Dimension(80, 30));
        prevBtn.setPreferredSize(new Dimension(80, 30));
        submitBtn.setPreferredSize(new Dimension(80, 30));

        answerPanel.add(prevBtn);
        answerPanel.add(nextBtn);
        answerPanel.add(submitBtn);
        add(answerPanel, BorderLayout.CENTER);

        prevBtn.addActionListener(this);
        nextBtn.addActionListener(this);
        submitBtn.addActionListener(this);

        loadQuestion(currentQuestionIndex);
    }

    private void loadQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            Question question = questions.get(index);
            questionLabel.setText(question.getQuestion());
            for (int i = 0; i < answerRadioButtons.size(); i++) {
                JRadioButton radioButton = answerRadioButtons.get(i);
                radioButton.setText(question.getChoices()[i]);
                radioButton.setSelected(i == question.getUserAnswer());
            }
        }
    }

    private void updateAnswer() {
        for (int i = 0; i < answerRadioButtons.size(); i++) {
            if (answerRadioButtons.get(i).isSelected()) {
                questions.get(currentQuestionIndex).setUserAnswer(i);
                break;
            }
        }
    }

    private void goToQuestion(int index) {
        currentQuestionIndex = index;
        loadQuestion(currentQuestionIndex);
    }

    private void nextQuestion() {
        updateAnswer();
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            loadQuestion(currentQuestionIndex);
        } else {
            showResults();
        }
    }

    private void prevQuestion() {
        updateAnswer();
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            loadQuestion(currentQuestionIndex);
        }
    }

    private void showResults() {
        int correctAnswers = 0;
        for (Question question : questions) {
            if (question.getUserAnswer() == question.getCorrectAnswerIndex()) {
                correctAnswers++;
            }
        }

        double percentageCorrect = (double) correctAnswers / questions.size() * 100;
        String resultMessage = "Quiz Finished!\n";
        resultMessage += "You answered " + correctAnswers + " out of " + questions.size() + " questions correctly.\n";
        resultMessage += "Percentage Correct: " + String.format("%.2f", percentageCorrect) + "%";

        JOptionPane.showMessageDialog(this, resultMessage);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == prevBtn) {
            prevQuestion();
        } else if (e.getSource() == nextBtn) {
            nextQuestion();
        } else if (e.getSource() == submitBtn) {
            updateAnswer();
            showResults();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            try {
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            new QuizApp().setVisible(true);
        });
    }
}

class Question {
    private String question;
    private String[] choices;
    private int correctAnswerIndex;
    private int userAnswer = -1;

    public Question(String question, String[] choices, int correctAnswerIndex) {
        this.question = question;
        this.choices = choices;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getChoices() {
        return choices;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public int getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(int userAnswer) {
        this.userAnswer = userAnswer;
    }
}
