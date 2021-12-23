package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Confirm;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class ResUserConfirmList {

    private String challengeName;

    private Integer totalCnt;

    private Date date;

    private String imgUrl;


    private ResUserConfirmList(Confirm confirm) {

        this.challengeName=confirm.getId().getChallenge().getName();
        this.totalCnt=CalTotalCnt(confirm.getId().getUser().getConfirmList(),confirm.getId().getChallenge().getSeq());
        this.date=confirm.getId().getDate();
        this.imgUrl=confirm.getImgUrl();

    }

    Integer CalTotalCnt(List<Confirm> confirmList, Long seq){
        Integer cnt=0;
        for(Confirm confirm:confirmList){
            if(confirm.getId().getChallenge().getSeq()==seq)    cnt++;
        }
        return cnt;
    }

    public static ResUserConfirmList of(Confirm confirm) {return new ResUserConfirmList(confirm);}
}
