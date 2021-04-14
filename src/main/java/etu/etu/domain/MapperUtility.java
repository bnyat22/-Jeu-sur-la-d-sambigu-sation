package etu.etu.domain;

import org.springframework.stereotype.Component;

@Component
public class MapperUtility {

    public MotAmbigu buildOrderDTO(int mot_id,String mot , String choix1,
                         String choix2 , String choix3 , String choix4) {
        MotAmbigu motAmbigu = new MotAmbigu();
        motAmbigu.setId(mot_id);
        motAmbigu.setMot(mot);
        motAmbigu.setChoix1(choix1);
        motAmbigu.setChoix2(choix2);
        motAmbigu.setChoix3(choix3);
        motAmbigu.setChoix4(choix4);
        return motAmbigu;
    }
}
