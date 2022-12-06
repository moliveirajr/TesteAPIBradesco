package exceptions;

import model.ErroBradescoContentEntity;
import model.ErroBradescoEntity;

public class ErroBradescoException extends Exception {

    public ErroBradescoException(String errorMsg, ErroBradescoEntity erroBradesco) {
        super(errorMsg);
        for (ErroBradescoContentEntity erro : erroBradesco.getErros()) {
            System.out.println(erro.getMensagem());
        }
    }
}
