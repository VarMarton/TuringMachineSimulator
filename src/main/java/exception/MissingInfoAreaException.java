package exception;

public class MissingInfoAreaException extends Exception{

    @Override
    public String getMessage() {
        return "MessageController.setInfoArea(TextArea infoArea) has to be set before trying to call write methods!";
    }
}
