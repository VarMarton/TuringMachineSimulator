package hu.vm.controller.message;

import hu.vm.controller.data.RuleProcessor;
import hu.vm.controller.run.SpecialRunControlKey;

import java.util.Arrays;

public class DefaultMessage {
    public static final String STARTING = "Turing Machine Simulator\n\nThe main goal of the program is to help you write and " +
            "test your Turing machine, so the rule syntax matches the mathematical one:\n\te.g.:\t[q0;0]->[q1;1;>]\n\n" +
            "For more information click on the 'Help' button above. I hope you will like the design and will find the layout logical.\n\n" +
            "Thank you for using Turing Machine Simulator!";

    public static final String HELP_TITLE = "Help";
    public static final String HELP_HEADER = "";
    public static final String HELP_CONTENT = "Turing Machine Simulator\n\nThe main goal of the program is to help you write and " +
            "test your Turing machine, so the rule syntax matches the mathematical one:\n\te.g.:\t[q0;0]->[q1;1;>]\n\n" +
            "Information about rule input:\n" +
            "Although, the mathematically correct form is the one above, all '[' and ']' characters will be removed, " +
            "but using them is recommended to increase readability.\n\n" +
            "Movement symbols:\n" +
            "\tLeft: " + Arrays.toString(RuleProcessor.LEFT_MOVEMENT) + "\n" +
            "\tStay: " + Arrays.toString(RuleProcessor.NO_MOVEMENT) + "\n" +
            "\tRight: " + Arrays.toString(RuleProcessor.RIGHT_MOVEMENT) + "\n" +
            "You can multiply the movement by writing: multiplier*MovementKey, e.g.: 5*L\n\n" +
            "To inspect and write characters that are special, there are special keys:\n" +
            "\tRead any character, " + "\n" +
            "\t\tor write what was there before = " + SpecialRunControlKey.ANY.getReadValue() + "\n" +
            "\tSpace = " + SpecialRunControlKey.SPACE.getReadValue() + "\n" +
            "\t[ = " + SpecialRunControlKey.LEFT_SQUARE_BRACKET.getReadValue() + "\n" +
            "\t] = " + SpecialRunControlKey.RIGHT_SQUARE_BRACKET.getReadValue() + "\n" +
            "\t; = " + SpecialRunControlKey.SEMICOLON.getReadValue() + "\n" +
            "\t, = " + SpecialRunControlKey.COLON.getReadValue() + "\n\n" +
            "Thank you for using Turing Machine Simulator!";
}
