package lab5.ExamSystem;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Question {
    private final String type;
    private final Element elem;
    public Question(String questiontype, Element element) {
        type = questiontype;
        elem = element;
    }
    int getDifficulty(){
        return Integer.parseInt(elem.getElementsByTagName("difficulty").item(0).getTextContent());
    }
    public static void reorderQuestions(List<Question> questions) {
        Comparator<Question> comp = Comparator.comparing(Question::getDifficulty);
        questions.sort(comp);
    }
    // print question tasks
    public static StringBuilder print(List<Question> questions) {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < questions.size(); i++){
            Question q = questions.get(i);
            res.append(i + 1 + ") ");
            res.append(q.elem.getElementsByTagName("questiontext").item(0).getTextContent() + "\n");
            switch (q.type) {
                case ("short"):
                    res.append("Answer: ____________________\n");
                    break;
                case ("truefalse"):
                    res.append("Answer: true false (circle the right answer)\n");
                    break;
                case ("essay"):
                    res.append("\n\n\n\n\n");
                    break;
                case ("multichoice"):
                    for (int j = 0; j < q.elem.getElementsByTagName("answer").getLength(); j++) {
                        res.append("\t" + (j + 1) + ") " + q.elem.getElementsByTagName("answer").item(j).getTextContent() + "\n");
                    }
                    break;
                default:
                    throw new NullPointerException();
            }
            res.append("\n");
        }
        return res;
    }
    // print questions tasks with answers
    public static StringBuilder printWithAnswers(List<Question> questions) {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < questions.size(); i++){
            Question q = questions.get(i);
            res.append(i + 1 + ") ");
            res.append(q.elem.getElementsByTagName("questiontext").item(0).getTextContent() + "\n");
            switch (q.type) {
                case ("short"):
                    res.append("Accepted answers: [");
                    String s = q.elem.getElementsByTagName("answers").item(0).getTextContent();
                    List<String> answers = new ArrayList<>(); // correct answers
                    for(String u: s.split(",")){ // splitting by , to get words
                        answers.add(u);
                    }
                    for(int j = 0; j < answers.size() - 1; j++)
                        res.append(answers.get(j) + ", ");
                    res.append(answers.get(answers.size()-1) + "]\n");
                    break;
                case ("truefalse"):
                    res.append("Answer: " + q.elem.getElementsByTagName("answer").item(0).getTextContent() + "\n");
                    break;
                case ("essay"):
                    res.append(q.elem.getElementsByTagName("answer").item(0).getTextContent() + "\n" + "Note: To be checked manually\n");
                    break;
                case ("multichoice"):
                    String st = q.elem.getElementsByTagName("solution").item(0).getTextContent();
                    for (int j = 0; j < q.elem.getElementsByTagName("answer").getLength(); j++) {
                        if(st.contains(String.valueOf(j+1))) // correct answer case
                            res.append(" -> ");
                        else
                            res.append("\t");
                        res.append(j + 1 + ") " + q.elem.getElementsByTagName("answer").item(j).getTextContent() + "\n");
                    }
                    break;
            }
            if(i < questions.size() - 1)
                res.append("\n");
        }
        return res;
    }


    public String getType() {
        return type;
    }

    public Element getElem() {
        return elem;
    }
}