package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.PostboxMessageAnimal;
import lombok.Getter;


@Getter
public class ResNoticeOfPostboxMessageAnimal {

    private Long postboxMessageAnimalSeq;

    private Long nextAnimalSeq;


    private ResNoticeOfPostboxMessageAnimal(PostboxMessageAnimal postboxMessageAnimal){

        this.postboxMessageAnimalSeq=postboxMessageAnimal.getSeq();


        this.nextAnimalSeq=postboxMessageAnimal.getLevel()==3?postboxMessageAnimal.getAnimal().getSeq()+1:null;

    }

    public static ResNoticeOfPostboxMessageAnimal of(PostboxMessageAnimal postboxMessageAnimal){
        return new ResNoticeOfPostboxMessageAnimal(postboxMessageAnimal);
    }

}
