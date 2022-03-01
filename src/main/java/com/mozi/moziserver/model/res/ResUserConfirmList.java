package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Confirm;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ResUserConfirmList {

    private Long seq;

    private String challengeName;

    private Integer totalCnt;

    private LocalDate date;

    private String imgUrl;


    private ResUserConfirmList(Confirm confirm) {

        this.seq=confirm.getSeq();
        this.challengeName=confirm.getChallenge().getName();
        this.totalCnt=CalTotalCnt(confirm.getUser().getConfirmList(),confirm.getChallenge().getSeq());
        this.date=confirm.getDate();
        this.imgUrl=confirm.getImgUrl();

    }

    Integer CalTotalCnt(List<Confirm> confirmList, Long seq){
        Integer cnt=0;
        for(Confirm confirm:confirmList){
            if(confirm.getChallenge().getSeq()==seq)    cnt++;
        }
        return cnt;
    }

    public static ResUserConfirmList of(Confirm confirm) {return new ResUserConfirmList(confirm);}
}
