package etu.demo.domain;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface Questions {
    int getPhrase_id();
    String getPhrase();
    @Value("#{@mapperUtility.buildOrderDTO(target.mot_id , target.mot , target.choix1 , target.choix2 , target.choix3 , target.choix4)}")
    List<MotAmbigu> getMot();
/*List<motView> getMots();
    interface motView {
        String getMot();
        String getChoix1();
        String getChoix2();
        String getChoix3();
        String getChoix4();
    }*/
}
