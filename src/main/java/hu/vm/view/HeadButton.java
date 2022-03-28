package hu.vm.view;

import javafx.scene.control.Button;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HeadButton extends Button {

    @Getter
    private HeadPosition headPosition;

}
