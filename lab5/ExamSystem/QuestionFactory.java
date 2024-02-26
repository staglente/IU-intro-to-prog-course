package lab5.ExamSystem;

import org.w3c.dom.Element;

public class QuestionFactory {
    public Question createQuestion(String questiontype, Element element){ // creating new question
        return new Question(questiontype, element);
    }
}